package com.example.numbergame;

import java.util.ArrayList;
import java.util.Random;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Chronometer;

public class GameSurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback{

	SurfaceHolder surfaceHolder;
	Thread thread;
	Resources res = this.getContext().getResources();
    ArrayList<Bitmap> bmGifArray;
   	private final int totalPanel = 25;
   	BaseBoard baseBoard;
   	AlertDialog.Builder aleartDialog;
    private final Bitmap IMG_CLEAR = BitmapFactory.decodeResource(res, R.drawable.clear);	static final long FPS = 10;
	static final long FRAME_TIME = 1000 / FPS;

	private class BaseBoard{		
		private class NumPanel{
//			int id;
			int assignNumber;
			boolean check;
			int minx, maxx, miny, maxy;
			Bitmap panelBM;
			int countdown;
			
			NumPanel(int j){
//				id = j;
				check = false;
				countdown = 72;
			}
		}
		ArrayList<NumPanel> panelArray;
		private int nextNum;
		Chronometer chronometer;

		
// constructor
		BaseBoard(){
			nextNum=1;
			panelArray = new ArrayList<NumPanel>();
			for(int i = 0; i < totalPanel; i++){
				panelArray.add(new NumPanel(i));
			}
			bmGifArray = new ArrayList<Bitmap>();
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan1));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan2));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan3));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan4));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan5));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan6));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan7));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan8));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan9));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan10));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan11));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan12));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan13));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan14));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan15));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan16));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan17));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan18));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan19));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan20));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan21));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan22));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan23));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan24));
			bmGifArray.add(BitmapFactory.decodeResource(res, R.drawable.pan25));

//          random assignment
			Boolean checkTable[] = new Boolean[totalPanel];
			for(int i=0;i<totalPanel;i++){
				checkTable[i]=false;
			}
			Random rnd = new Random();

			for(int i=0,k=0; i<totalPanel; i++){
				int j = rnd.nextInt(totalPanel - i)+1;
				for(k=0; k<j; k++){
					if(checkTable[k]==true){
						j++;
					}
				}
				panelArray.get(i).assignNumber = k;
				checkTable[k-1]=true;
			}
//  test code for random number assignment
			for(int i=0; i<totalPanel;i++){
				Log.d("MyDEBUG", "number="+i+" , assign ="+panelArray.get(i).assignNumber);
			}
//  setup cordinate for each panel
			for(int i=0; i<totalPanel;i++){
				if(i%5 ==0){
					panelArray.get(i).minx = 10;
					panelArray.get(i).maxx = 90;
				}else if(i%5 == 1){
					panelArray.get(i).minx = 90;
					panelArray.get(i).maxx = 170;
				}else if(i%5 == 2){
					panelArray.get(i).minx = 170;
					panelArray.get(i).maxx = 250;
				}else if(i%5 == 3){
					panelArray.get(i).minx = 250;
					panelArray.get(i).maxx = 330;
				}else if(i%5 == 4){
					panelArray.get(i).minx = 330;
					panelArray.get(i).maxx = 410;
				}
				if(0<= i && i<=4){
					panelArray.get(i).miny = 100;
					panelArray.get(i).maxy = 180;
				}else if(5<=i && i<=9){
					panelArray.get(i).miny = 180;
					panelArray.get(i).maxy = 260;
				}else if(10<=i && i<=14){
					panelArray.get(i).miny = 260;
					panelArray.get(i).maxy = 340;
				}else if(15<=i && i<=19){
					panelArray.get(i).miny = 340;
					panelArray.get(i).maxy = 420;
				}else if(20<=i && i<=24){
					panelArray.get(i).miny = 420;
					panelArray.get(i).maxy = 500;
				}
			}
// setup layout resource .gif file for each panel
			for(int i=0; i<totalPanel;i++){
				panelArray.get(i).panelBM = bmGifArray.get(panelArray.get(i).assignNumber-1);
			}
// setup chronometer
			chronometer = (Chronometer)((Activity)getContext()).findViewById(R.id.chronometer1);
			chronometer.setBase(SystemClock.elapsedRealtime());
			chronometer.start();
		}
	}

	public GameSurfaceView(Context context, SurfaceView sv) {
		super(context);
		surfaceHolder = sv.getHolder();
		surfaceHolder.addCallback(this);
		// TODO 自動生成されたコンストラクター・スタブ
	}
