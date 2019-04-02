package com.example.com.zy_demo.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.zy_demo.MainActivity;
import com.example.com.zy_demo.R;
import com.example.com.zy_demo.User_Handle;

public class ForgotAdapter extends android.support.v4.view.PagerAdapter {

    public ViewPager viewPager;
    public int[] layout = new int[]{R.layout.forgot_1,R.layout.forgot_1,R.layout.forgot_1};
    private Context context;
    private User_Handle user_handle;

    private EditText username;
    private EditText passwd;
    private EditText secpasswd;
    private TextView question;
    private EditText answer;
    private String confirm_username;

    public ForgotAdapter(Context context){
        this.context = context;
        user_handle = new User_Handle(context);
    }

    @Override
    public int getCount() {
        return layout.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(layout[position],null);
        Button next = view.findViewById(R.id.next);
        switch (position){
            case 0:
                username = view.findViewById(R.id.forgot_username);
                view.findViewById(R.id.fr1).setVisibility(View.VISIBLE);
                break;
            case 1:
                answer = view.findViewById(R.id.forgot_answer);
                question = view.findViewById(R.id.forgot_question1);
                view.findViewById(R.id.fr2).setVisibility(View.VISIBLE);
                break;
            case 2:
                passwd = view.findViewById(R.id.forgot_passwd);
                secpasswd = view.findViewById(R.id.forgot_secpasswd);
                next.setText("完成");
                view.findViewById(R.id.fr3).setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        container.addView(view);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        if(user_handle.Forgot_Test_Username(username.getText().toString())){
                            question.setText(user_handle.question);
                            confirm_username = username.getText().toString();
                            viewPager.setCurrentItem(1);
                        }
                        break;
                    case 1:
                        if(answer.getText().toString().equals(user_handle.answer)){
                            viewPager.setCurrentItem(2);
                        }else{
                            Toast.makeText(context,"答案输入错误",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        if(user_handle.Test_Passwd(passwd.getText().toString(),secpasswd.getText().toString())){
                            user_handle.Reset_Passwd(confirm_username,passwd.getText().toString());
                            Intent intent = new Intent(context,MainActivity.class);
                            context.startActivity(intent);
                        }
                        break;
                }
            }
        });
        return view;
    }
}
