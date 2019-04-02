package com.example.com.zy_demo.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.com.zy_demo.MainActivity;
import com.example.com.zy_demo.R;
import com.example.com.zy_demo.RegisterAcitvity;
import com.example.com.zy_demo.User_Handle;


public class PagerAdapter extends android.support.v4.view.PagerAdapter {

    public ViewPager viewPager;
    public int[] layout = new int[]{R.layout.register_1,R.layout.register_1,R.layout.register_1,R.layout.register_1};
    private Context context;
    private EditText username;
    private EditText passwd;
    private EditText secpasswd;
    private Spinner grade;
    private EditText name;
    private EditText groupname;
    private RadioGroup radioGroup;
    private EditText question;
    private EditText answer;
    private EditText root;
    private User_Handle user_handle;
    public View Ra;

    public PagerAdapter(Context con){
        this.context = con;
        user_handle= new User_Handle(context);
    }
    @Override
    public int getCount() {
        return layout.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(layout[position],null);
        Button next = view.findViewById(R.id.next);
        switch (position){
            case 0:
                username = view.findViewById(R.id.add_username);
                view.findViewById(R.id.r1).setVisibility(View.VISIBLE);
                break;
            case 1:
                passwd = view.findViewById(R.id.add_passwd);
                secpasswd = view.findViewById(R.id.add_secpasswd);
                view.findViewById(R.id.r2).setVisibility(View.VISIBLE);
                break;
            case 2:
                grade = view.findViewById(R.id.grade);
                name = view.findViewById(R.id.name);
                groupname = view.findViewById(R.id.group_name);
                radioGroup = view.findViewById(R.id.radiogroup);
                String[] item = {"选择羽毛球等级","1","2","3","4","5","6","7","8","9"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,item);
                adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                grade.setAdapter(adapter);
                view.findViewById(R.id.r3).setVisibility(View.VISIBLE);
                break;
            case 3:
                question = view.findViewById(R.id.question);
                answer = view.findViewById(R.id.answer);
                root = view.findViewById(R.id.root);
                next.setText("完成");
                view.findViewById(R.id.r4).setVisibility(View.VISIBLE);
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
                        if(user_handle.Test_Username(username.getText().toString())) {
                            viewPager.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    return false;
                                }
                            });
                            viewPager.setCurrentItem(1);
                        }
                        break;
                    case 1:
                        if(user_handle.Test_Passwd(passwd.getText().toString(),secpasswd.getText().toString())) {
                            viewPager.setCurrentItem(2);
                        }
                        break;
                    case 2:
                        CharSequence charSequence = (CharSequence) grade.getSelectedItem();
                        if(user_handle.Add_UserInfo(charSequence.toString(),name.getText().toString(),groupname.getText().toString(),radioGroup.getCheckedRadioButtonId())){
                            viewPager.setCurrentItem(3);
                        }
                        break;
                    case 3:
                        user_handle.Set_QAR(question.getText().toString(),answer.getText().toString(),root.getText().toString());
                        Intent intent = new Intent(context,MainActivity.class);
                        context.startActivity(intent);
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View)object;
        container.removeView(view);
    }
}
