package adosy.edu.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminUsersList extends AppCompatActivity implements AdminRecyclerViewAdapter.ItemClickListener {

    String jsonStr="";
    int length;

    HashMap<Character, Character> rule = new HashMap<>();       //encryption table
    HashMap<Character, Character> ruleDe = new HashMap<>();     //decryption table
    HashMap<Character, Character> rule2 = new HashMap<>();       //encryption table2
    HashMap<Character, Character> ruleDe2 = new HashMap<>();     //decryption table2

    RecyclerView recyclerView;
    AdminRecyclerViewAdapter adminRecyclerViewAdapter;
    AdminRecyclerViewAdapter adminRecyclerViewAdapter2;
    ArrayList<String> arrListName = new ArrayList<>();
    ArrayList<String> arrListPhone = new ArrayList<>();
    ArrayList<String> arrListMail = new ArrayList<>();
    ArrayList<String> arrListLocation = new ArrayList<>();
    ArrayList<String> arrListDate = new ArrayList<>();

    AutoCompleteTextView autoCompleteTextView;
    CardView showAllUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_list);
        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.rvUsersList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminUsersList.this));
        autoCompleteTextView = findViewById(R.id.autotv);
        showAllUsers = findViewById(R.id.SHOW_ALL_USERS);

        //Auto Complete TextView Listener
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int p, long l) {
                itemClicked = autoCompleteTextView.getText().toString();
                autoCompleteTextView.setText("");
                new AdminUsersList.SearchByName().execute();
            }
        });


        //creating rule table for decryption
        CreateRuleForEncryptionAndDecryption();
        Create2ndRuleForEncryptionAndDecryption();

        //getting users details
        new AdminUsersList.GetApiCall().execute();
    }

    public void logout(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)//set icon
                .setTitle("Logout ?")//set title
                .setMessage("You have to login again later to access the app if you logout now !")//set message
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {//set positive button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHelper user= new dbHelper(AdminUsersList.this);
                        SQLiteDatabase dbW = user.getWritableDatabase();
                        user.updateAdminVerifiedData("no",dbW);

                        Intent intent = new Intent(AdminUsersList.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
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

    public void showAll(View view) {
        searchByNameTypeListenerFlag = false;
        //setting user details in recyclerview
        adminRecyclerViewAdapter = new AdminRecyclerViewAdapter
                (AdminUsersList.this, arrListName, arrListPhone, arrListMail, arrListLocation, arrListDate);
        adminRecyclerViewAdapter.setClickListener( (AdminRecyclerViewAdapter.ItemClickListener) AdminUsersList.this);
        recyclerView.setAdapter(adminRecyclerViewAdapter);
        showAllUsers.setVisibility(View.GONE);
    }

    private class GetApiCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            searchByNameTypeListenerFlag = false;
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            jsonStr = sh.makeServiceCall("https://swarnava.delgradecorporation.in/project2/get_users_details.php?apikey=swarnava_get");
            try {
                JSONArray parentArray = new JSONArray(jsonStr);
                length = parentArray.length();
                for(int i=0; i<length; i++){
                    JSONObject temp = parentArray.getJSONObject(i);
                    //arrListName, arrListPhone, arrListMail, arrListLocation, arrListDate

                    arrListName.add(decrypt2(temp.getString("name")));
                    arrListPhone.add(decrypt(temp.getString("phone")));
                    arrListMail.add(decrypt(temp.getString("email")));
                    arrListLocation.add((temp.getString("location")));
                    arrListDate.add((temp.getString("created")));

                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //setting user details in recyclerview
            adminRecyclerViewAdapter = new AdminRecyclerViewAdapter
                    (AdminUsersList.this, arrListName, arrListPhone, arrListMail, arrListLocation, arrListDate);
            adminRecyclerViewAdapter.setClickListener( (AdminRecyclerViewAdapter.ItemClickListener) AdminUsersList.this);
            recyclerView.setAdapter(adminRecyclerViewAdapter);

            //Auto Complete TextView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminUsersList.this, android.R.layout.simple_spinner_dropdown_item, arrListName);
            autoCompleteTextView.setAdapter(adapter);//android.R.layout.select_dialog_item
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        }
    }




    String itemClicked;
    ArrayList<String> arrListNameTemp = new ArrayList<>();
    ArrayList<String> arrListPhoneTemp = new ArrayList<>();
    ArrayList<String> arrListMailTemp = new ArrayList<>();
    ArrayList<String> arrListLocationTemp = new ArrayList<>();
    ArrayList<String> arrListDateTemp = new ArrayList<>();

    ArrayList<String> arrListNameSearch = new ArrayList<>();
    ArrayList<String> arrListPhoneSearch = new ArrayList<>();
    ArrayList<String> arrListMailSearch = new ArrayList<>();
    ArrayList<String> arrListLocationSearch = new ArrayList<>();
    ArrayList<String> arrListDateSearch = new ArrayList<>();
    boolean searchByNameTypeListenerFlag = false;



    private class SearchByName extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            showAllUsers.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //clearing
            arrListNameSearch.clear();
            arrListPhoneSearch.clear();
            arrListMailSearch.clear();
            arrListLocationSearch.clear();
            arrListDateSearch.clear();
            arrListNameTemp.clear();
            arrListPhoneTemp.clear();
            arrListMailTemp.clear();
            arrListLocationTemp.clear();
            arrListDateTemp.clear();

            //declare and assigning
            searchByNameTypeListenerFlag = true;
            int pos;

            //cloning
            arrListNameTemp = (ArrayList)arrListName.clone();
            arrListPhoneTemp = (ArrayList)arrListPhone.clone();
            arrListMailTemp = (ArrayList)arrListMail.clone();
            arrListLocationTemp = (ArrayList)arrListLocation.clone();
            arrListDateTemp = (ArrayList)arrListDate.clone();

            while (arrListNameTemp.contains(itemClicked)){
                    pos = arrListNameTemp.indexOf(itemClicked);

                    //getting temp value
                    arrListNameSearch.add (arrListName.get(pos));
                    arrListPhoneSearch.add (arrListPhone.get(pos));
                    arrListMailSearch.add (arrListMail.get(pos));
                    arrListLocationSearch.add (arrListLocation.get(pos));
                    arrListDateSearch.add (arrListDate.get(pos));

                    //remove old value
                    arrListNameTemp.remove (pos);
                    arrListPhoneTemp.remove (pos);
                    arrListMailTemp.remove (pos);
                    arrListLocationTemp.remove (pos);
                    arrListDateTemp.remove (pos);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //setting 1 or some user details in recyclerview
            adminRecyclerViewAdapter2 = new AdminRecyclerViewAdapter
                    (AdminUsersList.this, arrListNameSearch, arrListPhoneSearch, arrListMailSearch, arrListLocationSearch, arrListDateSearch);
            adminRecyclerViewAdapter2.setClickListener( (AdminRecyclerViewAdapter.ItemClickListener) AdminUsersList.this);
            recyclerView.setAdapter(adminRecyclerViewAdapter2);
        }
    }

    //RecyclerView click listener
    @Override
    public void onItemClick(View view, int position) {
       // Toast.makeText(this, ""+arrListName.get(position), Toast.LENGTH_LONG).show();


        if(searchByNameTypeListenerFlag) {

            AlertDialog.Builder builder = new AlertDialog.Builder(AdminUsersList.this);
            builder.setTitle("Contact");
            builder.setMessage("Want to contact with "+arrListNameTemp.get(position)+" ?");
            builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+91"+arrListPhoneTemp.get(position), null));
                    startActivity(intent);
                    dialog.cancel();
                    //Toast.makeText(AdminUsersList.this, "Yes button Clicked!", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Send Mail", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] TO = {arrListMailTemp.get(position)};
                    String[] CC = {"gourav.kapoor@adosy.in", "akhilesh.shaw@adosy.in"};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Adosy");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello "+arrListNameTemp.get(position)+",\n");
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        //Log.i("Finished sending email...", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(AdminUsersList.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNeutralButton("Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String locationMap="";
                    if(arrListLocation.get(position).length()>14){
                        locationMap = "https://www.google.com/maps/search/" +
                                arrListLocation.get(position);
                    }
                    else{
                        locationMap = "http://ip-api.com/json/" +
                                arrListLocation.get(position);
                    }
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(locationMap));
                    startActivity(intent);
                    //Toast.makeText(AdminUsersList.this, "Neutral button Clicked!", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });
            AlertDialog diag = builder.create();
            diag.show();




        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminUsersList.this);
            builder.setTitle("Contact");
            builder.setMessage("Want to contact with "+arrListName.get(position)+" ?");
            builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+91"+arrListPhone.get(position), null));
                    startActivity(intent);
                    dialog.cancel();
                    //Toast.makeText(AdminUsersList.this, "Yes button Clicked!", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Send Mail", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String[] TO = {arrListMail.get(position)};
                    String[] CC = {"gourav.kapoor@adosy.in", "akhilesh.shaw@adosy.in"};
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Adosy");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello "+arrListName.get(position)+",\n");
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                        //Log.i("Finished sending email...", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(AdminUsersList.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNeutralButton("Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String locationMap="";
                    if(arrListLocation.get(position).length()>14){
                        locationMap = "https://www.google.com/maps/search/" +
                                arrListLocation.get(position);
                    }
                    else{
                        locationMap = "http://ip-api.com/json/" +
                                arrListLocation.get(position);
                    }
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(locationMap));
                    startActivity(intent);
                    //Toast.makeText(AdminUsersList.this, "Neutral button Clicked!", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                }
            });
            AlertDialog diag = builder.create();
            diag.show();
        }



        //findResourceAndView(arrList.get(position));

    }

    String decrypt(String str){
        String Newstr="";
        try {
            for (int i=0;i<str.length();i++) {
                char ch = str.charAt(i);
                Newstr += ruleDe.get(ch);
            }
        }
        catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return Newstr;
    }

    String decrypt2(String str){
        String Newstr="";
        //System.out.print("Enter the String you want to Decrypt: ");
        try {
            for (int i=0;i<str.length();i++) {
                char ch = str.charAt(i);

                Newstr += ruleDe2.get(ch);

            }
        }
        catch(Exception ioe) {
            ioe.printStackTrace();
        }
        return Newstr;
    }


    //creating rule for encryption
    void CreateRuleForEncryptionAndDecryption(){
        rule.put(' ','!');
        rule.put('a','0');
        rule.put('b','c');
        rule.put('c','e');
        rule.put('d','G');
        rule.put('e','f');
        rule.put('f','1');
        rule.put('g','d');
        rule.put('h','j');
        rule.put('i','i');
        rule.put('j','X');
        rule.put('k','m');
        rule.put('l','n');
        rule.put('m','q');
        rule.put('n','h');
        rule.put('o','P');
        rule.put('p','S');
        rule.put('q','l');
        rule.put('r','o');
        rule.put('s','r');
        rule.put('t','s');
        rule.put('u','E');
        rule.put('v','6');
        rule.put('w','U');
        rule.put('x','v');
        rule.put('y','z');
        rule.put('z','y');
        rule.put('@','3');
        rule.put('.','x');
        rule.put('0','w');
        rule.put('1','5');
        rule.put('2','4');
        rule.put('3','b');
        rule.put('4','9');
        rule.put('5','A');
        rule.put('6','8');
        rule.put('7','@');
        rule.put('8','7');
        rule.put('9','.');
        rule.put('+','^');

        for ( Character key : rule.keySet() ) {
            ruleDe.put(rule.get(key), key);
        }
    }

    //creating encryption rule for user name
    void Create2ndRuleForEncryptionAndDecryption() {

        rule2.put(' ','0');
        rule2.put('a','f');
        rule2.put('b','p');
        rule2.put('c','a');
        rule2.put('d','1');
        rule2.put('e','q');
        rule2.put('f','9');
        rule2.put('g','i');
        rule2.put('h','7');
        rule2.put('i','o');
        rule2.put('j','g');
        rule2.put('k','6');
        rule2.put('l','c');
        rule2.put('m','n');
        rule2.put('n','h');
        rule2.put('o','b');
        rule2.put('p','2');
        rule2.put('q','j');
        rule2.put('r','l');
        rule2.put('s','5');
        rule2.put('t','k');
        rule2.put('u','4');
        rule2.put('v','d');
        rule2.put('w','8');
        rule2.put('x','3');
        rule2.put('y','m');
        rule2.put('z','e');

        rule2.put('A','I');
        rule2.put('B','G');
        rule2.put('C','P');
        rule2.put('D','W');
        rule2.put('E','J');
        rule2.put('F','B');
        rule2.put('G','X');
        rule2.put('H','A');
        rule2.put('I','V');
        rule2.put('J','D');
        rule2.put('K','S');
        rule2.put('L','R');
        rule2.put('M','F');
        rule2.put('N','O');
        rule2.put('O','C');
        rule2.put('P','K');
        rule2.put('Q','Z');
        rule2.put('R','E');
        rule2.put('S','H');
        rule2.put('T','U');
        rule2.put('U','M');
        rule2.put('V','Y');
        rule2.put('W','Q');
        rule2.put('X','N');
        rule2.put('Y','T');
        rule2.put('Z','L');

        for ( Character key : rule2.keySet() ) {
            ruleDe2.put(rule2.get(key), key);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //Exit Alert Dialog
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)//set icon
                .setTitle("Exit")//set title
                .setMessage("Are you sure to Exit ?")//set message
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {//set positive button
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        android.os.Process.killProcess(android.os.Process.myPid());
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
}