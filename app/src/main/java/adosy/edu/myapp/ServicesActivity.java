package adosy.edu.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
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

public class ServicesActivity extends AppCompatActivity {

    int content_id[] = { R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.adosy_logo};
    int noOfContent = 0, loopi=0;
    ImageView imageView;

    LinearLayout digital_marketing_layout;


    //search
    SearchView searchView;
    ListView listView;
    ArrayList<String> list;
    ArrayAdapter<String > adapter;

    //8085
    ListView lv;
    LinearLayout keypad, disp, linearLayout, switchLayout, headingLayout;
    AutoCompleteTextView auto;
    TextView data, result, title, result123;
    Button clear;
    TextView mnemonics_tv, opcode_tv, short_desc_tv;
    int e, n=246;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);
        getSupportActionBar().hide();

        imageView = findViewById(R.id.header_content);
        noOfContent = content_id.length;

        digital_marketing_layout = findViewById(R.id.digital_marketing_layout);


        loopAnim();


/*
        //search
        searchView = (SearchView) findViewById(R.id.searchView);
        listView = (ListView) findViewById(R.id.lv1);

        list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Pineapple");
        list.add("Orange");
        list.add("Lychee");
        list.add("Gavava");
        list.add("Peech");
        list.add("Melon");
        list.add("Watermelon");
        list.add("Papaya");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(ServicesActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });

 */






}

/*
    ImageView icon;
    TextView title2;
    ListView lv2;
    String[] Title = {"Android", "Apple", "Blackberry"};
    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return n;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.elements, null);

            icon = view.findViewById(R.id.icon);
            title2  = view.findViewById(R.id.title);
            lv2  = view.findViewById(R.id.lv);

            //Picasso.with(MainActivity.this).load(movie_poster[i]).into(image);
            icon.setImageResource(R.drawable.sample_logo);
            title2.setText(Title[0]);


            ArrayAdapter<String> arr;
            arr
                    = new ArrayAdapter<String>(
                    this,
                    R.layout.support_simple_spinner_dropdown_item,
                    tutorials);
            lv2.setAdapter(arr);




            return view;
        }
    }

 */





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