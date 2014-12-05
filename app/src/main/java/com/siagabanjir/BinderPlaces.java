package com.siagabanjir;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.siagabanjir.BinderData.ViewHolder;

public class BinderPlaces extends BaseAdapter {
	LayoutInflater inflater;
	HashMap<LatLng, String> myPlaces;
	ArrayList<String> placesName;
	ArrayList<LatLng> placesLatLng;
	ViewHolder holder;
	Context context;
	
	public BinderPlaces() {
		// TODO Auto-generated constructor stub
	}
	
	public BinderPlaces(Activity act, HashMap<LatLng, String> myPlaces) {
		
		this.myPlaces = myPlaces;
		
		placesName = new ArrayList<String>(this.myPlaces.values());
		placesLatLng = new ArrayList<LatLng>(this.myPlaces.keySet());
		
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = act;
	}
	
	public void clearData() {
		// TODO Auto-generated method stub
		myPlaces.clear();
	}

	public int getCount() {
		return myPlaces.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		 
		View vi=convertView;
	    if(convertView==null){
	     
	      vi = inflater.inflate(R.layout.list_row_places, null);
	      holder = new ViewHolder();
	     
	      holder.tvNamaLokasi = (TextView) vi.findViewById(R.id.namaLokasi);
	      holder.tvAlamat = (TextView) vi.findViewById(R.id.alamat);
	      
	      vi.setTag(holder);
	    }
	    else{
	    	
	    	holder = (ViewHolder)vi.getTag();
	    }

	      // Setting all values in listview
	      
	     	holder.tvNamaLokasi.setText(placesName.get(position));
	     	LatLng location = placesLatLng.get(position);
	     	
	     	Geocoder geocoder = new Geocoder(context);
			try {
				Address addr = geocoder.getFromLocation(location.latitude,
						location.longitude, 1).get(0);
				holder.tvAlamat.setText(addr.getAddressLine(0) + ", "
						+ (addr.getLocality() == null ? "" : addr.getLocality())
						+ ", " + addr.getSubAdminArea());

			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
			
			finally {
	      
				return vi;
			}
	}
	
	static class ViewHolder{
		
		TextView tvNamaLokasi;
		TextView tvAlamat;
	}
}
