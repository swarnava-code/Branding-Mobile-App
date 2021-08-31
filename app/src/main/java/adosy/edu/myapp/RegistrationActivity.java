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
import android.widget.EditText;
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
import java.util.Random;

import static android.content.ContentValues.TAG;

public class RegistrationActivity extends AppCompatActivity {
    Button send_otp;
    LinearLayout form1, form2;
    TextView otp_time_disp;
    final int fixedTime = 59, timer_expire_phone_otp_int = 180000; //59sec , 3min
    int time = fixedTime;

    TextInputEditText edit_text_phone, edit_text_name, edit_text_email;
    String name="", phone="", email="", locationStr = "";

    HashMap<Character, Character> rule = new HashMap<>();       //encryption table
    HashMap<Character, Character> ruleDe = new HashMap<>();     //decryption table
    HashMap<Character, Character> rule2 = new HashMap<>();       //encryption table2
    HashMap<Character, Character> ruleDe2 = new HashMap<>();     //decryption table2

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    LocationManager locationManager;

    EditText otp_et;

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
        otp_et = findViewById(R.id.otp_et);

        //creating rule table for encryption & decryption
        CreateRuleForEncryptionAndDecryption();
        Create2ndRuleForEncryptionAndDecryption();

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

        if( email.length()>0 && phone.length()>0 && name.length()>0  ){
            send_otp.setEnabled(false);
            time = fixedTime;
            loopOtp();
            form2.setVisibility(View.VISIBLE);
            new RegistrationActivity.sendOtp().execute();
        }else{
            Toast.makeText(this, "* All fields are mandatory", Toast.LENGTH_SHORT).show();
        }
    }

    public void submit(View view) {
        String otp_et_str = otp_et.getText().toString();
        if(otp_et_str.equals("")){
            Toast.makeText(this, "Kindly enter 4 digit OTP", Toast.LENGTH_SHORT).show();
        }else{
            if(otp_et_str.equals(otp_str)){
                if(!time_otp_phone_expire){
                    new RegistrationActivity.insertIntoDb().execute();
                }else{
                    Toast.makeText(this, "Sorry, Your OTP expired !", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this, "Wrong OTP or expired", Toast.LENGTH_SHORT).show();
            }
        }
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
    String jsonStr, otp_str;

    boolean SuccessGetApiCall = true, successSms=true;
    private class sendOtp extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name = edit_text_name.getText().toString().toLowerCase();
            email = edit_text_email.getText().toString().toLowerCase();
            phone = edit_text_phone.getText().toString().toLowerCase();
            //location = null;

            otp_str = "";
            Random randomGenerator=new Random();
            for (int i = 0; i < 4; i++) { //4digit
                otp_str += randomGenerator.nextInt(10);
            }

            url = "https://www.fast2sms.com/dev/bulkV2?authorization=FQIElkrSVLjzR2wAtD357qCovYxOMyTfpbBiGWmn98PKJ6e0Ug2r7S0hcnRmuYdVTD1HOP5bGNxpfFMX&route=v3&sender_id=TXTIND&message=Your%20OTP%20to%20register/access%20ADOSY%20is%20" +
                    otp_str +
                    "&language=english&flash=0" +
                    "&numbers=" +
                    phone;
            //Log.d("Adosy", url);

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            try{
                successSms = true;
                jsonStr = sh.makeServiceCall(url);
                jsonStr = jsonStr.trim().replaceAll("\n","").replaceAll("\t","").replaceAll(" ","");

                try {

                    JSONObject object = new JSONObject(jsonStr);
                    String getStr = object.getString("return");
                    if(getStr.equals("true")){
                        successSms = true;
                    }
                    else{
                        successSms = false;
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }catch (Exception e){
                successSms = false;
            }



            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
                if(successSms){
                    Toast.makeText(RegistrationActivity.this, "Kindly check phone number, OTP expire in <3 minute", Toast.LENGTH_LONG).show();
                    time_otp = timer_expire_phone_otp_int;
                    timer_expire_phone_otp();
                }
                else{
                    Toast.makeText(RegistrationActivity.this, "error : sms fail ", Toast.LENGTH_LONG).show();
                }
        }
    }

    int time_otp;
    boolean time_otp_phone_expire = true;
    void timer_expire_phone_otp(){
        if(time_otp>0){
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    time_otp--;
                    time_otp_phone_expire = false;
                    timer_expire_phone_otp();
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 1000);
        }else{
            time_otp_phone_expire = true;
        }
    }

    private class insertIntoDb extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name = encrypt2(edit_text_name.getText().toString().toLowerCase());
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
                    Bundle b = new Bundle();
                    b.putString("key", "Services");
                    Intent i=new Intent(getApplicationContext(), NavigationActivity.class);
                    i.putExtras(b);
                    startActivity(i);
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

    String encrypt2(String str){
        String Newstr = "";
        try {
            for (int i=0;i<str.length();i++) {
                char ch = Character.toLowerCase(str.charAt(i));
                Newstr += rule2.get(ch);
            }
        }
        catch(Exception ioe) {
            ioe.printStackTrace();
        }

        return Newstr;
    }
    String decrypt2(String str){
        String Newstr="";
        //System.out.print("Enter the String you want to Decrypt: ");
        try {
            for (int i=0;i<str.length();i++) {
                char ch = str.charAt(i);

                Newstr += ruleDe2.get(ch);

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

    //creating encryption rule for user name
    void Create2ndRuleForEncryptionAndDecryption(){
        rule2.put(' ','0');
        rule2.put('a','f');
        rule2.put('b','p');
        rule2.put('c','a');
        rule2.put('d','1');
        rule2.put('e','q');
        rule2.put('f','9');
        rule2.put('g','i');
        rule2.put('h','7');
        rule2.put('i','o');
        rule2.put('j','g');
        rule2.put('k','6');
        rule2.put('l','c');
        rule2.put('m','n');
        rule2.put('n','h');
        rule2.put('o','b');
        rule2.put('p','2');
        rule2.put('q','j');
        rule2.put('r','l');
        rule2.put('s','5');
        rule2.put('t','k');
        rule2.put('u','4');
        rule2.put('v','d');
        rule2.put('w','8');
        rule2.put('x','3');
        rule2.put('y','m');
        rule2.put('z','e');

        for ( Character key : rule2.keySet() ) {
            ruleDe2.put(rule2.get(key), key);
        }
    }

    public void signin(View view){
        finish();
    }

}