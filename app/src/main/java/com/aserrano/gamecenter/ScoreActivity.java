package com.aserrano.gamecenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<String> name, score2048, scorePEG;
    Database db;
    ScoreAdapter adapter;
    String mUsername;
    Cursor cursor;
    String aux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);



        db = new Database(this);

        Bundle extras = getIntent().getExtras();

        mUsername = extras.getString("user");

        name = new ArrayList<>();
        score2048 = new ArrayList<>();
        scorePEG = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);

        adapter = new ScoreAdapter(this, name, score2048, scorePEG);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        displayData();
    }

    private void displayData() {

        cursor = db.getData();
        if (cursor.getCount() != 0){
            if (cursor.moveToFirst()) {

                do {
                    name.add(cursor.getString(0));
                    score2048.add(cursor.getString(1));
                    scorePEG.add(cursor.getString(2));
                    aux = cursor.getString(0);

                } while (cursor.moveToNext());
            }
        }else{
            Toast.makeText(getApplicationContext(), "No entry exists", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToMenu(View view) {
        Intent intent = new Intent(ScoreActivity.this, MenuActivity.class);
        intent.putExtra("user", mUsername);
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (mUsername.equals("admin")){

                name.remove(viewHolder.getAdapterPosition());
                score2048.remove(viewHolder.getAdapterPosition());
                scorePEG.remove(viewHolder.getAdapterPosition());

                db.deleteScore(aux);
                Toast.makeText(getApplicationContext(), "DELETE SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "ONLY CAN DELETE IF YOU ARE AN ADMIN", Toast.LENGTH_SHORT).show();
            }
            adapter.notifyDataSetChanged();
        }
    };
}