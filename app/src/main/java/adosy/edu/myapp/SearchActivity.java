package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide(); //hide action bar

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);



        int resId = getResources().getIdentifier("suggestions", "array", getPackageName());
        String[] suggestion = getResources().getStringArray(resId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, suggestion);
        autoCompleteTextView.setAdapter(adapter);


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
                String itemClicked = autoCompleteTextView.getText().toString();
                autoCompleteTextView.setText("");
                Bundle b = new Bundle();
                b.putString("key", itemClicked);
                Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });








    }

    public void back(View view) {
        finish();
    }
}


