package adosy.edu.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

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
            }
        })
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {//set negative button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Snackbar.make(findViewById(android.R.id.content), "Thank You to allow Location Access", Snackbar.LENGTH_LONG).show();
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

        int resId = getResources().getIdentifier("Services", "array", getPackageName());
        String[] stringArray = getResources().getStringArray(resId);
        Bundle b = new Bundle();
        b.putStringArray("key", stringArray);
        Intent i=new Intent(getApplicationContext(), NavigationActivity.class);
        i.putExtras(b);
        startActivity(i);
        finish();

    }

    String api_key, message_data, sender_info, ph_number;

    public String postSms() {
        try {
            // Construct data
            String apiKey = "apikey=" + "yourapiKey";
            String message = "&message=" + "This is your message";
            String sender = "&sender=" + "TXTLCL";
            String numbers = "&numbers=" + "918123456789";

            // Send data
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
            String data = apiKey + numbers + message + sender;
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
            conn.getOutputStream().write(data.getBytes("UTF-8"));
            final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            final StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                stringBuffer.append(line);
            }
            rd.close();

            return stringBuffer.toString();
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }

    public String getSms() {
        try {
            // Construct data
            String apiKey = "apikey=" + URLEncoder.encode("yourapiKey", "UTF-8");
            String message = "&message=" + URLEncoder.encode("This is your message", "UTF-8");
            String sender = "&sender=" + URLEncoder.encode("TXTLCL", "UTF-8");
            String numbers = "&numbers=" + URLEncoder.encode("918123456789", "UTF-8");

            // Send data
            String data = "https://api.textlocal.in/send/?" + apiKey + numbers + message + sender;
            URL url = new URL(data);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String sResult="";
            while ((line = rd.readLine()) != null) {
                // Process line...
                sResult=sResult+line+" ";
            }
            rd.close();

            return sResult;
        } catch (Exception e) {
            System.out.println("Error SMS "+e);
            return "Error "+e;
        }
    }


}