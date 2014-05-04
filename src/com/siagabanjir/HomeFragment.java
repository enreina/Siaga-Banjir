package com.siagabanjir;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.siagabanjir.utility.JSONParser;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;


public class HomeFragment extends ListFragment {
	private ArrayList<DataPintuAir> pintuAir;
	BinderData binder;
	private boolean empty;

	private Context context;

	public HomeFragment() {
		this(0);
		refreshHome();
	}

	public HomeFragment(ArrayList<DataPintuAir> pintuAir, Context context) {
		this.pintuAir = pintuAir;
		this.context = context;
		refreshHome();
	}

	public HomeFragment(int status) {
		pintuAir = new ArrayList<DataPintuAir>();
		refreshHome();
	}

	public void refreshHome() {
		new JSONParse().execute();
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
		View view = inflater.inflate(R.layout.list_fragment, container, false);
		return view;
	}

	/*
	 * @Override public void onListItemClick(ListView list, View v, int
	 * position, long id) { if (this.empty == false) { Intent i = new
	 * Intent(this.getActivity().getBaseContext(), DetailActivity.class);
	 * 
	 * i.putParcelableArrayListExtra("pintuair", pintuAir);
	 * i.putExtra("selected", position);
	 * 
	 * startActivity(i); }
	 * 
	 * }
	 */

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
			this.getActivity().findViewById(R.id.tvEmpty)
					.setVisibility(View.VISIBLE);
		} else {
			this.getListView().setVisibility(View.VISIBLE);
			this.getActivity().findViewById(R.id.tvEmpty)
					.setVisibility(View.GONE);
		}
	}

	private class JSONParse extends AsyncTask<String, String, JSONObject> {
		private static final String url = "http://labs.pandagostudio.com/siaga-banjir/";

		// ProgressDialog pd;
		protected void onPreExecute() {
			Toast.makeText(context, "Updating data...", Toast.LENGTH_LONG)
					.show();
			/**
			 * pd = new ProgressDialog(context);
			 * pd.setTitle("Updating data..."); pd.setMessage("Please wait.");
			 * pd.setCancelable(false); pd.setIndeterminate(true); pd.show();
			 */
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
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
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
				Log.e("JSON Parser", "Error parsing data " + e.toString() );
			}
			
			return jObj;
		}
		
		private void writeToFile(String json) {
			try {
		        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("datapintuair.txt", Context.MODE_PRIVATE));
		        outputStreamWriter.write(json);
		        outputStreamWriter.close();
		    }
		    catch (IOException e) {
		        Log.e("Exception", "File write failed: " + e.toString());
		    }
		}
		
		private String readFromFile() {

		    String ret = "";

		    try {
		        InputStream inputStream = context.openFileInput("datapintuair.txt");


		        if ( inputStream != null ) {
		            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		            String receiveString = "";
		            StringBuilder stringBuilder = new StringBuilder();

		            while ( (receiveString = bufferedReader.readLine()) != null ) {
		                stringBuilder.append(receiveString);
		            }

		            inputStream.close();
		            ret = stringBuilder.toString();
		        }
		    }
		    catch (FileNotFoundException e) {
		        Log.e("login activity", "File not found: " + e.toString());
		    } catch (IOException e) {
		        Log.e("login activity", "Can not read file: " + e.toString());
		    }

		    return ret;
		}

		protected void onProgressUpdate(String... values) {
			Toast.makeText(context, "Updating data...", Toast.LENGTH_LONG)
					.show();
		}

		protected void onPostExecute(JSONObject json) {
			pintuAir.clear();

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

					String status = dp.getStatus()[0];

					pintuAir.add(dp);

				}
				Collections.sort(pintuAir);
				refresh();

				Toast.makeText(context, "Done!", Toast.LENGTH_LONG).show();
				/**
				 * if (pd != null) { pd.dismiss(); }
				 **/

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Toast.makeText(context, "No internet connection",
						Toast.LENGTH_LONG).show();
				/**
				 * if (pd != null) { pd.dismiss(); }
				 **/
			} catch (NullPointerException e) {
				Toast.makeText(context,
						"Error fetching data or no internet connection",
						Toast.LENGTH_LONG).show();
				/**
				 * if (pd != null) { pd.dismiss(); }
				 **/
			}

		}

	}
}
