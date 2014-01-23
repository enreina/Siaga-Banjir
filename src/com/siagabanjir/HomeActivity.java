package com.siagabanjir;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.siagabanjir.utility.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends ActionBarActivity implements TabListener {
	private ActionBar actionBar;
	private ArrayList<DataPintuAir> dataKritis;
	private ArrayList<DataPintuAir> dataRawan;
	private ArrayList<DataPintuAir> dataWaspada;
	private ArrayList<DataPintuAir> dataNormal;
	private Fragment fragment;
	
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	private static final String url = "http://enreina.besaba.com/siaga-banjir/getdata.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
        
        dataKritis = new ArrayList<DataPintuAir>();
        dataRawan = new ArrayList<DataPintuAir>();
        dataWaspada = new ArrayList<DataPintuAir>();
        dataNormal = new ArrayList<DataPintuAir>();
		
		actionBar = getSupportActionBar();
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    
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
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(url);
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
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(HomeActivity.this, "No internet connection", Toast.LENGTH_LONG).show();
			} catch (NullPointerException e) {
				Toast.makeText(HomeActivity.this, "Error fetching data", Toast.LENGTH_LONG).show();
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

}
