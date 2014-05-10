package com.siagabanjir;

import java.util.ArrayList;

import com.siagabanjir.DataPintuAir;
import com.siagabanjir.adapter.TabsPagerAdapter;

import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity implements
		ActionBar.TabListener {

	private Fragment fragment;
	
	private HomeFragment homeFragment;
	private MyPlaceFragment myPlaceFragment;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	
	private ArrayList<DataPintuAir> dataKritis;

	// Tab titles
	private String[] tabs = { "Home", "My Place" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/

		ActionBar actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ico_actionbar);
		actionBar.setDisplayShowTitleEnabled(false);
		
		

		//setUp data
		dataKritis = new ArrayList<DataPintuAir>();
		
		//set up data
		/* for(int i = 0; i < 5; i++) {
			DataPintuAir dp = new DataPintuAir("Pintu air " + i);
			dp.setTanggal("2014/05/01");
			dp.addTinggiAir(528, "KRITIS", 7*i);
			
			dataKritis.add(dp);
		} */

		
		// Initilization
		/* viewPager = (ViewPager) findViewById(R.id.container);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), dataKritis, this);
		
		viewPager.setAdapter(mAdapter); */
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Adding Tabs
		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}
		
		
	
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	        	if (fragment instanceof HomeFragment)
	        		((HomeFragment)fragment).refreshHome();
	            return true;
	        case R.id.action_settings:
	        	return true;
	        case R.id.action_information:
				Intent i = new Intent(this, InformationActivity.class);
				startActivity(i);
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

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		String tag = "CURRENT_FRAGMENT";
		switch (tab.getPosition()) {
		case 0:
			fragment = new HomeFragment(dataKritis, this);
			break;
		case 1:
			fragment = new MyPlaceFragment(this);
			break;
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

}
