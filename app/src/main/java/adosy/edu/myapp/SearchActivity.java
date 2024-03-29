package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
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
                go_main_page(itemClicked);
            }
        });

    }

    void go_main_page(String itemClicked){
        Bundle b = new Bundle();
        b.putString("key", itemClicked);
        Intent i = new Intent(getApplicationContext(), NavigationActivity.class);
        i.putExtras(b);
        startActivity(i);
        finish();
    }

    public void back(View view) {
        finish();
    }

    public void search_tv(View view) {
        TextView tv = (TextView) view;
        go_main_page(tv.getText().toString());

    }

    int test2 = 0;
    public void test_client_4(View view) {
        TextView textView = (TextView) view;
        test2++;
        if(test2>4) {
            textView.setText("Dev. by "+getString(R.string.developer));
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    test2 = 0;
                    textView.setText(getString(R.string.client4));
                }
            };
            Handler h = new Handler();
            h.postDelayed(r, 1500);
        }
        else{
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    test2 = 0;
                }
            };
            Handler h = new Handler();
            h.postDelayed(r,1500);
        }
    }
}


