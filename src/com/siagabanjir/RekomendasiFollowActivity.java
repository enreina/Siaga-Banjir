package com.siagabanjir;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.siagabanjir.follow.FollowPintuAir;
import com.siagabanjir.places.MyPlaces;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RekomendasiFollowActivity extends ActionBarActivity {
	private ActionBar actionBar;
	private ListView listRekomendasi;
	private TextView txtLocation;
	private FollowPintuAir followPintuAir;
	private BinderRekomendasiFollow binder;
	private String nama;
	private LatLng location;

	private MyPlaces myPlaces;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rekomendasi_follow);

		actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ico_actionbarcopy);
		actionBar.setDisplayShowTitleEnabled(false);

		Intent i = getIntent();

		ArrayList<DataPintuAir> rekomendasi = i
				.getParcelableArrayListExtra("inarea");

		listRekomendasi = (ListView) findViewById(R.id.rekomendasi_list);
		binder = new BinderRekomendasiFollow(this, rekomendasi, this);

		listRekomendasi.setAdapter(binder);
		txtLocation = (TextView) findViewById(R.id.location);
		
		location = new LatLng(i.getDoubleExtra("lat", 0), i.getDoubleExtra(
				"long", 0));
		
		txtLocation.setText(location.toString());

		Geocoder geocoder = new Geocoder(this);
		try {
			Address addr = geocoder.getFromLocation(location.latitude,
					location.longitude, 1).get(0);
			txtLocation.setText(addr.getAddressLine(0) + ", "
					+ (addr.getLocality() == null ? "" : addr.getLocality())
					+ ", " + addr.getSubAdminArea());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		followPintuAir = new FollowPintuAir(this);
		ArrayList<String> listFollowing = followPintuAir.getListFollowing();

		myPlaces = new MyPlaces(this);

		nama = i.getStringExtra("nama");
		if (nama != null) {
			((EditText) findViewById(R.id.location_name)).setText(nama);
			myPlaces.removePlace(nama);
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
		
		if (rekomendasi.isEmpty()) {
			findViewById(R.id.rekomendasiEmpty).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.rekomendasiEmpty).setVisibility(View.GONE);
		}

		actionBar.setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rekomendasi_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_save:
			savePlace();
			return true;
		case R.id.action_discard:
			discardPlace();
			return true;
		case R.id.action_about:
			Intent ii = new Intent(this, AboutActivity.class);
			startActivity(ii);
			return true;
		case R.id.action_information:
			Intent i = new Intent(this, InformationActivity.class);
			startActivity(i);
			return true;
		case R.id.action_tutorial:
			Intent iii = new Intent(this, WalkthroughActivity.class);
			startActivity(iii);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	public void onBackPressed() {
		savePlace();
		super.onBackPressed();
	}

	private void discardPlace() {
		Intent i = new Intent();
		i.putExtra("lat", location.latitude);
		i.putExtra("long", location.longitude);
		this.setResult(Activity.RESULT_OK, i);

		finish();

	}

	private void savePlace() {
		EditText etLocName = (EditText) findViewById(R.id.location_name);
		String locName = etLocName.getText().toString();
		if (locName.isEmpty()) {
			Toast.makeText(this, "You have to specify the location's name",
					Toast.LENGTH_SHORT).show();
			return;
		}

		myPlaces.savePlace(locName, location);
		Intent i = new Intent();
		i.putExtra("lat", location.latitude);
		i.putExtra("long", location.longitude);
		this.setResult(Activity.RESULT_OK, i);

		finish();

	}

	public void followPintuAir(String pintuAir) {
		followPintuAir.followPintuAir(pintuAir);
	}

	public void unfollowPintuAir(String pintuAir) {
		followPintuAir.unfollowPintuAir(pintuAir);
	}

}
