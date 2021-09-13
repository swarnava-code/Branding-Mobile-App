package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int DELAY = 3000; //3000

    Animation slide_up_in;
    TextView the_power_of_marketing;
    ImageView a, dosy;

    String theme="light", verified="no", admin_verified="no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//1st way
        getSupportActionBar().hide();//2nd way

        slide_up_in = AnimationUtils.loadAnimation(this, R.anim.slide_up_in_);
        the_power_of_marketing = findViewById(R.id.textView);
        //logo = findViewById(R.id.imageView);
        a = findViewById(R.id.a);
        dosy = findViewById(R.id.dosy);
        
        Runnable r = new Runnable() {
            @Override
            public void run() {
                dosy.setVisibility(View.VISIBLE);
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 1000);

    }


    String print = "THE POWER OF MARKETING", update="";
    char temp;
    void typing_anim(){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if(print.length()>0){
                    temp = print.charAt(0);
                    print = print.substring(1);
                    update += temp;
                    the_power_of_marketing.setText(update);
                    typing_anim();
                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, 100);
    }



    public void check_db(){
        dbHelper user= new dbHelper(MainActivity.this);
        SQLiteDatabase dbR= user.getReadableDatabase();
        Cursor c = user.viewData(dbR);

        if(c.getCount() == 0) {
            SQLiteDatabase dbW = user.getWritableDatabase();
            user.insertData("no", "light", "no", dbW);
        }
        else {
            c.moveToFirst();
            if( (c.getString(1).length()!=0) & (c.getString(2).length()!=0) ){
                verified = c.getString(1);
                theme = c.getString(2);
                admin_verified = c.getString(3);
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();

        check_db(); //checking db, getting value

        if(theme.equals("dark")){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }



        Runnable r = new Runnable() {
            @Override
            public void run() {

                if(verified.equals("yes")){
                    Bundle b = new Bundle();
                    b.putString("key", "Services");
                    Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                }
                else if(admin_verified.equals("yes")){
                    Intent i = new Intent(getApplicationContext(), AdminUsersList.class);
                    startActivity(i);
                    finish();
                }
                else{
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        Handler h = new Handler();
        h.postDelayed(r, DELAY);




        the_power_of_marketing.startAnimation(slide_up_in);
        a.startAnimation(slide_up_in);
        typing_anim();
    }
}