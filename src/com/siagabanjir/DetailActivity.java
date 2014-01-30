package com.siagabanjir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity
{
	private ActionBar actionBar;
	private LinearLayout viewGraph;
	private TextView tvnama;
	private ListView listDetail;
	
	//private TextView tvnama, tvtinggiair, tvstatus;
	//private DetailGraph graph;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		
		actionBar = getSupportActionBar();
		actionBar.setTitle("");
		
		Intent i = getIntent();
		
		ArrayList<DataPintuAir> curr = i.getParcelableArrayListExtra("pintuair");
		int position = i.getIntExtra("selected", 0);
		final DataPintuAir pintuair = (DataPintuAir)curr.get(position);
		String nama = pintuair.getNama().toUpperCase();
		String tinggiAir = pintuair.getTinggiAir()[0] + "";
		String status = pintuair.getStatus()[0];
		
		tvnama = (TextView) findViewById(R.id.tvNamaPintuAir);
		listDetail = (ListView) findViewById(R.id.listDetail);
		listDetail.setAdapter(new BinderDataDetail((Activity)this, pintuair));
		
		/* tvtinggiair = (TextView) findViewById(R.id.tvKetinggianAir);
		tvstatus = (TextView) findViewById(R.id.tvStatus); */
		
		tvnama.setText(nama);
		/* tvtinggiair.setText(tinggiAir);
		tvstatus.setText(status); */
		
		if (status.equals("NORMAL")) {
	    	  tvnama.setBackgroundColor(Color.parseColor("#B7CC54"));
	    	  /* tvtinggiair.setTextColor(Color.parseColor("#B7CC54"));
	    	  tvstatus.setTextColor(Color.parseColor("#B7CC54")); */
	      } else if (status.equals("WASPADA")) {
	    	  tvnama.setBackgroundColor(Color.parseColor("#FFB031"));
//	    	  tvtinggiair.setTextColor(Color.parseColor("#FFB031"));
//	    	  tvstatus.setTextColor(Color.parseColor("#FFB031"));
	      } else if (status.equals("RAWAN")) {
	    	  tvnama.setBackgroundColor(Color.parseColor("#F2571E"));
//	    	  tvtinggiair.setTextColor(Color.parseColor("#F2571E"));
//	    	  tvstatus.setTextColor(Color.parseColor("#F2571E"));
	      } else if (status.equals("KRITIS")) {
	    	  tvnama.setBackgroundColor(Color.parseColor("#A52728"));
//	    	  tvtinggiair.setTextColor(Color.parseColor("#A52728"));
//	    	  tvstatus.setTextColor(Color.parseColor("#A52728"));
	      }
		
		actionBar.setDisplayHomeAsUpEnabled(true);

//		viewGraph = (LinearLayout) findViewById(R.id.viewGraph);
//		graph = new DetailGraph(this, pintuair.getTinggiAir(), pintuair.getStatus(), pintuair.getTanggal(), pintuair.getRentangWaktu());
		
//		viewGraph.addView(graph);
		
		Button shareButton = (Button)findViewById(R.id.btnShare);
		shareButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bitmap bitmap = takeScreenshot();
				File img = saveBitmap(bitmap);
				String text = "Status Tinggi Muka Air di Pintu " + pintuair.getNama() + ", " + pintuair.getHari() + " (" + pintuair.getTanggalShort() + ") " + String.format("%02d",pintuair.getWaktuTerakhir()) + ".00 WIB: " + pintuair.getTinggiAir()[0] + " cm (" + pintuair.getStatus()[0] + ") http://bit.ly/siagabanjir";
				
				
				Intent shareIntent = new Intent();
				shareIntent.setAction(Intent.ACTION_SEND);
				shareIntent.setType("image/jpeg");
				shareIntent.putExtra(Intent.EXTRA_TEXT, text);
				shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(img));
				startActivity(Intent.createChooser(shareIntent, "Share via"));
			}
		});
		
		
	}
	
	

	protected File saveBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		File imagePath = new File(Environment.getExternalStorageDirectory() + "/siagabanjir.jpg");
	    FileOutputStream fos;
	    try {
	        fos = new FileOutputStream(imagePath);
	        bitmap.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
	    } catch (FileNotFoundException e) {
	        Log.e("GREC", e.getMessage(), e);
	    } catch (IOException e) {
	        Log.e("GREC", e.getMessage(), e);
	    }
	    
	    return imagePath;
	}



	protected Bitmap takeScreenshot() {
		// TODO Auto-generated method stub
		View rootView = findViewById(android.R.id.content).getRootView();
		rootView.setDrawingCacheEnabled(true);
		return rootView.getDrawingCache();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		//inflater.inflate(R.menu.home_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	public boolean OnOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_refresh:
	            refreshData();
	            return true;
	        case android.R.id.home:
	        	NavUtils.navigateUpFromSameTask(this);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
	
	public void refreshData() {
		
	}


}
