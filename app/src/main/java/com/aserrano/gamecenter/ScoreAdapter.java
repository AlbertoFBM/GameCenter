package com.aserrano.gamecenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder>{

    private Context context;
    private ArrayList username_list, score_2048_list, score_peg_list;
    private View v;
    private ScoreActivity list;

    public ScoreAdapter(Context context, ArrayList username_list, ArrayList score_2048_list, ArrayList score_peg_list) {
        this.context = context;
        this.username_list = username_list;
        this.score_2048_list = score_2048_list;
        this.score_peg_list = score_peg_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(String.valueOf(username_list.get(position)));
        holder.score_2048.setText(String.valueOf(score_2048_list.get(position)));
        holder.score_PEG.setText(String.valueOf(score_peg_list.get(position)));
    }

    @Override
    public int getItemCount() {
        return username_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, score_2048, score_PEG;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.userText);
            score_2048 = itemView.findViewById(R.id.score2048Text);
            score_PEG = itemView.findViewById(R.id.scorePEGText);
        }
    }
}
