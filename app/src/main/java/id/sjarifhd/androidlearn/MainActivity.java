package id.sjarifhd.androidlearn;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PACKAGE_NAME = "id.sjarifhd.androidlearn.";

    private ListView mListView;
    private String data[] = new String[] {
      "Permission", "etc."
    };
    private ArrayList<String> mList;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_main);
        mList = new ArrayList<>();

        initData();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) parent.getItemAtPosition(position);
                Log.v(TAG, item);
                try {
                    Class<?> aClass = Class.forName(PACKAGE_NAME+item+"Activity");
                    Intent intent = new Intent(MainActivity.this, aClass);
                    startActivity(intent);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initData() {
        // manual copy array
        //for(String values : data) {
        //    mList.add(values);
        // }

        // use collection for copy array
        Collections.addAll(mList, data);

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
    }
}
