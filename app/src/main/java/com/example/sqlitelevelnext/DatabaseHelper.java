package com.example.sqlitelevelnext;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DataBase="crud.db";
    private static final String Table="UserData";
    private static final String column1="Id";
    private static final String column2="Name";
    private static final String column3="phone";
    private static final String column4="Email";
    private static final String column5="Image";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DataBase, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String Create_Table="Create table "+Table+"("+column1+" integer,"+column2+" varchar(30),"+column3+"  varchar(20),"+column4+" varchar(100) primary key,"+column5+" blob)";
        db.execSQL(Create_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("Drop table if exists "+Table);

    }

    public boolean userFound(String email) {

        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from "+Table+" where "+column4+"=?", new String[]{email});
        if(cursor.getCount()==0)
        {
            return false;
        }
        else
            return true;
    }

    public boolean addData(UserData data) {

        SQLiteDatabase database=getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(column1,data.getId());
        contentValues.put(column2,data.getName());
        contentValues.put(column3,data.getPhone());
        contentValues.put(column4,data.getEmail());
        contentValues.put(column5,data.getImageBytes());

        long add=database.insert(Table,null,contentValues);
        if(add==-1)
            return false;
        else
            return true;
    }

    public Cursor getData() {

        SQLiteDatabase database=getReadableDatabase();
        Cursor cursor=database.rawQuery("select * from "+Table,null);
        return cursor;
    }
}
