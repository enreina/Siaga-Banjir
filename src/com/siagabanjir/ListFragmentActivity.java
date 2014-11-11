package com.siagabanjir;

import java.util.ArrayList;

import android.app.Activity;
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

import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.model.LatLng;
import com.siagabanjir.places.MyPlaces;

public class ListFragmentActivity extends ActionBarActivity {

	private ActionBar actionBar;
	private ListView listPlaces;
	private BinderPlaces binderPlaces;
	
	private MyPlaces myPlaces;
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.setReportLocation(true);
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.onStartSession(this, "CZWJXGNWJVHM35JYDTRC");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_fragment_places);

		actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ico_actionbar);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);

		
		myPlaces = new MyPlaces(this);
		binderPlaces = new BinderPlaces(this, myPlaces.getPlaces());
		if (binderPlaces.getCount() == 0) {
			findViewById(R.id.placeEmpty).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.placeEmpty).setVisibility(View.GONE);
		}
		
		//Flurry log
		FlurryAgent.logEvent("View_MySavedPlaces");
		
		
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
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (1): {
			if (resultCode == Activity.RESULT_OK) {
				refreshList();
			}
			break;
		}
		}
	}

	private void refreshList() {
		// TODO Auto-generated method stub
		myPlaces = new MyPlaces(this);
		binderPlaces = new BinderPlaces(this, myPlaces.getPlaces());
		if (binderPlaces.getCount() == 0) {
			findViewById(R.id.placeEmpty).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.placeEmpty).setVisibility(View.GONE);
		}
		
		listPlaces.setAdapter(binderPlaces);
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
