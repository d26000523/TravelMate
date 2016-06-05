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
public class RecyclerAdapter_ProcessList extends RecyclerView.Adapter<RecyclerAdapter_ProcessList.RecyclerHolder> {
    ArrayList<String> ProcessName, ProcessAddress;
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    //String[] TripName,TripDate;
    public void delete(int position) { //removes the row
        ProcessName.remove(position);
        ProcessAddress.remove(position);
        notifyItemRemoved(position);
    }

    public RecyclerAdapter_ProcessList(ArrayList<String> trip_Name, ArrayList<String> trip_Date) {
        ProcessName = trip_Name;
        ProcessAddress = trip_Date;
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView textViewProcessName, textViewProcessAddress;

        public RecyclerHolder(View itemView) {
            super(itemView);
            textViewProcessName = (TextView) itemView.findViewById(R.id.processName);
            textViewProcessAddress = (TextView) itemView.findViewById(R.id.processAddress);
        }
    }

    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_process_list, parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, int position) {
        holder.textViewProcessName.setText(ProcessName.get(position));
        holder.textViewProcessAddress.setText(ProcessAddress.get(position));
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
        if (ProcessName == null)
            return 0;
        else
            return ProcessName.size();
    }





    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
