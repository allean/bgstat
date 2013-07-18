package com.borgge.stat.dbstat;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.borgge.stat.utils.BgLogUtils;

public class BaseStatSrc {

	// private DbHelper db;

	private String product_id;
	private String product_platform;
	private String create_date;

	private LogDataDao logDataDao;
	private SqlSession session;

	public BaseStatSrc(String product_id, String product_platform, String create_date) {
		this.product_id = product_id;
		this.product_platform = product_platform;
		this.create_date = create_date;

		this.logDataDao = getLogDataDao();
	}

	private LogDataDao getLogDataDao() {
		this.session = DbHelper.getInstance().getSqlSession();
		return this.session.getMapper(LogDataDao.class);
	}

	public DailyStat genDailyStat() {

		DailyStat ds = new DailyStat(product_id, product_platform, create_date);

		ds.setCreate_date(create_date);
		ds.setToday_new_user_count(logDataDao.today_new_user_count(product_id, product_platform,
				create_date));

		ds.setToday_active_user_count(logDataDao.today_active_user_count(
				product_id, product_platform, create_date));
		ds.setWeek_active_user_count(week_active_user_count());
		ds.setMonth_active_user_count(month_active_user_count());

		ds.setTotal_user_count(logDataDao.total_user_count(product_id, product_platform));

		ds.setWeek_new_user_count(ds.getToday_active_user_count() + week_new_user_count());
		ds.setMonth_new_user_count(ds.getToday_active_user_count() + month_new_user_count());

		ds.setToday_ip(logDataDao.ip(product_id, product_platform, create_date, create_date));
		ds.setMonth_ip(logDataDao.ip(product_id, product_platform, BgLogUtils.getLastMonth(create_date), create_date));
		ds.setTotal_ip(logDataDao.ip(product_id, product_platform, BgLogUtils.earliest_date, create_date));

		ds.setToday_pv(logDataDao.pv(product_id, product_platform, create_date, create_date));
		ds.setMonth_pv(logDataDao.pv(product_id, product_platform, BgLogUtils.getLastMonth(create_date), create_date));
		
		
		if (ds.getToday_active_user_count() != 0){
			int day_after_use_count = day_after_use_count();
//			System.out.println("day_after_use_count : " + day_after_use_count);
//			System.out.println("ds.getToday_active_user_count() : " + ds.getToday_active_user_count());
			ds.setDay_after_use_rate((float)day_after_use_count
					/ (float)ds.getToday_active_user_count());
			
			int week_after_use_count = week_after_use_count();
			ds.setWeek_after_use_rate((float)week_after_use_count / (float)ds.getToday_active_user_count());
//			System.out.println("week_after_use_count : " + week_after_use_count);
			
			int month_after_use_count = month_after_use_count();
			ds.setMonth_after_use_rate((float)month_after_use_count / (float)ds.getToday_active_user_count());
		}
		
		
		DailyStat yesterday = logDataDao.getDailyStat(product_id, product_platform_for_sql_select(), BgLogUtils.getYesterday(create_date));
		if (yesterday != null){
			int today_after_new_user_lose_count = logDataDao.after_new_user_lose_count(product_id, product_platform, create_date, BgLogUtils.getYesterday(create_date));
			ds.setDay_after_new_user_lose_rate((float)today_after_new_user_lose_count / (float)yesterday.getToday_new_user_count()); 
//			System.out.println("today_after_new_user_lose_count : " + today_after_new_user_lose_count);
		}
		
		DailyStat lastWeek = logDataDao.getDailyStat(product_id, product_platform_for_sql_select(), BgLogUtils.getLastWeek(create_date));
		if (lastWeek != null){
			int week_after_new_user_lose_count = logDataDao.after_new_user_lose_count(product_id, product_platform, create_date, BgLogUtils.getLastWeek(create_date));
			ds.setWeek_after_new_user_lose_rate((float)week_after_new_user_lose_count  / (float)lastWeek.getToday_new_user_count()); 
		}
		
		DailyStat lastMonth = logDataDao.getDailyStat(product_id, product_platform_for_sql_select(), BgLogUtils.getLastMonth(create_date));
		if (lastMonth != null){
			int month_after_new_user_lose_count = logDataDao.after_new_user_lose_count(product_id, product_platform, create_date, BgLogUtils.getLastMonth(create_date));
			ds.setMonth_after_new_user_lose_rate((float)month_after_new_user_lose_count  / (float)lastMonth.getToday_new_user_count()); 
		}

		
		
		ds.setToday_forward_count(logDataDao.forward_count(product_id, product_platform, create_date));
		ds.setMonth_forward_count(logDataDao.forward_count_range(product_id, product_platform, BgLogUtils.getLastMonth(create_date), create_date));
		ds.setTotal_forward_count(logDataDao.total_forward_count(product_id, product_platform));
		
		ds.setToday_comment_count(logDataDao.comment_count(product_id, product_platform, create_date));
		ds.setMonth_comment_count(logDataDao.comment_count_range(product_id, product_platform, BgLogUtils.getLastMonth(create_date), create_date));
		ds.setTotal_comment_count(logDataDao.total_comment_count(product_id, product_platform));
		
		if (ds.getToday_active_user_count() != 0){
			ds.setToday_active_user_read_rate((float)logDataDao.user_read_count(product_id, product_platform, create_date) / (float)ds.getToday_active_user_count());
		}
		
		if (ds.getMonth_active_user_count() != 0){
			ds.setMonth_active_user_read_rate((float)logDataDao.user_read_count_range(product_id, product_platform, BgLogUtils.getLastMonth(create_date), create_date) / (float)ds.getMonth_active_user_count());
		}
		
//		ds.setToday_active_user_read_rate(logDataDao.total_user_read_count(product_id) / ds.gettot)
		logDataDao.insertDailyStat(ds);
		this.session.commit();
		this.session.close();
		
		return ds;
	}
	
	
	//查询daily_stat表时，获取某产品所有平台统计时，需要转译一下
	private String product_platform_for_sql_select(){
		if ("%".equals(product_platform)){
			return "\\%";
		}else{
			return product_platform;
		}
	}
	
