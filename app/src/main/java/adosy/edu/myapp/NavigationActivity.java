package adosy.edu.myapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
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

public class NavigationActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, MyRecyclerViewAdapter.ItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    //Navigatio drawer
    //Variables
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    TextView textView;
    Animation anim_bounce, anim_slide_up_in, anim_slide_left_in, anim_slide_right_in ;

    LinearLayout why_choose_us_layout;
    TextView choose_title_1, choose_title_2, choose_title_3, choose_title_4,
            choose_body_1, choose_body_2, choose_body_3, choose_body_4, price_tag;
    ImageButton choose_btn1, choose_btn2, choose_btn3, choose_btn4;

    //SERVICES
    int content_id[] = { R.drawable.banner1, R.drawable.banner2, R.drawable.banner3};
    int noOfContent = 0, loopi=0;
    ImageView imageView, details_imageview, main_image_view;
    TextView details_desc, details_head, details_list, main_head, main_desc;
    LinearLayout digital_marketing_layout, details_layout;

    MyRecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    ArrayList<String> arrList = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;
    SearchView searchView;



    ImageView contact_controller, contact1, contact2, contact3;
    boolean close = true;



    NestedScrollView nestedScrollView;
    ImageView header_banner;



    //RecyclerView
    @Override
    public void onItemClick(View view, int position) {
        findResourceAndView(arrList.get(position));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //Nav Draw.
        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        textView=findViewById(R.id.textView);
        toolbar=findViewById(R.id.toolbar);
        //fab = findViewById(R.id.fab);
        why_choose_us_layout = findViewById(R.id.why_choose_us);
        choose_title_1 = findViewById(R.id.choose_title_1);
        choose_title_2 = findViewById(R.id.choose_title_2);
        choose_title_3 = findViewById(R.id.choose_title_3);
        choose_title_4 = findViewById(R.id.choose_title_4);
        choose_body_1 = findViewById(R.id.choose_body_1);
        choose_body_2 = findViewById(R.id.choose_body_2);
        choose_body_3 = findViewById(R.id.choose_body_3);
        choose_body_4 = findViewById(R.id.choose_body_4);
        choose_btn1 = findViewById(R.id.choose_ib1);
        choose_btn2 = findViewById(R.id.choose_ib2);
        choose_btn3 = findViewById(R.id.choose_ib3);
        choose_btn4 = findViewById(R.id.choose_ib4);
        main_image_view = findViewById(R.id.main_image_view);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        header_banner = findViewById(R.id.header_content);
        price_tag = findViewById(R.id.price_tag);
        anim_bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        anim_slide_up_in = AnimationUtils.loadAnimation(this, R.anim.slide_up_in_);
        anim_slide_left_in = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
        anim_slide_right_in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        noOfContent = content_id.length;
        loopAnim();

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new
                ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        findResourceAndView(b.getString("key"));

        imageView = findViewById(R.id.header_content);
        autoCompleteTextView = findViewById(R.id.auto);
        searchView = findViewById(R.id.search_view);
        noOfContent = content_id.length;

        digital_marketing_layout = findViewById(R.id.digital_marketing_layout);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvServices);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapter(this, arrList);
        adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) this);
        recyclerView.setAdapter(adapter);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view); // initiate a Navigation View
