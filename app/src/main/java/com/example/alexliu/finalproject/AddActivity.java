package com.example.alexliu.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by alexliu on 16-08-02.
 */
public class AddActivity extends AppCompatActivity {
    public static final String DEFAULT = "not available";
    EditText tyname;
    String loginUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_setting);

        tyname = (EditText)findViewById(R.id.tyname);


    }

    public void Add(View view){
        Toast.makeText(this, "New type has been added", Toast.LENGTH_SHORT).show();
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        loginUser = sharedPrefs.getString("loginUser", DEFAULT);
        Set<String> incometypedata = sharedPrefs.getStringSet(loginUser.toString()+"incomelist",new HashSet<String>());
        List<String> list = new ArrayList<String>(incometypedata);

        Set<String> typelist1 = new HashSet<String>();
        typelist1.addAll(list);
        typelist1.add(tyname.getText().toString());
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putStringSet(loginUser.toString()+"incomelist", typelist1);

        editor.commit();
        Intent intent = new Intent(this, TypeEditActivity.class);
        startActivity(intent);
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
