package com.siagabanjir.follow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;

public class FollowPintuAir {
	public static final String PREFS_NAME="FollowPrefs";
	
	private Map<String, ?> listFollow;
	private Context context;
	private SharedPreferences settings;
	
	public FollowPintuAir(Context context) {
		this.context = context;
		listFollow = new HashMap<String, Boolean>();
		readFromStorage();
	}
	
	public void readFromStorage() {
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		listFollow = settings.getAll();
	}
	
	
	
	public void followPintuAir(String pintuAir) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(pintuAir, true);
	}
	
	public void unfollowPintuAir(String pintuAir) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(pintuAir, false);
	}
	
	public ArrayList<String> getListFollowing() {
		ArrayList<String> listFollowing = new ArrayList<String>();
		for (String key : listFollow.keySet()) {
			if (listFollow.get(key) instanceof Boolean && ((Boolean)listFollow.get(key)) == true) {
				listFollowing.add(key);
			}
		}
		
		return listFollowing;
	}
	
	public boolean isFollowing(String pintuAir) {
		return settings.getBoolean(pintuAir, false);
	}
	
	
}
