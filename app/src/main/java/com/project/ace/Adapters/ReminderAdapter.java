package com.project.ace.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.project.ace.R;
import com.project.ace.RecyclerViewItems.Reminder;

public class ReminderAdapter extends FirestoreRecyclerAdapter<Reminder, ReminderAdapter.ReminderHolder> {


    public ReminderAdapter(@NonNull FirestoreRecyclerOptions<Reminder> options) {
        super(options);
        Log.d("count", String.valueOf(getItemCount()));
    }

    @Override
    protected void onBindViewHolder(@NonNull ReminderHolder holder, int position, @NonNull Reminder model) {

        holder.textViewTitle.setText(model.getReminderTitle());
        holder.textViewDescription.setText(model.getReminderDescription());
        holder.textViewDate.setText(model.getReminderDate());
        holder.textViewTime.setText(model.getReminderTime());

    }

    @NonNull
    @Override
    public ReminderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_item,
                parent, false);
        return new ReminderHolder(v);
    }

    class ReminderHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle,textViewDescription,textViewDate,textViewTime;

        public ReminderHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.reminder_title);
            textViewDescription = itemView.findViewById(R.id.reminder_description);
            textViewDate = itemView.findViewById(R.id.reminder_date);
            textViewTime = itemView.findViewById(R.id.reminder_time);
        }
    }
}
