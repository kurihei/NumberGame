package com.example.numbergame;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Comparator;

import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import twitter4j.Twitter;
import twitter4j.TwitterException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ResultActivity extends Activity implements OnClickListener{
	Button sampleBtn, worldRBtn;
	TextView textView;
	ListView scoreListView;
	Long result;
	MyDBHelper dbhlpr;
    SQLiteDatabase mydb;
    Twitter mTwitter;
    String tweet;
    private RequestQueue mQueue;
    String userName;
    String dateString;
    
    private class NameDateScore {
    	private String name;
    	private String date;
    	private String score;
    	private Long lscore;
    	
    	public NameDateScore(String name, String date, String score, Long lscore){
    		this.name = name;
    		this.date = date;
    		this.score = score;
    		this.lscore = lscore;
    	}
    	public String getName(){
    		return name;
    	}
    	public String getDate(){
    		return date;
    	}
    	public String getScore(){
    		return score;
    	}
    	public Long getLscore(){
    		return lscore;
    	}
    }
    
    public class NdsComparator implements java.util.Comparator<NameDateScore>{

		@Override
		public int compare(NameDateScore lhs, NameDateScore rhs) {
			// TODO 自動生成されたメソッド・スタブ
			return  (lhs.getLscore() < rhs.getLscore())?-1:1;
		}
    	
    }
    
    private static class RowViewHolder{
    	public TextView userNameView;
    	public TextView dateView;
    	public TextView scoreView;
    }

    public class ScoreAdapter extends BaseAdapter{
    	private Context context;
    	private List<NameDateScore> list;

    	public ScoreAdapter(Context context){
    		super();
    		this.context = context;
    	}
    	public ScoreAdapter(Context context, ArrayList<NameDateScore> userlist){
    		super();
    		this.context = context;
    		list = new ArrayList<NameDateScore>(userlist);
    	}
    	
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			NameDateScore nameDateScore = (NameDateScore)getItem(position);
			View v = convertView;
			RowViewHolder rowViewHolder;
	
			if(v==null){
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = inflater.inflate(R.layout.scorecol, null);
				rowViewHolder = new RowViewHolder();
				rowViewHolder.userNameView = (TextView)v.findViewById(R.id.userName);
				rowViewHolder.dateView = (TextView)v.findViewById(R.id.date);
				rowViewHolder.scoreView = (TextView)v.findViewById(R.id.score);
/*				((ViewGroup) v).addView(rowViewHolder.userNameView);
				((ViewGroup) v).addView(rowViewHolder.dateView);
				((ViewGroup) v).addView(rowViewHolder.scoreView);*/

				
				v.setTag(rowViewHolder);
			}else{
				rowViewHolder = (RowViewHolder) v.getTag();
			}
			rowViewHolder.userNameView.setText(nameDateScore.getName());
			rowViewHolder.dateView.setText(nameDateScore.getDate());
			rowViewHolder.scoreView.setText(nameDateScore.getScore());
		
			return v;
		}
    }
 
    
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        SharedPreferences preferences = getSharedPreferences("pref",MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
        sampleBtn = (Button)findViewById(R.id.button1);
		sampleBtn.setOnClickListener(this);
		worldRBtn = (Button)findViewById(R.id.button2);
		worldRBtn.setOnClickListener(this);
		scoreListView =(ListView)findViewById(R.id.listView1);
		scoreListView.setScrollingCacheEnabled(false);
		textView = (TextView)findViewById(R.id.textView1);
		Intent intent = getIntent();
		result = intent.getLongExtra("score", 0);
		textView.setText("Your Score is " + result/1000 + "s"+result%1000+"ms !");		
		dbhlpr = new MyDBHelper(this);
		mydb = dbhlpr.getWritableDatabase();

		userName = preferences.getString("user", "YOU");
//		Date date = new Date();
//		String dateString = date.toString(); 
		dateString = (String) DateFormat.format("yyyy/MM/dd|kk:mm:ss", Calendar.getInstance());
//		String insertSQL = "INSERT into highscore VALUES(null," +  userName 
//			+ dateString + String.valueOf(result) + ");";
		Log.d("MyDEBUG", "start insert!");
		String insertSQL = "INSERT into highscore VALUES( \'" +  userName +"\', \'"
		+ dateString +"\', " + String.valueOf(result) + " );";
		tweet = userName +"\', \'"
		+ dateString +"\', " + String.valueOf(result);
		Log.d("MyDEBUG", "SQL is   "+ insertSQL);
//		mydb.rawQuery(insertSQL,null);
		mydb.execSQL(insertSQL);

		Long highscore = preferences.getLong("highscore", 0);
		Log.d("MyDEBUG","preferences highscore is "+highscore);
		Log.d("MyDEBUG","current result is "+result);
		Editor edit = preferences.edit();
		if(highscore == 0 | highscore > result){
			Log.d("MyDEBUG","highscore record!");
			edit.putLong("highscore", result);
			edit.commit();
			
			if(TwitterUtils.hasAccessToken(getApplicationContext())){
				mTwitter = TwitterUtils.getTwitterInstance(this);
				Log.d("MyDEBUG","Let's tweet!");
				Toast.makeText(this, "Conglatulations!!! HighScore!", Toast.LENGTH_LONG).show();
				AsyncTask<String, Void, Void> task = new AsyncTask<String, Void, Void>(){

					@Override
					protected Void doInBackground(String... params) {
						// TODO 自動生成されたメソッド・スタブ
						try {
							mTwitter.updateStatus("テスト " + tweet); 
						} catch (TwitterException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
						return null;
					}
				
				};
				task.execute();	
			
				pushScoreServer();
			}
		}

// Test Code
		ArrayList pushlist = new ArrayList<NameDateScore>();
		String selectSql = "select * from highscore";
		Cursor c = mydb.rawQuery(selectSql, null);
		boolean isEof = c.moveToFirst();
		Log.d("MyDEBUG","kitakore! # of record is " + c.getCount());
//		for(int i=0;i<c.getCount();i++){
		while(isEof){
			Log.d("MyDEBUG","looping db record");
			String dname = c.getString(0);
			String ddate = c.getString(1);
			Long dresult = c.getLong(2);
			Log.d("MyDEBUG","dname="+dname+" ddate="+ddate+" dresult="+dresult);
			StringBuilder sb = new StringBuilder();
			sb.append(dresult/1000);
			sb.append("s ");
			sb.append(dresult%1000);
			sb.append("ms");
            NameDateScore ndm = new NameDateScore(dname, ddate, new String(sb), dresult);
            pushlist.add(ndm);
            isEof = c.moveToNext();
		}
		Collections.sort(pushlist, new NdsComparator());
		scoreListView.setAdapter(new ScoreAdapter(this, (ArrayList<NameDateScore>) pushlist));
		mydb.close();
		
		
		

/*		Twitter mTwitter = TwitterUtils.getTwitterInstance(this);
		try {
			mTwitter.updateStatus("Twitter4jのテスト");
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}*/
	}

	private void pushScoreServer() {
//		mQueue = Volley.newRequestQueue(this);
		mQueue = VolleyUtil.getmQueue(getApplicationContext());
		StringBuilder pushUrl = new StringBuilder();
		pushUrl.append("http://49.212.118.187/numbergame/pushscore.php?gameuser=");
		pushUrl.append(userName);
		pushUrl.append("&date=");
		pushUrl.append(dateString);
		pushUrl.append("&score=");
		pushUrl.append(result.toString());
		String pushUrls=null;
/*		try {
			pushUrls = URLEncoder.encode(new String(pushUrl), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}*/
		pushUrls = new String(pushUrl);
		Log.d("MyDEBUG", "push high score to server --"+ pushUrls+ " --");
		mQueue.add(new JsonObjectRequest(Method.GET, pushUrls, null,
			new Response.Listener<JSONObject>() {
//			Listener<JSONObject><JSONObject>(){
				@Override
				public void onResponse(JSONObject response){
					Log.d("MyDEBUG","volley");
				}
			},
			null
		));
	}

	private Object Calender() {
		return null;
	}

	@Override
	public void onClick(View v) {
		if(v==sampleBtn){
			finish();
		}
		if(v==worldRBtn){
			Intent intent = new Intent(ResultActivity.this, WorldRActivity.class);
			finish();
			startActivity(intent);
		}
	}
	
	


}
