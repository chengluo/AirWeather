package com.example.testingapp;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;

import org.json.JSONObject;
import android.util.Log;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, LocationListener, View.OnClickListener{

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private LocationManager locationManager;
    private String locationProvider;
    private TextView latTextView;
    private TextView lonTextView;
    private Button getWeatherButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //check GPS status 
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = locationManager
          .isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Check if enabled and if not send user to the GSP settings
        if (!enabled) {
          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          startActivity(intent);
        }
        
        //TODO: get nearby airport from geo-coordinates
        
        //initial the UI
        getWeatherButton = (Button) findViewById(R.id.button1);
        getWeatherButton.setOnClickListener(this);
        
        //get LAST_KNOWN_WEATHER from the SharedPreferences
        SharedPreferences myPrefs;  
        myPrefs = getSharedPreferences("com.example.testingapp", Context.MODE_PRIVATE);
        String lastKnownWeather = myPrefs.getString("com.example.testingapp.LAST_KNOWN_WEATHER", "");
        TextView weatherTextView = (TextView)findViewById(R.id.textView5);
        weatherTextView.setText(lastKnownWeather);
    }

    @Override
    public void onStart() {
    	super.onStart();
        Criteria criteria = new Criteria();
        locationProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(locationProvider);
        
        locationManager.requestLocationUpdates(locationProvider, 400, 1, this);
      
        latTextView = (TextView) findViewById(R.id.textView3);
        lonTextView = (TextView) findViewById(R.id.textView4);
        
        if(location != null) {
        	latTextView.setText(String.valueOf(location.getLatitude()));
        	lonTextView.setText(String.valueOf(location.getLongitude()));
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lon = (double) (location.getLongitude());
        latTextView.setText(String.valueOf(lat));
        lonTextView.setText(String.valueOf(lon));
    }
    
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	
    }
    
    @Override
    public void onProviderEnabled(String provider) {
    	
    }
    
    @Override
    public void onProviderDisabled(String provider) {
    	
    }
    
    
    /**
     * 	Get the current weather from the given ICAO code
     */
	@Override
	public void onClick(View arg0) {
		String icaoCode = ((EditText)findViewById(R.id.editText1)).getText().toString();
		//TODO:check icaoCode is valid before send the request
		
    	JSONParser jsonParser = new JSONParser(this);
    	jsonParser.execute("http://ws.geonames.org/weatherIcaoJSON?ICAO="+icaoCode);
    	
    }
}
