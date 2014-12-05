package com.siagabanjir;

import android.content.SharedPreferences;

public class FirstRun {
    
    protected static boolean isFirstRun() {
                //default value to return if this preference does not exist is <em>true</em>
        return MainActivity.sharedPreferences.getBoolean("isFirstRun", true);
        
    }
    
    protected static void appRunned() {
        SharedPreferences.Editor edit = MainActivity.sharedPreferences.edit();
        //value of "isFirstRun" changed to false
        edit.putBoolean("isFirstRun", false);
        edit.commit();
    }
}
    
  
