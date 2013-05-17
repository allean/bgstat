package com.borgge.stat.dbstat;


public class DailyStat {

	private String create_date;

	private String product_id;
	private String product_platform;
	
	private int today_ip;
	private int month_ip;
	private int total_ip;

	private int today_pv;
	private int month_pv;

	private int today_new_user_count;
	private int today_active_user_count;

	private int week_new_user_count;
	private int week_active_user_count;

	private int month_new_user_count;
	private int month_active_user_count;

	private int total_user_count;

	private float day_after_use_rate;
	private float week_after_use_rate;
	private float month_after_use_rate;
	
	private float day_after_new_user_lose_rate;
	private float week_after_new_user_lose_rate;
	private float month_after_new_user_lose_rate;

	private int today_avg_boot_count;
	private int month_avg_boot_count;
	private int total_avg_boot_count;

	private int today_avg_use_count;
	private int month_avg_use_count;
	private int total_avg_use_count;

	private int today_forward_count;
	private int month_forward_count;
	private int total_forward_count;

	private int today_comment_count;
	private int month_comment_count;
	private int total_comment_count;


	private float today_active_user_read_rate;
	private float month_active_user_read_rate;
	private float total_active_user_read_rate;
	
	public DailyStat(){}
	
	public DailyStat(String product_id, String product_platform, String create_date){
		this.product_id = product_id;
		this.product_platform = product_platform;
		this.create_date = create_date;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_platform() {
		return product_platform;
	}

	public void setProduct_platform(String product_platform) {
		this.product_platform = product_platform;
	}

	public int getToday_ip() {
		return today_ip;
	}

	public void setToday_ip(int today_ip) {
		this.today_ip = today_ip;
	}

	public int getMonth_ip() {
		return month_ip;
	}

	public void setMonth_ip(int month_ip) {
		this.month_ip = month_ip;
	}

	public int getTotal_ip() {
		return total_ip;
	}

	public void setTotal_ip(int total_ip) {
		this.total_ip = total_ip;
	}

	public int getToday_pv() {
		return today_pv;
	}

	public void setToday_pv(int today_pv) {
		this.today_pv = today_pv;
	}

	public int getMonth_pv() {
		return month_pv;
	}

	public void setMonth_pv(int month_pv) {
		this.month_pv = month_pv;
	}

	public int getToday_new_user_count() {
		return today_new_user_count;
	}

	public void setToday_new_user_count(int today_new_user_count) {
		this.today_new_user_count = today_new_user_count;
	}

	public int getToday_active_user_count() {
		return today_active_user_count;
	}

	public void setToday_active_user_count(int today_active_user_count) {
		this.today_active_user_count = today_active_user_count;
	}

	public int getWeek_new_user_count() {
		return week_new_user_count;
	}

	public void setWeek_new_user_count(int week_new_user_count) {
		this.week_new_user_count = week_new_user_count;
	}

	public int getWeek_active_user_count() {
		return week_active_user_count;
	}

	public void setWeek_active_user_count(int week_active_user_count) {
		this.week_active_user_count = week_active_user_count;
	}

	public int getMonth_new_user_count() {
		return month_new_user_count;
	}

	public void setMonth_new_user_count(int month_new_user_count) {
		this.month_new_user_count = month_new_user_count;
	}

	public int getMonth_active_user_count() {
		return month_active_user_count;
	}

	public void setMonth_active_user_count(int month_active_user_count) {
		this.month_active_user_count = month_active_user_count;
	}

	public int getTotal_user_count() {
		return total_user_count;
	}

	public void setTotal_user_count(int total_user_count) {
		this.total_user_count = total_user_count;
	}

	public float getDay_after_use_rate() {
		return Float.isNaN(day_after_use_rate)? 0 : day_after_use_rate;
	}

	public void setDay_after_use_rate(float day_after_use_rate) {
		this.day_after_use_rate = day_after_use_rate;
	}

	public float getWeek_after_use_rate() {
		return  Float.isNaN(week_after_use_rate) ? 0 : week_after_use_rate;
	}

	public void setWeek_after_use_rate(float week_after_use_rate) {
		this.week_after_use_rate = week_after_use_rate;
	}

	public float getMonth_after_use_rate() {
		return Float.isNaN(month_after_use_rate) ? 0 : month_after_use_rate;
	}

	public void setMonth_after_use_rate(float month_after_use_rate) {
		this.month_after_use_rate = month_after_use_rate;
	}

	public float getDay_after_new_user_lose_rate() {
		return Float.isNaN(day_after_new_user_lose_rate) ? 0 : day_after_new_user_lose_rate;
	}

	public void setDay_after_new_user_lose_rate(float day_after_new_user_lose_rate) {
		this.day_after_new_user_lose_rate = day_after_new_user_lose_rate;
	}

	public float getWeek_after_new_user_lose_rate() {
		return Float.isNaN(week_after_new_user_lose_rate) ? 0 : week_after_new_user_lose_rate;
	}

	public void setWeek_after_new_user_lose_rate(float week_after_new_user_lose_rate) {
		this.week_after_new_user_lose_rate = week_after_new_user_lose_rate;
	}

	public float getMonth_after_new_user_lose_rate() {
		return Float.isNaN(month_after_new_user_lose_rate)  ? 0 : month_after_new_user_lose_rate;
	}

	public void setMonth_after_new_user_lose_rate(
			float month_after_new_user_lose_rate) {
		this.month_after_new_user_lose_rate = month_after_new_user_lose_rate;
	}

	public int getToday_avg_boot_count() {
		return today_avg_boot_count;
	}

	public void setToday_avg_boot_count(int today_avg_boot_count) {
		this.today_avg_boot_count = today_avg_boot_count;
	}

	public int getMonth_avg_boot_count() {
		return month_avg_boot_count;
	}

	public void setMonth_avg_boot_count(int month_avg_boot_count) {
		this.month_avg_boot_count = month_avg_boot_count;
	}

	public int getTotal_avg_boot_count() {
		return total_avg_boot_count;
	}

	public void setTotal_avg_boot_count(int total_avg_boot_count) {
		this.total_avg_boot_count = total_avg_boot_count;
	}

	public int getToday_avg_use_count() {
		return today_avg_use_count;
	}

	public void setToday_avg_use_count(int today_avg_use_count) {
		this.today_avg_use_count = today_avg_use_count;
	}

	public int getMonth_avg_use_count() {
		return month_avg_use_count;
	}

	public void setMonth_avg_use_count(int month_avg_use_count) {
		this.month_avg_use_count = month_avg_use_count;
	}

	public int getTotal_avg_use_count() {
		return total_avg_use_count;
	}

	public void setTotal_avg_use_count(int total_avg_use_count) {
		this.total_avg_use_count = total_avg_use_count;
	}

	public int getToday_forward_count() {
		return today_forward_count;
	}

	public void setToday_forward_count(int today_forward_count) {
		this.today_forward_count = today_forward_count;
	}

	public int getMonth_forward_count() {
		return month_forward_count;
	}

	public void setMonth_forward_count(int month_forward_count) {
		this.month_forward_count = month_forward_count;
	}

	public int getTotal_forward_count() {
		return total_forward_count;
	}

	public void setTotal_forward_count(int total_forward_count) {
		this.total_forward_count = total_forward_count;
	}

	public int getToday_comment_count() {
		return today_comment_count;
	}

	public void setToday_comment_count(int today_comment_count) {
		this.today_comment_count = today_comment_count;
	}

	public int getMonth_comment_count() {
		return month_comment_count;
	}

	public void setMonth_comment_count(int month_comment_count) {
		this.month_comment_count = month_comment_count;
	}

	public int getTotal_comment_count() {
		return total_comment_count;
	}

	public void setTotal_comment_count(int total_comment_count) {
		this.total_comment_count = total_comment_count;
	}

	public float getToday_active_user_read_rate() {
		return Float.isNaN(today_active_user_read_rate)  ? 0 : today_active_user_read_rate;
	}

	public void setToday_active_user_read_rate(float today_active_user_read_rate) {
		this.today_active_user_read_rate = today_active_user_read_rate;
	}

	public float getMonth_active_user_read_rate() {
		return Float.isNaN(month_active_user_read_rate) ? 0 : month_active_user_read_rate;
	}

	public void setMonth_active_user_read_rate(float month_active_user_read_rate) {
		this.month_active_user_read_rate = month_active_user_read_rate;
	}

	public float getTotal_active_user_read_rate() {
		return Float.isNaN(total_active_user_read_rate) ? 0 : total_active_user_read_rate;
	}

	public void setTotal_active_user_read_rate(float total_active_user_read_rate) {
		this.total_active_user_read_rate = total_active_user_read_rate;
	}

	@Override
	public String toString() {
		return "DailyStat [create_date=" + create_date + ", product_id="
				+ product_id + ", product_platform=" + product_platform
				+ ", today_ip=" + today_ip + ", month_ip=" + month_ip
				+ ", total_ip=" + total_ip + ", today_pv=" + today_pv
				+ ", month_pv=" + month_pv + ", today_new_user_count="
				+ today_new_user_count + ", today_active_user_count="
				+ today_active_user_count + ", week_new_user_count="
				+ week_new_user_count + ", week_active_user_count="
				+ week_active_user_count + ", month_new_user_count="
				+ month_new_user_count + ", month_active_user_count="
				+ month_active_user_count + ", total_user_count="
				+ total_user_count + ", day_after_use_rate="
				+ day_after_use_rate + ", week_after_use_rate="
				+ week_after_use_rate + ", month_after_use_rate="
				+ month_after_use_rate + ", day_after_new_user_lose_rate="
				+ day_after_new_user_lose_rate
				+ ", week_after_new_user_lose_rate="
				+ week_after_new_user_lose_rate
				+ ", month_after_new_user_lose_rate="
				+ month_after_new_user_lose_rate + ", today_avg_boot_count="
				+ today_avg_boot_count + ", month_avg_boot_count="
				+ month_avg_boot_count + ", total_avg_boot_count="
				+ total_avg_boot_count + ", today_avg_use_count="
				+ today_avg_use_count + ", month_avg_use_count="
				+ month_avg_use_count + ", total_avg_use_count="
				+ total_avg_use_count + ", today_forward_count="
				+ today_forward_count + ", month_forward_count="
				+ month_forward_count + ", total_forward_count="
				+ total_forward_count + ", today_comment_count="
				+ today_comment_count + ", month_comment_count="
				+ month_comment_count + ", total_comment_count="
				+ total_comment_count + ", today_active_user_read_rate="
				+ today_active_user_read_rate
				+ ", month_active_user_read_rate="
				+ month_active_user_read_rate
				+ ", total_active_user_read_rate="
				+ total_active_user_read_rate + "]";
	}



	
	
	
//	@Override
//	public String toString() {
//		return "create_date},#{product_id},#{ip},#{today_new_user_count},#{today_active_user_count},#{week_new_user_count},#{week_active_user_count},#{month_new_user_count},#{month_active_user_count},#{total_user_count},#{nextday_not_use_rate},#{week_not_use_rate},#{month_not_use_rate},#{today_avg_boot_count},#{month_avg_boot_count},#{total_avg_boot_count},#{today_avg_use_count},#{month_avg_use_count},#{total_avg_use_count},#{today_forward_count},#{month_forward_count},#{total_forward_count},#{today_comment_count},#{month_comment_count},#{total_comment_count},#{today_active_user_read_rate},#{month_active_user_read_rate},#{total_active_user_read_rate}";
//	}

	

	
	
	
	
//	public String toString() {
//		return "create_date, product_id, ip, today_new_user_count, today_active_user_count, week_new_user_count, week_active_user_count, month_new_user_count, month_active_user_count, total_user_count, nextday_not_use_rate, week_not_use_rate, month_not_use_rate, today_avg_boot_count, month_avg_boot_count, total_avg_boot_count, today_avg_use_time, month_avg_use_time, total_avg_use_time, today_forward_count, month_forward_count, total_forward_count, today_comment_count, month_comment_count, total_comment_count, today_active_user_read_rate, month_active_user_read_rate, total_active_user_read_rate";
//	}
	
	
	

//	
//	public static void main(String[] args) {
//		DailyStat ds = new DailyStat();
//		System.out.println(ds);
//	}
//	
//	
	
}
