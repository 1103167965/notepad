package com.example.notepad;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.notepad.ui.home.FilterListener;
import com.example.notepad.ui.home.HomeFragment;
import com.example.notepad.ui.home.myAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static DBHelper dbh;
    public static ListView lv;
    public static MainActivity th;
    public static String COLLECT;
    public static boolean show = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        th = this;
        dbh = new DBHelper(this);
        lv = (ListView) findViewById(R.id.kindList);
        COLLECT = "全部笔记";
        ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAllKind();
        String[] m = new String[li.size() + 1];
        for (int i = 0; i < li.size(); i++)
            m[i + 1] = li.get(i).get("KIND");
        m[0] = "未分类";
        addMemory.m = m;
        //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                startActivityForResult(new Intent(getBaseContext(), addMemory.class), 0);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        EditText et = (EditText) findViewById(R.id.textView9);
        et.addTextChangedListener(new TextWatcher() {
            /**
             *
             * 编辑框内容改变的时候会执行该方法
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 如果adapter不为空的话就根据编辑框中的内容来过滤数据
                if (((ListView) findViewById(R.id.mylist)).getAdapter() != null) {
                    ((myAdapter) ((ListView) findViewById(R.id.mylist)).getAdapter()).getFilter().filter(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAll();
        //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));
        ListView lv = (ListView) findViewById(R.id.mylist);
        //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
        myAdapter ma = new myAdapter(this, R.layout.fragment_home, li);
        lv.setVisibility(View.GONE);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(ma);
        lv.setOnItemLongClickListener(ma);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //item.getItemId();
        ListView lv=findViewById(R.id.mylist);
        if (!show) {
            for (int i = 0; i < myAdapter.showList.size(); i++) {
                HashMap<String, String> hm = myAdapter.showList.get(i);
                hm.put("check", "false");
                myAdapter.showList.remove(i);
                myAdapter.showList.add(i, hm);
            }
            myAdapter.adapter.notifyDataSetChanged();
            show = true;
            findViewById(R.id.delete_all).setVisibility(View.VISIBLE);
        } else {
            show = false;
            for (int i = 0; i < myAdapter.showList.size(); i++) {
                HashMap<String, String> hm = myAdapter.showList.get(i);
                hm.remove("check");
                myAdapter.showList.remove(i);
                myAdapter.showList.add(i, hm);
            }
            lv.setAdapter(new myAdapter(this, R.layout.fragment_home, myAdapter.showList));
            findViewById(R.id.delete_all).setVisibility(View.INVISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        HashMap<String, String> count = dbh.getCount();
        TextView tv = (TextView) findViewById(R.id.collectnumber);
        tv.setText(count.get("COUNT"));

        ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAllKind();
        ListView lv = (ListView) findViewById(R.id.kindList);
        //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
        KindAdapter ka = new KindAdapter(this, R.layout.kindlist, li);
        lv.setAdapter(ka);
        lv.setOnItemClickListener(ka);
        lv.setOnItemLongClickListener(ka);

        HashMap<String, String> hm = MainActivity.dbh.listAllKind("未分类");
        tv = (TextView) findViewById(R.id.number4);
        tv.setText(hm.get("NUMBER"));

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void addKind(View view) {
        AlertDialog.Builder bb = new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.prompts, null);
        bb.setView(promptsView);
        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        bb.setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                HashMap<String, String> hm = new HashMap<>();
                hm.put("KIND", String.valueOf(userInput.getText()));
                MainActivity.dbh.addKind(hm);
                ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAllKind();
                String[] m = new String[li.size() + 1];
                for (int i0 = 0; i0 < li.size(); i0++)
                    m[i0 + 1] = li.get(i0).get("KIND");
                m[0] = "未分类";
                addMemory.m = m;
                //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));
                ListView lv = (ListView) th.findViewById(R.id.kindList);
                //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
                KindAdapter ma = new KindAdapter(HomeFragment.root.getContext(), R.layout.kindlist, li);
                lv.setAdapter(ma);
            }
        });
        bb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        bb.setTitle("提示");
        bb.show();
    }

    public void listall(View view) {
        ListView lv = (ListView) findViewById(R.id.mylist);
        ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAll();
        //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
        myAdapter ma = new myAdapter(this, R.layout.fragment_home, li);
        lv.setAdapter(ma);
        lv.setOnItemClickListener(ma);
        lv.setOnItemLongClickListener(ma);
    }

    public void listnokind(View view) {
        ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAllbyKind("未分类");
        ListView lv = (ListView) HomeFragment.root.findViewById(R.id.mylist);
        com.example.notepad.ui.home.myAdapter ma = new com.example.notepad.ui.home.myAdapter(HomeFragment.root.getContext(), R.layout.fragment_home, li);
        lv.setAdapter(ma);
    }

    public void delete_all(View view) {
        AlertDialog.Builder bb = new AlertDialog.Builder(this);
        bb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int j) {
                ListView lv=HomeFragment.root.findViewById(R.id.mylist);
                for (int i = 0; i <myAdapter.showList.size(); i++) {
                    if (myAdapter.showList.get(i).containsKey("delete")&&myAdapter.showList.get(i).get("delete").equalsIgnoreCase("true")) {
                        MainActivity.dbh.delete(myAdapter.showList.get(i));
                    }
                }
                ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAll();
                //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));
                //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
                myAdapter ma = new myAdapter(HomeFragment.root.getContext(), R.layout.fragment_home, li);
                lv.setAdapter(ma);
                MainActivity.th.findViewById(R.id.delete_all).setVisibility(View.INVISIBLE);
                MainActivity.show=false;
            }
        });
        bb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        bb.setMessage("确定删除记录么？");
        bb.setTitle("提示");
        bb.show();
    }
}

