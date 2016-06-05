package com.example.e1_531_use.travelmate;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by E1-531-USE on 2016/4/15.
 */
public class RecyclerAdapter_UserProcessList extends RecyclerView.Adapter<RecyclerAdapter_UserProcessList.RecyclerHolder> {
    ArrayList<String> TripName, TripDate;
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view , int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //String[] TripName,TripDate;
    public void delete(int position) { //removes the row
        TripName.remove(position);
        TripDate.remove(position);
        notifyItemRemoved(position);
    }

    public RecyclerAdapter_UserProcessList(ArrayList<String> trip_Name, ArrayList<String> trip_Date) {
        TripName = trip_Name;
        TripDate = trip_Date;
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView textViewTripName, textViewTripDate;

        public RecyclerHolder(View itemView) {
            super(itemView);
            textViewTripName = (TextView) itemView.findViewById(R.id.TripName);
            textViewTripDate = (TextView) itemView.findViewById(R.id.TripDate);
        }
    }

    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_process_list, parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, int position) {
        holder.textViewTripName.setText(TripName.get(position));
        holder.textViewTripDate.setText(TripDate.get(position));
        if (mOnItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                int pos = holder.getLayoutPosition();
                mOnItemClickListener.onItemClick(holder.itemView, pos);
            }
        });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (TripName == null)
            return 0;
        else
            return TripName.size();
    }





    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
