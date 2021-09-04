package adosy.edu.myapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class AdminUsersList extends AppCompatActivity implements AdminRecyclerViewAdapter.ItemClickListener {

    ListView lv;
    TextView nameTv, mobileTv, emailTv, addressTv, genderTv, imageTv;

    String jsonStr="";
    int length;

    HashMap<Character, Character> rule = new HashMap<>();       //encryption table
    HashMap<Character, Character> ruleDe = new HashMap<>();     //decryption table
    HashMap<Character, Character> rule2 = new HashMap<>();       //encryption table2
    HashMap<Character, Character> ruleDe2 = new HashMap<>();     //decryption table2

    RecyclerView recyclerView;
    AdminRecyclerViewAdapter adminRecyclerViewAdapter;
    ArrayList<String> arrListName = new ArrayList<>();
    ArrayList<String> arrListPhone = new ArrayList<>();
    ArrayList<String> arrListMail = new ArrayList<>();
    ArrayList<String> arrListLocation = new ArrayList<>();
    ArrayList<String> arrListDate = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_list);
        getSupportActionBar().hide();

        //connecting frontend
        //textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.rvUsersList);
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminUsersList.this));


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

    private class GetApiCall extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
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

                    //nameArr[i] = decrypt2(temp.getString("name"));
                    //phoneArr[i] = decrypt(temp.getString("phone"));
                    //emailArr[i] = decrypt(temp.getString("email"));
                    //locationArr[i] = (temp.getString("location"));
                    //createdArr[i] = (temp.getString("created"));

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
        }
    }

    //RecyclerView click listener
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, ""+arrListName.get(position), Toast.LENGTH_LONG).show();
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
    void Create2ndRuleForEncryptionAndDecryption(){
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

        for ( Character key : rule2.keySet() ) {
            ruleDe2.put(rule2.get(key), key);
        }
    }


}