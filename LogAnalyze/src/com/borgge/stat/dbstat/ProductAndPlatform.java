package com.borgge.stat.dbstat;

public class ProductAndPlatform {

	private String product_id = "%";
	private String product_platform;
	
	
	public ProductAndPlatform() {
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
	@Override
	public String toString() {
		return "ProductAndPlatform [product_id=" + product_id
				+ ", product_platform=" + product_platform + "]";
	}
	
	

}
