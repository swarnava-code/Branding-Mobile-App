package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int DELAY = 500; //3000

    Animation slide_up_in;
    TextView com;
    ImageView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//1st way
        getSupportActionBar().hide();//2nd way

        slide_up_in = AnimationUtils.loadAnimation(this, R.anim.slide_up_in_);
        com = findViewById(R.id.textView);
        logo = findViewById(R.id.imageView);

        Runnable r = new Runnable() {
            @Override
            public void run() {

                //   /*
                //int resId = getResources().getIdentifier("services", "array", getPackageName());
                //String[] stringArray = getResources().getStringArray(resId);
                Bundle b = new Bundle();
                b.putString("key", "Services");
                Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
                i.putExtras(b);
                startActivity(i);
                finish();

                //   */





    /*
                Intent i=new Intent(getApplicationContext(), LoginActivity.class); //LoginActivity
                startActivity(i);
                finish();
    */


            }
        };
        Handler h = new Handler();
        h.postDelayed(r, DELAY);

    }

    @Override
    protected void onStart() {
        super.onStart();
        com.startAnimation(slide_up_in);
        logo.startAnimation(slide_up_in);
    }
}