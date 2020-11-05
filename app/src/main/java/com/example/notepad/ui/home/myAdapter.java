package com.example.notepad.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.notepad.MainActivity;
import com.example.notepad.R;
import com.example.notepad.addMemory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class myAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, Filterable {
    MyFilter filter = null;
    public static myAdapter adapter = null;
    public static ArrayList<HashMap<String, String>> showList = null;
    ArrayList<HashMap<String, String>> oldList = new ArrayList<>();
    public static HashMap<Integer,CheckBox> cbs =new HashMap<>();

    public myAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HashMap<String, String>> objects) {
        super(context, resource, objects);
        showList = objects;
        oldList.addAll(objects);
        adapter = this;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Map<String, String> map = (Map<String, String>) getItem(position);
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, parent, false);
        }
        TextView title = (TextView) itemView.findViewById(R.id.content2);
        TextView detail = (TextView) itemView.findViewById(R.id.date2);
        if (map.containsKey("check")&&map.get("check").equalsIgnoreCase("false")) {
            CheckBox cb = itemView.findViewById(R.id.checkBox);
            cb.setVisibility(View.VISIBLE);
            cbs.put(position,cb);
        }
        title.setText(map.get("COMMENT"));
        Date date = new Date(map.get("TIME"));
        detail.setText((date.getYear() + 1900) + "/" + (date.getMonth() + 1) + "/" + date.getDate());
        detail.setPadding(10, 2, 20, 2);
        title.setPadding(10, 0, 20, 2);
        Resources resources = getContext().getResources();
        itemView.setBackgroundDrawable(resources.getDrawable(R.color.design_default_color_background));
        return itemView;
    }

    String TAG = "lalala:";

    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        Object itemAtPosition = parent.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        Log.i(TAG, map.containsKey("check")+"");
        if (map.containsKey("check")) {
            CheckBox cb=view.findViewById(R.id.checkBox);
            cb.setChecked(!cb.isChecked());
            if(!map.containsKey("delete"))
                map.put("delete","true");
            else if(map.get("delete").equalsIgnoreCase("true"))
                map.put("delete","false");
            else if(map.get("delete").equalsIgnoreCase("false"))
                map.put("delete","true");
        } else {
            Intent intent = new Intent(getContext(), addMemory.class);
            intent.putExtra("COMMENT", map.get("COMMENT"));
            intent.putExtra("TIME", map.get("TIME"));
            getContext().startActivity(intent);
        }
        //TextView title = (TextView) view.findViewById(R.id.title);
        //TextView detail = (TextView) view.findViewById(R.id.detail);
        /*Intent second = new Intent(getContext(), calc_rate.class);
        second.putExtra("money", titleStr);
        second.putExtra("rate", detailStr);
        getContext().startActivity(second);*/
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
                MainActivity.dbh.delete(myAdapter.temp);
                ArrayList<HashMap<String, String>> li = MainActivity.dbh.listAll();

                //ListAdapter adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>(MainActivity.rate.keySet()));
                ListView lv = (ListView) HomeFragment.root.findViewById(R.id.mylist);
                //lv.setAdapter(new SimpleAdapter(this, li, R.layout.activity_list, new String[]{"title", "detail"}, new int[]{R.id.title, R.id.detail}));
                myAdapter ma = new myAdapter(HomeFragment.root.getContext(), R.layout.fragment_home, li);
                lv.setAdapter(ma);
            }
        });
        bb.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        bb.setMessage("确定删除该记录么？");
        bb.setTitle("提示");
        bb.show();
        return true;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        // 如果MyFilter对象为空，那么重写创建一个
        if (filter == null) {
            filter = new MyFilter(oldList);
        }
        return filter;
    }


    class MyFilter extends Filter {

        // 创建集合保存原始数据
        private ArrayList<HashMap<String, String>> original = null;

        public MyFilter(ArrayList<HashMap<String, String>> list) {
            this.original = list;
        }

        /**
         * 该方法返回搜索过滤后的数据
         */
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // 创建FilterResults对象
            FilterResults results = new FilterResults();

            /**
             * 没有搜索内容的话就还是给results赋值原始数据的值和大小
             * 执行了搜索的话，根据搜索的规则过滤即可，最后把过滤后的数据的值和大小赋值给results
             *
             */
            if (TextUtils.isEmpty(constraint)) {
                results.values = original;
                results.count = original.size();
            } else {
                // 创建集合保存过滤后的数据
                ArrayList<HashMap<String, String>> mList = new ArrayList<>();
                // 遍历原始数据集合，根据搜索的规则过滤数据
                for (HashMap<String, String> s : original) {
                    // 这里就是过滤规则的具体实现【规则有很多，大家可以自己决定怎么实现】
                    if (s.get("COMMENT").trim().toLowerCase().contains(constraint.toString().trim().toLowerCase())) {
                        // 规则匹配的话就往集合中添加该数据
                        mList.add(s);
                    }
                }
                results.values = mList;
                results.count = mList.size();
            }

            // 返回FilterResults对象
            return results;
        }

        /**
         * 该方法用来刷新用户界面，根据过滤后的数据重新展示列表
         */
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // 获取过滤后的数据
            showList.clear();
            showList.addAll((ArrayList<HashMap<String, String>>) results.values);
            // 如果接口对象不为空，那么调用接口中的方法获取过滤后的数据，具体的实现在new这个接口的时候重写的方法里执行
            // 刷新数据源显示
            notifyDataSetChanged();
        }

    }
}
