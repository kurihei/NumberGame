package com.example.numbergame;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class VolleyUtil {
	private static RequestQueue rq;
	
	public static RequestQueue getmQueue(Context context){
		if(rq==null){
			rq = Volley.newRequestQueue(context);
		}
		return rq;
	}
	
}
