package com.siagabanjir;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TabFragment extends ListFragment {
	private ArrayList<DataPintuAir> pintuAir;
	BinderData binder;
	private boolean empty;
	public TabFragment() {
		this(0);
	}
	
	public TabFragment(ArrayList<DataPintuAir> pintuAir) {
		this.pintuAir = pintuAir;
	}
		
	public TabFragment(int status) {
		pintuAir = new ArrayList<DataPintuAir>();
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binder = new BinderData(getActivity(), pintuAir);
		setListAdapter(binder);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list_fragment, container, false);
	}

	@Override
	public void onListItemClick(ListView list, View v, int position, long id) {
		/* 	Temporarily disabled
		 *  if (this.empty == false) {
			Intent i = new Intent(this.getActivity().getBaseContext(), DetailActivity.class);
			
			i.putParcelableArrayListExtra("pintuair", pintuAir);
			i.putExtra("selected", position);
			
			startActivity(i);
		} */
		
	}
	
	public void addData(DataPintuAir dp) {
		pintuAir.add(dp);
	}

	public void clearData() {
		// TODO Auto-generated method stub
		pintuAir.clear();
	}
	
	public void refresh() {
		binder.notifyDataSetChanged();
		if (binder.isEmpty()) {
			this.getListView().setVisibility(View.GONE);
			this.getActivity().findViewById(R.id.tvEmpty).setVisibility(View.VISIBLE);
		} else {
			this.getListView().setVisibility(View.VISIBLE);
			this.getActivity().findViewById(R.id.tvEmpty).setVisibility(View.GONE);
		}
	}
	
	
}
