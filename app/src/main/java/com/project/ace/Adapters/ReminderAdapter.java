package com.project.ace.Adapters;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.ace.Notifications.AlertReceiver;
import com.project.ace.R;
import com.project.ace.RecyclerViewItems.Reminder;

public class ReminderAdapter extends FirestoreRecyclerAdapter<Reminder, ReminderAdapter.ReminderHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();     //firestore instance

    public ReminderAdapter(@NonNull FirestoreRecyclerOptions<Reminder> options) {
        super(options);
        Log.d("count", String.valueOf(getItemCount()));
    }

    @Override
    protected void onBindViewHolder(@NonNull final ReminderHolder holder, int position, @NonNull Reminder model) {
        final int reminderID;
        String status;

        holder.textViewTitle.setText(model.getReminderTitle());
        holder.textViewDescription.setText(model.getReminderDescription());
        holder.textViewDate.setText(model.getReminderDate());
        holder.textViewTime.setText(model.getReminderTime());
        reminderID = model.getReminderID();
        status = model.getStatus();

        if(status.equals("done")){
            holder.cardView.setCardBackgroundColor(Color.parseColor("#7CFC00"));    //green
            holder.done.setVisibility(View.INVISIBLE);
        }
        else{
            holder.cardView.setCardBackgroundColor(Color.parseColor("#A9A9A9"));    //grey
        }

        final Context context = holder.cardView.getContext();

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteReminder(context,reminderID,holder.getAdapterPosition());
            }
        });

        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reminderTaskDone(context,holder.getAdapterPosition(),reminderID);
            }
        });
    }

    public void deleteReminder(Context context,final int reminderID, int position){
        String docID = getSnapshots().getSnapshot(position).getId();
        final DocumentReference docRef = db.collection("Reminder").document(docID);
        docRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("delete","Reminder with id "+reminderID+" successfully deleted");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("delete","Reminder with id "+reminderID+" could not be deleted");
                    }
                });

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,reminderID,intent,0);
        alarmManager.cancel(pendingIntent);
    }

    public void reminderTaskDone(Context context,int position,final int reminderID){
        String docID = getSnapshots().getSnapshot(position).getId();
        final DocumentReference docRef = db.collection("Reminder").document(docID);

        docRef.update("status","done")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("delete","Reminder with id "+reminderID+" marked completed");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("delete","Reminder with id "+reminderID+" could not be marked completed");
                    }
                });

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,reminderID,intent,0);
        alarmManager.cancel(pendingIntent);
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
        ImageButton done,delete;
        CardView cardView;

        public ReminderHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.reminder_title);
            textViewDescription = itemView.findViewById(R.id.reminder_description);
            textViewDate = itemView.findViewById(R.id.reminder_date);
            textViewTime = itemView.findViewById(R.id.reminder_time);
            cardView = itemView.findViewById(R.id.reminder_item);
            done = itemView.findViewById(R.id.reminder_task_done);
            delete = itemView.findViewById(R.id.delete_reminder);
        }
    }
}