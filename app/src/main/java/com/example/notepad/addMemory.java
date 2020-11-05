package com.example.notepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notepad.ui.home.myAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class addMemory extends AppCompatActivity {
    public static String[] m;
    //private TextView view;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    public HashMap<String, String> hm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmemory);

        Intent intent = getIntent();
        if(intent.getStringExtra("TIME")==null){
            hm=new HashMap<String,String>();
            hm.put("COMMENT","");
            hm.put("COLLECT",MainActivity.COLLECT);
            hm.put("KIND",m[0]);
            hm=MainActivity.dbh.add(hm);
        }else{
            hm=new HashMap<String,String>();
            hm.put("COMMENT",intent.getStringExtra("COMMENT"));
            hm.put("TIME",intent.getStringExtra("TIME"));
        }
        TextView tv = (TextView) findViewById(R.id.textView7);
        Date date = new Date(hm.get("TIME"));
        tv.setText((date.getYear()+1900)+"/"+(date.getMonth()+1)+"/"+date.getDate()+"   "+date.getHours()+":"+date.getMinutes());
        tv = (TextView) findViewById(R.id.textView8);
        tv.setText(hm.get("COMMENT"));

        spinner = (Spinner) findViewById(R.id.Spinner01);
        //将可选内容与ArrayAdapter连接起来
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m);

        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //将adapter 添加到spinner中
        spinner.setAdapter(adapter);

        //添加事件Spinner事件监听
        spinner.setOnItemSelectedListener(new SpinnerSelectedListener());

        //设置默认值
        spinner.setVisibility(View.VISIBLE);

        EditText et=findViewById(R.id.textView8);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //MainActivity.dbh.add(String.valueOf(Float.parseFloat(charSequence.toString())));
                hm.put("COMMENT",charSequence.toString());
                hm.put("COLLECT",MainActivity.COLLECT);
                hm.put("KIND", String.valueOf(spinner.getSelectedItem()));
                hm=MainActivity.dbh.update(hm);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //item.getItemId();
        this.finish();
        return super.onOptionsItemSelected(item);
    }
    class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            //view.setText("你的血型是："+m[arg2]);
            hm.put("COMMENT",((EditText)findViewById(R.id.textView8)).getText().toString());
            hm.put("COLLECT",MainActivity.COLLECT);
            hm.put("KIND", String.valueOf(spinner.getSelectedItem()));
            hm=MainActivity.dbh.update(hm);
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }
}
