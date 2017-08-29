package com.example.alexliu.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by alexliu on 16-07-27.
 */
public class TypeEditActivity extends FragmentActivity implements View.OnClickListener{
    public static final String DEFAULT = "not available";
    private TextView t1;
    private TextView t2;

    private Fragment tab1;
    private Fragment tab2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_typeedit);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initView();
        initEvent();
        setSelect(0);

    }



    private void initEvent() {
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
    }

    private void initView() {
        t1 = (TextView) findViewById(R.id.text1);
        t2 = (TextView) findViewById(R.id.text2);
    }

    public void reset() {
        t1.setText("Income");

        t2.setText("Expense");

    }

    private void setSelect(int i) {
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction trs=fm.beginTransaction();
        hideFragment(trs);

        switch (i) {
            case 0:
                if(tab1==null)
                {
                    tab1=new Fragment1();
                    trs.add(R.id.id_content, tab1);
                }else
                {
                    trs.show(tab1);
                }
                t1.setText("Selected");
                t1.setTextColor(Color.parseColor("#9AEAED"));
                t2.setTextColor(Color.parseColor("#979797"));
                break;
            case 1:
                if(tab2==null)
                {
                    tab2=new Fragment2();
                    trs.add(R.id.id_content, tab2);
                }else
                {
                    trs.show(tab2);
                }
                t2.setText("Selected");
                t1.setTextColor(Color.parseColor("#979797"));
                t2.setTextColor(Color.parseColor("#9AEAED"));

                break;


            default:
                break;
        }
        trs.commit();
    }

    private void hideFragment(FragmentTransaction trs) {

        if(tab1!=null)
        {
            trs.hide(tab1);
        }
        if(tab2!=null)
        {
            trs.hide(tab2);
        }


    }

    @Override
    public void onClick(View v) {
        reset();
        switch (v.getId()) {
            case R.id.text1:
                setSelect(0);
                break;
            case R.id.text2:
                setSelect(1);

                break;

            default:
                break;
        }
    }


    //button click to open income page
    public void incomePage(View view){
        String input = "Income";
        Intent intent = new Intent(this, IncomeActivity.class);
        intent.putExtra("query",input);
        startActivity(intent);
    }
    //button click to open expense page
    public void expensePage(View view){
        String inputT = "Expense";
        Intent intent = new Intent(this, ExpenseActivity.class);
        intent.putExtra("query",inputT);
        startActivity(intent);
    }
    //button click to open input page
    public void inputPage(View view){
        Intent intent = new Intent(this, InputActivity.class);
        startActivity(intent);
    }
    //button click to open result page
    public void resultPage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //button click to open setting page
    public void settingPage(View view){
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }


}
