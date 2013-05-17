package com.borgge.stat.dbstat;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.joda.time.DateTime;

public class DbHelper {
	private static DbHelper dbHelper = null;
	private static SqlSessionFactory sqlSessionFactory = null;

	private DbHelper() {
		String resource = "com/borgge/stat/dbstat/mybatis.xml";
		Reader reader;
		try {
			reader = Resources.getResourceAsReader(resource);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			sqlSessionFactory.getConfiguration().addMapper(LogDataDao.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SqlSession getSqlSession() {
		return sqlSessionFactory.openSession();
	}

	public static DbHelper getInstance() {
		if (dbHelper == null)
			dbHelper = new DbHelper();
		return dbHelper;
	}
	
	public static void main(String[] args) {
		SqlSession session = DbHelper.getInstance().getSqlSession();
        LogDataDao logDataDao = session.getMapper(LogDataDao.class);
        
//        int userCount = logDataDao.today_active_user_count("dailynews", "%", "2013-05-11");
//        int c = logDataDao.after_new_user_lose_count("dailynews", "%", "2013-05-11", "2013-05-10");
//        List pv = logDataDao.getDailyStatByProductAndPlatformAndVertionAndDate("banbao", "wp7", "2013-05-11");
		List<ProductChannel> p = logDataDao
				.getDailyStatByProductAndPlatformAndChannelAndDate("dailynews",
						"wp7", "2013-05-11");
        System.out.println(p);
//        System.out.println(userCount);
        
        
//        System.out.println(userCount);
        
//        DailyStat ds = new DailyStat();
//        ds.setCreate_date("2013-05-12");
//        ds.setToday_active_user_count(10);
//        
//        System.out.println(logDataDao.insertDailyStat(ds));
//        session.commit();
//        
//        List<DailyStat> dds = logDataDao.allDailyStat("");
//        
//        System.out.println(ds);
        
        
//        DateTime start = new DateTime("2013-05-12");
//        DateTime end = new DateTime("2013-06-12");
//        
//        while (!start.equals(end)){
//        	start = start.plusDays(1);
//        }
//        System.out.println(start.toString("yyyy-MM-dd"));
        
        
//        Collection<File> files = FileUtils.listFiles(new File("./log_parsed"), new String[]{"log"},
//				false);
//        
//        System.out.println(files);
        
	}
	
}