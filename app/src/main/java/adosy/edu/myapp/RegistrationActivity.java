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

public class RegistrationActivity extends AppCompatActivity {
    Button send_otp;
    LinearLayout form1, form2;
    TextView otp_time_disp;
    int time = 59; //59sec

    TextInputEditText edit_text_phone, edit_text_name, edit_text_email;
    String name, phone, email, location;

    HashMap<Character, Character> rule = new HashMap<>();       //encryption table
    HashMap<Character, Character> ruleDe = new HashMap<>();     //decryption table


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

    }


    String stringLatitude , stringLongitude, country , city,  postalCode , addressLine ;

    @Override
    protected void onStart() {
        super.onStart();


        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                requestLocation();
            }
        } catch (Exception e){
            e.printStackTrace();
        }


    }

    void requestLocation(){
        AlertDialog.Builder abuilder = new AlertDialog.Builder(RegistrationActivity.this, R.style.Theme_MaterialComponents_Light_Dialog_Alert);
        abuilder.setTitle("Location Access");
        abuilder.setIcon(R.drawable.adosy_logo);
        abuilder.setMessage("We need location access for better services !");

        abuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                requestLocation();
            }
        })
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
                                //Snackbar.make(findViewById(android.R.id.content), "Thank You to allow Location Access", Snackbar.LENGTH_LONG).show();
                                GPSTracker gpsTracker = new GPSTracker(RegistrationActivity.this);

                                if (gpsTracker.getIsGPSTrackingEnabled()) {
                                    stringLatitude = String.valueOf(gpsTracker.latitude);
                                    stringLongitude = String.valueOf(gpsTracker.longitude);
                                    country = gpsTracker.getCountryName(RegistrationActivity.this);
                                    city = gpsTracker.getLocality(RegistrationActivity.this);
                                    postalCode = gpsTracker.getPostalCode(RegistrationActivity.this);
                                    addressLine = gpsTracker.getAddressLine(RegistrationActivity.this);
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
                            Toast.makeText(RegistrationActivity.this, "location error", Toast.LENGTH_LONG).show();

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

    String url = "https://swarnava.delgradecorporation.in/project2/reg_insert.php?apikey=4556&name=adi&phone=2330&email=adi123@gmail.com&location=19.162704,%202072.995057";
    String url2 = "https://delgradecorporation.in/swarnava/project1/get_users_details.php";
    String jsonStr;

    private class GetApiCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            name = encrypt(edit_text_name.getText().toString().toLowerCase());
            email = encrypt(edit_text_email.getText().toString().toLowerCase());
            phone = encrypt(edit_text_phone.getText().toString().toLowerCase());
            location = null;

            url = "https://swarnava.delgradecorporation.in/project2/reg_insert.php?apikey=swarnava_insert&phone="+
                    phone+"&email="+email+"&location="+location+"&name="+name;
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            jsonStr = sh.makeServiceCall(url);
            jsonStr = jsonStr.trim().replaceAll("\n","").replaceAll("\t","").replaceAll(" ","");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

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
    }

    String encrypt(String str){
        String Newstr="";
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

        for ( Character key : rule.keySet() ) {
            ruleDe.put(rule.get(key), key);
        }
    }

}