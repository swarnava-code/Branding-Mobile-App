package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CoursesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        getSupportActionBar().hide();


    }
}