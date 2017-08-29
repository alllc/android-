package com.example.alexliu.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.Legend;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by alexliu on 16-07-27.
 */
public class PreviousActivity extends AppCompatActivity{
    public static final String DEFAULT = "not available";
    TextView incomeNumTxt, expenseNumTxt, resultNumTxt;
    MyDataBase db;
    Cursor c_income, c_expense;
    float sum_income, sum_expense, total;
    EditText checkM, checkY;
    TextView monthView;
    String monthinput;
    BarChart mBarChart;
    BarData mBarData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preivous);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_pre);

        incomeNumTxt = (TextView)findViewById(R.id.incomeNumTxt);
        expenseNumTxt = (TextView)findViewById(R.id.expenseNumTxt);
        resultNumTxt = (TextView)findViewById(R.id.resultNumTxt);

        checkM = (EditText)findViewById(R.id.checkMonthEdit);
        checkY = (EditText)findViewById(R.id.checkYearEdit);


        monthView = (TextView)findViewById(R.id.MonthView);




//        day = c.get(Calendar.DAY_OF_MONTH);

        //check if there is a data


        checkM.setText("08");
        checkY.setText("2016");



    }
    public void Check(View view){

        db = new MyDataBase(this);

        int year = Integer.parseInt(checkY.getText().toString());
        int month = Integer.parseInt(checkM.getText().toString());

        String[] monthcal = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        monthView.setText(monthcal[month-1]);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String loginUser = sharedPrefs.getString("loginUser", DEFAULT);

        c_income = db.query_income(loginUser,checkM.getText().toString(),checkY.getText().toString());
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

        c_expense = db.query_expense(loginUser,checkM.getText().toString(),checkY.getText().toString());
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
//        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        barChart.setTouchEnabled(true); // 设置是否可以触摸

        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放

        barChart.setPinchZoom(false);//

//      barChart.setBackgroundColor();// 设置背景

        barChart.setDrawBarShadow(true);

        barChart.setData(barData); // 设置数据

        Legend mLegend = barChart.getLegend(); // 设置比例图标示

        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
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
