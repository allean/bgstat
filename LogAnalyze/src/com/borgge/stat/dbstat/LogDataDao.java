package com.borgge.stat.dbstat;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

public interface LogDataDao {

	public static String product_platform = " and product_platform like #{product_platform}";
	public static String create_date = " and create_date=#{create_date}";
	public static String create_date_range = " and create_date>=#{start_date} and create_date<=#{to_date}";
	
	
	
	
	//活跃用户
	@Select("select count(distinct(user_id)) from data where product_id=#{product_id}" +
			product_platform + 
			create_date +
			" and data_source='s';")
	public int today_active_user_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
		
	//今日新增用户
	@Select("SELECT count(distinct(user_id)) FROM"+
 				"(SELECT user_id, create_date, create_time, RANK() OVER(PARTITION BY user_id ORDER BY create_date, create_time) RK FROM data" + 
 					" where user_id in (select user_id from  data where product_id=#{product_id} and product_platform like #{product_platform} and create_date=#{create_date})) T1" +
 		 " WHERE RK = 1 and create_date = #{create_date};")
	public int today_new_user_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	//独立ip
	@Select("select count(distinct(ip)) from data where product_id=#{product_id} " +
			product_platform + 
			create_date_range +
			" and data_source='s';")
	public int ip(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date
			);
	
	//pv
	@Select("select count(1) from data where product_id=#{product_id} " +
			product_platform + 
			create_date_range +
			" and data_source='s';")
	public int pv(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date
			);
	
	//总用户数
	@Select("select count(distinct(user_id)) from data where product_id=#{product_id} " +
			product_platform +
			"and data_source='s';")
	public int total_user_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	//昨天新增，今日继续登录的用户
//	@Select("select count(distinct(user_id)) from data where product_id=#{product_id} " +
//			product_platform +
//			" and create_date=#{create_date} " +
//			"and user_id in" +
//				"(SELECT distinct(user_id) FROM"+
//					"(SELECT user_id, create_date, create_time, RANK() OVER(PARTITION BY user_id ORDER BY create_date, create_time) RK FROM data" + 
//						" where user_id in (select user_id from  data where product_id=#{product_id} and product_platform=#{product_platform} and create_date=#{yesterday})) T1" +
//			" WHERE RK = 1 and create_date = #{yesterday});")
	@Select("select count(distinct(user_id)) from data where product_id=#{product_id} and product_platform like #{product_platform} and create_date=#{create_date} " +
			"and user_id in(SELECT distinct(user_id) FROM " +
				"(SELECT user_id, create_date, create_time, RANK() OVER(PARTITION BY user_id ORDER BY create_date, create_time) RK FROM data where " +
					"user_id in (select user_id from  data where product_id=#{product_id} and product_platform like #{product_platform} and create_date=#{create_date})) T1 WHERE RK = 1 and create_date = #{yesterday});")
	public int after_new_user_lose_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date,
			@Param("yesterday") String yesterday
			);
	
	@Select("select count(distinct(user_id)) from data where product_id=#{product_id} " +
			product_platform +
			"and create_date=#{today_date} " +
			"and user_id in (select distinct(user_id) from data where  product_id=#{product_id} " +
				product_platform +
				"and create_date>=#{start_date} and create_date<=#{to_date}" +
				")" +
			";")
	public int after_use_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("today_date") String create_date,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date
			);
	
	
	//时间范围活跃
