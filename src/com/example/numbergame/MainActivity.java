package com.example.numbergame;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{
	
	Button startButton;
	SharedPreferences sharedPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startButton = (Button) findViewById(R.id.button1);
		startButton.setOnClickListener(this);
		sharedPref = getSharedPreferences("pref", MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
// temporary user name = "YOU"
		Editor editor = sharedPref.edit();
		editor.putString("user", "YOU");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		if(v==startButton){
			Intent intent = new Intent(MainActivity.this, GameActivity.class);
//			intent.putExtra("org.jpn.techbooster.demo.intent.testString", "!TEST STRING!");
			startActivity(intent);
		}
	}

}
