package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class AdminLogin extends AppCompatActivity {

    LinearLayout credintials_layout, otp_layout;
    TextInputEditText fullname_et, email_et, phone_et, otp_et;
    Button submit_btn, send_otp_btn;
    String fullname_str, email_str, phone_str, otp_str="X", email="", url, jsonStr;
    final int timer_expire_phone_otp_int = 180000; // 3min
    boolean successMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        fullname_et = findViewById(R.id.fullname);
        email_et = findViewById(R.id.email);
        phone_et = findViewById(R.id.phone);
        otp_et = findViewById(R.id.otp);
        submit_btn = findViewById(R.id.submit);
        send_otp_btn = findViewById(R.id.send_otp);
        credintials_layout = findViewById(R.id.credential_layout);
        otp_layout = findViewById(R.id.otp_layout);


        send_otp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullname_str = fullname_et.getText().toString();
                email_str = email_et.getText().toString();
                phone_str = phone_et.getText().toString();

                if( fullname_str.equals("") && email_str.equals("") && phone_str.equals("") ){
                    Toast.makeText(AdminLogin.this, "Enter Something!", Toast.LENGTH_SHORT).show();
                }
                else if( fullname_str.equals("Akhilesh Shaw")
                        && email_str.equals("akhilesh.shaw@adosy.in")
                        && phone_str.equals("9038026497") ) {
                    Log.d("Adosy", "login request by A");
                    new AdminLogin.sendOtp().execute();
                }
                else if( fullname_str.equals("Gourav Kapoor")
                        && email_str.equals("gourav.kapoor@adosy.in")
                        && phone_str.equals("8240892690") ) {
                    Log.d("Adosy", "login request by G");
                    new AdminLogin.sendOtp().execute();
                }
                else if( fullname_str.equals("Swarnava Chakraborty")
                        && email_str.equals("swarnava415@gmail.com")
                        && phone_str.equals("8697187822") ) {
                    Log.d("Adosy", "login request by S");
                    new AdminLogin.sendOtp().execute();
                }
                else{
                    credintials_layout.setVisibility(View.GONE);
                    otp_layout.setVisibility(View.GONE);
                    Toast.makeText(AdminLogin.this, "You are not Admin, try to login in user form !", Toast.LENGTH_LONG).show();
                    new AdminLogin.sendWarningOtp().execute();
                    Log.d("Adosy", "suspicious credentials");
                }


            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp_et.getText().toString().equals(otp_str)  ) {
                    //go to NEXT activity :)

                    dbHelper user= new dbHelper(AdminLogin.this);
                    SQLiteDatabase dbR= user.getReadableDatabase();
                    Cursor c = user.viewData(dbR);

                    if(c.getCount() == 1){
                        SQLiteDatabase dbW = user.getWritableDatabase();
                        user.updateAdminVerifiedData("yes", dbW);
                    }

                    Toast.makeText(AdminLogin.this, "Welcome ADMIN "+fullname_str, Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getApplicationContext(), AdminUsersList.class);
                    startActivity(i);
                }
            }
        });
    }

    private class sendOtp extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            otp_str = "";
            Random randomGenerator=new Random();
            for (int i = 0; i < 4; i++) { //4digit
                otp_str += randomGenerator.nextInt(10);
            }
                url = "https://swarnava.delgradecorporation.in/project2/mail.php?apikey=swarnava_mail" +
                        "&email=" + email_str +
                        "&otp=" + otp_str;

            Log.d("Adosy", url);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //For mail
            try{
                successMail = true;
                jsonStr = sh.makeServiceCall(url);
                jsonStr = jsonStr.trim().replaceAll("\n","").replaceAll("\t","").replaceAll(" ","");
                if(jsonStr.equals("success")){
                    successMail = true;
                }
                else{
                    successMail = false;
                }
            }catch (Exception e){
                successMail = false;
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
                if(successMail){
                    credintials_layout.setVisibility(View.GONE);
                    otp_layout.setVisibility(View.VISIBLE);

                    Toast.makeText(AdminLogin.this, "OTP sent, it expire in 3 minute", Toast.LENGTH_LONG).show();
                    time_otp = timer_expire_phone_otp_int;
                    timer_expire_phone_otp();
                }
                else{
                    Toast.makeText(AdminLogin.this, "error : sending otp failed !", Toast.LENGTH_LONG).show();
                }
        }
    }


    private class sendWarningOtp extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
                url = "https://swarnava.delgradecorporation.in/project2/warning_mail.php?apikey=swarnava_mail" +
                        "&name=" + fullname_str +
                        "&phone=" + phone_str +
                        "&email=" + email_str;
            Log.d("Adosy", url);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            //For mail
            try{
                jsonStr = sh.makeServiceCall(url);
            }catch (Exception e){
            }

            return null;
        }
    }

    int time_otp;
    boolean time_otp_phone_expire = true;
    void timer_expire_phone_otp() {
        if (time_otp > 0) {
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
        } else {
            time_otp_phone_expire = true;
        }
    }

}