	private int day_after_use_count() {
		String yesterday = BgLogUtils.getYesterday(create_date);
		return this.logDataDao.after_use_count(product_id, product_platform, create_date,
				yesterday, yesterday);
	}

	private int week_after_use_count() {
		String lastWeek = BgLogUtils.getLastWeek(create_date);
		return this.logDataDao.after_use_count(product_id, product_platform, create_date,
				lastWeek, lastWeek);
	}
	
	private int month_after_use_count() {
		String lastMonth = BgLogUtils.getLastMonth(create_date);
		return this.logDataDao.after_use_count(product_id, product_platform, create_date,
				lastMonth, lastMonth);
	}


	private int week_active_user_count() {
//		DateTime dt = new DateTime(this.create_date);
//		String lastWeek = dt.minusDays(7).toString("yyyy-MM-dd");
		
//		System.out.println(BgLogUtils.getLastWeek(create_date));
//		System.out.println(create_date);
		
		return this.logDataDao.active_user_count(product_id, product_platform, BgLogUtils.getLastWeek(create_date),
				create_date);
	}

	private int month_active_user_count() {
//		DateTime dt = new DateTime(this.create_date);
//		String lastWeek = dt.minusDays(30).toString("yyyy-MM-dd");
		return this.logDataDao.active_user_count(product_id, product_platform, BgLogUtils.getLastMonth(create_date),
				create_date);
	}

	private int week_new_user_count() {
//		DateTime dt = new DateTime(this.create_date);
//		String lastWeek = dt.minusDays(7).toString("yyyy-MM-dd");
		return this.logDataDao
				.new_user_count(product_id, product_platform_for_sql_select(), BgLogUtils.getLastWeek(create_date), create_date);
	}

	private int month_new_user_count() {
//		DateTime dt = new DateTime(this.create_date);
//		String lastWeek = dt.minusDays(30).toString("yyyy-MM-dd");
		return this.logDataDao
				.new_user_count(product_id, product_platform_for_sql_select(), BgLogUtils.getLastMonth(create_date), create_date);
	}
	
	public static List<ProductAndPlatform> getAllProductAndVertion(String create_date){
		SqlSession session = DbHelper.getInstance().getSqlSession();
		LogDataDao ld = session.getMapper(LogDataDao.class);
		
		List<ProductAndPlatform> rs = ld.get_product_id_and_platform(create_date);
		List<ProductAndPlatform> product = ld.get_product_id(create_date);
		for (ProductAndPlatform productAndPlatform : product) {
			productAndPlatform.setProduct_platform("%"); 
		}
		
		rs.addAll(product);
		session.close();
		return rs;
	}
	

	public static void main(String[] args) {
//		DateTime dt = new DateTime("2013-05-12");
//		System.out.println(dt.toString("yyyy-MM"));

		
		
//		BaseStatSrc src = new BaseStatSrc("dailynews", "%", "2013-05-12");
//		DailyStat ds = src.genDailyStat();
		
		
//		List<ProductAndPlatform> l = BaseStatSrc.getAllProductAndVertion("2013-05-03");
//		System.out.println(l);
		
		SqlSession session = DbHelper.getInstance().getSqlSession();
		LogDataDao logDataDao = session.getMapper(LogDataDao.class);
		
		List<NumTypePair> l = logDataDao.device_oem_by_count("banbao", "wp7", "2013-07-15", "2013-07-15");
		System.out.println(l);
		
		
		
		
//		System.out.println(ds);
		
	}

}
