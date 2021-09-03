package adosy.edu.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Random;

public class LoginActivity extends AppCompatActivity {
    Button send_otp;
    LinearLayout form1, form2;
    TextView otp_time_disp;
    final int fixedTime = 59; // 59 sec
    int time = fixedTime;

    TextInputEditText edit_text_phone, edit_text_name, edit_text_email;
    String name, phone, email, location;

    HashMap<Character, Character> rule = new HashMap<>();       //encryption table
    HashMap<Character, Character> ruleDe = new HashMap<>();     //decryption table

    Animation anim_bounce;

    EditText otp_et;
    CardView skip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();//2nd way

        form1 = findViewById(R.id.form1);
        form2 = findViewById(R.id.form2);
        send_otp = findViewById(R.id.otp_button);
        otp_time_disp = findViewById(R.id.otp_time_disp);
        edit_text_phone = findViewById(R.id.edit_text_phone);
        edit_text_name = findViewById(R.id.edit_text_name);
        edit_text_email = findViewById(R.id.edit_text_email);
        otp_et = findViewById(R.id.otp_et);
        skip = findViewById(R.id.skip);
        anim_bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

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
        if(edit_text_phone.getText().toString().length()>0)
            new LoginActivity.GetApiCall().execute();
        else
            Toast.makeText(this, "Enter your email address", Toast.LENGTH_SHORT).show();
    }

    public void submit(View view) {
        /*
        Intent i=new Intent(getApplicationContext(), ServicesActivity.class);
        startActivity(i);

         */


        //HttpHandler sh = new HttpHandler();
        //String str = "Fail";
        //str = sh.makeServiceCall("https://swarnava.delgradecorporation.in/project1/get_users_details.php");

        if(successSms){
            if(otp_et.getText().toString().equals(otp_str)){

                //for update
                dbHelper user = new dbHelper(LoginActivity.this);
                SQLiteDatabase dbW = user.getWritableDatabase();
                user.updateVerifiedData("yes",dbW);

                Bundle b = new Bundle();
                b.putString("key", "Services");
                Intent i=new Intent(getApplicationContext(), NavigationActivity.class);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
            else{
                Toast.makeText(this, "OTP not matched", Toast.LENGTH_SHORT).show();
            }
        }else{
            skip.setVisibility(View.VISIBLE);
            Toast.makeText(this, "send failed", Toast.LENGTH_SHORT).show();
        }





    }

    public void skip(View view) {
        Bundle b = new Bundle();
        b.putString("key", "Services");
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
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
            phone = encrypt(edit_text_phone.getText().toString().toLowerCase());
            url = "https://swarnava.delgradecorporation.in/project2/log_in.php?apikey=swarnava_login&id="+phone;
            //Log.d("Adosy", url);
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


            if(jsonStr.equals("noaccount")){
                otp_time_disp.setText("No account found with this email id, Kindly register first if you have no account !");
                otp_time_disp.setTextColor(Color.RED);
                otp_time_disp.startAnimation(anim_bounce);
            }else{
                otp_time_disp.setText("success");
                send_otp.setEnabled(false);
                time = fixedTime;
                loopOtp();
                form2.setVisibility(View.VISIBLE);
                otp_time_disp.setTextColor(Color.BLUE);
                otp_time_disp.startAnimation(anim_bounce);
                new LoginActivity.sendOtp().execute();
            }
        }
    }

    String otp_str;
    boolean successSms = false;
    private class sendOtp extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //name = edit_text_name.getText().toString().toLowerCase();
            //email = edit_text_email.getText().toString().toLowerCase();
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
                Toast.makeText(LoginActivity.this, "OTP send in your mobile, expire in 3 min.", Toast.LENGTH_SHORT).show();
            }else{
                skip.setVisibility(View.VISIBLE);
                Toast.makeText(LoginActivity.this, "fail to send sms", Toast.LENGTH_SHORT).show();
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

    public void register(View view) {
        Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
        startActivity(i);
    }

    public void hidden_admin_login(View view) {
        Intent i = new Intent(getApplicationContext(), AdminLogin.class);
        startActivity(i);
        /*
        test2++;
        if(test2>4) {
            Intent i = new Intent(getApplicationContext(), AdminLogin.class);
            startActivity(i);
        }
        else{
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    test2 = 0;
                }
            };
            Handler h = new Handler();
            h.postDelayed(r,2500);
        }

         */
    }
    int test2 = 0;

}