//	@Select("select coalesce(sum(today_active_user_count), 0) from daily_stat where product_id=#{product_id} " +
//			product_platform + 
//			create_date_range +
//			";")
	@Select("select count(distinct(user_id)) from data where product_id=#{product_id}" +
			product_platform +
			create_date_range +
			" and data_source='s';")
	public int active_user_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	//时间范围新增
	@Select("select coalesce(sum(today_new_user_count), 0) from daily_stat where product_id=#{product_id} " +
			product_platform + 
			create_date_range +
			";")
	public int new_user_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	@Insert("insert into daily_stat(create_date, product_id, product_platform, today_ip, month_ip, total_ip, today_pv, month_pv, today_new_user_count, today_active_user_count, week_new_user_count, week_active_user_count, month_new_user_count, month_active_user_count, total_user_count, day_after_use_rate, week_after_use_rate, month_after_use_rate, day_after_new_user_lose_rate, week_after_new_user_lose_rate, month_after_new_user_lose_rate, today_avg_boot_count, month_avg_boot_count, total_avg_boot_count, today_avg_use_count, month_avg_use_count, total_avg_use_count, today_forward_count, month_forward_count, total_forward_count, today_comment_count, month_comment_count, total_comment_count, today_active_user_read_rate, month_active_user_read_rate, total_active_user_read_rate) " +
			" values(#{create_date},#{product_id},#{product_platform},#{today_ip},#{month_ip},#{total_ip},#{today_pv},#{month_pv},#{today_new_user_count},#{today_active_user_count},#{week_new_user_count},#{week_active_user_count},#{month_new_user_count},#{month_active_user_count},#{total_user_count},#{day_after_use_rate},#{week_after_use_rate},#{month_after_use_rate},#{day_after_new_user_lose_rate},#{week_after_new_user_lose_rate},#{month_after_new_user_lose_rate},#{today_avg_boot_count},#{month_avg_boot_count},#{total_avg_boot_count},#{today_avg_use_count},#{month_avg_use_count},#{total_avg_use_count},#{today_forward_count},#{month_forward_count},#{total_forward_count},#{today_comment_count},#{month_comment_count},#{total_comment_count},#{today_active_user_read_rate},#{month_active_user_read_rate},#{total_active_user_read_rate});")
	public int insertDailyStat(DailyStat dailyStat);
	
	
	//获取所有daily统计
	@Select("select * from daily_stat where product_id=#{product_id}" +
			product_platform + 
			";")
	public List<DailyStat> allDailyStat(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	//获取某天某平台某产品的统计
	@Select("select * from daily_stat where product_id=#{product_id} " +
			product_platform + 
			create_date + 
			" limit 1;")
	public DailyStat getDailyStat(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	// 获取某天所有
	@Select("select * from daily_stat where create_date=#{create_date}" +
			" order by today_active_user_count desc" +
			";")
	public List<DailyStat> getDailyStatByDate(
			@Param("create_date") String create_date);
	
	// 获取某天所有
	@Select("select * from daily_stat where product_id=#{product_id} " + 
				product_platform + 
				create_date_range + 
				" order by create_date desc" +
				";")
	public List<DailyStat> getDailyStatByProductAndPlatformAndDate(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	@Select("select product_version, count(distinct(user_id)) as active_user_count" +
			" from data where product_id=#{product_id} " + 
			product_platform + 
			create_date + 
			" group by product_version" +
			";")
	public List<ProductVersoin> getDailyStatByProductAndPlatformAndVertionAndDate(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	@Select("select channels.name as channel_name, article_by_channel from (select channel_id, count(distinct(user_id)) as article_by_channel" +
			" from data,channels where product_id=#{product_id} " + 
			product_platform + 
			create_date + 
			"and operate like 'article%' " +
			"group by channel_id) as data, channels where data.channel_id=channels.id" +
			";")
	public List<ProductChannel> getDailyStatByProductAndPlatformAndChannelAndDate(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	
	@Select("select product_id" +
			" from (select count(1) as count, product_id from data where 1=1" +
			create_date +
			" group by product_id order by count desc) as tt" +
			";")
	public List<String> getProduct(
			@Param("create_date") String create_date
			);
	
	//获取某天转发数
	@Select("select count(1) from data where product_id=#{product_id} and operate='sns_fwd'" +
			product_platform +
			create_date + 
			";")
	public int forward_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	//获取某段时间的转发数
	@Select("select count(1) from data where product_id=#{product_id} and operate='sns_fwd'" +
			product_platform + 
			create_date_range + 
			";")
	public int forward_count_range(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	//全部转发
	@Select("select count(1) from data where product_id=#{product_id} and operate='sns_fwd'" +
			product_platform +
			";")
	public int total_forward_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	//获取某天评论数
	@Select("select count(1) from data where product_id=#{product_id} and operate='comment'" +
			product_platform + 
			create_date + 
			";")
	public int comment_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	//获取某段时间的评论数
	@Select("select count(1) from data where product_id=#{product_id} and operate='comment'" +
			product_platform +
			create_date_range +
			";")
	public int comment_count_range(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	//全部评论数
	@Select("select count(1) from data where product_id=#{product_id} and operate='comment'" +
			product_platform +
			";")
	public int total_comment_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	//获取某天阅读文章数
	@Select("select count(1) from data where product_id=#{product_id} and operate like 'article%'" +
			product_platform +
			create_date +
			";")
	public int user_read_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	//获取某段时间阅读文章数
	@Select("select count(1) from data where product_id=#{product_id} and operate like 'article%'" +
			product_platform +
			create_date_range +
			";")
	public int user_read_count_range(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	//全部阅读文章数
	@Select("select count(1) from data where product_id=#{product_id} and operate like 'article%'" +
			product_platform +
			";")
	public int total_user_read_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	
	//平台和产品
	@Select("select product_platform, product_id from data where create_date=#{create_date} group by product_platform, product_id;")
	public List<ProductAndPlatform> get_product_id_and_platform(
			@Param("create_date") String create_date);
	
	//产品
	@Select("select product_id from data where create_date=#{create_date} group by product_id;")
	public List<ProductAndPlatform> get_product_id(
			@Param("create_date") String create_date);
		
//	@Select("select * from daily_stat where product_id=#{0};")
//	public int total_forward_count(String product_id);
	
}
