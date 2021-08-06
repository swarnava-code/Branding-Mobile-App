package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ServicesActivity extends AppCompatActivity {

    int content_id[] = { R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
    int noOfContent = 0, loopi=0;
    ImageView imageView;

    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        getSupportActionBar().hide();

        imageView = findViewById(R.id.header_content);
        noOfContent = content_id.length;

        linear = findViewById(R.id.linear);


        loopAnim();

    }


    void loopAnim(){
        Runnable r = new Runnable() {
            @Override
            public void run() {


                if(noOfContent-1==loopi)
                    loopi = 0;
                else
                    loopi++;

                imageView.setImageResource(content_id[loopi]);

                loopAnim();


                //body
            }
        };
        Handler h = new Handler();
        h.postDelayed(r,2500);
    }


    public void click(View view) {
        if (linear.getVisibility() == View.GONE)
            linear.setVisibility(View.VISIBLE);
        else
            linear.setVisibility(View.GONE);
    }
}