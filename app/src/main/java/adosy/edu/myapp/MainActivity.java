package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    int DELAY = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);//1st way
        getSupportActionBar().hide();//2nd way

        Runnable r = new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                finish();
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, DELAY);

    }
}