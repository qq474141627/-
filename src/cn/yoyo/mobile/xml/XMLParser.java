package cn.yoyo.mobile.xml;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import android.text.TextUtils;
import android.util.Log;

import cn.yoyo.mobile.beans.Categorie;
import cn.yoyo.mobile.beans.Parameter;
import cn.yoyo.mobile.beans.SmallVideoBean;
import cn.yoyo.mobile.beans.VideoBean;
@SuppressWarnings("deprecation")
public class XMLParser {
	
	public static ArrayList<VideoBean> getVideoBeans(Parameter p){
		ArrayList<VideoBean> list=new ArrayList<VideoBean>();
		StringBuffer string=new StringBuffer("https://openapi.youku.com/v2/shows/by_category.json?client_id=426249cfc92d3b67");
			string.append("&category="+URLEncoder.encode(p.getCategory()));
        if(!TextUtils.isEmpty(p.getGenre())){
        	string.append("&genre="+URLEncoder.encode(p.getGenre()));
		}
        if(!TextUtils.isEmpty(p.getArea())){
        	string.append("&area="+URLEncoder.encode(p.getArea()));
        }
        if(!TextUtils.isEmpty(p.getRelease_year())){
        	string.append("&release_year="+URLEncoder.encode(p.getRelease_year()));
        }
        if(!TextUtils.isEmpty(p.getOrderby())){
        	string.append("&orderby="+URLEncoder.encode(p.getOrderby()));
        }
        	string.append("&page="+URLEncoder.encode(p.getPage()+""));
		try {
			String url=XMLUtil.readJsonFromUrl(string.toString());
			JSONObject obj=new JSONObject(url);
			JSONArray array=obj.optJSONArray("shows");
			for(int i=0;i<array.length();i++){
				obj=array.optJSONObject(i);
				VideoBean bean=new VideoBean();
				bean.setId(obj.optString("id"));
				bean.setThumbnail(obj.optString("poster"));
				bean.setName(obj.optString("name"));
				bean.setLink(obj.optString("link"));
				bean.setEpisode_count(obj.optString("episode_count"));
				bean.setEpisode_updated(obj.optString("episode_updated"));
				bean.setCategory(obj.optString("category"));
				bean.setView_count(obj.optString("view_count"));
				bean.setPublished(obj.optString("published"));
				bean.setScore(obj.optString("score"));
				bean.setFavorite_count(obj.optString("favorite_count"));
				JSONArray arr=obj.optJSONArray("streamtypes");
				List<String> types=new ArrayList<String>();
				for(int j=0;j<arr.length();j++){
					types.add(arr.opt(j).toString());
				}
				bean.setTypes(types);
				list.add(bean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static void getVideoInfo(VideoBean bean){
		StringBuffer string=new StringBuffer("https://openapi.youku.com/v2/shows/show.json?client_id=426249cfc92d3b67&show_id="+bean.getId());
		try {
			String url=XMLUtil.readJsonFromUrl(string.toString());
			JSONObject obj=new JSONObject(url);
				bean.setId(obj.optString("id"));
				bean.setThumbnail(obj.optString("poster"));
				bean.setName(obj.optString("name"));
				bean.setLink(obj.optString("link"));
				bean.setEpisode_count(obj.optString("episode_count"));
				bean.setEpisode_updated(obj.optString("episode_updated"));
				bean.setCategory(obj.optString("category"));
				bean.setView_count(obj.optString("view_count"));
				bean.setPublished(obj.optString("published"));
				bean.setScore(obj.optString("score"));
				bean.setFavorite_count(obj.optString("favorite_count"));
				bean.setDescription(obj.optString("description"));
				bean.setUp_count(obj.optString("up_count"));
				bean.setDown_count(obj.optString("down_count"));
				bean.setGenre(obj.optString("genre"));
				bean.setArea(obj.optString("area"));
				bean.setView_week_count(obj.optString("view_week_count"));
				bean.setComment_count(obj.optString("comment_count"));
				if(bean.getCategory().equals("电影")||bean.getCategory().equals("电视剧")){
					obj=obj.optJSONObject("attr");
					JSONArray array=obj.optJSONArray("director");
					if(array!=null){
						String director="";
						for(int i=0;i<array.length();i++){
							director+=array.optJSONObject(i).optString("name")+" ";
						}
						bean.setDirector("导演："+director);
					}
					array=obj.optJSONArray("performer");
					if(array!=null){
						String performer="";
						for(int i=0;i<array.length();i++){
							performer+=array.optJSONObject(i).optString("name")+" ";
						}
						bean.setPerformer("演员："+performer);
					}
				}else if(bean.getCategory().equals("综艺")){
					obj=obj.optJSONObject("attr");
					JSONArray array=obj.optJSONArray("host");
					if(array!=null){
						String director="";
						for(int i=0;i<array.length();i++){
							director+=array.optJSONObject(i).optString("name")+" ";
						}
						bean.setDirector("主持人："+director);
					}
				}else if(bean.getCategory().equals("动漫")){
					obj=obj.optJSONObject("attr");
					JSONArray array=obj.optJSONArray("director");
					if(array!=null){
						String director="";
						for(int i=0;i<array.length();i++){
							director+=array.optJSONObject(i).optString("name")+" ";
						}
						bean.setDirector(director);
					}
				}else if(bean.getCategory().equals("音乐")){
					obj=obj.optJSONObject("attr");
					JSONArray array=obj.optJSONArray("singer");
					if(array!=null){
						String director="";
						for(int i=0;i<array.length();i++){
							director+=array.optJSONObject(i).optString("name")+" ";
						}
						bean.setDirector("主唱："+director);
					}
				}
				else if(bean.getCategory().equals("纪录片")){
					obj=obj.optJSONObject("attr");
					JSONArray array=obj.optJSONArray("director");
					if(array!=null){
						String director="";
						for(int i=0;i<array.length();i++){
							director+=array.optJSONObject(i).optString("name")+" ";
						}
						bean.setDirector("导演："+director);
					}
					 array=obj.optJSONArray("host");
					if(array!=null){
						String director="";
						for(int i=0;i<array.length();i++){
							director+=array.optJSONObject(i).optString("name")+" ";
						}
						bean.setPerformer("主持人："+director);
					}
				}else if(bean.getCategory().equals("教育")){
					obj=obj.optJSONObject("attr");
					JSONArray array=obj.optJSONArray("teacher");
					if(array!=null){
						String director="";
						for(int i=0;i<array.length();i++){
							director+=array.optJSONObject(i).optString("name")+" ";
						}
						bean.setDirector("教师："+director);
					}
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Categorie> getCategories(){
		ArrayList<Categorie> list=new ArrayList<Categorie>();
		String string="https://openapi.youku.com/v2/schemas/show/category.json";
		try {
			String url=XMLUtil.readJsonFromUrl(string);
			JSONObject obj=new JSONObject(url);
			JSONArray array=obj.optJSONArray("categories");
			for(int i=0;i<array.length();i++){
				obj=array.optJSONObject(i);
				Categorie bean=new Categorie();
				bean.setName(obj.optString("label"));
				Log.i("TAG999", "----"+bean.getName());
				JSONArray arr=obj.optJSONArray("genre");
				List<String> genre=new ArrayList<String>();
				for(int j=0;j<arr.length();j++){
					genre.add(arr.optJSONObject(j).getString("label"));
					Log.i("TAG999", arr.optJSONObject(j).getString("label"));
				}
				bean.setGenre(genre);
				list.add(bean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static ArrayList<SmallVideoBean> getSmallVideoBeans(String id,int page){
		ArrayList<SmallVideoBean> list=new ArrayList<SmallVideoBean>();
		try {
			String url=XMLUtil.readJsonFromUrl("https://openapi.youku.com/v2/shows/videos.json?client_id=426249cfc92d3b67&show_id="+id
					+"&count=100&page="+page);
			JSONObject obj=new JSONObject(url);
			JSONArray array=obj.optJSONArray("videos");
			for(int i=0;i<array.length();i++){
				obj=array.optJSONObject(i);
				SmallVideoBean bean=new SmallVideoBean();
				bean.setId(obj.optString("id"));
				bean.setThumbnail(obj.optString("thumbnail"));
				bean.setName(obj.optString("title"));
				bean.setLink(obj.optString("link"));
				bean.setDuration("duration");
				JSONArray arr=obj.optJSONArray("streamtypes");
				List<String> types=new ArrayList<String>();
				for(int j=0;j<arr.length();j++){
					types.add(arr.opt(j).toString());
				}
				bean.setTypes(types);
				list.add(bean);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
  
}
