package com.example.numbergame;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TestSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback{

	SurfaceHolder surfaceHolder;
	Thread thread;
	Resources res = getResources();
	private final Bitmap IMG1 = BitmapFactory.decodeResource(res, R.drawable.pan1);
	static final long FPS = 1;
	static final long FRAME_TIME = 1000 / FPS;
	
	public TestSurfaceView(Context context) {
		super(context);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		// TODO 自動生成されたコンストラクター・スタブ

	}

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		Canvas canvas = null;
		Paint paint = new Paint();
		long loopCount = 0;
		long waitTime = 0;
		long startTime = System.currentTimeMillis();
		Bitmap drawBitmap, drawBitmap2;

		while(thread != null){
			try{
				loopCount++;
				canvas = surfaceHolder.lockCanvas();
				canvas.drawColor(Color.BLACK);
				Matrix matrix = new Matrix();
				matrix.postRotate(5 * (loopCount % 72), 40,40);
				matrix.preScale((float)(1/1.42), (float)(1/1.42));
				drawBitmap = Bitmap.createBitmap(IMG1, 0,0,80,80,matrix,true);
//				if(loopCount % 72 != 2){
					canvas.drawBitmap(drawBitmap,
							200,200, paint);
//				}
/*				if(loopCount % 72 == 2){
					Matrix matrix2 = new Matrix();
					matrix2.postTranslate(100, 100);
					drawBitmap2 = Bitmap.createBitmap(drawBitmap, 0,0,80,80,matrix2,true);
					canvas.drawBitmap(drawBitmap2, 200,200,paint);
				}*/

				surfaceHolder.unlockCanvasAndPost(canvas);
				waitTime = (loopCount * FRAME_TIME) 
				- System.currentTimeMillis() - startTime;

			if( waitTime > 0 ){
				Log.d("MyDEBUG","wait time="+waitTime*10000);
				Thread.sleep(waitTime*10000);
				
			}
			Thread.sleep(200);
			}catch(Exception e){}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO 自動生成されたメソッド・スタブ
		thread = new Thread(this);
		thread.start();
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

}
