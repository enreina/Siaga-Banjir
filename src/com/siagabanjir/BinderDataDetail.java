package com.siagabanjir;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BinderDataDetail extends BaseAdapter {
	
	LayoutInflater inflater;
	DataPintuAir pintuAir;
	ViewHolderDetail holder;
	public BinderDataDetail() {
		// TODO Auto-generated constructor stub
	}
	
	public BinderDataDetail(Activity act, DataPintuAir pintuAir) {
		
		this.pintuAir = pintuAir;
		
		
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	     
	      vi = inflater.inflate(R.layout.list_row_detail, null);
	      holder = new ViewHolderDetail();
	     
	      holder.tvWaktu = (TextView)vi.findViewById(R.id.tvWaktu);
	      holder.tvTinggiAirDetail = (TextView)vi.findViewById(R.id.tvTinggiAirDetail);
	      holder.tvStatusDetail = (TextView)vi.findViewById(R.id.tvStatusDetail);
	      holder.ivStatusChangeDetail = (ImageView)vi.findViewById(R.id.ivStatusChangeDetail);
	      
	      vi.setTag(holder);
	    }
	    else{
	    	
	    	holder = (ViewHolderDetail)vi.getTag();
	    }

	      // Setting all values in listview
	      
	      holder.tvWaktu.setText(String.format("%02d.00", pintuAir.getWaktu()[position]));
	      holder.tvTinggiAirDetail.setText(pintuAir.getTinggiAir()[position] + "");
	      holder.tvStatusDetail.setText(pintuAir.getStatus()[position]);
	      String status = pintuAir.getStatus()[position];
	      
	      if (status.equals("NORMAL")) {
	    	  holder.tvTinggiAirDetail.setBackgroundColor(Color.parseColor("#B7CC54"));
	      } else if (status.equals("WASPADA")) {
	    	  holder.tvTinggiAirDetail.setBackgroundColor(Color.parseColor("#FFB031"));
	      } else if (status.equals("RAWAN")) {
	    	  holder.tvTinggiAirDetail.setBackgroundColor(Color.parseColor("#F2571E"));
	      } else if (status.equals("KRITIS")) {
	    	  holder.tvTinggiAirDetail.setBackgroundColor(Color.parseColor("#A52728"));
	      }
	      if (position < getCount() - 1) {
		      int curTinggiAir = pintuAir.getTinggiAir()[position];
		      int prevTinggiAir = pintuAir.getTinggiAir()[position + 1];
		      
		      if (curTinggiAir < prevTinggiAir) {
		    	  holder.ivStatusChangeDetail.setImageResource(R.drawable.status_turun);
		      } else if (curTinggiAir > prevTinggiAir) {
		    	  holder.ivStatusChangeDetail.setImageResource(R.drawable.status_naik);
		      } else {
		    	  holder.ivStatusChangeDetail.setImageResource(R.drawable.status_sama);
		      }
	      }
	      
	      return vi;
		
	}
	
	static class ViewHolderDetail{
		
		TextView tvWaktu;
		ImageView ivStatusChangeDetail;
		TextView tvTinggiAirDetail;
		TextView tvStatusDetail;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pintuAir.getJumlahStatus()-1;
	}
	
}
