package com.borgge.stat.loganalyz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import com.borgge.stat.dbstat.BaseStatSrc;
import com.borgge.stat.dbstat.ProductAndPlatform;
import com.borgge.stat.utils.BgLogUtils;

public class LogParser {

	private Logger logger;

	// private File remoteILogDir; // Զ�̻�����־�����ļ���
	private File localLogDir; // ��Զ�̻�����load�������ļ�
	private File parsedDataDir; // ��־������ɵ��ļ�
	private File localParsedDir; // ��־������ɺ󱸷ݴ�������ļ�
	private File loadCompleteDir; // �������ݿ���ɺ���ļ�

	private List<String> logDate = null;
	
	private List<File> downLoadSuccessFiles = new ArrayList();
	private List<File> parsedSuccessFiles = new ArrayList();
	
	private List<RemoteServer> servers = new ArrayList<RemoteServer>();

	
	public LogParser(File localLogDir, File parsedDataDir, File localParsedDir, File loadCompleteDir, List<RemoteServer> servsers){
		this.localLogDir = localLogDir;
		this.parsedDataDir = parsedDataDir;
		this.localParsedDir = localParsedDir;
		this.loadCompleteDir = loadCompleteDir;
		this.servers = servsers;
		
		
		if (!this.localLogDir.exists()){
			this.localLogDir.mkdir();
		}
		
		if (!this.parsedDataDir.exists()){
			this.parsedDataDir.mkdir();
		}
		
		if (!this.localParsedDir.exists()){
			this.localParsedDir.mkdir();
		}
		
		if (!this.loadCompleteDir.exists()){
			this.loadCompleteDir.mkdir();
		}
		
		try {
			logger = BgLogUtils.getLogger();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public LogParser(List<String> logDate, File localLogDir, File parsedDataDir,
			File localParsedDir, File loadCompleteDir,
			List<RemoteServer> servsers) {
		this(localLogDir, parsedDataDir, localParsedDir, loadCompleteDir, servsers);
		this.logDate = logDate;
	}
	
	
	public LogParser() {

	}

	public static List<RemoteServer> getRemoteServers(Properties prop) {

		List<RemoteServer> servers = new ArrayList();
		
		String remoteIps = prop.getProperty("remote_ip");
		if (remoteIps == null) {
			return servers;
		}

		String ports = prop.getProperty("ssh_port") != null ? prop
				.getProperty("ssh_port") : "0";

		String sshName = prop.getProperty("ssh_name") != null ? prop
				.getProperty("ssh_name") : "";
		String sshPasswd = prop.getProperty("ssh_passwd") != null ? prop
				.getProperty("ssh_passwd") : "";
		String remoteDir = prop.getProperty("dir_remote_log") != null ? prop
				.getProperty("dir_remote_log") : "";

		String[] ips = remoteIps.split(",");
		String[] names = sshName.split(",");
		String[] passwds = sshPasswd.split(",");
		String[] dirs = remoteDir.split(",");
		String[] pts = ports.split(",");

		for (int i = 0; i < ips.length; i++) {
			String ip = ips[i];
			String name = names[i];  
			String passwd = passwds[i];
			String dir = dirs[i];
			String pt = pts[i];

			servers.add(new RemoteServer(ip, name, passwd, dir, Integer
					.valueOf(pt)));
		}
		return servers;
	}

	public void getLogs() {

		String logPerfix = "django-";
		String logSuffix = ".log";

//		String logDate = this.logDate == null? BgLogUtils.getYesterday(): this.logDate; 
		
		for (String lgdate : this.logDate) {
			String logName = logPerfix + lgdate + logSuffix;
			logger.info("Begin get log. at " + new Date());
			for (RemoteServer server : this.servers) {
				logger.info("Get log on server : " + server.getIp() + " : "
						+ server.getPort());
				try {
					logger.info("begin-remote-tar. At : " + new Date());
					String tarFileName = server.tar(logName);
					logger.info("finish-remote-tar " + tarFileName + ". At : "
							+ new Date());

					logger.info("begin-get-remote-tar " + tarFileName
							+ ". At : " + new Date());
					server.getFile(tarFileName, this.localLogDir.toString());
					logger.info("finish-get-remote-tar " + tarFileName
							+ ". At : " + new Date());
					server.rm(tarFileName);
					//
					String localTarGz = this.localLogDir.toString() + "/"
							+ tarFileName;
					BgLogUtils.deCompressTGZFile(localTarGz);

					FileUtils.forceDelete(new File(localTarGz));

					// ����server��Ϣ��������server��log����
					File src = new File(this.localLogDir.toString() + "/"
							+ logName);
					File serverLog = new File(this.localLogDir.toString() + "/"
							+ logPerfix + lgdate + "-"
							+ server.getLastIpNumber() + "-" + server.getPort()
							+ logSuffix);

					FileUtils.moveFile(src, serverLog);
					this.downLoadSuccessFiles.add(serverLog);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			logger.info("finish get log. at " + new Date());
			logger.info("==================================");
		}

	}

	public void processLog() throws IOException {

		// init parse complete dir
		// File parseComplete = new File("./parseComplete/");

		// init output
//		FileUtils.deleteDirectory(this.parsedDataDir);
//		this.parsedDataDir.mkdirs();

		Collection<File> files = FileUtils.listFiles(this.localLogDir, new String[]{"log"},
				false);
		

		for (File file : files) {

			try {

				List<String> lines = FileUtils.readLines(file);

				File output = new File(this.parsedDataDir.toString()
						+ "/" + file.getName() + ".parse_" + BgLogUtils.getTodayTimeStr()
						+ ".log");
				OutputStream os = new FileOutputStream(output, true);
				List buffer = new ArrayList();

				logger.info("Begin parse : " + file.getName()
						+ ". Total line : " + lines.size());

				int complete_line = 0;

				newline: for (String line : lines) {
					// System.out.println(line);

					Log log = new Log();
					Iterator<String> iterator = operation.keySet().iterator();
					while (iterator.hasNext()) {
						String key = iterator.next();
						if (line.indexOf(key) > 0) {
							log.setOperate(operation.get(key));
							break;
						}
					}

					if ("".equals(log.getOperate())) {
						// unkonw opt, just skip
						// System.out.println("skip log : " + line);

						for (String skip : skipopt) {
							if (line.indexOf(skip) > 0) {
								continue newline;
							}
						}

						if (!(line.indexOf("get") > 0)) {
							continue;
						}

						logger.info("skip log : " + line);
						continue;
					}

					parse(line, log);

					buffer.add(log.toSqlString());
					// buffer.add(line);
					if (buffer.size() >= 500) {
						writeToFile(buffer, os);
					}
					complete_line++;
					if ((complete_line % 10000) == 0) {
						logger.info("complete line: " + complete_line);
					}

				}
				if (buffer.size() > 0) {
					writeToFile(buffer, os);
				}
				
				this.parsedSuccessFiles.add(output);
				
				
				logger.info("Finish pasre : " + file.getName()
						+ ". Process line: " + complete_line + ". Total line : " + lines.size());

			} catch (Exception e) {
				e.printStackTrace();
				logException(e);
			}

			FileUtils.moveFileToDirectory(file, this.localParsedDir, true);
		}
	}

	private void logException(Exception e) {
		Writer w = new StringWriter();
		PrintWriter pw = new PrintWriter(w);
		e.printStackTrace(pw);
		logger.log(Level.SEVERE, "Exception : " + w.toString());
	}

	public void loadIntoDatabase(){
		
		logger.info("Begin load data. At : " + new Date());
		
//		Collection<File> parsedFiles =  FileUtils.listFiles(this.parsedDataDir, new String[]{"log"}, false);
		
		for (File file : this.parsedSuccessFiles) {
		//	String loadStr = "/opt/vertica/bin/vsql -w bg -c \"copy data(device_id,create_date,create_time,ip,user_id,product_id,product_version,product_platform,distribute_oem_id,data_source,device_oem,netword_brand,location,operate,item_id,channel_id,app_id,sns_platform,opt_data) from '" + file.getAbsolutePath() + "' DELIMITER ','\"";
			String loadStr = "./script/loaddata.sh '" + file.getAbsolutePath() + "'";
			
			logger.info("load script : " + loadStr);
			try {
				
				Process p = Runtime.getRuntime().exec(loadStr);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				BufferedReader error = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				
				while (true)     
		        {     
		            String line = error.readLine();    
		            if (line == null)     
		                break;     
		            logger.info("=====loaddata error=====" + line);     
		        }
				
		        while (true)     
		        {     
		            String line = br.readLine();    
		            if (line == null)     
		                break;     
		            logger.info("=====loaddata output=====" + line);     
		        }  
			} catch (IOException e) {
				e.printStackTrace();
				logException(e);
			} finally{
				try {
					FileUtils.moveFileToDirectory(file, this.loadCompleteDir, true);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		logger.info("Finish load data. At : " + new Date());
	}
	
	public void genDailyStat() {
		

		List<String> dates = this.logDate;

		logger.info("dates : " + dates);
		for (String date : dates) {
			List<ProductAndPlatform> l = BaseStatSrc.getAllProductAndVertion(date);
			
			for (ProductAndPlatform p : l) {
				
				logger.info("product_id : " + p.getProduct_id()
						+ " product_platform : " + p.getProduct_platform()
						+ " create_date : " + date);
				
				BaseStatSrc bs = new BaseStatSrc(
						p.getProduct_id(),
						p.getProduct_platform(), date);
				bs.genDailyStat();
			}
		}

	}
	
	
	public static void main(String[] args) throws IOException {
		
		LogParser lp = null;
		try {

			Properties prop = new Properties();
			FileInputStream fis = new FileInputStream("config/config.properties");
			prop.load(fis);

			List servers = getRemoteServers(prop);

			File localLogDir = new File(prop.getProperty("dir_local_log"));
			File parsedDataDir = new File(prop.getProperty("dir_parsed_data"));
			File localParsedDir = new File(
					prop.getProperty("dir_local_parsed_log"));
			File loadCompleteDir = new File(
					prop.getProperty("dir_load_complete_data"));

			String logDate = prop.getProperty("get_log_date");
			
			List<String> logdates = new ArrayList();
			
			if (logDate == null){
				logdates.add(BgLogUtils.getYesterday());
				
				lp = new LogParser(logdates, localLogDir, parsedDataDir, localParsedDir, loadCompleteDir, servers);
			}else{
				String[] s = logDate.split("_");
				if (s.length > 1){
					DateTime start = new DateTime(s[0]);
					DateTime end = new DateTime(s[1]);
					
					logdates.add(start.toString("yyyy-MM-dd"));
					while (!start.equals(end)){
			        	start = start.plusDays(1);
			        	logdates.add(start.toString("yyyy-MM-dd"));
			        }
				}else{
					logdates.add(new DateTime(s[0]).toString("yyyy-MM-dd"));
				}
				
				lp = new LogParser(logdates, localLogDir, parsedDataDir, localParsedDir, loadCompleteDir, servers);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (args.length == 0){
			lp.getLogs();
			lp.processLog();
			lp.loadIntoDatabase();
			lp.genDailyStat();
		}else if ("get".equals(args[0])){
			lp.getLogs();
		}else if ("parse".equals(args[0])){
			lp.processLog();
		}else if ("load".equals(args[0])){
			
			File[] parsedFiles = lp.parsedDataDir.listFiles();
			for (File file : parsedFiles) {
				lp.parsedSuccessFiles.add(file);
			}
			
			lp.loadIntoDatabase();
		}else if ("stat".equals(args[0])){
			lp.genDailyStat();
		}else{
			System.out
					.println("Use : \r\n 1. get: to load log from remote server; \r\n 2. parse: to genarate format data;\r\n 3. load: to load data into database;");
		}
		
	}

	private static void writeToFile(List buffer, OutputStream os)
			throws IOException {
		IOUtils.writeLines(buffer, null, os);
		buffer.clear();
	}

	static String getCreateTime(String time) {
		String timestamp = (time.substring(0, time.length() - 1)); // trim last
																	// space
		String[] t = timestamp.split(",");
		return t[0];
	}

	private void parse(String rawLine, Log log) {
		String[] l = rawLine.split("INFO");

		String dateTime = getCreateTime(l[0]);
		String[] dt = dateTime.split(" ");
		if (dt.length > 1) {
			log.setCreate_date(dt[0]);
			log.setCreate_time(dt[1]);
		}
		String line = l[1];

		String ip = getIP(line);
		if (ip != null) {
			log.setIp(ip);
		}

		String platform = getPlatform(line);
		if (platform != null) {
			platform = platform.trim();
			String[] pf = platform.split("_");
			if (pf.length > 1) {
				log.setProduct_platform(pf[0]);
				log.setProduct_version(pf[1]);
			} else {
				log.setProduct_platform(pf[0]);
			}
		}

		String product = getProduct(line);
		if (product != null) {
			log.setProduct_id(product.trim());
		}

		String oem = getOem(line);
		if (oem != null) {
			log.setDevice_oem(oem.trim());
		}

		String deviceId = getDeviceId(line);
		if (deviceId != null) {
			log.setDevice_id(deviceId);
		}

		String userId = getUserId(line);
		if (userId != null) {
			log.setUser_id(userId);
		}

		String itemId = getItemId(line);
		if (itemId != null) {
			log.setItem_id(itemId);
		}

		String channelId = getChannelId(line);
		if (channelId != null) {
			log.setChannel_id(channelId);
		}

	}

	static Map<String, String> operation = new HashMap();
	static List<String> skipopt = new ArrayList<String>();

	static {
		operation.put("get content item without pic", "article");
		operation.put("get content item with pic", "article_pic");
		operation.put("get recommendations", "rec");
		operation.put("get channel content subset merged", "channel"); // �屨�������ɻ�ȡ�����б�
		operation.put("get front page", "front");
		operation.put(" like item", "like");
		operation.put("dislike item", "dislike");
		operation.put("SNS forwarding", "sns_fwd");
		operation.put("get comment", "article_comment");
		operation.put("get sns friends", "sns_frs");
		operation.put("get my SNS content", "sns_cont");
		operation.put("get all channel list", "add_channel"); // ���channel��ʱ�����ã�˵���û������Ƶ��
		operation.put("reset my channel list", "modify_channel"); // �û�ȷ��Ҫ�޸�Ƶ��
		operation.put("set my gender", "set_gd");
		operation.put("get daily news", "channel"); // ͷ����ȡ�����б�
		operation.put("user get basic info", "get_profile"); // web�ϻ�ȡ������Ϣ
		operation.put("get my favourite merged", "get_keep"); // �鿴�ղ�
		operation.put("add to my favourite", "keep"); // �ղ�����
		operation.put("delete from my favourite", "del_keep"); // �ղ�����

		operation.put("get top apps", "rec_app"); // ��Ӧ���Ƽ�
		operation.put("post app stat", "like_app"); // ���ĳ���Ƽ�Ӧ��
		operation.put("post user interest", "click_tag"); // �û������ϲ����tag

		operation.put("user register", "register"); // ��վע��
		operation.put("user login", "login_home"); // ��¼��վ
		operation.put("SNS login first step", "login_sns"); // ��¼��վ
		operation.put("user logout", "logout"); // �ǳ�

		operation.put("get my channel list", "my_channel_list"); // ��ȡ�ҵ�Ƶ���б�

		// ������
		operation.put("get model detail", "get_model_detail"); // ��ȡģ������
		operation.put("get midnight channel", "get_midnight_channel");
		operation.put("get model channel", "get_model_channel");

		operation.put("post comment to our site", "comment"); // ����
		operation.put("get search result", "search"); // ����

		// operation.put("user logout", "logout"); //�ǳ�

		// operation.put("user login", "home_site"); //�ղ�
	}

	static {
		skipopt.add("get source comment");
		skipopt.add("get user all source feedback");
		skipopt.add("get famous quote");
		skipopt.add("get midnight channel");
		skipopt.add("SNS second step account register");
		skipopt.add("Add Coin Product:");
		skipopt.add("Result1:tully_api_content_forward_2");
		skipopt.add("SNS second step account rebinding");
		skipopt.add("post comment to sns");
		skipopt.add("Portal:");
		skipopt.add("access home page from partner");
		skipopt.add("get film detail");
		skipopt.add("SNS second step fail");
		skipopt.add("SNS second step account login");
		skipopt.add("get rec apps");
		skipopt.add("SNS forward show");
		skipopt.add("get channel content subset");

		skipopt.add("get content item without pic");
		skipopt.add("get content item with pic");
	}

	static Pattern OEM = Pattern.compile("OEM: [\\w|.|-]+");

	private String getOem(String line) {
		return ifMatch(OEM, line);
	}

	static Pattern PLATFORM = Pattern.compile("platform: [\\w|.]+");

	private String getPlatform(String line) {
		return ifMatch(PLATFORM, line);
	}

	static Pattern PRODUCT = Pattern.compile("product: \\w+");

	private String getProduct(String line) {
		return ifMatch(PRODUCT, line);
	}

	static Pattern DEVCIE_ID = Pattern.compile("device id: \\w+");

	private String getDeviceId(String line) {
		return ifMatch(DEVCIE_ID, line);
	}

	static Pattern SNS = Pattern.compile("SNS name: \\w+");

	private String getSnsName(String line) {
		return ifMatch(SNS, line);
	}

	static Pattern IP = Pattern
			.compile("ip: \\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

	private String getIP(String line) {
		return ifMatch(IP, line);
	}

	static Pattern USER_ID = Pattern.compile("user id: \\d+");

	private String getUserId(String line) {
		return ifMatch(USER_ID, line);
	}

	static Pattern ITEM_ID = Pattern.compile("item id: \\d+");

	private String getItemId(String line) {
		return ifMatch(ITEM_ID, line);
	}

	static Pattern CHANNEL_ID = Pattern.compile("channel id: \\d+");

	private String getChannelId(String line) {
		return ifMatch(CHANNEL_ID, line);
	}

	private String ifMatch(Pattern p, String line) {
		Matcher matcher = p.matcher(line);

		while (matcher.find()) {
			String id = matcher.group();
			String[] s = id.split(": ");
			return s[1];
		}
		return null;
	}

}
