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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class RegistrationActivity extends AppCompatActivity {
    Button send_otp;
    LinearLayout form1, form2;
    TextView otp_time_disp;
    final int fixedTime = 59; //59sec
    int time = fixedTime;

    TextInputEditText edit_text_phone, edit_text_name, edit_text_email;
    String name="", phone="", email="", locationStr = "";

    HashMap<Character, Character> rule = new HashMap<>();       //encryption table
    HashMap<Character, Character> ruleDe = new HashMap<>();     //decryption table

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();//2nd way

        form1 = findViewById(R.id.form1);
        form2 = findViewById(R.id.form2);
        send_otp = findViewById(R.id.otp_button);
        otp_time_disp = findViewById(R.id.otp_time_disp);
        edit_text_phone = findViewById(R.id.edit_text_phone);
        edit_text_name = findViewById(R.id.edit_text_name);
        edit_text_email = findViewById(R.id.edit_text_email);

        //creating rule table for encryption & decryption
        CreateRuleForEncryptionAndDecryption();

/*
        String en = encrypt("Swarnava415@gmail.com");
        String de = decrypt(en);
        otp_time_disp.setText(en+"\n===\n"+de);

 */

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(RegistrationActivity.this);

    }

    //String stringLatitude , stringLongitude, country , city,  postalCode , addressLine ;

    @Override
    protected void onStart() {
        super.onStart();

        try {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            } else {

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                try{
                                    locationStr = location.getLatitude() + ", " + location.getLongitude();
                                    //otp_time_disp.setText("onStart()=" + locationStr);
                                }catch (Exception e){
                                    //Toast.makeText(RegistrationActivity.this, "ccc", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                } else {
                    loopGps();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loopGps() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(RegistrationActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        locationStr = location.getLatitude() + ", " + location.getLongitude();
                        //otp_time_disp.setText("loopGps()=" + locationStr);
                    }
                });
                return;
            }
            else{
                requestLocation();
            }
        }else{


            AlertDialog.Builder abuilder = new AlertDialog.Builder(RegistrationActivity.this, R.style.Widget_AppCompat_ActionBar_Solid);
            abuilder.setTitle("Enable GPS");
            abuilder.setIcon(R.drawable.adosy_logo);
            abuilder.setMessage("Kindly enable your location gps and try again later");

            abuilder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loopGps();
                }
            }).setIcon(R.drawable.ic_baseline_location_on_24);
            AlertDialog mydialog = abuilder.create();
            mydialog.show();
            //loopGps();
        }



        //Toast.makeText(this, "Enabled your GPS", Toast.LENGTH_SHORT).show();
    }



    void requestLocation(){
        AlertDialog.Builder abuilder = new AlertDialog.Builder(RegistrationActivity.this, R.style.Widget_AppCompat_ActionBar_Solid );//R.style.Theme_MaterialComponents_Light_Dialog_Alert
        abuilder.setTitle("Location Access");
        abuilder.setIcon(R.drawable.adosy_logo);
        abuilder.setMessage("We need location access for better services !");

        abuilder
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {//set negative button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                                ActivityCompat.requestPermissions(RegistrationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        try{
                            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                                requestLocation();
                            }
                            else{

                                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                    try{
                                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(RegistrationActivity.this, new OnSuccessListener<Location>() {
                                            @Override
                                            public void onSuccess(Location location) {

                                                try{
                                                    locationStr = location.getLatitude()+", "+location.getLongitude();
                                                }catch (Exception e){
                                                    Log.d(TAG, "fail to get location");
                                                }


                                                //otp_time_disp.setText("requestLocation()="+locationStr);
                                            }
                                        });
                                    }catch (Exception e){
                                        loopGps();
                                    }
                                }
                                else{
                                    loopGps();
                                }
                            }
                        } catch (Exception e){
                            Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity.class);
                            startActivity(intent);
                            finish();
                            //Toast.makeText(RegistrationActivity.this, "location error", Toast.LENGTH_LONG).show();
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
        email = edit_text_email.getText().toString();
        phone = edit_text_phone.getText().toString();
        name = edit_text_name.getText().toString();

        if(  email.length()>0 && phone.length()>0 && name.length()>0  ){
            send_otp.setEnabled(false);
            time = fixedTime;
            loopOtp();
            form2.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(this, "* All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
    }

    public void submit(View view) {
        /*
        Intent i=new Intent(getApplicationContext(), ServicesActivity.class);
        startActivity(i);

         */




        //HttpHandler sh = new HttpHandler();
        //String str = "Fail";
        //str = sh.makeServiceCall("https://swarnava.delgradecorporation.in/project1/get_users_details.php");


        new RegistrationActivity.GetApiCall().execute();





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

    public void skip(View view) {
        Bundle b = new Bundle();
        b.putString("key", "Services");
        Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
        i.putExtras(b);
        startActivity(i);
        finish();
    }

    String url = "";
    String url2 = "https://delgradecorporation.in/swarnava/project1/get_users_details.php";
    String jsonStr;

    boolean SuccessGetApiCall = true;
    private class GetApiCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name = encrypt(edit_text_name.getText().toString().toLowerCase());
            email = encrypt(edit_text_email.getText().toString().toLowerCase());
            phone = encrypt(edit_text_phone.getText().toString().toLowerCase());
            //location = null;

            url = "https://swarnava.delgradecorporation.in/project2/reg_insert.php?apikey=swarnava_insert&phone="+
                    phone+"&email="+email+"&location="+locationStr+"&name="+name;
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            try{
                SuccessGetApiCall = true;
                jsonStr = sh.makeServiceCall(url);
                jsonStr = jsonStr.trim().replaceAll("\n","").replaceAll("\t","").replaceAll(" ","");

            }catch (Exception e){
                SuccessGetApiCall = false;
            }



            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(SuccessGetApiCall){
                if(jsonStr.equals("success")){
                /*
                int resId = getResources().getIdentifier("Services", "array", getPackageName());
                String[] stringArray = getResources().getStringArray(resId);
                Bundle b = new Bundle();
                b.putStringArray("key", stringArray);
                Intent i=new Intent(getApplicationContext(), NavigationActivity.class);
                i.putExtras(b);
                startActivity(i);
                 */

                    finish();
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "error : "+jsonStr, Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(RegistrationActivity.this, "Something Wrong ! Check your internet connection and try again later !", Toast.LENGTH_SHORT).show();

            }
        }
    }

    String encrypt(String str){
        String Newstr = "";
        try {
            for (int i=0;i<str.length();i++) {
                char ch = Character.toLowerCase(str.charAt(i));
                Newstr += rule.get(ch);
            }
        }
        catch(Exception ioe) {
            ioe.printStackTrace();
        }

        return Newstr;
    }
    String decrypt(String str){
        String Newstr="";
        System.out.print("Enter the String you want to Decrypt: ");
        try {
            for (int i=0;i<str.length();i++) {
                char ch = str.charAt(i);

                Newstr += ruleDe.get(ch);

            }
        }
        catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return Newstr;
    }



    //creating rule for encryption
    void CreateRuleForEncryptionAndDecryption(){
        rule.put(' ','!');
        rule.put('a','0');
        rule.put('b','c');
        rule.put('c','e');
        rule.put('d','G');
        rule.put('e','f');
        rule.put('f','1');
        rule.put('g','d');
        rule.put('h','j');
        rule.put('i','i');
        rule.put('j','X');
        rule.put('k','m');
        rule.put('l','n');
        rule.put('m','q');
        rule.put('n','h');
        rule.put('o','P');
        rule.put('p','S');
        rule.put('q','l');
        rule.put('r','o');
        rule.put('s','r');
        rule.put('t','s');
        rule.put('u','E');
        rule.put('v','6');
        rule.put('w','U');
        rule.put('x','v');
        rule.put('y','z');
        rule.put('z','y');
        rule.put('@','3');
        rule.put('.','x');
        rule.put('0','w');
        rule.put('1','5');
        rule.put('2','4');
        rule.put('3','b');
        rule.put('4','9');
        rule.put('5','A');
        rule.put('6','8');
        rule.put('7','@');
        rule.put('8','7');
        rule.put('9','.');
        rule.put('+','^');

        for ( Character key : rule.keySet() ) {
            ruleDe.put(rule.get(key), key);
        }
    }

    public void signin(View view){
        finish();
    }

}