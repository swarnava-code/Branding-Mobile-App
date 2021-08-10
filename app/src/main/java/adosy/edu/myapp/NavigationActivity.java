package adosy.edu.myapp;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NavigationActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, MyRecyclerViewAdapter.ItemClickListener {

    private AppBarConfiguration mAppBarConfiguration;

    //SERVICES
    int content_id[] = { R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.adosy_logo};
    int noOfContent = 0, loopi=0;
    ImageView imageView, details_imageview;
    TextView details_desc, details_head, details_list, main_head, main_desc;
    LinearLayout digital_marketing_layout, details_layout;

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<String> arrList = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;
    SearchView searchView;

    //RecyclerView

    @Override
    public void onItemClick(View view, int position) {
        //findResourceAndView(arrList.get(position));

        main_desc.setText("");
        main_head.setText("");

        try{
            String name = arrList.get(position).toLowerCase().replaceAll("\\s", "_");

            int resId = getResources().getIdentifier(name, "array", getPackageName());
            String[] stringArray = getResources().getStringArray(resId);

            main_head.setText(arrList.get(position));
            main_desc.setText(getResources().getString(getResources().getIdentifier(name+"_main", "string", getPackageName())));


            arrList.clear();


            for(int i=0; i<stringArray.length; i++)
                arrList.add(stringArray[i]);

            // set up the RecyclerView
            recyclerView = findViewById(R.id.rvServices);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyRecyclerViewAdapter(this, arrList);
            adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) this);
            recyclerView.setAdapter(adapter);





        } catch (Exception e){

            try{
                String string ="";
                String details = arrList.get(position).toLowerCase().replaceAll("\\s", "_");

                String details_head_str = details+"_", details_desc_str = details+"_desc", details_list_str = details+"_list";

                //int resId = getResources().getIdentifier(, "string", getPackageName());
                //string = getResources().getString(getResources().getIdentifier(, "string", getPackageName()));

                details_desc.setText(getResources().getString(getResources().getIdentifier(details_desc_str, "string", getPackageName())));
                details_list.setText(getResources().getString(getResources().getIdentifier(details_list_str, "string", getPackageName())));
                details_head.setText(arrList.get(position));
                details_imageview.setImageResource(getResources().getIdentifier(details, "drawable", getPackageName()));
                //main_head, main_desc
                //.setText(string);

                details_layout.setVisibility(View.VISIBLE);
            }catch (Exception e1){
                Toast.makeText(this, "No data in string.xml", Toast.LENGTH_SHORT).show();
            }



        }

    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), ContactActivity.class);
                startActivity(i);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        //SERVICES START


        details_desc = findViewById(R.id.details_desc);
        details_head = findViewById(R.id.details_head);
        details_list = findViewById(R.id.details_list);
        details_imageview = findViewById(R.id.details_imageview);
        details_layout = findViewById(R.id.details);
        main_head = findViewById(R.id.main_head);
        main_desc = findViewById(R.id.main_desc);

        //details = findViewById(R.id.details);

        Bundle b = this.getIntent().getExtras();

        imageView = findViewById(R.id.header_content);
        autoCompleteTextView = findViewById(R.id.auto);
        searchView = findViewById(R.id.search_view);
        noOfContent = content_id.length;

        digital_marketing_layout = findViewById(R.id.digital_marketing_layout);


        for(int i=0; i<b.getStringArray("key").length; i++)
            arrList.add(b.getStringArray("key")[i]);





        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, arrList);
        adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);




        String[] suggestion = {"Digital Marketing", "Website Development", "Dynamic Website", "E-Commerce Website", "Customized Website"};
        //try{

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, suggestion);
        //autoCompleteTextView.setAdapter(adapter);
        //searchView.setAppSearchData(bundle);//bundle
        //searchView.setSuggestionsAdapter(adapter);



/*
            String[] columnNames = {"_id","text"};
            MatrixCursor cursor = new MatrixCursor(columnNames);
            String[] array = getResources().getStringArray(R.array.services); //if strings are in resources
            String[] temp = new String[2];
            int id = 0;
            for(String item : array){
                temp[0] = Integer.toString(id++);
                temp[1] = item;
                cursor.addRow(temp);
            }
            String[] from = {"text"};
            int[] to = {R.id.name_entry};
            CursorAdapter busStopCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.listentry, cursor, from, to);

            searchView.setSuggestionsAdapter(busStopCursorAdapter);

 */

        //android.widget.SearchView search = findViewById(R.id.search);






/*
            List<String> items = Lists.newArrayList(new String[] {"aaaaa", "bbbbb", "ccccc", "ddddd"});
            SearchView searchView = (SearchView) findViewById(R.id.search_view);
            SearchView.SearchAutoComplete searchSrcTextView = (SearchView.SearchAutoComplete) findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchSrcTextView.setThreshold(1);
            searchSrcTextView.setAdapter(new SuggestionAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, items));
            searchSrcTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    return;
                }
            });

 */

/*
    }
        catch(
    Resources.NotFoundException e){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, suggestion);
        autoCompleteTextView.setAdapter(adapter);
        //searchView.setSuggestionsAdapter(adapter);
    }

 */

/*
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
            //hideKeyboard(OpCodeConvertor.this);

            String itemClicked = autoCompleteTextView.getText().toString();
            autoCompleteTextView.setText("");
            findResourceAndView(itemClicked);

 */



        //Toast.makeText(ServicesActivity.this, ":"+itemClicked, Toast.LENGTH_SHORT).show();



    //}
    //});




    //loopAnim();


        //SERVICES END





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
            //details.setText(string);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void digital_marketing(MenuItem item) {
        Toast.makeText(this, "MenuItem clicked", Toast.LENGTH_SHORT).show();
    }

    public void digital(MenuItem item) {
        finish();
        Toast.makeText(this, "MenuItem clicked2", Toast.LENGTH_SHORT).show();
    }

/*
    @Override
    public boolean onCreateOptionsMenu() {
        return onCreateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

 */

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_gallery:
                Toast.makeText(getApplicationContext(), "Gallery", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_payment:
                Toast.makeText(getApplicationContext(), "Payment", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_refund:
                Toast.makeText(getApplicationContext(), "Refund", Toast.LENGTH_LONG).show();
                return true;



        }

        return false;
    }
}