package com.siagabanjir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.siagabanjir.utility.JSONParser;

public class HomeActivity extends ActionBarActivity implements TabListener,ConnectionCallbacks, OnConnectionFailedListener, LocationListener {
	private ActionBar actionBar;
	private ArrayList<DataPintuAir> dataKritis;
	private ArrayList<DataPintuAir> dataRawan;
	private ArrayList<DataPintuAir> dataWaspada;
	private ArrayList<DataPintuAir> dataNormal;
	private Fragment fragment;
	private ProgressDialog pd;
	private Context context;
	
	private LocationClient locationClient;
	private LocationRequest locationRequest;
	private boolean locationEnabled;
	
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final String url = "http://labs.pandagostudio.com/siaga-banjir/";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
        
		context = this;
        
        
        dataKritis = new ArrayList<DataPintuAir>();
        dataRawan = new ArrayList<DataPintuAir>();
        dataWaspada = new ArrayList<DataPintuAir>();
        dataNormal = new ArrayList<DataPintuAir>();
		
		actionBar = getSupportActionBar();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setTitle("");
	    
	    Tab tabKritis = actionBar.newTab();
	    
	    tabKritis.setText("KRITIS");
	    tabKritis.setTabListener(this);
        actionBar.addTab(tabKritis);
        
        Tab tabRawan = actionBar.newTab();
        tabRawan.setText("RAWAN");
	    tabRawan.setTabListener(this);
        actionBar.addTab(tabRawan);
        
        Tab tabWaspada = actionBar.newTab().setText("WASPADA");
        tabWaspada.setText("Waspada");
	    tabWaspada.setTabListener(this);
        actionBar.addTab(tabWaspada);
        
        Tab tabNormal = actionBar.newTab();
        tabNormal.setText("Normal");
	    tabNormal.setTabListener(this);
        actionBar.addTab(tabNormal);
        
        refreshHome();
        
	}
	
	
	
	private void setupUserLocation() {
		locationClient = new LocationClient(this, this, this);
		locationRequest = new LocationRequest();
		
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(5);
		locationRequest.setFastestInterval(1);
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationEnabled = false;
			Toast.makeText(this, "Enable location services for accurate data", Toast.LENGTH_SHORT).show();
		}
		
		else  {
			locationEnabled = true;
		}
		
		locationClient.connect();
		
		
		
	}
	
	@Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            actionBar.setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, actionBar.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.home_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	            refreshHome();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
	
	public void refreshHome() {
		new JSONParse().execute();
	}
	
	private class JSONParse extends AsyncTask<String, String, JSONObject> {

		protected void onPreExecute() {
			Toast.makeText(HomeActivity.this, "Updating data...", Toast.LENGTH_LONG).show();
			pd = new ProgressDialog(context);
			pd.setTitle("Updating data...");
			pd.setMessage("Please wait.");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONObject json = null;
			try {
				JSONParser jParser = new JSONParser();
				json = jParser.getJSONFromUrl(url);
				Thread.sleep(5000);
			} catch(InterruptedException e) {
				
			}
			return json;
		}
		
		protected void onProgressUpdate(String... values) {
			Toast.makeText(HomeActivity.this, "Updating data...", Toast.LENGTH_LONG).show();
		}
		
		
		protected void onPostExecute(JSONObject json) {
			dataKritis.clear();
			dataRawan.clear();
			dataWaspada.clear();
			dataNormal.clear();
			
			JSONArray dataPintuAir;
			try {
				dataPintuAir = json.getJSONArray("datapintuair");
				for (int ii=0; ii<dataPintuAir.length(); ii++) {
					JSONObject obj = dataPintuAir.getJSONObject(ii);
					String nama = obj.getString("nama");
					String tanggal = obj.getString("tanggal");
					JSONArray dataTinggi = obj.getJSONArray("tinggiair");
					
					DataPintuAir dp = new DataPintuAir(nama);
					dp.setTanggal(tanggal);
					
					for (int jj=0; jj<dataTinggi.length(); jj++) {
						int tinggi = 0;
						String status = dataTinggi.getJSONObject(jj).getString("status");
						if (!dataTinggi.getJSONObject(jj).getString("tinggi").split(" ")[0].equals("-")) {
							tinggi = Integer.parseInt(dataTinggi.getJSONObject(jj).getString("tinggi").split(" ")[0]);
						} else {
							status = "N/A";
						}
						int waktu = dataTinggi.getJSONObject(jj).getInt("waktu");
						dp.addTinggiAir(tinggi, status, waktu);
					}
					
					String status = dp.getStatus()[0];
					
					
					if (status.equals("KRITIS")) {
						dataKritis.add(dp);
					} else if (status.equals("RAWAN")) {
						dataRawan.add(dp);
					} else if (status.equals("WASPADA")) {
						dataWaspada.add(dp);
					} else if (status.equals("NORMAL")) {
						dataNormal.add(dp);
					}
					
				}
				((TabFragment)getSupportFragmentManager().findFragmentByTag("CURRENT_FRAGMENT")).refresh();
				
				Toast.makeText(HomeActivity.this, "Done!", Toast.LENGTH_LONG).show();
				if (pd != null) {
					pd.dismiss();
				}

		        setupUserLocation();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(HomeActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
				if (pd != null) {
					pd.dismiss();
				}
			} catch (NullPointerException e) {
				Toast.makeText(HomeActivity.this, "Error fetching data or no internet connection", Toast.LENGTH_LONG).show();
				if (pd != null) {
					pd.dismiss();
				}
			}
			
			
		}
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		String tag = "CURRENT_FRAGMENT";
		switch (tab.getPosition()) {
		case 0:
			fragment = new TabFragment(dataKritis);
			break;
		case 1:
			fragment = new TabFragment(dataRawan);
			break;
		case 2:
			fragment = new TabFragment(dataWaspada);
			break;
		case 3:
			fragment = new TabFragment(dataNormal);
			break;
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	protected void onDestroy() {
		if (pd != null) {
			pd.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		locationClient.setMockMode(true);
		Location location = locationClient.getLastLocation();
		if (location != null) {
			Geocoder geocoder = new Geocoder(this);
			try {
				List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
				address.get(0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Toast.makeText(this, "Initial location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_LONG).show();
		}
		else if (location == null && locationEnabled) {
			locationClient.requestLocationUpdates(locationRequest, this);
		} 
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		locationClient.removeLocationUpdates(this);
		Toast.makeText(this, "Location: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
		
	}
	
	

}
