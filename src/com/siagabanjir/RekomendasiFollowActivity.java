package com.siagabanjir;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class RekomendasiFollowActivity extends ActionBarActivity
{
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rekomendasi_follow);
		
		actionBar = getSupportActionBar();
		actionBar.setTitle("");
		
		Intent i = getIntent();
		
		/** ArrayList<DataPintuAir> curr = i.getParcelableArrayListExtra("pintuair");
		int position = i.getIntExtra("selected", 0);
		final DataPintuAir pintuair = (DataPintuAir)curr.get(position);
		String nama = pintuair.getNama().toUpperCase();
		String tinggiAir = pintuair.getTinggiAir()[0] + "";
		String status = pintuair.getStatus()[0]; **/
		
		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.home_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean OnOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	            return true;
	        case android.R.id.home:
	        	NavUtils.navigateUpFromSameTask(this);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
	
}
