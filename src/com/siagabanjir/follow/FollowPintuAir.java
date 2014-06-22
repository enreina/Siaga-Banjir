package com.siagabanjir.follow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.siagabanjir.DataPintuAir;
import com.siagabanjir.R;
import com.siagabanjir.utility.Installation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class FollowPintuAir {
	public static final String PREFS_NAME="FollowPrefs";
	private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String PREFS_IDS="IdsPrefs";
    
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "816692104137";

    /**
     * Tag used on log messages.
     */
    static final String TAG = "SiagaBanjir";
	
	private Map<String, ?> listFollow;
	private Context context;
	private SharedPreferences settings;
	
	private String regid;
	private GoogleCloudMessaging gcm;
	
	private String installid;
	
	public FollowPintuAir(Context context) {
		this.context = context;
		listFollow = new HashMap<String, Boolean>();
		readFromStorage();
		
		if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
		
		installid = Installation.id(context);
	}
	
	/**
	 * Check the device to make sure it has the Google Play Services APK. If
	 * it doesn't, display a dialog that allows users to download the APK from
	 * the Google Play Store or enable it in the device's system settings.
	 */
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i(TAG, "This device is not supported.");
	            ((Activity) context).finish();
	        }
	        return false;
	    }
	    return true;
	}
	
	/**
	 * Gets the current registration ID for application on GCM service.
	 * <p>
	 * If result is empty, the app needs to register.
	 *
	 * @return registration ID, or empty string if there is no existing
	 *         registration ID.
	 */
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}
	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return context.getSharedPreferences(PREFS_IDS,
	            0);
	}
	
	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration ID and app versionCode in the application's
	 * shared preferences.
	 */
	private void registerInBackground() {
		
		new AsyncTask<Void, Void, String>() {
	        @Override
	        protected String doInBackground(Void... params) {
	            String msg = "";
	            try {
	                if (gcm == null) {
	                    gcm = GoogleCloudMessaging.getInstance(context);
	                }
	                regid = gcm.register(SENDER_ID);
	                msg = "Device registered, registration ID=" + regid;

	                // You should send the registration ID to your server over HTTP,
	                // so it can use GCM/HTTP or CCS to send messages to your app.
	                // The request to your server should be authenticated if your app
	                // is using accounts.
	                //sendRegistrationIdToBackend();

	                // For this demo: we don't need to send it because the device
	                // will send upstream messages to a server that echo back the
	                // message using the 'from' address in the message.

	                // Persist the regID - no need to register again.
	                storeRegistrationId(context, regid);
	                
	            } catch (IOException ex) {
	                msg = "Error :" + ex.getMessage();
	                // If there is an error, don't just keep trying to register.
	                // Require the user to click a button again, or perform
	                // exponential back-off.
	            }
	            return msg;
	        }
	    }.execute(null, null, null);
	}
	
	private void storeRegistrationId(Context context, String regId) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    int appVersion = getAppVersion(context);
	    Log.i(TAG, "Saving regId on app version " + appVersion);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putString(PROPERTY_REG_ID, regId);
	    editor.putInt(PROPERTY_APP_VERSION, appVersion);
	    editor.commit();
	}
	
	public void readFromStorage() {
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		listFollow = settings.getAll();
	}
	
	
	
	public void followPintuAir(String pintuAir) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(pintuAir, true);
		editor.apply();
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		DataPintuAir.mapsPintuAir.get(pintuAir).setFollowing(true);
		
		new FollowRequest().execute("follow", installid, regid, pintuAir);
		
	}
	
	public void unfollowPintuAir(String pintuAir) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(pintuAir, false);
		editor.apply();
		settings = context.getSharedPreferences(PREFS_NAME, 0);
		DataPintuAir.mapsPintuAir.get(pintuAir).setFollowing(false);
		
		new FollowRequest().execute("unfollow", installid, regid, pintuAir);
		
	}
	
	public ArrayList<String> getListFollowing() {
		ArrayList<String> listFollowing = new ArrayList<String>();
		for (String key : listFollow.keySet()) {
			if (listFollow.get(key) instanceof Boolean && ((Boolean)listFollow.get(key)) == true) {
				listFollowing.add(key);
			}
		}
		
		return listFollowing;
	}
	
	public boolean isFollowing(String pintuAir) {
		return settings.getBoolean(pintuAir, false);
	}
	
	private class FollowRequest extends AsyncTask<String, Void, String> {
		private static final String url = "http://labs.pandagostudio.com/siaga-banjir/";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			BufferedReader reader = null;
			
			try {
				String data = URLEncoder.encode("installid", "UTF-8") + "="
						+ URLEncoder.encode(params[1]+"", "UTF-8");
				data += "&" + URLEncoder.encode("regid", "UTF-8") + "="
						+ URLEncoder.encode(params[2], "UTF-8");
				data += "&" + URLEncoder.encode("pintuair", "UTF-8") + "="
						+ URLEncoder.encode(params[3], "UTF-8");
				
				URL recUrl = new URL(url +params[0]+".php");
				URLConnection conn = recUrl.openConnection();
				conn.setDoOutput(true);
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(data);
				wr.flush();
				
				reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line = null;
				
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				
				String text = sb.toString();
				
				return params[0];
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return "";
		}
		
		
	}
	
	
}
