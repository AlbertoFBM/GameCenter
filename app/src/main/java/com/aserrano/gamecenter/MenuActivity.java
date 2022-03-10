package com.aserrano.gamecenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Bundle extras = getIntent().getExtras();

        String username = extras.getString("user");

        ListView listView = findViewById(R.id.listView);

        String[] array = {
                getResources().getString(R.string.play2048),
                getResources().getString(R.string.playPEG),
                getResources().getString(R.string.scores),
                getResources().getString(R.string.settings)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.menu_item, array);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view;
                String stringText = text.getText().toString();
                if (stringText.equals(getResources().getString(R.string.play2048))){
                    Intent intent = new Intent(MenuActivity.this, Game2048Activity.class);
                    intent.putExtra("user", username);
                    startActivity(intent);

                }else if (stringText.equals(getResources().getString(R.string.playPEG))){
                    Intent intent = new Intent(MenuActivity.this, GamePEGActivity.class);
                    intent.putExtra("user", username);
                    startActivity(intent);

                }else if (stringText.equals(getResources().getString(R.string.scores))){
                    Intent intent = new Intent(MenuActivity.this, ScoreActivity.class);
                    intent.putExtra("user", username);
                    startActivity(intent);

                }else if (stringText.equals(getResources().getString(R.string.settings))){
                    Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                    intent.putExtra("user", username);
                    startActivity(intent);

                }
            }
        });

    }

    @Override
    public void onBackPressed() {}
}