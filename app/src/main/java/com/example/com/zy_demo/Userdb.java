package com.example.com.zy_demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class Userdb extends SQLiteOpenHelper {

    private Userdb helper;
    public Userdb(Context context){
        super(context,"user.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(_id integer primary key autoincrement," +
                "phone varchar(11)," +
                "passwd text," +
                "grade text," +
                "groupname text," +
                "name text," +
                "sex text," +
                "question text," +
                "answer text," +
                "root varchar(1))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
