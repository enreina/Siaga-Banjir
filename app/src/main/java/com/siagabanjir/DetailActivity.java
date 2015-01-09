package com.siagabanjir;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.iguanaui.controls.DataChart;
import com.iguanaui.controls.axes.CategoryAxis;
import com.iguanaui.controls.axes.CategoryXAxis;
import com.iguanaui.controls.axes.NumericAxis;
import com.iguanaui.controls.axes.NumericYAxis;
import com.iguanaui.controls.valuecategory.ColumnSeries;
import com.iguanaui.controls.valuecategory.ValueCategorySeries;
import com.iguanaui.graphics.Brush;
import com.iguanaui.graphics.SolidColorBrush;
import com.siagabanjir.follow.FollowPintuAir;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DetailActivity extends ActionBarActivity {
	private ActionBar actionBar;

	private TextView tvPintuAir;
	private TextView tvStatus;
	private TextView lastUpdate;
    private TextView tvAffectedArea;

	private DataChart dataChart;
	private List<String> categories = new ArrayList<String>();
	private List<Float> column1 = new ArrayList<Float>();
	
	private Button btnFollow;
	private Button btnUnfollow;
	
	private FollowPintuAir followPintuAir;
	
	public GoogleMap peta = null;
	private SupportMapFragment mapFragment;
	private LatLng locPintuAir;
	
	private DataPintuAir pintuair;

	// private TextView tvnama, tvtinggiair, tvstatus;
	// private DetailGraph graph;
	
	@Override
	protected void onStart()
	{
		super.onStart();
		FlurryAgent.setReportLocation(true);
		FlurryAgent.setLogEnabled(true);
		FlurryAgent.onStartSession(this, "CZWJXGNWJVHM35JYDTRC");
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		actionBar = getSupportActionBar();
		actionBar.setIcon(R.drawable.ico_actionbar);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
		

		
		tvPintuAir = (TextView) findViewById(R.id.tvPintuAir);
		tvStatus = (TextView) findViewById(R.id.tvStatus);
		lastUpdate = (TextView) findViewById(R.id.tvWaktuUpdate);
        tvAffectedArea = (TextView) findViewById(R.id.tvAffectedArea);
		
		followPintuAir = new FollowPintuAir(this);

		Intent i = getIntent();

		pintuair = (DataPintuAir) i.getParcelableExtra("pintuair");
		
		initializeData();

		
	}
	
	
	private void getPintuAirData() {
		// TODO Auto-generated method stub
		new JSONParseDetail().execute();
	}


	private void initializeData() {
		// TODO Auto-generated method stub
		createData(pintuair);

		createChart(pintuair);

		String nama = pintuair.getNama();
		
		//Flurry log
		Map<String, String> pintuAirParams = new HashMap<String, String>();
		pintuAirParams.put("Name", nama);
		FlurryAgent.logEvent("View_PintuAir",pintuAirParams,true);
		

		String status = pintuair.getStatus()[0];
		int waktu = pintuair.getWaktuTerakhir();
		
		tvPintuAir.setText(nama);
        tvAffectedArea.setText(pintuair.getAffectedArea());
		tvStatus.setText(status);
		lastUpdate.setText(getResources().getString(R.string.lastupdated) + " " + pintuair.getTanggal() + " " + waktu + ".00 WIB");

		if (status.equals("NORMAL")) {
			tvStatus.setTextColor(Color.parseColor("#2ecc71"));
		} else if (status.equals("WASPADA")) {
			tvStatus.setTextColor(Color.parseColor("#f1c40f"));
		} else if (status.equals("RAWAN")) {
			tvStatus.setTextColor(Color.parseColor("#f39c12"));
		} else if (status.equals("KRITIS")) {
			tvStatus.setTextColor(Color.parseColor("#e74c3c"));
		}
		
		
		btnFollow = (Button) findViewById(R.id.follow_btn);
		btnUnfollow = (Button) findViewById(R.id.unfollow_btn);
		
		btnFollow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnFollow.setVisibility(View.GONE);
				btnUnfollow.setVisibility(View.VISIBLE);
				followPintuAir.followPintuAir(pintuair.getNama());
			}
		});
		
		btnUnfollow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnFollow.setVisibility(View.VISIBLE);
				btnUnfollow.setVisibility(View.GONE);
				followPintuAir.unfollowPintuAir(pintuair.getNama());
				
			}
		});
		if (followPintuAir.isFollowing(nama)) {
			btnFollow.setVisibility(View.GONE);
			btnUnfollow.setVisibility(View.VISIBLE);
		} else {
			btnFollow.setVisibility(View.VISIBLE);
			btnUnfollow.setVisibility(View.GONE);
		}
		
		FragmentManager fm = getSupportFragmentManager();
		mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
		if (mapFragment == null) {
			mapFragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.map, mapFragment).commit();
		}
		
		locPintuAir = DataPintuAir.locationPintuAir.get(nama);
		
	}


	@Override
	public void onResume() {
		super.onResume();
		initializeMap();
			
	}

	private void initializeMap() {
		if (peta == null) {
			peta = mapFragment.getMap();

			// check if map is created successfully or not
			if (peta == null) {
				Toast.makeText(this.getApplicationContext(),
						"Error showing map", Toast.LENGTH_SHORT).show();
			} else {
				peta.addMarker(new MarkerOptions()
				.position(locPintuAir)
				.title("Pintu Air " + pintuair.getNama())
				.snippet(""));
				
				peta.moveCamera(CameraUpdateFactory.newLatLngZoom(locPintuAir, 15));
			}

		}
		
	}

	private void createData(DataPintuAir pintuair) {
		float value1 = 0.0f;
		Random random = new Random();

		for (int i = 0; i < 6; ++i) {
			value1 = (float) pintuair.getTinggiAir()[i];

			// categories1=pintuair.getWaktu()[i];

			categories.add(Integer.toString(pintuair.getWaktu()[i]) + ".00");
			column1.add(value1);
			// column2.add(value2);
		}
		Collections.reverse(column1);
		Collections.reverse(categories);
	}

	private void createChart(DataPintuAir pintuair) {
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
		int[] tinggiAir = Arrays.copyOf(pintuair.getTinggiAir(), pintuair.getTinggiAir().length);
		Arrays.sort(tinggiAir);
		int minValue = tinggiAir[0];
		int maxValue = tinggiAir[5];

		valueAxis.setMinimumValue((float) minValue - 10.0f); // the random data
																// look much
																// nicer with
		// a fixed axis range
		valueAxis.setMaximumValue((float) maxValue + 10.0f); // the random data
																// look much
																// nicer
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
				+ "/siagabanjir" + System.currentTimeMillis() + ".png");
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
	
	public void captureMapScreen() {
        SnapshotReadyCallback callback = new SnapshotReadyCallback() {

            @Override
            public void onSnapshotReady(Bitmap snapshot) {
                try {
                	View rootView = findViewById(android.R.id.content).getRootView();
                	
                	
                    rootView.setDrawingCacheEnabled(true);

                    rootView.setDrawingCacheBackgroundColor(Color.WHITE);
                    Bitmap backBitmap = rootView.getDrawingCache();
                    backBitmap = Bitmap.createBitmap(backBitmap, 0, 0, backBitmap.getWidth(), backBitmap.getHeight()-220);
                    Bitmap bmOverlay = Bitmap.createBitmap(
                    		backBitmap.getWidth(), backBitmap.getHeight(),
                            backBitmap.getConfig());
                    bmOverlay.eraseColor(Color.WHITE);
                    Canvas canvas = new Canvas(bmOverlay);

                    
                    //rootView.layout(0, 0, rootView.getLayoutParams().width, rootView.getLayoutParams().height);
                    //rootView.draw(canvas);
                    canvas.drawBitmap(backBitmap, 0, 0, null);
                    
                    //canvas.drawBitmap(snapshot, (bmOverlay.getWidth() - snapshot.getWidth())/2, height - snapshot.getHeight() - 50, null);
                    File imagePath = new File (Environment.getExternalStorageDirectory()
                                    + "/siagabanjir"
                                    + System.currentTimeMillis() + ".png");
                    FileOutputStream out = new FileOutputStream(imagePath);

                    bmOverlay.compress(Bitmap.CompressFormat.PNG, 100, out);
                    
                    share(imagePath);
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        peta.snapshot(callback);

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
			captureMapScreen();
			
			//Flurry log
			FlurryAgent.logEvent("Share");
			
			return true;
		case R.id.action_about:
			Intent ii = new Intent(this, AboutActivity.class);
			startActivity(ii);
			return true;
		case R.id.action_information:
			Intent i = new Intent(this, InformationActivity.class);
			startActivity(i);
			return true;
		case R.id.action_tutorial:
			Intent iii = new Intent(this, WalkthroughActivity.class);
			startActivity(iii);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void share(File img) {
		String text = "Status Tinggi Muka Air di Pintu " + pintuair.getNama()
				+ ", " + pintuair.getHari() + " (" + pintuair.getTanggalShort()
				+ ") " + String.format("%02d", pintuair.getWaktuTerakhir())
				+ ".00 WIB: " + pintuair.getTinggiAir()[0] + " cm ("
				+ pintuair.getStatus()[0] + ") http://bit.ly/siagabanjir";

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("image/jpeg");
		shareIntent.putExtra(Intent.EXTRA_TEXT, text);
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(img));
		startActivity(Intent.createChooser(shareIntent, "Share via"));
	}

	public void refreshData() {

	}
	
	private class JSONParseDetail extends AsyncTask<String, String, JSONObject> {
		private static final String url = "http://labs.pandagostudio.com/siaga-banjir/";

		// ProgressDialog pd;
		protected void onPreExecute() {
			lastUpdate.setText("Mengunduh data...");
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			InputStream is = null;
			String json = "";
			JSONObject jObj = null;

			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				writeToFile(json);
			} catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e);
			}

			try {
				jObj = new JSONObject(readFromFile());
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

			return jObj;
		}

		private void writeToFile(String json) {
			try {
				OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
						DetailActivity.this.openFileOutput("datapintuair.txt",
								Context.MODE_PRIVATE));
				outputStreamWriter.write(json);
				outputStreamWriter.close();
			} catch (IOException e) {
				Log.e("Exception", "File write failed: " + e.toString());
			}
		}

		private String readFromFile() {

			String ret = "";

			try {
				InputStream inputStream = DetailActivity.this
						.openFileInput("datapintuair.txt");

				if (inputStream != null) {
					InputStreamReader inputStreamReader = new InputStreamReader(
							inputStream);
					BufferedReader bufferedReader = new BufferedReader(
							inputStreamReader);
					String receiveString = "";
					StringBuilder stringBuilder = new StringBuilder();

					while ((receiveString = bufferedReader.readLine()) != null) {
						stringBuilder.append(receiveString);
					}

					inputStream.close();
					ret = stringBuilder.toString();
				}
			} catch (FileNotFoundException e) {
				Log.e("login activity", "File not found: " + e.toString());
			} catch (IOException e) {
				Log.e("login activity", "Can not read file: " + e.toString());
			}

			return ret;
		}

		protected void onPostExecute(JSONObject json) {
			//pintuAir.clear();

			JSONArray dataPintuAir;
			try {
				dataPintuAir = json.getJSONArray("datapintuair");
				for (int ii = 0; ii < dataPintuAir.length(); ii++) {
					JSONObject obj = dataPintuAir.getJSONObject(ii);
					String nama = obj.getString("nama");
					String tanggal = obj.getString("tanggal");
					JSONArray dataTinggi = obj.getJSONArray("tinggiair");

					DataPintuAir dp = new DataPintuAir(nama);
					dp.setTanggal(tanggal);

					for (int jj = 0; jj < dataTinggi.length(); jj++) {
						int tinggi = 0;
						String status = dataTinggi.getJSONObject(jj).getString(
								"status");
						if (!dataTinggi.getJSONObject(jj).getString("tinggi")
								.split(" ")[0].equals("-")) {
							tinggi = Integer.parseInt(dataTinggi
									.getJSONObject(jj).getString("tinggi")
									.split(" ")[0]);
						} else {
							status = "N/A";
						}
						int waktu = dataTinggi.getJSONObject(jj)
								.getInt("waktu");
						dp.addTinggiAir(tinggi, status, waktu);
					}

					ArrayList<String> listFollowing = followPintuAir.getListFollowing();
					
					String status = dp.getStatus()[0];
					dp.setFollowing(followPintuAir.isFollowing(dp.getNama()));

					//pintuAir.add(dp);
					DataPintuAir.mapsPintuAir.put(dp.getNama(), dp);

				}
				//Collections.sort(pintuAir);
				//refresh();

				//Toast.makeText(context, "Done!", Toast.LENGTH_LONG).show();
				//((MainActivity) HomeFragment.this.getActivity()).setRefreshActionButtonState(false);
				/**
				 * if (pd != null) { pd.dismiss(); }
				 **/
				pintuair = DataPintuAir.mapsPintuAir.get(DetailActivity.this.getIntent().getStringExtra("namapintuair"));
				initializeData();
				initializeMap();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(DetailActivity.this, "No internet connection",
						Toast.LENGTH_LONG).show();
				/**
				 * if (pd != null) { pd.dismiss(); }
				 **/
			} catch (NullPointerException e) {
				Toast.makeText(DetailActivity.this,
						"Error fetching data or no internet connection",
						Toast.LENGTH_LONG).show();
				/**
				 * if (pd != null) { pd.dismiss(); }
				 **/
			}

		}

	}

}
