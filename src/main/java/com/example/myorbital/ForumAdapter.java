package com.example.myorbital;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ForumAdapter extends FirestoreRecyclerAdapter<TravelDetails, ForumAdapter.ForumViewHolder> {

    private List<TravelDetails> forumList;
    private LayoutInflater layoutInflater;
    private ForumAdapter.OnItemClickListener listener;

    public ForumAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);

    }


    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = layoutInflater.from(parent.getContext())
                .inflate(R.layout.forum_list, parent, false);
        return new ForumViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(@NonNull ForumAdapter.ForumViewHolder holder, int i, @NonNull TravelDetails travelDetails) {
        holder.totalSpending.setText("" + travelDetails.getCurrency() + travelDetails.getTotalSpending());
        holder.travelDate.setText(travelDetails.getDates());
        holder.countryName.setText(travelDetails.getCountryName());
    }

    /*@Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        holder.totalSpending.setText(forumList.get(position).getTotalSpending());
        holder.travelDate.setText(forumList.get(position).getDates());
        holder.countryName.setText(forumList.get(position).getCountryName());
    }*/


    public class ForumViewHolder extends RecyclerView.ViewHolder {

        private TextView countryName;
        private TextView travelDate;
        private TextView totalSpending;


        public ForumViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
            travelDate = itemView.findViewById(R.id.forum_travel_dates);
            totalSpending = itemView.findViewById(R.id.totalSpending);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.OnItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(ForumAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
