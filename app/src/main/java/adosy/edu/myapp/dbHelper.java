package adosy.edu.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "adosy_db";
    private static final int DB_VERSION = 2;

    private static final String TABLE_NAME = "setting";

    private static final String COL0 = "id";
    private static final String COL1 = "verified";    //after installation verified or not
    private static final String COL2 = "theme";       //night or day
    private static final String COL3 = "admin_verified";



    private static final String CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + " (" + COL0 + "," + COL1 + "," + COL2 + "," + COL3 + ")";

    public  dbHelper(Context context){
        super(context, DB_NAME,null, DB_VERSION);
        Log.i("DB Message","setting details db created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY);
        Log.i("DB Message","setting details table created");
    }

    public void insertData(String verified, String theme , String admin_verified, SQLiteDatabase db){
        ContentValues content = new ContentValues();

        content.put(COL0,"415");
        content.put(COL1,verified);
        content.put(COL2,theme);
        content.put(COL3,admin_verified);


        db.insert(TABLE_NAME,null,content);
        Log.i("DB Message","1 data inserted in details");
    }

    public Cursor viewData(SQLiteDatabase db){
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return  cursor;
    }

    public int updateVerifiedData (String verified , SQLiteDatabase db ){
        ContentValues content=new ContentValues();

        content.put(COL1,verified);

        String where="id=?";

        String[] whereArgs={String.valueOf("415")};

        int num_rows=db.update(TABLE_NAME, content, where, whereArgs);

        return num_rows;
    }

    public int updateAdminVerifiedData (String admin_verified , SQLiteDatabase db ){
        ContentValues content=new ContentValues();

        content.put(COL3, admin_verified);

        String where = "id=?";

        String[] whereArgs = {String.valueOf("415")};

        int num_rows = db.update(TABLE_NAME, content, where, whereArgs);

        return num_rows;
    }

    public int updateThemeData (String theme , SQLiteDatabase db ){
        ContentValues content=new ContentValues();

        content.put(COL2, theme );

        String where="id=?";

        String[] whereArgs={String.valueOf("415")};

        int num_rows=db.update(TABLE_NAME,content,where,whereArgs);

        return num_rows;

    }

    @Override
    public void onUpgrade (SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
