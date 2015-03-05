package cn.yoyo.mobile.beans;

import android.text.TextUtils;

public class Parameter {
	private String client_id="client_id";
	private String category="电视剧";//电视剧
	private String genre;//古装,言情
	private String area;//大陆,香港
	private String release_year;//2014
	private String orderby="view-today-count";
	/*
	 * view-count: 总播放数 
	 * comment-count: 总评论数
	 * view-today-count: 今日播放数 
	 * view-week-count: 本周播放数 
	 * score: 评分
	 */
	private String streamtypes;//流格式 flvhd flv 3gphd 3gp hd hd2
	private String person;//人物姓名或ID  刘德华
	private int page=1;//页数
	//private int count=20;//页个数
	
	
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	public String getCategory() {
		if(TextUtils.isEmpty(category)){
			category="电视剧";
		}
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getRelease_year() {
		return release_year;
	}
	public void setRelease_year(String release_year) {
		this.release_year = release_year;
	}
	public String getOrderby() {
		return orderby;
	}
	public void setOrderby(int i) {
		/*
		 * view-count: 总播放数 
		 * comment-count: 总评论数
		 * view-today-count: 今日播放数 
		 * view-week-count: 本周播放数 
		 * score: 评分
		 */
		if(i==0){
			orderby="view-today-count";
		}else if(i==1){
			orderby="view-count";
		}else if(i==2){
			orderby="score";
		}else if(i==3){
			orderby="comment-count";
		}else if(i==4){
			orderby="view-today-count";
		}else if(i==5){
			orderby="view-week-count";
		}
	}
	public String getStreamtypes() {
		return streamtypes;
	}
	public void setStreamtypes(String streamtypes) {
		this.streamtypes = streamtypes;
	}
	public String getPerson() {
		return person;
	}
	public void setPerson(String person) {
		this.person = person;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public void clearData(){
		page=1;
		genre=null;
		area=null;
		release_year=null;
		orderby="view-today-count";
	}
	
}
