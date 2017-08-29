package com.example.alexliu.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.Legend;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static final String DEFAULT = "not available";
    TextView incomeNumTxt, expenseNumTxt, resultNumTxt;
    MyDataBase db;
    Cursor c_income, c_expense;
    float sum_income, sum_expense, total;
    String loginUser;
    TextView monthView;
    String monthinput;

    BarChart mBarChart;
    BarData mBarData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar);

        incomeNumTxt = (TextView)findViewById(R.id.incomeNumTxt);
        expenseNumTxt = (TextView)findViewById(R.id.expenseNumTxt);
        resultNumTxt = (TextView)findViewById(R.id.resultNumTxt);
        db = new MyDataBase(this);

        monthView = (TextView)findViewById(R.id.MonthView);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        String[] monthcal = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        monthView.setText(monthcal[month]);


        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String loginUser = sharedPrefs.getString("loginUser", DEFAULT);
        String loginPassword = sharedPrefs.getString("loginPassword", DEFAULT);
        //check if there is a data on user
        Toast.makeText(this,"loading to log in page",Toast.LENGTH_LONG).show();
        if(loginUser.equals(DEFAULT) && loginPassword.equals(DEFAULT)){
            Toast.makeText(this,"No user log in, loading to log in page",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if(month <= 9){
            monthinput = "0" + Integer.toString(month+1);
        }else{
            monthinput = Integer.toString(month+1);
        }

        c_income = db.query_income(loginUser,monthinput,Integer.toString(year));
        if (c_income != null && c_income.getCount() > 0) {
            if (c_income.moveToFirst()){
                sum_income = c_income.getFloat(0);
            }else {
                sum_income = -1;
            }
        }else {
            sum_income = -2;
        }
        incomeNumTxt.setText(" $ "+sum_income);

        c_expense = db.query_expense(loginUser,monthinput,Integer.toString(year));
        if (c_expense != null && c_expense.getCount() > 0) {
            if (c_expense.moveToFirst()){
                sum_expense = c_expense.getFloat(0);
            }else {
                sum_expense = -1;
            }
        }else {
            sum_expense = -2;
        }
        expenseNumTxt.setText("- $ "+sum_expense);

        total = sum_income - sum_expense;
        resultNumTxt.setText(" $ "+total);

        mBarChart = (BarChart) findViewById(R.id.spread_bar_chart);
        mBarData = getBarData(2, 100);
        showBarChart(mBarChart, mBarData);
    }
    private void showBarChart(BarChart barChart, BarData barData) {
//        barChart.setDrawBorders(false);  ////是否在折线图上添加边框

        barChart.setDescription("");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("You need to provide data for the chart.");

        barChart.setDrawGridBackground(false); // 是否显示表格颜色
        //barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        barChart.setTouchEnabled(true); // 设置是否可以触摸

        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放

        barChart.setPinchZoom(false);//

//      barChart.setBackgroundColor();// 设置背景

        barChart.setDrawBarShadow(true);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(5.5f);// 字体

        int tColor = getResources().getColor(R.color.colorGrey);
        mLegend.setTextColor(tColor);// 颜色


        barChart.animateX(2500); // 立即执行的动画,x轴
    }

    private BarData getBarData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();

        xValues.add("Income");
        xValues.add("Expense");

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();

        yValues.add(new BarEntry(sum_income, 0));
        yValues.add(new BarEntry(sum_expense, 1));

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "DATA RESULT");

        int myColor = getResources().getColor(R.color.colorPrimary);
        barDataSet.setColor(myColor);

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSets);

        return barData;
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
