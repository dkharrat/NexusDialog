package com.github.dkharrat.nexusdialog.sample;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;


public class MainActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ListView listView = (ListView)findViewById(R.id.examples_list);

        String[] items = {"Simple Example", "Basic Form", "Custom Form"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch(position) {
                    case 0: {
                        intent = new Intent(MainActivity.this, SimpleExample.class);
                        break;
                    }
                    case 1: {
                        intent = new Intent(MainActivity.this, BasicForm.class);
                        break;
                    }
                    case 2: {
                        intent = new Intent(MainActivity.this, CustomFormActivity.class);
                        break;
                    }
                    default: {
                        throw new Error("Unhandled list item " + position);
                    }
                }
                startActivity(intent);
            }
        });
    }
}

