package adosy.edu.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

public class ServicesActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    int content_id[] = { R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.adosy_logo};
    int noOfContent = 0, loopi=0;
    ImageView imageView;
    TextView details;
    LinearLayout digital_marketing_layout;

    MyRecyclerViewAdapter adapter;

    ArrayList<String> arrList = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;

    //search
    /*
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;

    ListView listview;
    //RecyclerView listview;
    String text="services";

     */

    @Override
    public void onItemClick(View view, int position) {
        findResourceAndView(arrList.get(position));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        getSupportActionBar().hide();

        Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();

        details = findViewById(R.id.details);

        Bundle b = this.getIntent().getExtras();

        imageView = findViewById(R.id.header_content);
        autoCompleteTextView = findViewById(R.id.auto);
        noOfContent = content_id.length;

        digital_marketing_layout = findViewById(R.id.digital_marketing_layout);


        for(int i=0; i<b.getStringArray("key").length; i++)
            arrList.add(b.getStringArray("key")[i]);


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, arrList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);




        String[] suggestion = {"Digital Marketing", "Website Development", "Dynamic Website", "E-Commerce Website", "Customized Website"};
        try{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, suggestion);
            autoCompleteTextView.setAdapter(adapter);
        }
        catch(Resources.NotFoundException e){
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, suggestion);
            autoCompleteTextView.setAdapter(adapter);
        }

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
                //hideKeyboard(OpCodeConvertor.this);

                String itemClicked = autoCompleteTextView.getText().toString();
                autoCompleteTextView.setText("");
                findResourceAndView(itemClicked);



                //Toast.makeText(ServicesActivity.this, ":"+itemClicked, Toast.LENGTH_SHORT).show();



            }
        });




        loopAnim();
    }
    void findResourceAndView(String stringPass){
        try{
            int resId = getResources().getIdentifier(stringPass.toLowerCase().replaceAll("\\s", "_"), "array", getPackageName());
            String[] stringArray = getResources().getStringArray(resId);
            Bundle b = new Bundle();
            b.putStringArray("key", stringArray);
            Intent i=new Intent(getApplicationContext(), ServicesActivity.class);
            i.putExtras(b);
            startActivity(i);

        }catch (Exception e){
            String string ="";
            int resId = getResources().getIdentifier(stringPass.toLowerCase().replaceAll("\\s", "_"), "string", getPackageName());
            string = getResources().getString(resId);
            details.setText(string);
        }
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




    public void click_sub_menu(View view) {
        TextView tv = (TextView) view;
        String text = tv.getText().toString().toLowerCase().replaceAll("\\s","_");
        int id =  getResources().getIdentifier(text, "array", getPackageName());



    }









    public void back(View view) {
        finish();
    }
    public void contact(View view) {
        Intent i=new Intent(getApplicationContext(), ContactActivity.class);
        startActivity(i);
    }

    public void digital_marketing_btn(View view) {
        ImageButton b = (ImageButton) view;
        if (digital_marketing_layout.getVisibility() == View.GONE) {
            b.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            digital_marketing_layout.setVisibility(View.VISIBLE);
        }
        else{
            b.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            digital_marketing_layout.setVisibility(View.GONE);
        }

    }


}