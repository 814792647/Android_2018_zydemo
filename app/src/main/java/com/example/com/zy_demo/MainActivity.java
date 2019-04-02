package com.example.com.zy_demo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Userdb helper;
    private User_Handle user_handle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new Userdb(this);
        user_handle = new User_Handle(this);
    }

    public void Login(View view){
        EditText username = findViewById(R.id.username);
        EditText passwd = findViewById(R.id.passwd);
        int root = user_handle.Login(username.getText().toString(),passwd.getText().toString());
        if(root != 0){
            Cursor cursor = user_handle.GetUserInfo(username.getText().toString());
            if(cursor!=null){
                cursor.moveToFirst();
                Intent intent = new Intent(this, MySelf.class);
                intent.putExtra("username", username.getText());
                intent.putExtra("groupname",cursor.getString(cursor.getColumnIndex("groupname")));
                intent.putExtra("grade",cursor.getString(cursor.getColumnIndex("grade")));
                if(root == 1){
                    intent.putExtra("root","馆长");
                }else{
                    intent.putExtra("root","管理员");
                }
                startActivity(intent);
                user_handle.Close();
                this.finish();
            }
        }
    }

    public void Register(View view){
        Intent intent = new Intent(this,RegisterAcitvity.class);
        startActivity(intent);
    }

    public void Forgot(View view){
        Intent intent = new Intent(this,ForgotActivity.class);
        startActivity(intent);
    }

    public void Delete(View view){
        user_handle.Delete();
    }
    public void Create(View view){
        user_handle.Create();
    }

}
