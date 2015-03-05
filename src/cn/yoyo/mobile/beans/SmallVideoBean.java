package cn.yoyo.mobile.beans;

import java.io.Serializable;
import java.util.List;

public class SmallVideoBean implements Serializable{
 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String id;      //视频唯一ID
 private String name;//视频标题
 private String link;//视频播放链接
 private String thumbnail;//视频截图
 private List<String> types;//视频格式flvhd flv 3gphd 3gp hd hd2
 private String duration;
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
public List<String> getTypes() {
	return types;
}
public void setTypes(List<String> types) {
	this.types = types;
}
public String getDuration() {
	return duration;
}
public void setDuration(String duration) {
	this.duration = duration;
}

 
}
