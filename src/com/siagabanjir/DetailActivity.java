package com.siagabanjir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.iguanaui.controls.DataChart;
import com.iguanaui.controls.axes.CategoryAxis;
import com.iguanaui.controls.axes.CategoryXAxis;
import com.iguanaui.controls.axes.NumericAxis;
import com.iguanaui.controls.axes.NumericYAxis;
import com.iguanaui.controls.valuecategory.ColumnSeries;
import com.iguanaui.controls.valuecategory.ValueCategorySeries;
import com.iguanaui.graphics.Brush;
import com.iguanaui.graphics.SolidColorBrush;

public class DetailActivity extends ActionBarActivity {
	private ActionBar actionBar;
	private LinearLayout viewGraph;
	private TextView tvnama;
	private ListView listDetail;

	private DataChart dataChart;
	private List<String> categories = new ArrayList<String>();
	private List<Float> column1 = new ArrayList<Float>();

	// private TextView tvnama, tvtinggiair, tvstatus;
	// private DetailGraph graph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		createData();

		createChart();

		actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ico_actionbar);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);

		Intent i = getIntent();
		/*
		 * ArrayList<DataPintuAir> curr =
		 * i.getParcelableArrayListExtra("pintuair"); int position =
		 * i.getIntExtra("selected", 0); final DataPintuAir pintuair =
		 * (DataPintuAir)curr.get(position); String nama =
		 * pintuair.getNama().toUpperCase(); String tinggiAir =
		 * pintuair.getTinggiAir()[0] + ""; String status =
		 * pintuair.getStatus()[0];
		 * 
		 * tvnama = (TextView) findViewById(R.id.tvNamaPintuAir); listDetail =
		 * (ListView) findViewById(R.id.listDetail); listDetail.setAdapter(new
		 * BinderDataDetail((Activity)this, pintuair));
		 * 
		 * tvtinggiair = (TextView) findViewById(R.id.tvKetinggianAir); tvstatus
		 * = (TextView) findViewById(R.id.tvStatus);
		 * 
		 * tvnama.setText(nama); tvtinggiair.setText(tinggiAir);
		 * tvstatus.setText(status);
		 * 
		 * if (status.equals("NORMAL")) {
		 * tvnama.setBackgroundColor(Color.parseColor("#B7CC54"));
		 * tvtinggiair.setTextColor(Color.parseColor("#B7CC54"));
		 * tvstatus.setTextColor(Color.parseColor("#B7CC54")); } else if
		 * (status.equals("WASPADA")) {
		 * tvnama.setBackgroundColor(Color.parseColor("#FFB031")); //
		 * tvtinggiair.setTextColor(Color.parseColor("#FFB031")); //
		 * tvstatus.setTextColor(Color.parseColor("#FFB031")); } else if
		 * (status.equals("RAWAN")) {
		 * tvnama.setBackgroundColor(Color.parseColor("#F2571E")); //
		 * tvtinggiair.setTextColor(Color.parseColor("#F2571E")); //
		 * tvstatus.setTextColor(Color.parseColor("#F2571E")); } else if
		 * (status.equals("KRITIS")) {
		 * tvnama.setBackgroundColor(Color.parseColor("#A52728")); //
		 * tvtinggiair.setTextColor(Color.parseColor("#A52728")); //
		 * tvstatus.setTextColor(Color.parseColor("#A52728")); }
		 * 
		 * // viewGraph = (LinearLayout) findViewById(R.id.viewGraph); // graph
		 * = new DetailGraph(this, pintuair.getTinggiAir(),
		 * pintuair.getStatus(), pintuair.getTanggal(),
		 * pintuair.getRentangWaktu());
		 * 
		 * // viewGraph.addView(graph);
		 * 
		 * Button shareButton = (Button)findViewById(R.id.btnShare);
		 * shareButton.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Bitmap bitmap =
		 * takeScreenshot(); File img = saveBitmap(bitmap); String text =
		 * "Status Tinggi Muka Air di Pintu " + pintuair.getNama() + ", " +
		 * pintuair.getHari() + " (" + pintuair.getTanggalShort() + ") " +
		 * String.format("%02d",pintuair.getWaktuTerakhir()) + ".00 WIB: " +
		 * pintuair.getTinggiAir()[0] + " cm (" + pintuair.getStatus()[0] +
		 * ") http://bit.ly/siagabanjir";
		 * 
		 * 
		 * Intent shareIntent = new Intent();
		 * shareIntent.setAction(Intent.ACTION_SEND);
		 * shareIntent.setType("image/jpeg");
		 * shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		 * shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(img));
		 * startActivity(Intent.createChooser(shareIntent, "Share via")); } });
		 */
	}

	private void createData() {
		Random random = new Random();
		float value1 = 25.0f;
		// float value2=25.0f;

		for (int i = 0; i < 6; ++i) {
			value1 += 2.0 * (random.nextFloat() - 0.5f);
			// value2+=2.0*(random.nextFloat()-0.5f);

			categories.add(Integer.toString(i));
			column1.add(value1);
			// column2.add(value2);
		}
	}

	private void updateData() {
		Random random = new Random();
		float value1 = 25.0f;
		// float value2=25.0f;

		for (int i = 0; i < categories.size(); ++i) {
			value1 += 2.0 * (random.nextFloat() - 0.5f);
			// value2+=2.0*(random.nextFloat()-0.5f);

			column1.set(i, value1);
			// column2.set(i, value2);
		}
	}

	private void createChart() {
		dataChart = (DataChart) findViewById(R.id.dataChart); // get the empty
																// chart view
																// from the
																// activity
																// layout
		dataChart.setHorizontalZoomEnabled(true); // allow horizontal zooming
		dataChart.setVerticalZoomEnabled(false); // don't allow vertical zooming
		// dataChart.setPadding(15, 15, 15, 15);

		// set up an x axis

		CategoryXAxis categoryAxis = new CategoryXAxis();
		categoryAxis.setDataSource(categories); // tell the axis about the data
												// table
		categoryAxis.setLabelFormatter(new CategoryAxis.LabelFormatter() {
			public String format(CategoryAxis axis, Object item) {
				return (String) item; // return the axis item as a string
			}
		});
		dataChart.scales().add(categoryAxis); // all axes must be added to the
												// chart's scales collection
		categoryAxis = (CategoryXAxis) dataChart.scales().get(0);
		categoryAxis.setGap(1.0f);
		categoryAxis.setLabelBrush(new SolidColorBrush(Color
				.parseColor("#424242")));
		categoryAxis.setLabelTextSize(categoryAxis.getLabelTextSize() + 5.0f);
		categoryAxis.setMinorBrush(null);
		categoryAxis.setMajorBrush(null);
		categoryAxis.setStripBrush(null);

		// set up a y axis

		NumericYAxis valueAxis = new NumericYAxis();
		valueAxis.setMinimumValue(0.0f); // the random data look much nicer with
											// a fixed axis range
		valueAxis.setMaximumValue(40.0f); // the random data look much nicer
											// with a fixed axis range
		valueAxis.setLabelFormatter(new NumericAxis.LabelFormatter() {
			public String format(NumericAxis axis, float item, int precision) {
				if (precision != numberFormat.getMinimumFractionDigits()) {
					numberFormat.setMinimumFractionDigits(precision); // set the
																		// formatting
																		// precision
					numberFormat.setMaximumFractionDigits(precision); // set the
																		// formatting
																		// precision
				}

				return numberFormat.format(item); // return item as a string
			}

			final NumberFormat numberFormat = NumberFormat.getInstance(); // numeric
																			// formatter
																			// for
																			// axis
																			// values
		});
		valueAxis
				.setLabelBrush(new SolidColorBrush(Color.parseColor("#424242")));
		valueAxis.setLabelTextSize(valueAxis.getLabelTextSize() + 5.0f);
		valueAxis.setMinorBrush(null);
		valueAxis.setMajorBrush(null);
		valueAxis.setStripBrush(null);
		dataChart.scales().add(valueAxis); // all axes must be added to the
											// chart's scales collection

		{
			ValueCategorySeries series = new ColumnSeries();
			series.setCategoryAxis(categoryAxis); // tell the series its x axis
			series.setValueAxis(valueAxis); // tell the series its y axis
			series.setValueMember(""); // tell the series the data rows are the
										// values
			series.setDataSource(column1); // tell the series the data table

			Brush serieColorBrush = new SolidColorBrush(
					Color.parseColor("#f7c343"));
			series.setBrush(serieColorBrush);

			dataChart.series().add(series); // add the series to the chart
		}
	}

	protected File saveBitmap(Bitmap bitmap) {
		// TODO Auto-generated method stub
		File imagePath = new File(Environment.getExternalStorageDirectory()
				+ "/siagabanjir.jpg");
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
		getMenuInflater().inflate(R.menu.detail, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		// Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_share:
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


	public void refreshData() {

	}

}
