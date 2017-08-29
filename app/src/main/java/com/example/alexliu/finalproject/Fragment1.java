package com.example.alexliu.finalproject;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Fragment1 extends Fragment implements View.OnTouchListener{
    private View tab1view;
    ListView incomelist;
    TextView testview;
    EditText tyname;
    String loginUser;
    public static final String DEFAULT = "not available";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        tab1view=inflater.inflate(R.layout.activity_incomeedit, container, false);
        return tab1view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        incomelist = (ListView) this.getView().findViewById(R.id.listView2);
        SharedPreferences sharedPrefs = this.getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        loginUser = sharedPrefs.getString("loginUser", DEFAULT);
        Set<String> incometypedata = sharedPrefs.getStringSet(loginUser.toString()+"incomelist",new HashSet<String>());
        List<String> list = new ArrayList<String>(incometypedata);
        testview = (TextView)this.getView().findViewById(R.id.tyviewtest);

        tyname = (EditText)tab1view.findViewById(R.id.tyname);

        incomelist.setAdapter(new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,list));

        Button button=(Button)tab1view.findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "点击了我", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getActivity(),AddActivity.class);
                startActivity(intent);
            }
        });
        incomelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //parent.getItemAtPosition(position) returns the value of item clicked.. use it to do whatever you wish to do
                String item = ((TextView)view).getText().toString();
                testview.setText(item);
            }
        });
        Button deleteB=(Button)tab1view.findViewById(R.id.DeleteB);
        deleteB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
                loginUser = sharedPrefs.getString("loginUser", DEFAULT);
                Set<String> incometypedata = sharedPrefs.getStringSet(loginUser.toString()+"incomelist",new HashSet<String>());
                List<String> list = new ArrayList<String>(incometypedata);

                Set<String> typelist1 = new HashSet<String>();
                typelist1.addAll(list);
                typelist1.remove(testview.getText().toString());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putStringSet(loginUser.toString()+"incomelist", typelist1);
                editor.commit();

                Intent intent = new Intent(getActivity(), TypeEditActivity.class);
                startActivity(intent);
            }
        });
//        incomelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //parent.getItemAtPosition(position) returns the value of item clicked.. use it to do whatever you wish to do
//            }
//        });
//        incomelist.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    ClipData data = ClipData.newPlainText("", "");
//                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
//
//                    v.startDrag(data, shadowBuilder, v, 0);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}