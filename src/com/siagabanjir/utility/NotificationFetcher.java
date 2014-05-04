package com.siagabanjir.utility;

import java.util.ArrayList;

import com.siagabanjir.follow.FollowPintuAir;

import android.app.IntentService;
import android.content.Intent;

public class NotificationFetcher extends IntentService {

	public NotificationFetcher(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		FollowPintuAir fpa = new FollowPintuAir(this);
		ArrayList<String> listFollowing = fpa.getListFollowing();
		
		
		
	}

}
