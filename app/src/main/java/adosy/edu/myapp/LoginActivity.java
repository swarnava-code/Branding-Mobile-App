package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class LoginActivity extends AppCompatActivity {

    Button send_otp;
    LinearLayout form1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        form1 = findViewById(R.id.form1);





    }


    public void sendOTP(View view) {
        form1.setVisibility(View.GONE);
    }
}