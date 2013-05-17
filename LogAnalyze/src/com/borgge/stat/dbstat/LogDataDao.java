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
	
	
	
	
	//��Ծ�û�
	@Select("select count(distinct(user_id)) from data where product_id=#{product_id}" +
			product_platform + 
			create_date +
			" and data_source='s';")
	public int today_active_user_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
		
	//���������û�
	@Select("SELECT count(distinct(user_id)) FROM"+
 				"(SELECT user_id, create_date, create_time, RANK() OVER(PARTITION BY user_id ORDER BY create_date, create_time) RK FROM data" + 
 					" where user_id in (select user_id from  data where product_id=#{product_id} and product_platform like #{product_platform} and create_date=#{create_date})) T1" +
 		 " WHERE RK = 1 and create_date = #{create_date};")
	public int today_new_user_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	//����ip
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
	
	//���û���
	@Select("select count(distinct(user_id)) from data where product_id=#{product_id} " +
			product_platform +
			"and data_source='s';")
	public int total_user_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	//�������������ռ�����¼���û�
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
	
	
	//ʱ�䷶Χ��Ծ
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
	
	
	//ʱ�䷶Χ����
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
	
	
	//��ȡ����dailyͳ��
	@Select("select * from daily_stat where product_id=#{product_id}" +
			product_platform + 
			";")
	public List<DailyStat> allDailyStat(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	//��ȡĳ��ĳƽ̨ĳ��Ʒ��ͳ��
	@Select("select * from daily_stat where product_id=#{product_id} " +
			product_platform + 
			create_date + 
			" limit 1;")
	public DailyStat getDailyStat(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	// ��ȡĳ������
	@Select("select * from daily_stat where create_date=#{create_date}" +
			" order by today_active_user_count desc" +
			";")
	public List<DailyStat> getDailyStatByDate(
			@Param("create_date") String create_date);
	
	// ��ȡĳ������
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
	
	//��ȡĳ��ת����
	@Select("select count(1) from data where product_id=#{product_id} and operate='sns_fwd'" +
			product_platform +
			create_date + 
			";")
	public int forward_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	//��ȡĳ��ʱ���ת����
	@Select("select count(1) from data where product_id=#{product_id} and operate='sns_fwd'" +
			product_platform + 
			create_date_range + 
			";")
	public int forward_count_range(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	//ȫ��ת��
	@Select("select count(1) from data where product_id=#{product_id} and operate='sns_fwd'" +
			product_platform +
			";")
	public int total_forward_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	//��ȡĳ��������
	@Select("select count(1) from data where product_id=#{product_id} and operate='comment'" +
			product_platform + 
			create_date + 
			";")
	public int comment_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	//��ȡĳ��ʱ���������
	@Select("select count(1) from data where product_id=#{product_id} and operate='comment'" +
			product_platform +
			create_date_range +
			";")
	public int comment_count_range(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	//ȫ��������
	@Select("select count(1) from data where product_id=#{product_id} and operate='comment'" +
			product_platform +
			";")
	public int total_comment_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	//��ȡĳ���Ķ�������
	@Select("select count(1) from data where product_id=#{product_id} and operate like 'article%'" +
			product_platform +
			create_date +
			";")
	public int user_read_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("create_date") String create_date);
	
	
	//��ȡĳ��ʱ���Ķ�������
	@Select("select count(1) from data where product_id=#{product_id} and operate like 'article%'" +
			product_platform +
			create_date_range +
			";")
	public int user_read_count_range(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform,
			@Param("start_date") String start_date,
			@Param("to_date") String to_date);
	
	
	//ȫ���Ķ�������
	@Select("select count(1) from data where product_id=#{product_id} and operate like 'article%'" +
			product_platform +
			";")
	public int total_user_read_count(
			@Param("product_id") String product_id,
			@Param("product_platform") String product_platform);
	
	
	
	//ƽ̨�Ͳ�Ʒ
	@Select("select product_platform, product_id from data where create_date=#{create_date} group by product_platform, product_id;")
	public List<ProductAndPlatform> get_product_id_and_platform(
			@Param("create_date") String create_date);
	
	//��Ʒ
	@Select("select product_id from data where create_date=#{create_date} group by product_id;")
	public List<ProductAndPlatform> get_product_id(
			@Param("create_date") String create_date);
		
//	@Select("select * from daily_stat where product_id=#{0};")
//	public int total_forward_count(String product_id);
	
}
