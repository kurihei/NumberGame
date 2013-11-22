package com.example.numbergame;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
	
	Button startButton;
	Button twitterButton;
	SharedPreferences sharedPref;
	private String mCallbackURL;
	private Twitter mTwitter;
	private RequestToken mRequestToken;
	private static final String REQUEST_TOKEN = "request_token";
	Editor prefEditor;
	private class RetTwitter{
		private AccessToken actk;
		private String screenName;
		
		RetTwitter(AccessToken gActk, String gScreenName){
			this.actk = gActk;
			this.screenName = gScreenName;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		startButton = (Button) findViewById(R.id.button1);
		startButton.setOnClickListener(this);
		twitterButton = (Button) findViewById(R.id.twitter_oauth);
		twitterButton.setOnClickListener(this);
		sharedPref = getSharedPreferences("pref", MODE_WORLD_READABLE|MODE_WORLD_WRITEABLE);
//		sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		// temporary user name = "YOU" 
	    prefEditor = sharedPref.edit();
//		editor.putString("user", "YOU");
// Twitter Setting
		mCallbackURL = getString(R.string.twitter_callback_url);
		mTwitter = TwitterUtils.getTwitterInstance(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 @Override
	    protected void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        outState.putSerializable(REQUEST_TOKEN, mRequestToken);
	    }

	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	        super.onRestoreInstanceState(savedInstanceState);
	        mRequestToken = (RequestToken) savedInstanceState.getSerializable(REQUEST_TOKEN);
	    }

	
	@Override
	public void onClick(View v) {

		if(v==startButton){
			Intent intent = new Intent(MainActivity.this, GameActivity.class);
			startActivity(intent);
		}else if(v==twitterButton){
			if(!TwitterUtils.hasAccessToken(this)){
		        mCallbackURL = getString(R.string.twitter_callback_url);
		        mTwitter = TwitterUtils.getTwitterInstance(getApplicationContext());
				startAuthorize();
			}else{
				Toast.makeText(this, "You already registered "+sharedPref.getString("user", "YOu"), Toast.LENGTH_SHORT).show();
			}
		}
	}
	
    private void startAuthorize() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    mRequestToken = mTwitter.getOAuthRequestToken(mCallbackURL);
                    return mRequestToken.getAuthorizationURL();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String url) {
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } else {
                    // 失敗。。。
                }
            }
        };
        task.execute();
    }

    @Override
    public void onNewIntent(Intent intent) {
        if (intent == null
                || intent.getData() == null
                || !intent.getData().toString().startsWith(mCallbackURL)) {
            return;
        }
        String verifier = intent.getData().getQueryParameter("oauth_verifier");
        if(verifier==null)return;

        AsyncTask<String, Void, RetTwitter> task = new AsyncTask<String, Void, RetTwitter>() {
            @Override
            protected RetTwitter doInBackground(String... params) {
                try {
                	AccessToken oAAcTk = mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
//               		return mTwitter.getOAuthAccessToken(mRequestToken, params[0]);
                	String screenName = mTwitter.getScreenName();
                	if(screenName==null)screenName ="You";
                	RetTwitter retTwitter = new RetTwitter(oAAcTk, screenName);

                	return retTwitter;
                } catch (TwitterException e) {
//                	Log.d("MyDEBUG", "getOAuthAccessToken throw twitter exception");
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(RetTwitter retTwitter) {
                if (retTwitter.actk != null) {
                    // 認証成功！
                    showToast("認証成功！");
                    successOAuth(retTwitter.actk);
//                	sharedPref = getApplicationContext().getSharedPreferences("pref", MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
//                	prefEditor = sharedPref.edit();
                	Log.d("myDEBUG","retTwitter.screenName = " + retTwitter.screenName);
                    prefEditor.putString("user", retTwitter.screenName);
                    prefEditor.commit();
                } else {
                    // 認証失敗。。。
                    showToast("認証失敗。。。");
                }
            }
        };
        task.execute(verifier);
    }

    private void successOAuth(AccessToken accessToken) {
        TwitterUtils.storeAccessToken(getApplicationContext(), accessToken);
        String idName="YOU";
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}

