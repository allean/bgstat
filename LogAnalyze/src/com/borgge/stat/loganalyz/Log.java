package com.borgge.stat.loganalyz;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Log {

	String device_id = "";
	
	String create_date = "";
	String create_time = "";
	
	String ip = "";
	String user_id = "";
	String product_id = "banbao";
	String product_version = "1.0";
	String product_platform = "";
	String distribute_oem_id = "";
	String data_source = "s";
	String device_oem = "";
	String netword_brand = "";
	String location = "";
	
	String operate = "";
	String item_id = "";
	String channel_id = "";
	String app_id = "";
	String sns_platform = "";
	String opt_data = "";
	
	

	
	public String toSqlString() {
		return device_id + "," + create_date + "," + create_time + "," + ip
				+ "," + user_id + "," + product_id + "," + product_version
				+ "," + product_platform + "," + distribute_oem_id + ","
				+ data_source + "," + device_oem + "," + netword_brand + ","
				+ location + "," + operate + "," + item_id + "," + channel_id
				+ "," + app_id + "," + sns_platform + "," + opt_data;
	}




	public String getDevice_id() {
		return device_id;
	}




	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}




	public String getCreate_date() {
		return create_date;
	}




	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}




	public String getCreate_time() {
		return create_time;
	}




	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}




	public String getIp() {
		return ip;
	}




	public void setIp(String ip) {
		this.ip = ip;
	}




	public String getUser_id() {
		return user_id;
	}




	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}




	public String getProduct_id() {
		return product_id;
	}




	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}




	public String getProduct_version() {
		return product_version;
	}




	public void setProduct_version(String product_version) {
		this.product_version = product_version;
	}




	public String getProduct_platform() {
		return product_platform;
	}




	public void setProduct_platform(String product_platform) {
		this.product_platform = product_platform;
	}




	public String getDistribute_oem_id() {
		return distribute_oem_id;
	}




	public void setDistribute_oem_id(String distribute_oem_id) {
		this.distribute_oem_id = distribute_oem_id;
	}




	public String getData_source() {
		return data_source;
	}




	public void setData_source(String data_source) {
		this.data_source = data_source;
	}




	public String getDevice_oem() {
		return device_oem;
	}




	public void setDevice_oem(String device_oem) {
		this.device_oem = device_oem;
	}




	public String getNetword_brand() {
		return netword_brand;
	}




	public void setNetword_brand(String netword_brand) {
		this.netword_brand = netword_brand;
	}




	public String getLocation() {
		return location;
	}




	public void setLocation(String location) {
		this.location = location;
	}




	public String getOperate() {
		return operate;
	}




	public void setOperate(String operate) {
		this.operate = operate;
	}




	public String getItem_id() {
		return item_id;
	}




	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}




	public String getChannel_id() {
		return channel_id;
	}




	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}




	public String getApp_id() {
		return app_id;
	}




	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}




	public String getSns_platform() {
		return sns_platform;
	}




	public void setSns_platform(String sns_platform) {
		this.sns_platform = sns_platform;
	}




	public String getOpt_data() {
		return opt_data;
	}




	public void setOpt_data(String opt_data) {
		this.opt_data = opt_data;
	}




	public static void main(String[] args) {
		Pattern p = Pattern
				.compile("platform: [\\w|.|-]+");
		
		String s = "2013-05-07 19:12:50,878 INFO ip: 120.194.25.72, UA: NativeHost | platform: wp8_3.3-s, product: banbao , OEM: NOKIA_RM-846_apac_prc_225 | get content item with pic, user id: 4032125 item id:16941625411181020188 channel id: 32, position: ";
//		String s = "2013-05-07 19:12:50,903 INFO ip: 112.225.159.78 | platform: win8, product: banbao | Add Coin Product:banbao version:None";
		Matcher matcher = p.matcher(s);
		
//		System.out.println(matcher.find());
		
		while(matcher.find()){
			System.out.println(matcher.group());
		}
		
		
	}
	
}
