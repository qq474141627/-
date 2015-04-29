package cn.yoyo.mobile.ui;


import android.app.Application;
import android.content.Context;
import android.os.Environment;

import cn.yoyo.mobile.util.FileUtil;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class OPlayerApplication extends Application {

	private static OPlayerApplication mApplication;

	/** OPlayer SD卡缓存路径 */
	public static final String OPLAYER_CACHE_BASE = Environment.getExternalStorageDirectory() + "/aplayer";
	/** 视频截图缓冲路径 */
	public static final String OPLAYER_VIDEO_THUMB = OPLAYER_CACHE_BASE + "/thumb/";
	/** 首次扫描 */
	public static final String PREF_KEY_FIRST = "application_first";

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication = this;
		init();
		initImagerLoder();
	}

	private void init() {
		//创建缓存目录
		FileUtil.createIfNoExists(OPLAYER_CACHE_BASE);
		FileUtil.createIfNoExists(OPLAYER_VIDEO_THUMB);
	}

	public static OPlayerApplication getApplication() {
		return mApplication;
	}

	public static Context getContext() {
		return mApplication;
	}

	/** 销毁 */
	public void destory() {
		mApplication = null;
	}
	private void initImagerLoder()
	{
		DisplayImageOptions  options = new DisplayImageOptions.Builder()            
        .cacheInMemory()                                             
        .cacheOnDisc()                                                   
        .displayer(new RoundedBitmapDisplayer(5))       
        .build();
     ImageLoaderConfiguration config2 = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .threadPriority(Thread.NORM_PRIORITY - 2)
        .defaultDisplayImageOptions(options)
        .denyCacheImageMultipleSizesInMemory()
        .discCacheFileNameGenerator(new Md5FileNameGenerator())
        .tasksProcessingOrder(QueueProcessingType.LIFO)
        .enableLogging() 
        .build();
       ImageLoader.getInstance().init(config2);
       
	}

}
