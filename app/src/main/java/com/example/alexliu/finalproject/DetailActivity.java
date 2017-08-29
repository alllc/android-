package com.example.alexliu.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.util.Calendar;

/**
 * Created by alexliu on 16-07-01.
 */
public class DetailActivity extends AppCompatActivity {
    String input,name,type,amount,date, pos;
    MyDataBase db;
    TextView inputTxt,nameTxt,typeTxt,amountTxt,dateTxt;
    public static final String DEFAULT = "not available";
    Cursor cursor ;
    ImageView image;
    String loginUser;
    String monthinput;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_details);

        db = new MyDataBase(this);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        loginUser = sharedPrefs.getString("loginUser", DEFAULT);

        Intent intent = getIntent();

        Bundle b = intent.getExtras();


        if(b!=null){
            input = (String) b.get("INPUT");
            pos = (String) b.get("POS");
            monthinput = (String) b.get("MON");
            cursor = db.getSelectedData(input,loginUser,monthinput,Integer.toString(year));
        }

        inputTxt = (TextView)findViewById(R.id.BType);
        nameTxt = (TextView)findViewById(R.id.Name);
        typeTxt = (TextView)findViewById(R.id.Type);
        amountTxt = (TextView)findViewById(R.id.Amount);
        dateTxt = (TextView)findViewById(R.id.Date);

        cursor.moveToPosition(Integer.parseInt(pos));
//        String input2 = cursor.getString(cursor.getColumnIndex(Constants.INPUT));

        name = cursor.getString(cursor.getColumnIndex(Constants.NAME));
        type = cursor.getString(cursor.getColumnIndex(Constants.TYPE));
        amount = cursor.getString(cursor.getColumnIndex(Constants.AMOUNT));
        date = cursor.getString(cursor.getColumnIndex(Constants.DATE));

        inputTxt.setText(input);
        nameTxt.setText(name);
        typeTxt.setText(type);
        amountTxt.setText(" $ "+ amount);
        dateTxt.setText(date);

        byte[] blob = cursor.getBlob(cursor.getColumnIndex(Constants.IMAGE));
        if(blob == null){
            bitmap = null;
        }else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
            bitmap = BitmapFactory.decodeStream(inputStream);
        }

        //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView image = (ImageView) findViewById(R.id.img);

        image.setImageBitmap(bitmap);

//        byte[] in = cursor.getBlob(cursor.getColumnIndex(Constants.IMAGE));
//        Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
//
//        image.setImageBitmap(bmpout);

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
