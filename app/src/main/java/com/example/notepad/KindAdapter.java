package com.example.notepad;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.notepad.ui.home.FilterListener;
import com.example.notepad.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KindAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public KindAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(HomeFragment.root.getContext()).inflate(R.layout.kindlist, parent, false);
        }
        Map<String, String> map = (Map<String, String>) getItem(position);
        TextView title = (TextView) itemView.findViewById(R.id.textView5);
        TextView detail = (TextView) itemView.findViewById(R.id.number);

        title.setText(map.get("KIND"));
        detail.setText(map.get("NUMBER"));
        Resources resources = getContext().getResources();
        itemView.setBackgroundDrawable(resources.getDrawable(R.drawable.bg));
        return itemView;
    }

    String TAG = "lalala:";

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Object itemAtPosition = parent.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAllbyKind(map.get("KIND"));

        ListView lv = (ListView) HomeFragment.root.findViewById(R.id.mylist);
        com.example.notepad.ui.home.myAdapter ma = new com.example.notepad.ui.home.myAdapter(HomeFragment.root.getContext(), R.layout.fragment_home,li);

        lv.setAdapter(ma);
    }

    public static HashMap<String, String> temp;

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        //remove(adapterView.getItemAtPosition(i));
        Object itemAtPosition = adapterView.getItemAtPosition(i);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        temp = map;
        AlertDialog.Builder bb = new AlertDialog.Builder(getContext());
        bb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.dbh.deleteKind(temp);
                ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAll();

                //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));
                ListView lv = (ListView) HomeFragment.root.findViewById(R.id.mylist);
                //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
                com.example.notepad.ui.home.myAdapter ma = new com.example.notepad.ui.home.myAdapter(HomeFragment.root.getContext(), R.layout.fragment_home, li);
                lv.setAdapter(ma);
                li = MainActivity.dbh.listAllKind();
                lv = (ListView)MainActivity.th.findViewById(R.id.kindList);
                //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
                KindAdapter ka = new KindAdapter(MainActivity.th, R.layout.kindlist, li);
                lv.setAdapter(ka);
            }
        });
        bb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        bb.setMessage("确定删除该类别么？");
        bb.setTitle("提示");
        bb.show();
        return true;
    }
}
