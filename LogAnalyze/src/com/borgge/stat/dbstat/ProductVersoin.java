package com.borgge.stat.dbstat;

public class ProductVersoin {

	private String product_id;
	private String product_platform;
	private String product_version;
	private String create_date;
	private int active_user_count;
	
	
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
	public String getProduct_version() {
		return product_version;
	}
	public void setProduct_version(String product_version) {
		this.product_version = product_version;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getActive_user_count() {
		return active_user_count;
	}
	public void setActive_user_count(int active_user_count) {
		this.active_user_count = active_user_count;
	}
	@Override
	public String toString() {
		return "ProductVersoin [product_id=" + product_id
				+ ", product_platform=" + product_platform
				+ ", product_version=" + product_version + ", create_date="
				+ create_date + ", active_user_count=" + active_user_count
				+ "]";
	}


	
}
