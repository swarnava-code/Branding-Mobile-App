package adosy.edu.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    TableRow tr1, tr2;
    Animation slide_right_in, slide_left_in, fade_in, slide_right_in_;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        tr1 = findViewById(R.id.row1);
        tr2 = findViewById(R.id.row2);
        imageView = findViewById(R.id.logo);

        slide_right_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        slide_right_in_ = AnimationUtils.loadAnimation(this, R.anim.slide_right_in_);
        slide_left_in = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);

    }

    @Override
    protected void onStart() {
        super.onStart();
        imageView.startAnimation(slide_left_in);
        tr1.startAnimation(slide_right_in);
        tr2.startAnimation(slide_right_in_);

    }

    public void click(View view) {
        Toast.makeText(this, "Extra", Toast.LENGTH_SHORT).show();
    }

    public void blogs(View view) {
        Intent i=new Intent(getApplicationContext(), BlogActivity.class);
        startActivity(i);
    }
    public void courses(View view) {
        Intent i=new Intent(getApplicationContext(), CoursesActivity.class);
        startActivity(i);
    }
    public void services(View view) {
        Intent i = new Intent(getApplicationContext(), ServicesActivity.class);
        startActivity(i);
    }

    public void contact(View view) {
        Intent i = new Intent(getApplicationContext(), ContactActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder abuilder = new AlertDialog.Builder(HomeActivity.this, R.style.Theme_AppCompat_Dialog_Alert);
        abuilder.setTitle("Want to Exit ?");
        abuilder.setIcon(R.drawable.adosy_logo);
        abuilder.setMessage("Click Yes to exit from app");

        abuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        })
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {//set negative button
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int pid = android.os.Process.myPid();
                android.os.Process.killProcess(pid);
            }
        })
                .show();

    }
}