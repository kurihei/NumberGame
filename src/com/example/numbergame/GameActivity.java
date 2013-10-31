package com.example.numbergame;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.LinearLayout;

public class GameActivity extends Activity {
	private SurfaceView mSvMain;
	private GameSurfaceView mMainDrawArea;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        setContentView(R.layout.game_activity);
//        mSvMain = (SurfaceView)findViewById(R.id.gamev);
//        Log.d("MyDebug","kitakore!");
//        l.addView(new GameView(this));
//        mSvMain.setClickable(true);
//        mMainDrawArea = new GameSurfaceView(this,mSvMain);
//        mMainDrawArea.setClickable(true);
        setContentView(new GameSurfaceView(this));

	}

}
