package com.siagabanjir;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;
import com.siagabanjir.places.MyPlaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListFragmentActivity extends ActionBarActivity {

	private ActionBar actionBar;
	private ListView listPlaces;
	private BinderPlaces binderPlaces;
	
	private MyPlaces myPlaces;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_fragment_places);

		actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ico_actionbarcopy);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);

		
		myPlaces = new MyPlaces(this);
		binderPlaces = new BinderPlaces(this, myPlaces.getPlaces());
		if (binderPlaces.getCount() == 0) {
			findViewById(R.id.placeEmpty).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.placeEmpty).setVisibility(View.GONE);
		}
		
		
		listPlaces = (ListView) findViewById(R.id.listPlaces);
		listPlaces.setAdapter(binderPlaces);
		
		listPlaces.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				LatLng loc = binderPlaces.placesLatLng.get(position);
				Intent i = new Intent(ListFragmentActivity.this, RekomendasiFollowActivity.class);
				ArrayList<DataPintuAir> inArea = MyPlaceFragment.checkLocation(loc);
				i.putParcelableArrayListExtra("inarea", inArea);
				i.putExtra("nama", binderPlaces.myPlaces.get(loc));
				i.putExtra("lat", loc.latitude);
				i.putExtra("long", loc.longitude);

				startActivityForResult(i, 1);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.overflow, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
