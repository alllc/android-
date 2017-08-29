package com.example.alexliu.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Fragment2 extends Fragment {
    private View tab2view;
    ListView incomelist;
    TextView testview;
    String loginUser;
    List<String> list;
    int pos;
    public static final String DEFAULT = "not available";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        tab2view = inflater.inflate(R.layout.activity_expenseedit, container, false);
        return tab2view;
    }
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        incomelist = (ListView) this.getView().findViewById(R.id.listView2);
        SharedPreferences sharedPrefs = this.getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
        loginUser = sharedPrefs.getString("loginUser", DEFAULT);
        Set<String> expensetypedata = sharedPrefs.getStringSet(loginUser.toString()+"expenselist",new HashSet<String>());
        list = new ArrayList<String>(expensetypedata);
        testview = (TextView)this.getView().findViewById(R.id.tyviewtest);

        incomelist.setAdapter(new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,list));
        Button button=(Button)tab2view.findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "点击了我!!", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(getActivity(),AddActivity2.class);
                startActivity(intent);
            }
        });
        incomelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //parent.getItemAtPosition(position) returns the value of item clicked.. use it to do whatever you wish to do
                String item = ((TextView)view).getText().toString();
                testview.setText(item);
                pos = position;
            }
        });
        Button deleteB=(Button)tab2view.findViewById(R.id.button6);
        deleteB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("MyData", Context.MODE_PRIVATE);
                loginUser = sharedPrefs.getString("loginUser", DEFAULT);
                Set<String> expensetypedata = sharedPrefs.getStringSet(loginUser.toString()+"expenselist",new HashSet<String>());
                List<String> list = new ArrayList<String>(expensetypedata);

                Set<String> typelist1 = new HashSet<String>();
                typelist1.addAll(list);
                typelist1.remove(testview.getText().toString());
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putStringSet(loginUser.toString()+"expenselist", typelist1);
                editor.commit();

                Intent intent = new Intent(getActivity(), TypeEditActivity.class);
                startActivity(intent);
            }
        });
    }


}