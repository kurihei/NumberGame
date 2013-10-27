package com.example.numbergame;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

public class GameActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game_activity);
        LinearLayout l = (LinearLayout)findViewById(R.id.gamev);
        Log.d("MyDebug","kitakore!");
        l.addView(new GameView(this));
	}

}
