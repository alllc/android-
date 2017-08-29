package com.example.alexliu.finalproject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by alexliu on 16-07-01.
 */
public class InputActivity extends AppCompatActivity implements LocationListener{
    EditText  moneyName,  moneyAmt, moneyDate;
    TextView textView;
    Spinner moneyInput,moneyType;
    String mInput,mType;
    MyDataBase db;
    ArrayAdapter adapterType, adapterincomeType,adapterexpenseType;
    String loginUser;
    public static final String DEFAULT = "not available";
//    String[] incometypedata;
    byte[] img;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String MM,DD,YYYY;

    private static final int CAMERA_REQUEST = 1888;
    private Button btnSelect;
    private ImageView ivImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_note);

        btnSelect = (Button) findViewById(R.id.imgButton);
        ivImage = (ImageView) findViewById(R.id.preview);


        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        loginUser = sharedPrefs.getString("loginUser", DEFAULT);

        moneyInput = (Spinner)findViewById(R.id.inputSpinner);
        moneyName = (EditText)findViewById(R.id.nameEditTxt);
        moneyType = (Spinner)findViewById(R.id.typeSpinner);
        moneyAmt = (EditText)findViewById(R.id.amountEditTxt);
        moneyDate = (EditText)findViewById(R.id.dateEditTxt);

        InputFilter[] filters = {new CashierInputFilter()};
        moneyAmt.setFilters(filters);
        moneyDate.setInputType(InputType.TYPE_NULL);
        myCalendar = Calendar.getInstance();


        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        moneyDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(InputActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //textView = (TextView)findViewById(R.id.textView6);
        db = new MyDataBase(this);

//        incometypedata = new String[]{"Interest", "Payment", "Others"};

        Set<String> incometypedata = sharedPrefs.getStringSet(loginUser.toString()+"incomelist",new HashSet<String>());
        List<String> incomelist = new ArrayList<String>(incometypedata);

        Set<String> expensetypedata = sharedPrefs.getStringSet(loginUser.toString()+"expenselist",new HashSet<String>());
        List<String> expenselist = new ArrayList<String>(expensetypedata);


//        adapterincomeType = ArrayAdapter.createFromResource(this,R.array.income_type,android.R.layout.simple_spinner_dropdown_item);
        adapterincomeType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,incomelist);
        adapterincomeType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        adapterexpenseType = ArrayAdapter.createFromResource(this,R.array.expense_type,android.R.layout.simple_spinner_dropdown_item);
        adapterexpenseType = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,expenselist);
        adapterexpenseType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        moneyInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String[] m = getResources().getStringArray(
                        R.array.input_type);
                mInput = m[position];
                //if income, then show income type, else show expense
                if(position == 0){
                    moneyType.setAdapter(adapterincomeType);
                    adapterType = adapterincomeType;
                }else if(position == 1){
                    moneyType.setAdapter(adapterexpenseType);
                    adapterType = adapterexpenseType;
                }
//                textView.setText("The type u choice is：" + m[position]);//显示
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        moneyType.setOnItemSelectedListener(new SpinnerXMLSelectedListener());
        moneyType.setVisibility(View.VISIBLE);

        //img = image(R.drawable.homegrey1);//测试用图。 应该是在这里改成相机的

        //gps



    }
    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        moneyDate.setText(sdf.format(myCalendar.getTime()));
        String m = moneyDate.getText().toString();
        MM = m.substring(0,2);
        DD = m.substring(3,5);
        YYYY = m.substring(6,10);
        //textView.setText("The type u choice is：" + mInput + " " + mType +" "+ MM+"/"+DD+"/"+YYYY);

    }


    class SpinnerXMLSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view, int position,
                                   long id) {
            mType = adapterType.getItem(position).toString();
//            textView.setText("The type u choice is：" + mInput + " " + mType);
//            textView.setText(img.toString());
        }
        public void onNothingSelected(AdapterView<?> arg0) {

        }
    }

    public void takeImageFromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            ivImage.setImageBitmap(mphoto);
        }
    }

    public void submitButton (View view) {
        String input = mInput;
        String name = moneyName.getText().toString();
        String type = mType;
        String amount = moneyAmt.getText().toString();
        String date = moneyDate.getText().toString();
//        byte[] img = ConvertDrawableToByteArray(ivImage.getDrawable());

        //ContentValues img = new ContentValues();
        //img.put(IMAGE, imageBytes);
        if(ivImage.getDrawable() == null){
            img = null;
        }else{
            img = ConvertDrawableToByteArray(ivImage.getDrawable());

        }
        Toast.makeText(this, loginUser + input + name + type + amount + date + img, Toast.LENGTH_SHORT).show();

        long id = db.insertData(loginUser, input ,name, type, amount, date, MM,DD,YYYY, img);
        if (id < 0) {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        moneyName.setText("");
        moneyAmt.setText("");
        moneyDate.setText("");
    }
    //图片
    //public byte[] image(int id) {
    //    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    //    Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(id)).getBitmap();
    //    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    //    return baos.toByteArray();
    //}
    public static byte[] ConvertDrawableToByteArray(Drawable drawableResource) {

        Bitmap imageBitmap = ((BitmapDrawable) drawableResource).getBitmap();
        ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, imageByteStream);
        byte[] imageByteData = imageByteStream.toByteArray();
        return imageByteData;
    }

    //gps
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
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
