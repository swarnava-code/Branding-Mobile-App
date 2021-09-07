package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    String theme="light", verified="no", admin_verified="no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//1st way
        getSupportActionBar().hide();//2nd way

        slide_up_in = AnimationUtils.loadAnimation(this, R.anim.slide_up_in_);
        com = findViewById(R.id.textView);
        logo = findViewById(R.id.imageView);



    }



    public void check_db(){
        dbHelper user= new dbHelper(MainActivity.this);
        SQLiteDatabase dbR= user.getReadableDatabase();
        Cursor c = user.viewData(dbR);

        if(c.getCount() == 0) {
            SQLiteDatabase dbW = user.getWritableDatabase();
            user.insertData("no", "light", "no", dbW);
            //user.updateThemeData("light",dbW);
            //user.updateVerifiedData("no",dbW);
            //user.updateAdminVerifiedData("no",dbW);
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
/*
//for update
                dbHelper user = new dbHelper(MainActivity.this);
                SQLiteDatabase dbW = user.getWritableDatabase();
                user.updateVerifiedData("yes",dbW);
 */

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







        com.startAnimation(slide_up_in);
        logo.startAnimation(slide_up_in);
    }
}