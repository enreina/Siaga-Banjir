package com.siagabanjir.follow;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

public class FollowPintuAir {
	public static final String PREFS_NAME="FollowPrefs";
	
	private HashMap<String, Boolean> listFollow;
	private Context context;
	
	public FollowPintuAir(Context context) {
		this.context = context;
		listFollow = new HashMap<String, Boolean>();
		readFromStorage();
	}
	
	public void readFromStorage() {
		SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
	}
	
	
}
