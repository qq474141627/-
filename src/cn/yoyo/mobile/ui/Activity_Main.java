package cn.yoyo.mobile.ui;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import cn.yoyo.mobile.util.FileUtil;
import cn.yoyo.mobile.yef.R;
import cn.yoyo.mobile.yes.SMSReceiver;
import cn.yoyo.slidingmenu.lib.SlidingMenu;
import cn.yoyo.slidingmenu.lib.app.SlidingFragmentActivity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.sup.ab.Manager;
import com.umeng.analytics.MobclickAgent;


public class Activity_Main extends SlidingFragmentActivity{
	private SlidingMenu mSlidingMenu;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_content);
		SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
		if(!preference.getBoolean("hasSend", false)){
			//preference.edit().putBoolean("hasSend", true).commit();
			MobclickAgent.onEvent(this, "receive",SMSReceiver.code);
		}
		setBehindContentView(R.layout.frame_left);
		FileUtil.makeAppCacheDir();
		init();
		
    }

    private void init() {
    	prepareActionBar() ;
		if (mSlidingMenu == null) {
			mSlidingMenu = getSlidingMenu();
			// 设置要使菜单滑动，触碰屏幕的范围
			mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			// 设置阴影宽度
			mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
			// 设置左菜单阴影图片
			mSlidingMenu.setShadowDrawable(R.drawable.shadow);
			// mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			// 设置菜单占屏幕的比例
			mSlidingMenu.setBehindOffset(getWindowManager().getDefaultDisplay().getWidth() / 3);
			// 设置滑动时拖拽效果
			mSlidingMenu.setBehindScrollScale(0);
			mSlidingMenu.setFadeDegree(0.35f);
			// 左侧显示
	        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
	        MenuFragment menuFragment = new MenuFragment();
	        t.replace(R.id.menu, menuFragment);
	        t.replace(R.id.content, new Fragment_TV());
	        t.commit();
	        
		}
		}
		public SlidingMenu getSlideMenu() {
			return mSlidingMenu;
		}
		@Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        return true;
	    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	toggle();
        	break;
        }
        return super.onOptionsItemSelected(item);
    }
   private long exitTime = 0;
   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event)
   {
       if (keyCode == KeyEvent.KEYCODE_BACK && mSlidingMenu.isMenuShowing())
       {
           showContent();
           return true;
       }
       if (keyCode == KeyEvent.KEYCODE_BACK)
       {
    	   /*if ((System.currentTimeMillis() - exitTime) > 2000)
           {
                   Toast.makeText(this, getString(R.string.exit_click), Toast.LENGTH_SHORT).show();
                   exitTime = System.currentTimeMillis();
           } else
           {
        	   System.exit(0);
           }*/
    	   exitApp();
   				return true;
       }
       return super.onKeyDown(keyCode, event);
   }
   
	private void prepareActionBar() {
	    ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        //mActionBar.setDisplayShowTitleEnabled(false);
    }
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		}

		public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		}
		
		private void exitApp(){
			 //退弹广告
				Manager.showAD2(Activity_Main.this,
						new DialogInterface.OnClickListener() {
							//点击事件
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case Dialog.BUTTON_POSITIVE:// yes
									//System.exit(0);
									finish();
									break;
								case Dialog.BUTTON_NEGATIVE:// no
									break;
								}
							}
						});
		   }	
		
}
