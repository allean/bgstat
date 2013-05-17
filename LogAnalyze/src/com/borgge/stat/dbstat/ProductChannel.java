package com.borgge.stat.dbstat;

public class ProductChannel {
	
	private String product_id;
	private String product_platform;
	private String channel_id;
	private String channel_name;
	private String create_date;
	private int article_by_channel;
	
	
	public String getChannel_name() {
		return channel_name;
	}
	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
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
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}
	public int getArticle_by_channel() {
		return article_by_channel;
	}
	public void setArticle_by_channel(int article_by_channel) {
		this.article_by_channel = article_by_channel;
	}
	@Override
	public String toString() {
		return "ProductChannel [product_id=" + product_id
				+ ", product_platform=" + product_platform + ", channel_id="
				+ channel_id + ", channel_name=" + channel_name
				+ ", create_date=" + create_date + ", article_by_channel="
				+ article_by_channel + "]";
	}
	
	
	
}
