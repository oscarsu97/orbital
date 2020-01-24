package com.example.myorbital;

import android.content.Context;
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

public class TravelCountryAdapter extends FirestoreRecyclerAdapter<TravelDetails, TravelCountryAdapter.TravelViewHolder> {
    private List<TravelDetails> travelList;
    private LayoutInflater layoutInflater;

    private OnItemClickListener listener;


    public TravelCountryAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    public TravelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //parent is the Recycler view
        View itemView = layoutInflater.from(parent.getContext())
                .inflate(R.layout.travel_country_list, parent, false);

        return new TravelViewHolder(itemView);

    }

    @Override
    protected void onBindViewHolder(@NonNull TravelViewHolder holder, int i, @NonNull TravelDetails travelDetails) {
        holder.travelTitle.setText(travelDetails.getTitle());
        holder.travelDates.setText(travelDetails.getDates());
    }


    public class TravelViewHolder extends RecyclerView.ViewHolder {
        public TextView travelTitle;
        public TextView travelDates;

        public TravelViewHolder(View view) {
            super(view);
            travelTitle = (TextView) view.findViewById(R.id.travelTittle);
            travelDates = (TextView) view.findViewById(R.id.travelDates);

            view.setOnClickListener(new View.OnClickListener() {
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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}