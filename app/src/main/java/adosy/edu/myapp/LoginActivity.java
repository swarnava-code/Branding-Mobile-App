package adosy.edu.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {
    Button send_otp;
    LinearLayout form1, form2;
    TextView otp_time_disp;
    int time=30; //30sec

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();//2nd way

        form1 = findViewById(R.id.form1);
        form2 = findViewById(R.id.form2);
        send_otp = findViewById(R.id.otp_button);
        otp_time_disp = findViewById(R.id.otp_time_disp);
    }


    String stringLatitude , stringLongitude, country , city,  postalCode , addressLine ;

    @Override
    protected void onStart() {
        super.onStart();
        AlertDialog.Builder abuilder = new AlertDialog.Builder(LoginActivity.this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        abuilder.setTitle("Location Access");
        abuilder.setIcon(R.drawable.adosy_logo);
        abuilder.setMessage("We need location access for better services !");

        abuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onStart();
            }
        })
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {//set negative button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                                onStart();
                            }
                            else{
                                //Snackbar.make(findViewById(android.R.id.content), "Thank You to allow Location Access", Snackbar.LENGTH_LONG).show();
                                GPSTracker gpsTracker = new GPSTracker(LoginActivity.this);

                                if (gpsTracker.getIsGPSTrackingEnabled()) {





                                     stringLatitude = String.valueOf(gpsTracker.latitude);
                                     stringLongitude = String.valueOf(gpsTracker.longitude);
                                     country = gpsTracker.getCountryName(LoginActivity.this);
                                     city = gpsTracker.getLocality(LoginActivity.this);
                                     postalCode = gpsTracker.getPostalCode(LoginActivity.this);
                                     addressLine = gpsTracker.getAddressLine(LoginActivity.this);
                                }
                                else {
                                    gpsTracker.showSettingsAlert();
                                }

/*
                                LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                double longitude = location.getLongitude();
                                double latitude = location.getLatitude();
                                //https://stackoverflow.com/questions/2227292/how-to-get-latitude-and-longitude-of-the-mobile-device-in-android
 */



                               // Toast.makeText(LoginActivity.this, longitude+" : "+latitude, Toast.LENGTH_LONG).show();


                            }
                        } catch (Exception e){
                            Toast.makeText(LoginActivity.this, "location error", Toast.LENGTH_LONG).show();

                        }







                        //allow
                    }
                })
                .show();
    }

    void loopOtp(){
        Runnable r = new Runnable() {
            @Override
            public void run() {

                if(time==0){
                    send_otp.setEnabled(true);
                    otp_time_disp.setText("");
                }
                else{
                    String time_str=time+"";
                    if(time_str.length()==1)
                        time_str = "0"+time;
                    otp_time_disp.setText("00:"+time_str);
                    time--;
                    loopOtp();
                }


            }
        };
        Handler h = new Handler();
        h.postDelayed(r,1000);
    }


    public void sendOTP(View view) {
        send_otp.setEnabled(false);
        time = 30;
        loopOtp();
        form2.setVisibility(View.VISIBLE);
    }

    public void submit(View view) {
        /*
        Intent i=new Intent(getApplicationContext(), ServicesActivity.class);
        startActivity(i);

         */




        //HttpHandler sh = new HttpHandler();
        //String str = "Fail";
        //str = sh.makeServiceCall("https://swarnava.delgradecorporation.in/project1/get_users_details.php");


        new LoginActivity.GetApiCall().execute();





/*
        int resId = getResources().getIdentifier("Services", "array", getPackageName());
        String[] stringArray = getResources().getStringArray(resId);
        Bundle b = new Bundle();
        b.putStringArray("key", stringArray);
        Intent i=new Intent(getApplicationContext(), NavigationActivity.class);
        i.putExtras(b);
        startActivity(i);
        finish();

 */

    }


    String url = "https://swarnava.delgradecorporation.in/project2/reg_insert.php?name=adi&phone=3330&email=adi123@gmail.com";
    String url2 = "https://delgradecorporation.in/swarnava/project1/get_users_details.php";
    String jsonStr;
    private class GetApiCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall(url);


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Toast.makeText(LoginActivity.this, "msg : "+jsonStr, Toast.LENGTH_LONG).show();


        }
    }



}