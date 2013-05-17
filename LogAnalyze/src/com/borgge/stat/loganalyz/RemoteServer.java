package com.borgge.stat.loganalyz;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RemoteServer {

	private String ip;
	private int port;
	private String sshName;
	private String sshPString;
	private String remoteDir;

	private SCPClient scp;
	private Connection conn;
//	private Session session;

	public RemoteServer(String ip, String sshName, String sshPasswd,
			String remoteDir, int port) {

		if (ip == null || sshName == null || sshPasswd == null
				|| remoteDir == null) {
			throw new NullPointerException("ip : " + ip + "sshName : "
					+ sshName + "sshPasswd : " + sshPasswd + "remoteDir : "
					+ remoteDir);
		}

		
		this.ip = ip;
		this.port = port != 0? port : 22;
		this.sshName = sshName;
		this.sshPString = sshPasswd;
		this.remoteDir = remoteDir.substring(remoteDir.length() - 1)
				.equals("/") ? remoteDir : remoteDir + "/";

		try {
			conn = new Connection(this.ip, this.port);
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(this.getSshName(),
					this.getSshPString());
			if (isAuthenticated == false){
				throw new IOException("Authentication failed.");
			}
			scp = new SCPClient(conn);
//			session = conn.openSession();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getFullPath(String fileName){
		return this.remoteDir + fileName;
	}
	
	public void getFile(String fileName, String localDir) throws IOException {
		this.scp.get("./" + fileName, localDir);
	}
	
	public String tar(String remoteFile) throws IOException {
		Session session = conn.openSession();
		
		String tarFileName = remoteFile + ".tar.gz";
		
		String cdToLog = "cd " + this.remoteDir;
		String tar = "tar czf " + tarFileName + " " + remoteFile;
		String mvToHome = "mv " + tarFileName + " ~";
		String toHome = "cd  ~";
		
		session.execCommand(cdToLog + " && " + tar  + " && " + mvToHome  + " && " + toHome);
		
		InputStream stdout = new StreamGobbler(session.getStdout());     
        BufferedReader br = new BufferedReader(new InputStreamReader(stdout));     

        while (true)     
        {     
            String line = br.readLine();     
            if (line == null)     
                break; 
            System.out.println(line);     
        }    
		
		session.close();
		return tarFileName;
	}

	public void rm(String remoteFile) throws IOException {
		Session session = conn.openSession();
		session.execCommand("rm " + remoteFile);
		session.close();
	}

	public void finish() {
		if (this.conn != null) {
			this.conn.close();
		}

	}
	
	
	public String getLastIpNumber(){
		String[] ips = ip.split("\\.");
		return ips[ips.length - 1];
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Connection conn = new Connection("192.168.1.244");
		conn.connect();
		boolean isAuthenticated = conn.authenticateWithPassword("dbadmin",
				"dbadmin");
		if (isAuthenticated == false)
			throw new IOException("Authentication failed.");
		 SCPClient client = new SCPClient(conn);

//		Session session = conn.openSession();
//		String tarFile = "log11.tar.gz";
//		session.execCommand("tar czf "
//				+ tarFile
//				+ " /home/dbadmin/log/django-2005-07.log.parse_2013-05-09_19-09-20.log");
//		List<String> s = IOUtils.readLines(session.getStderr());
//		System.out.println(Arrays.toString(s.toArray()));
		// session.execCommand("rm " + tarFile);

//		 client.put("./log/test.log", "./log/");
		 
		 client.get(
		 "django-2013-05-09.log.tar.gz",
		 "./log");
		 System.out.println(1);
		 conn.close();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSshName() {
		return sshName;
	}

	public void setSshName(String sshName) {
		this.sshName = sshName;
	}

	public String getSshPString() {
		return sshPString;
	}

	public void setSshPString(String sshPString) {
		this.sshPString = sshPString;
	}

	public String getRemoteDir() {
		return remoteDir;
	}

	public void setRemoteDir(String remoteDir) {
		this.remoteDir = remoteDir;
	}

	public SCPClient getScp() {
		return scp;
	}

	public void setScp(SCPClient scp) {
		this.scp = scp;
	}
	
	
	
}
