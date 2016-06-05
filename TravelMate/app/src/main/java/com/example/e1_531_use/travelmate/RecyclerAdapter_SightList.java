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
public class RecyclerAdapter_SightList extends RecyclerView.Adapter<RecyclerAdapter_SightList.RecyclerHolder> {
    ArrayList<String> SightName, SightAddress;
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
        SightName.remove(position);
        SightAddress.remove(position);
        notifyItemRemoved(position);
    }

    public RecyclerAdapter_SightList(ArrayList<String> sight_Name, ArrayList<String> sight_Address) {
        SightName = sight_Name;
        SightAddress = sight_Address;
    }

    public static class RecyclerHolder extends RecyclerView.ViewHolder {
        TextView textViewSightName, textViewSightAddress;

        public RecyclerHolder(View itemView) {
            super(itemView);
            textViewSightName = (TextView) itemView.findViewById(R.id.sightName);
            textViewSightAddress = (TextView) itemView.findViewById(R.id.sightAddress);
        }
    }

    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sight_list, parent, false);
        RecyclerHolder recyclerHolder = new RecyclerHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerHolder holder, int position) {
        holder.textViewSightName.setText(SightName.get(position));
        holder.textViewSightAddress.setText(SightAddress.get(position));
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
        if (SightName == null)
            return 0;
        else
            return SightName.size();
    }





    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
