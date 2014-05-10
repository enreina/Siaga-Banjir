package com.siagabanjir.places;

import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPlaces {
	public static String PREFS_NAME="PlacesPrefs";
	private Context context;
	private SharedPreferences savedPlaces;
	
	
	public MyPlaces(Context context) {
		this.context = context;
		readFromStorage();
	}

	private void readFromStorage() {
		savedPlaces = context.getSharedPreferences(PREFS_NAME, 0);
	}
	
	public void savePlace(String name, LatLng loc) {
		SharedPreferences.Editor editor = savedPlaces.edit();
		editor.putString(name, loc.latitude + "," + loc.longitude);
		editor.apply();
	}
	
	public void removePlace(String name) {
		SharedPreferences.Editor editor = savedPlaces.edit();
		editor.remove(name);
		editor.apply();
	}
	
	public HashMap<LatLng, String> getPlaces() {
		HashMap<LatLng, String> places = new HashMap<LatLng, String>();
		
		for (String name : savedPlaces.getAll().keySet()) {
			String[] location = savedPlaces.getString(name, "").split(",");
			places.put(new LatLng(Double.parseDouble(location[0]), Double.parseDouble(location[1])), name);
		}
		
		return places;
	}
	
	
	
	
	
}
