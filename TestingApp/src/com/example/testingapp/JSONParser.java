package com.example.testingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.*;
import android.view.View;

public class JSONParser extends AsyncTask<String, Void, JSONObject>{
 
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private Activity __callerActivity;
    // constructor
    public JSONParser(Activity activity) {
    	this.__callerActivity = activity;
    }
 
    /**
     * Return JSONOject from given URL
     * Code from http://www.androidhive.info/2012/01/android-json-parsing-tutorial/
     */
    static JSONObject getJSONFromUrl(String url) {
 
        // Making HTTP request
        try {
            // defaultHttpClient
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
 
        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
 
        // return JSON String
        return jObj;
 
    }

	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		return getJSONFromUrl(params[0]);
	}
	
	/**
	 * Return results on the UI View
	 */
	@Override
	protected void onPostExecute(JSONObject jsonObject){
    	Log.d("JSON Length:", String.valueOf(jsonObject.length()));
    	Log.d("Weather is:", jsonObject.toString());
    	
    	//process the JSONobject
    	TextView currentWeatherTextView = (TextView)this.__callerActivity.findViewById(R.id.textView5);
    	String weatherCondition = getWeatherConditionFromJSONObject(jsonObject);
    	currentWeatherTextView.setText(weatherCondition);
    	
    	//Set Preference for the next launch
    	SharedPreferences myPrefs = __callerActivity.getSharedPreferences("com.example.testingapp",  Context.MODE_PRIVATE);
    	SharedPreferences.Editor prefsEditor;  
    	prefsEditor = myPrefs.edit();  
    	prefsEditor.putString("com.example.testingapp.LAST_KNOWN_WEATHER", weatherCondition);
    	prefsEditor.commit();
	}
	
	/**
	 * Get weather condition from JSONObject 
	 */
	private String getWeatherConditionFromJSONObject(JSONObject jsonObj) {
		String weatherString = new String();
		try{
			JSONObject weatherObservation = jsonObj.getJSONObject("weatherObservation");
			weatherString = "Clouds:" + weatherObservation.getString("clouds") + ": Weather:" + weatherObservation.getString("weatherCondition");
		}
		catch (JSONException e) {
	         //throw new RuntimeException(e);
			return "Invalid ICAO code";
		}
		return weatherString;
	}
	
}