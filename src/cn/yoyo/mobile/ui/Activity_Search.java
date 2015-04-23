package cn.yoyo.mobile.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnCloseListener;
import android.widget.ListView;
import android.widget.SearchView;

import cn.yoyo.mobile.beans.VideoBean;
import cn.yoyo.mobile.ui.adapter.HotAdpter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import cn.yo.mobile.yeh.R;

public class Activity_Search extends SherlockActivity{
	private HotAdpter adpter;
	private ArrayList<VideoBean> beans;
	private ListView listView;
	private String key;
	private View pView;
	private SearchView searchView ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_one_type);
		prepareActionBar();
		pView = findViewById(R.id.pb);
		pView.setVisibility(View.GONE);
		listView = (ListView) findViewById(R.id.show_type_list);
		setlistener();
	}

	

	private void getDate() {
		pView.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				getDates();
				handler.sendEmptyMessage(1);
			};
		}.start();
	}

	private void getDates() {
		beans = new ArrayList<VideoBean>();
		//String string = Video.searchVideo(this, key, "15", "1")
		//		.toString();
		//JsonUtil.getSearch(string, beans,15);

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			pView.setVisibility(View.GONE);
			setDate();
		};
	};

	private void setDate() {
			adpter = new HotAdpter(this, beans);
			listView.setAdapter(adpter);
	}

	private void setlistener() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 >= adpter.beans.size()) {
					return;
				}
				Intent intent = new Intent();
				intent.putExtra("movie", adpter.beans.get(arg2));
				intent.setClass(Activity_Search.this,
						Activity_MovieInfo.class);
				startActivity(intent);

			}

		});
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        searchView = (SearchView) menu.findItem(R.id.menu_searchView).getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
       	searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
       	searchView.setIconifiedByDefault(false);
       	searchView.setOnCloseListener(new OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
      //表示输入框文字listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
               public boolean onQueryTextSubmit(String query) {
               	 if (!TextUtils.isEmpty(query)) {
               		key=query;
               		getDate();
               	 }
                   return true;
               }
               public boolean onQueryTextChange(String newText) {
                   return true;
               }
           });
        //searchView.setSubmitButtonEnabled(true);
        return super.onCreateOptionsMenu(menu);
    }
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case android.R.id.home:
		finish();
		break;
	default:
		break;
	}
	return super.onOptionsItemSelected(item);
	}
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
	private void prepareActionBar() {
	    ActionBar mActionBar = getSupportActionBar();
        //mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
       // mActionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        mActionBar.setDisplayHomeAsUpEnabled(true);
    }
}
