package com.example.financialorganizer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;

public class WeeklySpendingAdapater extends RecyclerView.Adapter<WeeklySpendingAdapater.ViewHolder>{
    private Context mContext;
    private List<Data> myDataList;

    public WeeklySpendingAdapater(Context mContext, List<Data> myDataList) {
        this.mContext = mContext;
        this.myDataList = myDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.get_layout, parent, false);
        return new WeeklySpendingAdapater.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position) {
        final Data data = myDataList.get(position);

        holder.item.setText("Item: " + data.getItem());
        holder.amount.setText("Amount: " + data.getAmount());
        holder.date.setText("On: " + data.getDate());
        holder.note.setText("Note: " + data.getNotes());

        switch (data.getItem()){
            case "Transportation":
                holder.imageView.setImageResource(R.drawable.budget);
                break;
            case "Food":
                holder.imageView.setImageResource(R.drawable.budget);
                break;
            case "House/Rent":
                holder.imageView.setImageResource(R.drawable.budget);
                break;
            case "Entertainment":
                holder.imageView.setImageResource(R.drawable.budget);
                break;
            case "Health":
                holder.imageView.setImageResource(R.drawable.budget);
                break;
            case "Other":
                holder.imageView.setImageResource(R.drawable.budget);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView item, amount, date, note;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            item = itemView.findViewById(R.id.item);
            amount = itemView.findViewById(R.id.amount);
            date = itemView.findViewById(R.id.date);
            note = itemView.findViewById(R.id.note);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
