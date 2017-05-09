package com.hzn.easytransition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("name" + i);
        }
        ListView listView = (ListView) findViewById(R.id.lv);
        listView.setAdapter(new MyAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ready for intent
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("name", list.get(position));

                // ready for transition options
                EasyTransitionOptions options =
                        EasyTransitionOptions.makeTransitionOptions(
                                MainActivity.this,
                                view.findViewById(R.id.iv_icon),
                                view.findViewById(R.id.tv_name),
                                findViewById(R.id.v_top_card));

                // start transition
                EasyTransition.startActivity(intent, options);
            }
        });
    }


    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            int count = 0;
            if (null != list)
                count = list.size();
            return count;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (null != convertView) {
                view = convertView;
            } else {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_main_list, null, false);
            }
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            tvName.setText(list.get(position));
            return view;
        }
    }
}
