package cn.yoyo.mobile.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import cn.yo.mobile.yeg.R;

public class Activity_Set extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_set);
		Button but=(Button) findViewById(R.id.button);
		but.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			}
		});
		
	}
}