/*	public GameSurfaceView(Context context) {
		super(context);
		surfaceHolder = getHolder();
		if(surfaceHolder == null){
			Log.d("MyDEBUG","surfaceHolder == null");
		}
		surfaceHolder.addCallback(this);
		setFocusable(true);
		requestFocus();
		// TODO 自動生成されたコンストラクター・スタブ
	}*/

	@Override
	public void run() {
		// TODO 自動生成されたメソッド・スタブ
		Canvas canvas = null;
		Paint paint = new Paint();
		long loopCount = 0;
		long waitTime = 0;
		long startTime = System.currentTimeMillis();
		Bitmap drawBm, drawBitmap2;

		while(thread != null){
			try{
				loopCount++;
//				Log.d("MyDEBUG","lockCanvas!");
//				canvas = this.surfaceHolder.lockCanvas();
				canvas = getHolder().lockCanvas();
				if(canvas == null){
//					Log.d("MyDEBUG","canvas == null");
					surfaceHolder.unlockCanvasAndPost(canvas);
					continue;
				}
				Log.d("MyDEBUG", "end of lockCanvas");
				canvas.drawColor(Color.BLACK);
				Log.d("MyDEBUG", "end of drawColor");
/*				Matrix matrix = new Matrix();
				matrix.postRotate(5 * (loopCount % 72), 40,40);
				matrix.preScale((float)(1/1.42), (float)(1/1.42));
				drawBitmap = Bitmap.createBitmap(IMG1, 0,0,80,80,matrix,true);
				if(loopCount % 72 != 2){
					canvas.drawBitmap(drawBitmap,
							200,200, paint);
				}
				if(loopCount % 72 == 2){
					Matrix matrix2 = new Matrix();
					matrix2.postTranslate(100, 100);
					drawBitmap2 = Bitmap.createBitmap(drawBitmap, 0,0,80,80,matrix2,true);
					canvas.drawBitmap(drawBitmap2, 200,200,paint);
				}*/
                Log.d("MyDEBUG", "starting loop!");
				for(int i=0;i<totalPanel;i++){
					Log.d("MyDEBUG","looping --" + i);
					if(baseBoard.panelArray.get(i).check == false){
						canvas.drawBitmap(baseBoard.panelArray.get(i).panelBM, 
								baseBoard.panelArray.get(i).minx, baseBoard.panelArray.get(i).miny
								,paint);
					}else if(baseBoard.panelArray.get(i).check == true && baseBoard.panelArray.get(i).countdown != 0){
						Matrix matrix = new Matrix();
						matrix.postRotate(5*(baseBoard.panelArray.get(i).countdown%72),40,40);
						matrix.preScale((float)(1/1.42), (float)(1/1.42));
						drawBm = Bitmap.createBitmap(baseBoard.panelArray.get(i).panelBM, 0,0,80,80,matrix,true);
						canvas.drawBitmap(drawBm,baseBoard.panelArray.get(i).minx, baseBoard.panelArray.get(i).miny
								,paint);
						baseBoard.panelArray.get(i).countdown--;
					}else{
						canvas.drawBitmap(IMG_CLEAR, 
								baseBoard.panelArray.get(i).minx, baseBoard.panelArray.get(i).miny
								,paint);
					}
				}
				Log.d("MyDEBUG","unlockCanvas!");
				surfaceHolder.unlockCanvasAndPost(canvas);
				waitTime = (loopCount * FRAME_TIME) 
				- System.currentTimeMillis() + startTime;
				Log.d("MyDEBUG"," - "+System.currentTimeMillis()+" - "+startTime);
				if( waitTime > 0 ){
					Log.d("MyDEBUG","wait time="+waitTime*10000);
					Thread.sleep(waitTime);
				
				}
			}catch(Exception e){
				Log.d("MyDEBUG","exception!!!!");
				e.printStackTrace();
			}
		}
	}

	public boolean onTouchEvent(MotionEvent me){
		switch(me.getAction()){
		case MotionEvent.ACTION_DOWN:
			int x = (int)me.getX();
			int y = (int)me.getY();
			int j=-1;
			int i;
			for(i=0;i<totalPanel;i++){
				if(baseBoard.panelArray.get(i).minx <= x && x <= baseBoard.panelArray.get(i).maxx
						&& baseBoard.panelArray.get(i).miny <= y && y <= baseBoard.panelArray.get(i).maxy){
					j = i;
				}
			}
			if(j!=-1){
				if(baseBoard.panelArray.get(j).assignNumber==baseBoard.nextNum){
					baseBoard.nextNum++;
					baseBoard.panelArray.get(j).check = true;
				}
				if(baseBoard.nextNum==26){
// Game End	
//					invalidate();
					baseBoard.chronometer.stop();
					aleartDialog.show();
//					((Activity)getContext()).finish();
				}
			}
//			invalidate();
			break;
		}
		return true;
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

