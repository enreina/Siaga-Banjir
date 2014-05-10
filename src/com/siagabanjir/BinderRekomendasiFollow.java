package com.siagabanjir;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.siagabanjir.BinderData.ViewHolder;

public class BinderRekomendasiFollow extends BaseAdapter {
	LayoutInflater inflater;
	ArrayList<DataPintuAir> pintuAirCollection;
	ViewHolder holder;
	public BinderRekomendasiFollow() {
		// TODO Auto-generated constructor stub
	}
	
	public BinderRekomendasiFollow(Activity act, ArrayList<DataPintuAir> collection) {
		
		this.pintuAirCollection = collection;
		
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void clearData() {
		// TODO Auto-generated method stub
		pintuAirCollection.clear();
	}
	
	public void addData(DataPintuAir dp) {
		pintuAirCollection.add(dp);
	}
	

	public int getCount() {
		return pintuAirCollection.size();
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
	     
	      vi = inflater.inflate(R.layout.rekomendasi_follow_list, null);
	      holder = new ViewHolder();
	     
	      holder.tvPintuAir = (TextView)vi.findViewById(R.id.tvPintuAir);
	      
	      vi.setTag(holder);
	    }
	    else{
	    	
	    	holder = (ViewHolder)vi.getTag();
	    }

	      // Setting all values in listview
	      
	      holder.tvPintuAir.setText(pintuAirCollection.get(position).getNama());
	      
	      
	      return vi;
	}
	
	static class ViewHolder{
		
		TextView tvPintuAir;
	}
}
