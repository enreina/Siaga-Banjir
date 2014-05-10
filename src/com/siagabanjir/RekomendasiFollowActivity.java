package com.siagabanjir;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class RekomendasiFollowActivity extends ActionBarActivity {
	private ActionBar actionBar;
	private ListView listRekomendasi;
	private TextView txtLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rekomendasi_follow);

		actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ico_actionbar);
		actionBar.setDisplayShowTitleEnabled(false);

		Intent i = getIntent();

		ArrayList<DataPintuAir> rekomendasi = i
				.getParcelableArrayListExtra("inarea");

		listRekomendasi = (ListView) findViewById(R.id.rekomendasi_list);
		listRekomendasi.setAdapter(new BinderRekomendasiFollow(this,
				rekomendasi));
		txtLocation = (TextView) findViewById(R.id.location);
		
		Geocoder geocoder  = new Geocoder(this);
		try {
			Address addr = geocoder.getFromLocation(i.getDoubleExtra("lat", 0), i.getDoubleExtra("long", 0), 1).get(0);
			txtLocation.setText(addr.getAddressLine(0));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		/**
		 * ArrayList<DataPintuAir> curr =
		 * i.getParcelableArrayListExtra("pintuair"); int position =
		 * i.getIntExtra("selected", 0); final DataPintuAir pintuair =
		 * (DataPintuAir)curr.get(position); String nama =
		 * pintuair.getNama().toUpperCase(); String tinggiAir =
		 * pintuair.getTinggiAir()[0] + ""; String status =
		 * pintuair.getStatus()[0];
		 **/

		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.home_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean OnOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		case R.id.action_information:
			Intent i = new Intent(this, InformationActivity.class);
			startActivity(i);
		default:
			return super.onOptionsItemSelected(item);
		}

	}

}
