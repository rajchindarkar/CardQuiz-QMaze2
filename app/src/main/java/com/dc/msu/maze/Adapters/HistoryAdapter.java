package com.dc.msu.maze.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dc.msu.maze.BaseUtils;
import com.dc.msu.maze.Models.HistoryModel;
import com.dc.msu.maze.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryModel> historyModels;

    public HistoryAdapter(List<HistoryModel> models) {
        this.historyModels = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_history_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryModel model = historyModels.get(position);

        holder.answersText.setText("Right Answers : " + model.getResult());
        holder.questionsText.setText("Total Questions : " + model.getTotal());
        double percentage = (model.getResult() * 1.0 / model.getTotal()) * 100;
        holder.perText.setText((int)percentage + "%");
        holder.dateText.setText(BaseUtils.formatDate(model.getDate(), "MMM dd, yyyy - hh:mm a").toUpperCase());
    }

    @Override
    public int getItemCount() {
        return historyModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView perText, answersText, questionsText, dateText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            perText = itemView.findViewById(R.id.card_history_per_label);
            answersText = itemView.findViewById(R.id.card_history_ans_label);
            questionsText = itemView.findViewById(R.id.card_history_que_label);
            dateText = itemView.findViewById(R.id.card_history_date_label);
        }
    }
}
