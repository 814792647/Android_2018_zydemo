package com.example.com.zy_demo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Toast;

import com.example.com.zy_demo.ViewPager.PagerAdapter;

public class User_Handle {

    private Userdb helper;
    private Context con;
    public ContentValues db;
    private SQLiteDatabase sqLiteDatabase;
    public String question;
    public String answer;

    public User_Handle(Context context){
        helper = new Userdb(context);
        con = context;
        sqLiteDatabase = helper.getWritableDatabase();
        db = new ContentValues();
        question = "";
    }

    public boolean Test_Username(String username){
        Cursor cursor = sqLiteDatabase.rawQuery("select * from user where phone=?", new String[]{username});
        if(username.equals("")){
            Toast.makeText(con,"手机号不能为空",Toast.LENGTH_SHORT).show();
        }else if(cursor.getCount()> 0) {
            Toast.makeText(con, "重复用户名", Toast.LENGTH_SHORT).show();
        }else if(username.length()!=11) {
            Toast.makeText(con, "请使用手机号注册", Toast.LENGTH_SHORT).show();
        }else{
            db.put("phone",username);
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean Test_Passwd(String passwd,String secpasswd){
        if(passwd.length() == 0){
            Toast.makeText(con, "密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(!passwd.equals(secpasswd)){
            Toast.makeText(con,"两次输入不一致",Toast.LENGTH_SHORT).show();
        }else{
            db.put("passwd",passwd);
            return true;
        }
        return false;
    }

    public boolean Add_UserInfo(String grade,String name,String groupname,int sex){
        if(grade.equals("选择羽毛球等级")){
            Toast.makeText(con, "羽毛球等级不能为空", Toast.LENGTH_SHORT).show();
        }else if (groupname.length() == 0){
            Toast.makeText(con, "群昵称不能为空", Toast.LENGTH_SHORT).show();
        }else{
            db.put("grade",grade);
            db.put("groupname",groupname);
            db.put("name",name);
            if(sex == R.id.man){
                db.put("sex","男");
            }else{
                db.put("sex","女");
            }
            return true;
        }
        return false;
    }

    public boolean Set_QAR(String question,String answer,String root){
        db.put("question",question);
        db.put("answer",answer);
        if(root.equals("root")){
            root = "馆长";
            db.put("root","1");
        }else{
            root = "管理员";
            db.put("root","2");
        }
        try{
            long row = sqLiteDatabase.insert("user",null,db);
            if(row != -1){
                Toast.makeText(con,"添加成功 权限：" + root,Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(con,"添加失败",Toast.LENGTH_SHORT).show();
            }
            sqLiteDatabase.close();
            db.clear();
            return true;
        }catch (Exception e){
            sqLiteDatabase.close();
            db.clear();
            Toast.makeText(con,e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean Forgot_Test_Username(String username){
        Cursor cursor = sqLiteDatabase.rawQuery("select * from user where phone=?", new String[]{username});
        if(username.equals("")){
            Toast.makeText(con,"手机号不能为空",Toast.LENGTH_SHORT).show();
        }else if(cursor.getCount()<= 0) {
            Toast.makeText(con, "当前手机号未注册", Toast.LENGTH_SHORT).show();
        }else if(username.length()!=11) {
            Toast.makeText(con, "非法手机号", Toast.LENGTH_SHORT).show();
        }else if(cursor.moveToFirst()){
            question = cursor.getString(cursor.getColumnIndex("question"));
            answer = cursor.getString(cursor.getColumnIndex("answer"));
            //Toast.makeText(con, answer, Toast.LENGTH_SHORT).show();
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean Reset_Passwd(String username,String passwd){
        db.put("passwd",passwd);
        int row =sqLiteDatabase.update("user",db,"phone=?",new String[]{username});
        if(row != -1){
            Toast.makeText(con, "修改成功", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public Cursor GetUserInfo(String username){
        Cursor cursor = sqLiteDatabase.rawQuery("select * from user where phone=?", new String[]{username});
        if(cursor.getCount()>0){
            return cursor;
        }
        return null;
    }

    /*
     登陆方法
     Login(用户名，密码)
     返回int类 馆长=1 管理员=2
     */
    public int Login(String username,String passwd){
        try{
            int flag = 0;
            ContentValues db = new ContentValues();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from user where phone=?",new String[]{username});
            if(username.equals("")||passwd.equals("")){
                Toast.makeText(con,"用户名或密码为空",Toast.LENGTH_SHORT).show();
            }else if(cursor.getCount()<=0){
                Toast.makeText(con,"用户名不存在",Toast.LENGTH_SHORT).show();
            }else if(cursor.moveToFirst()){
                if(cursor.getString(cursor.getColumnIndex("passwd")).equals(passwd)){
                    if(cursor.getString(cursor.getColumnIndex("root")).equals("1")){
                        flag = 1;
                    }else if(cursor.getString(cursor.getColumnIndex("root")).equals("2")){
                        flag = 2;
                    }
                }else {
                    Toast.makeText(con,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
            return flag;
        }catch(Exception e){
            Toast.makeText(con,e.getMessage(),Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public void Close(){
        db.clear();
        sqLiteDatabase.close();
    }

    /*
     <!!非调试请勿调用>
     删表
     创建表
     */
    public void Delete(){
        try{
            SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
            sqLiteDatabase.execSQL("drop table user");
            Toast.makeText(con,"删表",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(con,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    public void Create(){
        try{
            SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
            helper.onCreate(sqLiteDatabase);
            Toast.makeText(con,"创建表",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(con,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
