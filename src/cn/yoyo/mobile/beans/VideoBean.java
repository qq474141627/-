package cn.yoyo.mobile.beans;

import java.io.Serializable;
import java.util.List;

public class VideoBean implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String id;      //视频唯一ID
 private String name;//视频标题
 private String link;//视频播放链接
 private String thumbnail;//视频截图
 private String episode_count;//视频总集数
 private String episode_updated;//视频更新至
 private String category;//视频分类
 private String view_count;//总播放数
 private String score;//评分
 private String published;//发布时间
 private String paid;//是否付费 0.否 1.是
 private List<String> types;//视频格式flvhd flv 3gphd 3gp hd hd2
 private String favorite_count;//被评论
 
 private String genre;//类型
 private String area;//地区
 private String description;//详情
 private String up_count;//顶数
 private String down_count;//踩数
 private String director;//导演
 private String performer;//主演
 private List<String> links;//资源链接
 private String view_week_count;//本周观看人数
 private String comment_count;//评论数
 
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getLink() {
	return link;
}
public void setLink(String link) {
	this.link = link;
}
public String getThumbnail() {
	return thumbnail;
}
public void setThumbnail(String thumbnail) {
	this.thumbnail = thumbnail;
}
public String getEpisode_count() {
	return episode_count;
}
public void setEpisode_count(String episode_count) {
	this.episode_count = episode_count;
}
public String getEpisode_updated() {
	return episode_updated;
}
public void setEpisode_updated(String episode_updated) {
	this.episode_updated = episode_updated;
}
public String getCategory() {
	return category;
}
public void setCategory(String category) {
	this.category = category;
}
public String getView_count() {
	return view_count;
}
public void setView_count(String view_count) {
	this.view_count = view_count;
}
public String getPublished() {
	return published;
}
public void setPublished(String published) {
	this.published = published;
}
public String getPaid() {
	return paid;
}
public void setPaid(String paid) {
	this.paid = paid;
}
public List<String> getTypes() {
	return types;
}
public void setTypes(List<String> types) {
	this.types = types;
}
public String getScore() {
	return score;
}
public void setScore(String score) {
	this.score = score;
}
public String getFavorite_count() {
	return favorite_count;
}
public void setFavorite_count(String favorite_count) {
	this.favorite_count = favorite_count;
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
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getUp_count() {
	return up_count;
}
public void setUp_count(String up_count) {
	this.up_count = up_count;
}
public String getDown_count() {
	return down_count;
}
public void setDown_count(String down_count) {
	this.down_count = down_count;
}
public String getDirector() {
	return director;
}
public void setDirector(String director) {
	this.director = director;
}
public String getPerformer() {
	return performer;
}
public void setPerformer(String performer) {
	this.performer = performer;
}
public List<String> getLinks() {
	return links;
}
public void setLinks(List<String> links) {
	this.links = links;
}
public String getView_week_count() {
	return view_week_count;
}
public void setView_week_count(String view_week_count) {
	this.view_week_count = view_week_count;
}
public String getComment_count() {
	return comment_count;
}
public void setComment_count(String comment_count) {
	this.comment_count = comment_count;
}

 
}
