package com.example.numbergame;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.LinearLayout;

public class GameActivity extends Activity {
	private SurfaceView mSv;
	private GameSurfaceView gSv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Log.d("MyDebug","kitakore!");		
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game_activity);
 //       Log.d("MyDebug", "end of setContentView");
 //       mSv = (SurfaceView)findViewById(R.id.gamev);
 //       gSv = new GameSurfaceView(this, mSv);
 //       setContentView(new GameSurfaceView(this));
	}
}
