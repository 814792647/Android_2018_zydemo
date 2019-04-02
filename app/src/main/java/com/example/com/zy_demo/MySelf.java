package com.example.com.zy_demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;

public class MySelf extends Activity {
    private Button button;
    private Button output;
    private String path;
    private Context context;
    private ArrayList<Integer> w;
    private ArrayList<Integer> m;
    private Uri uri;
    private View view;
    private String[][] person;
    private String[][] team;
    private ExcelUtils excelUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself);
        this.context = this;
        Intent intent = getIntent();
        if(intent != null){
            button = findViewById(R.id.choose);
            output = findViewById(R.id.output);
            TextView id = findViewById(R.id.id);
            TextView groupname = findViewById(R.id.login_groupname);
            TextView grade = findViewById(R.id.login_grade);
            id.setText(intent.getStringExtra("root"));
            groupname.setText("群昵称:"+ intent.getStringExtra("groupname"));
            grade.setText("等级:"+ intent.getStringExtra("grade"));
        }
        output.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(team != null){
                    excelUtils.OutputExcel(team);
                }else{
                    Toast.makeText(context,"请先导入Excel",Toast.LENGTH_SHORT).show();
                }
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult( Intent.createChooser(intent, "选择Excel文件"), 0);
                } catch (android.content.ActivityNotFoundException ex) {

                }
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(context, "未知文件类型", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    EditText me = view.findViewById(R.id.man);
                    EditText we = view.findViewById(R.id.woman);
                    if (me.getText().toString().equals("") || we.getText().toString().equals("")) {
                        Toast.makeText(context, "无法分组", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    int mu = Integer.valueOf(me.getText().toString());
                    int wu = Integer.valueOf(we.getText().toString());
                    int all = 0;
                    int flag_m = 0;
                    int flag_w = 0;
                    if ((person.length % (mu + wu)) != 0) {
                        all = person.length / (mu + wu) + 1;
                    } else {
                        all = person.length / (mu + wu);
                    }
                    team = new String[all][wu + mu];
                    for (int i = 0; i < all; i++) {
                        for (int o = 0; o < mu; o++) {
                            if (i * mu + o < m.size()) {
                                team[i][o] = person[m.get(i * mu + o)][0];
                                flag_m = i * mu + o;
                            } else {
                                break;
                            }
                        }
                        for (int o = 0; o < wu; o++) {
                            if (i * wu + o < w.size()) {
                                team[i][o + mu] = person[w.get(i * wu + o)][0];
                                flag_w = i * wu + o;
                            } else {
                                break;
                            }
                        }
                    }
                    flag_m = flag_m==0?0:++flag_m;
                    flag_w = flag_w==0?0:++flag_w;
                    for (int i = 0; i < team.length; i++) {
                        for (int o = 0; o < team[i].length; o++) {
                            if(team[i][o] == null){
                                if(flag_m < m.size()){
                                    team[i][o] = person[m.get(flag_m)][0];
                                    flag_m += 1;
                                    mu = -1;
                                }else if(flag_w < w.size()){
                                    team[i][o] = person[w.get(flag_w)][0];
                                    flag_w += 1;
                                    mu = -1;
                                }
                            }
                        }
                    }

                    TextView textView = findViewById(R.id.person);
                    String s = "";
                    for(int i=0;i<team.length;i++){
                        s += "第" + String.valueOf(i+1) + "组\r\n\t\t\t";
                        for(int j=0;j<team[i].length;j++){
                            if(team[i][j] != null){
                                s+=team[i][j] + "    ";
                            }
                        }
                        s+="\r\n";
                    }
                    textView.setMovementMethod(ScrollingMovementMethod.getInstance());
                    textView.setText(s);
                    if(mu == -1){
                        Toast.makeText(context, "当前规则无法满足全部组\r\n自动平衡已启用", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 0:
                if(resultCode == RESULT_OK){
                    uri = data.getData();
                    path = uri.getPath();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("设置分组");
                    view = LayoutInflater.from(this).inflate(R.layout.alertdialog_layout,null);
                    builder.setView(view);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try{
                                        String[] type = path.split(":");
                                        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + type[1]);
                                        person = null;
                                        excelUtils = new ExcelUtils(context);
                                        if(path.indexOf("xlsx")!=-1){
                                            person = excelUtils.InputExcel(file,1);
                                        }else  if(path.indexOf("xls")!=-1){
                                            person = excelUtils.InputExcel(file,2);
                                        }else{
                                            Message msg = new Message();
                                            msg.what = 0;
                                            handler.sendMessage(msg);
                                        }
                                        w = new ArrayList<Integer>();
                                        m = new ArrayList<Integer>();
                                        for(int i=0;i<person.length;i++){
                                            if(person[i][2].equals("男")){
                                                m.add(i);
                                            }else{
                                                w.add(i);
                                            }
                                        }
                                        Message msg = new Message();
                                        msg.what = 1;
                                        handler.sendMessage(msg);
                                    }catch (Exception e){
                                    }
                                }
                            }).run();
                        }
                    }).create();
                    builder.show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
