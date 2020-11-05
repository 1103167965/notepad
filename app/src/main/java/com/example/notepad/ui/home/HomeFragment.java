package com.example.notepad.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.notepad.MainActivity;
import com.example.notepad.R;
import com.example.notepad.addMemory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public static View root = null;

    @SuppressLint("ResourceAsColor")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        /*final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        /*for (String i : MainActivity.money.keySet()) {
            HashMap<String, String> map = new HashMap<>();
            map.put("title", i);
            map.put("detail", String.valueOf(MainActivity.money.get(i)));
            li.add(map);
        }*/
        ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAll();

        //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));
        ListView lv = (ListView) root.findViewById(R.id.mylist);
        //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
        myAdapter ma = new myAdapter(root.getContext(), R.layout.fragment_home, li);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(ma);
        lv.setOnItemLongClickListener(ma);
        TextView tv = root.findViewById(R.id.textView2);
        tv.setText("没有笔记");
        lv.setEmptyView(tv);



        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAll();
        //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));
        ListView lv = (ListView) root.findViewById(R.id.mylist);
        //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
        myAdapter ma = new myAdapter(root.getContext(), R.layout.fragment_home, li);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(ma);
        lv.setOnItemLongClickListener(ma);
        super.onActivityResult(requestCode, resultCode, data);
    }
}

