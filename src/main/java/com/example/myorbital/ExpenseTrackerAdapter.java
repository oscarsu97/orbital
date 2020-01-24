package com.example.myorbital;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseTrackerAdapter extends RecyclerView.Adapter<ExpenseTrackerAdapter.ExpenseViewHolder> {

    private List<ExpenseDetails> expenseList;
    private LayoutInflater layoutInflater;


    public ExpenseTrackerAdapter(List<ExpenseDetails> expenseDetailsList) {
        this.expenseList = expenseDetailsList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.from(parent.getContext())
                .inflate(R.layout.expense_tracker_list, parent, false);

        return new ExpenseViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.itemDescription.setText(expenseList.get(position).getItemDescription());
        holder.itemCategory.setImageResource(expenseList.get(position).setImage());
        holder.itemCost.setText(expenseList.get(position).getItemCost());
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder {
        private TextView itemDescription;
        private ImageView itemCategory;
        private TextView itemCost;

        public ExpenseViewHolder(View view) {
            super(view);
            itemDescription = (TextView) view.findViewById(R.id.expenseDescription);
            itemCategory = (ImageView) view.findViewById(R.id.itemCategory);
            itemCost = (TextView) view.findViewById(R.id.expenseSpend);
        }

    }
}
