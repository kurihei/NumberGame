package com.example.numbergame;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class WorldRActivity extends Activity implements OnClickListener{
	Button topButton;
	private ArrayList<WorldS> arrWorldS; 
	private ListWorldSAdapter lwsAdapter;
	private ListView listView;
    private RequestQueue mQueue;
    private Bitmap bmp;
    private ArrayList<String> urlList = new ArrayList<String>();


	class WorldS{
		private Bitmap iconImage;
		private String screenName;
		private String date;
		private String highScore;
		
		public WorldS(Bitmap iconImage, String screenName, String date, String highScore){
			this.iconImage = iconImage;
			this.screenName = screenName;
			this.date = date;
			this.highScore = highScore;
		}
		
		public Bitmap getIconImage(){
			return iconImage;
		}
		
		public String getScreenName(){
			return screenName;
		}
		
		public String getDate(){
			return date;
		}
		
		public String getHighScore(){
			return highScore;
		}
	}
	
	private static class ViewHolder{
		ImageView image;
		TextView tvScreenName;
		TextView tvDate;
		TextView tvHighScore;
	}
	
	class ListWorldSAdapter extends BaseAdapter {
		private Context context;
		private List<WorldS> list;
		
		public ListWorldSAdapter(Context context){
			super();
			this.context = context;
		}

		public ListWorldSAdapter(Context context, ArrayList<WorldS> worldSList){
			super();
			this.context = context;
		}
		@Override
		public int getCount() {
			return arrWorldS.size();
		}

		@Override
		public Object getItem(int position) {
			return arrWorldS.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vHolder;
			View v = convertView;
			Log.d("MyDEBUG","getView()");
			WorldS worldScore = (WorldS)getItem(position);
			if(v==null){
				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = mInflater.inflate(R.layout.worlds_col, null);
				vHolder = new ViewHolder();
				vHolder.image =(ImageView) v.findViewById(R.id.icon);
				vHolder.tvScreenName = (TextView) v.findViewById(R.id.screenname);
				vHolder.tvDate = (TextView) v.findViewById(R.id.date);
				v.setTag(vHolder);
			}else{
				vHolder = (ViewHolder) v.getTag();
			}

			vHolder.image.setImageBitmap(bmp);
			vHolder.tvScreenName.setText(worldScore.getScreenName());
			vHolder.tvDate.setText(worldScore.getDate());
//			vHolder.tvHighScore.setText(worldScore.getHighScore());
			return v;
		}
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.worldr_activity);
		topButton = (Button) findViewById(R.id.button1);
		topButton.setOnClickListener(this);
		arrWorldS = new ArrayList<WorldS>();
/**/
	    Resources r = getResources();
	    bmp = BitmapFactory.decodeResource(r, R.drawable.pan1);
/**/

		listView = (ListView)findViewById(R.id.listView1);	
//		lwsAdapter = new ListWorldSAdapter(this,arrWorldS);
		lwsAdapter = new ListWorldSAdapter(this);
		listView.setAdapter(lwsAdapter);
		mQueue = VolleyUtil.getmQueue(getApplicationContext());
		String url = "http://49.212.118.187/numbergame/dataout.php";
		JsonArrayRequest jsonObjReq = new JsonArrayRequest(url, 
				new Response.Listener<JSONArray>(){
					@Override
					public void onResponse(JSONArray response) {
						parseJSON(response);
						lwsAdapter.notifyDataSetChanged();
						Log.d("MyDEBUG","jason array request success");
					}
				},
				new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO 自動生成されたメソッド・スタブ
						Log.i("VolleyERR", error.getMessage());
					}
				});
		mQueue.add(jsonObjReq);
		Log.d("MyDEBUG","new adapter()   # of element"+arrWorldS.size());

	}

	private void parseJSON(JSONArray json){
		int count = json.length();
		JSONObject jObj = null;
		for(int i=0; i<count;i++){
			try {
				jObj = json.getJSONObject(i);
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			String sUser = null, sDate = null, sScore = null;
			try {
				sUser = jObj.getString("user");
				sDate = jObj.getString("date");
				sScore = jObj.getString("score");
			} catch (JSONException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
			Log.d("MyDEBUG","creating Worlds -" + sUser + sDate + sScore);
		
			WorldS worldS = new WorldS(bmp, sUser, sDate, sScore);
			arrWorldS.add(worldS);
			StringBuilder iconUrl = new StringBuilder();
			iconUrl.append("http://api.dan.co.jp/twicon/");
			iconUrl.append(sUser);
			iconUrl.append("/bigger");
			urlList.add(new String(iconUrl));
			Log.d("MyDEBUG", "arrWorldS size is "+arrWorldS.size());
		}

		
	}

	@Override
	public void onClick(View v) {
		// TODO 自動生成されたメソッド・スタブ
		
	}
}

