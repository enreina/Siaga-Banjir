package com.siagabanjir;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.siagabanjir.BinderData.ViewHolder;
import com.siagabanjir.follow.FollowPintuAir;

public class BinderRekomendasiFollow extends BaseAdapter {
	LayoutInflater inflater;
	ArrayList<DataPintuAir> pintuAirCollection;
	ViewHolder holder;
	Context context;

	public BinderRekomendasiFollow() {
		// TODO Auto-generated constructor stub
		this.pintuAirCollection = new ArrayList<DataPintuAir>();
	}

	public BinderRekomendasiFollow(Activity act,
			ArrayList<DataPintuAir> collection, Context context) {

		this.pintuAirCollection = collection;
		
		this.context = context;

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

		final int finPos = position;
		View vi = convertView;
		if (convertView == null) {

			vi = inflater.inflate(R.layout.rekomendasi_follow_list, null);
			holder = new ViewHolder();

			holder.tvPintuAir = (TextView) vi.findViewById(R.id.tvPintuAir);
			holder.btnFollow = (Button) vi.findViewById(R.id.follow_btn);
			holder.btnUnfollow = (Button) vi.findViewById(R.id.unfollow_btn);

			vi.setTag(holder);
		} else {

			holder = (ViewHolder) vi.getTag();
		}

		// Setting all values in listview

		holder.tvPintuAir.setText(pintuAirCollection.get(position).getNama());
		holder.btnFollow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				((View)v.getParent()).findViewById(R.id.unfollow_btn).setVisibility(View.VISIBLE);
				((RekomendasiFollowActivity)context).followPintuAir(pintuAirCollection.get(finPos).getNama());

			}
			
		});
		
		holder.btnUnfollow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
				((View)v.getParent()).findViewById(R.id.follow_btn).setVisibility(View.VISIBLE);
				((RekomendasiFollowActivity)context).unfollowPintuAir(pintuAirCollection.get(finPos).getNama());
			}
		});
		String namaPintuAir = pintuAirCollection.get(position).getNama();
		
		if (DataPintuAir.mapsPintuAir.get(namaPintuAir).getFollowing()) {
			holder.btnFollow.setVisibility(View.GONE);
			holder.btnUnfollow.setVisibility(View.VISIBLE);
		} else {
			holder.btnFollow.setVisibility(View.VISIBLE);
			holder.btnUnfollow.setVisibility(View.GONE);
		}

		return vi;
	}

	static class ViewHolder {

		TextView tvPintuAir;
		Button btnFollow;
		Button btnUnfollow;
	}
}
