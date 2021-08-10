package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().hide();//2nd way

    }

    public void back_contact(View view) {
        Toast.makeText(this, "sdfet", Toast.LENGTH_SHORT).show();
        finish();
    }
}