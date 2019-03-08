package edu.fandm.pweiser.chitchat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class Settings extends AppCompatActivity {
    ListView lv;
    public final String TAG = "osalem.chitchat";

    @Override //Adds the About Item to our ListView and opens about's activity on click
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ArrayList<String> arr = new ArrayList<>();
        arr.add("About");

        lv = (ListView)findViewById(R.id.setting_options);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String val = (String) parent.getItemAtPosition(position);

                Log.e(TAG, "You Clicked "+val);

                Intent i;
                switch(val)
                {
                    case("About"):
                        i = new Intent(getApplicationContext(), AboutPopup.class);
                        startActivity(i);
                        break;
                }
            }
        });
    }
}