// implement setNavigationSelectedListener event

        navigationView.setItemIconTintList(null);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                if(menuItem.getGroupId()==R.id.gr_services
                        || menuItem.getGroupId()==R.id.gr_digital_branding
                        || menuItem.getGroupId()==R.id.gr_digital_marketing
                        || menuItem.getGroupId()==R.id.gr_digital_art){
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            findResourceAndView(menuItem.getTitle().toString());
                        }
                    };
                    Handler h = new Handler();
                    h.postDelayed(r,500);
                }
                else {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_login:
                            Toast.makeText(NavigationActivity.this, "login", Toast.LENGTH_SHORT).show();
                            menu.findItem(R.id.nav_logout).setVisible(true);
                            menu.findItem(R.id.nav_profile).setVisible(true);
                            menu.findItem(R.id.nav_login).setVisible(false);
                            break;
                        case R.id.nav_logout:
                            menu.findItem(R.id.nav_logout).setVisible(false);
                            menu.findItem(R.id.nav_profile).setVisible(false);
                            menu.findItem(R.id.nav_login).setVisible(true);
                            break;
                        case R.id.nav_share:
                            Intent i = new Intent(Intent.ACTION_SEND);
                            i.setType("text/plain");
                            i.putExtra(Intent.EXTRA_SUBJECT, "Adosy App link");
                            i.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id="+getPackageName());
                            startActivity(Intent.createChooser(i, "Share URL"));
                            break;
                        case R.id.nav_rate:
                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
                            startActivity(intent);
                            break;
                        case R.id.nav_close:
                            exit();
                            break;
                        case R.id.nav_dark:
                            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES)
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            else
                                Toast.makeText(NavigationActivity.this, "Already Night mode on", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.nav_light:
                            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO)
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            else
                                Toast.makeText(NavigationActivity.this, "Already Light mode on", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                drawerLayout.closeDrawer(GravityCompat.START); return true;
            }
        });

        //######################################################################

        contact_controller = findViewById(R.id.contact_controller);
        contact1 = findViewById(R.id.contact1);
        contact2 = findViewById(R.id.contact2);
        contact3 = findViewById(R.id.contact3);
        close = true;

    }

    void findResourceAndView(String stringPass){
        /*
        main_desc.setText("");
        main_head.setText("");
        main_head.setVisibility(View.GONE);
        main_desc.setVisibility(View.GONE);
         */


        details_layout.setVisibility(View.GONE);
        why_choose_us_layout.setVisibility(View.GONE);
        hideAllChooseBody();

        String main_name = stringPass.toLowerCase().replaceAll("\\s", "_")
                .replaceAll("-","_")
                .replaceAll(",","_")
                .replaceAll("3","_")
                .replaceAll("/","_");

        if(main_name.equals(""))
            main_name = "services";


        //nestedScrollView.scrollTo(0,0);

        //Toast.makeText(this, "="+main_name, Toast.LENGTH_SHORT).show();

        try{
            String name = main_name;
            int resId = getResources().getIdentifier(name, "array", getPackageName());
            String[] stringArray = getResources().getStringArray(resId);

            main_head.setText(stringPass);
            main_desc.setText(getResources().getString(getResources().getIdentifier(name+"_main", "string", getPackageName())));
            main_head.setVisibility(View.VISIBLE);
            main_head.startAnimation(anim_slide_right_in);
            main_desc.setVisibility(View.VISIBLE);
            main_desc.startAnimation(anim_slide_left_in);
            arrList.clear();

            //To load image in each row
            main_image_view.setImageResource(getResources().getIdentifier(main_name, "drawable", getPackageName()));
            main_image_view.startAnimation(anim_bounce);


            for(int i=0; i<stringArray.length; i++)
                arrList.add(stringArray[i]);

            // set up the RecyclerView
            recyclerView = findViewById(R.id.rvServices);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new MyRecyclerViewAdapter(this, arrList);
            adapter.setClickListener((MyRecyclerViewAdapter.ItemClickListener) this);
            recyclerView.setAdapter(adapter);
            recyclerView.startAnimation(anim_bounce);

            nestedScrollView.scrollTo(0,0);

            try{
                String [] choose_title = getResources().getStringArray(getResources().getIdentifier(main_name+"_choose_title", "array", getPackageName()));
                String [] choose_body = getResources().getStringArray(getResources().getIdentifier(main_name+"_choose_body", "array", getPackageName()));

                choose_title_1.setText(choose_title[0]);
                choose_title_2.setText(choose_title[1]);
                choose_title_3.setText(choose_title[2]);
                choose_title_4.setText(choose_title[3]);

                choose_body_1.setText(choose_body[0]);
                choose_body_2.setText(choose_body[1]);
                choose_body_3.setText(choose_body[2]);
                choose_body_4.setText(choose_body[3]);

                why_choose_us_layout.setVisibility(View.VISIBLE);
                why_choose_us_layout.startAnimation(anim_slide_up_in);
            }catch (Exception e){

            }

        } catch (Exception e){
            try {
                String details = main_name;
                String details_head_str = details+"_", details_desc_str = details+"_desc", details_list_str = details+"_list";
                details_layout.setVisibility(View.VISIBLE);
                details_desc.setText(getResources().getString(getResources().getIdentifier(details_desc_str, "string", getPackageName())));
                details_desc.startAnimation(anim_slide_right_in);
                details_list.setText(getString(R.string.list_point)+" "+getResources().getString(getResources().getIdentifier(details_list_str, "string", getPackageName())).replaceAll("\n",getString(R.string.list_point)).trim());
                details_list.startAnimation(anim_slide_right_in);
                details_head.setText(stringPass);
                details_head.startAnimation(anim_slide_up_in);
                details_imageview.setImageResource(getResources().getIdentifier(details, "drawable", getPackageName()));
                details_imageview.startAnimation(anim_slide_left_in);
                why_choose_us_layout.setVisibility(View.VISIBLE);
                //details_layout.startAnimation(anim_slide_left_in);

                nestedScrollView.scrollTo(0,1100);

                String price = "₹ "+getResources().getString(getResources().getIdentifier(main_name+"_price", "string", getPackageName()));
                price_tag.setText(price);
                price_tag.startAnimation(anim_bounce);

            } catch (Exception e1){
                Toast.makeText(this, "No data in string.xml", Toast.LENGTH_SHORT).show();
            }
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
        menu = navigationView.getMenu();
        menu.findItem(R.id.nav_logout).setVisible(false);
        menu.findItem(R.id.nav_profile).setVisible(false);
        //it's opening drawer
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void refund_policy(MenuItem item) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://adosy.in/refund-policy/"));
        startActivity(intent);
    }
    public void privacy_policy(MenuItem item) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://adosy.in/privacy-policy-2/"));
        startActivity(intent);
    }
    public void terms_and_condition(MenuItem item) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://adosy.in/terms-and-condition/"));
        startActivity(intent);
    }
    public void payment_process(MenuItem item) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://adosy.in/payment-process/"));
        startActivity(intent);
    }
    public void referral_policy(MenuItem item) {
        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://adosy.in/referral-policy/"));
        startActivity(intent);
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

        return false;
    }



    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void test(View view) {
        Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            //exit();
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }
    void exit(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)//set icon
                .setTitle("Exit")//set title
                .setMessage("Are you sure to Exit ?")//set message
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {//set positive button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        //finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {//set negative button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Toast.makeText(getApplicationContext(),"Nothing Happened", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    public void choose_title_1(View view) {
        ImageButton imageButton = (ImageButton) view;

        if(choose_body_1.getVisibility()==View.VISIBLE){
            imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            choose_body_1.setVisibility(View.GONE);
        }

        else{
            imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            choose_body_1.setVisibility(View.VISIBLE);
        }

    }
    public void choose_title_2(View view) {
        ImageButton imageButton = (ImageButton) view;

        if(choose_body_2.getVisibility()==View.VISIBLE){
            imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            choose_body_2.setVisibility(View.GONE);
        }

        else{
            imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            choose_body_2.setVisibility(View.VISIBLE);
        }

    }
    public void choose_title_3(View view) {

        ImageButton imageButton = (ImageButton) view;

        if(choose_body_3.getVisibility()==View.VISIBLE){
            imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            choose_body_3.setVisibility(View.GONE);
        }

        else{
            imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            choose_body_3.setVisibility(View.VISIBLE);
        }

    }
    public void choose_title_4(View view) {
        ImageButton imageButton = (ImageButton) view;

        if(choose_body_4.getVisibility()==View.VISIBLE){
            imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
            choose_body_4.setVisibility(View.GONE);
        }

        else{
            imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
            choose_body_4.setVisibility(View.VISIBLE);
        }

    }

    void hideAllChooseBody(){
        choose_body_1.setVisibility(View.GONE);
        choose_body_2.setVisibility(View.GONE);
        choose_body_3.setVisibility(View.GONE);
        choose_body_4.setVisibility(View.GONE);

        choose_btn1.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        choose_btn2.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        choose_btn3.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        choose_btn4.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
    }

    public void contact(View view){
        Intent i=new Intent(getApplicationContext(), ContactActivity.class);
        startActivity(i);
    }

    public void search(View view){
        Intent i=new Intent(getApplicationContext(), SearchActivity.class);
        startActivity(i);
    }

    //#############################################################################################

    public void mail(View view){
        String[] TO = {"akhilesh.shaw@adosy.in"};
        String[] CC = {"gourav.kapoor@adosy.in"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            //Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(NavigationActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public void call(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+919038026497", null));
        startActivity(intent);
        //Toast.makeText(this, "call", Toast.LENGTH_SHORT).show();

    }

    public void map(View view){
        String map = "https://www.google.com/maps/place/Adosy/@22.5487367,88.3595611,15z/data=!4m5!3m4!1s0x0:0x990e75adfb14ef22!8m2!3d22.5487154!4d88.3596674";

        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(intent);

        //Toast.makeText(this, "map", Toast.LENGTH_SHORT).show();

    }

//firebase firestore
//https://www.youtube.com/watch?v=RiHGwJ_u27k



    public void contact_controller(View view){
        if(close){
            close = false;
            contact_controller.setImageResource(R.drawable.ic_baseline_close_24);
            contact1.setVisibility(View.VISIBLE);
            contact2.setVisibility(View.VISIBLE);
            contact3.setVisibility(View.VISIBLE);
        }
        else{
            close = true;
            contact_controller.setImageResource(R.drawable.ic_baseline_contact_phone_24);
            contact1.setVisibility(View.GONE);
            contact2.setVisibility(View.GONE);
            contact3.setVisibility(View.GONE);
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

                header_banner.setImageResource(content_id[loopi]);
                header_banner.startAnimation(anim_slide_right_in);

                loopAnim();


                //body
            }
        };
        Handler h = new Handler();
        h.postDelayed(r,3500);
    }


}