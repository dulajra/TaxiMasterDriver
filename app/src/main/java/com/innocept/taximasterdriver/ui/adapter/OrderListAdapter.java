package com.innocept.taximasterdriver.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.innocept.taximasterdriver.R;
import com.innocept.taximasterdriver.model.foundation.Order;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Dulaj on 30-Dec-15.
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    Context context;
    private List<Order> dataSet;

    public OrderListAdapter(Context context, List<Order> dataSet) {
        this.context = context;
        this.dataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textFromTo;
        TextView textTime;
        TextView textContact;
        TextView textNote;
        TextView textState;

        public ViewHolder(View itemView) {
            super(itemView);

            textFromTo = (TextView) itemView.findViewById(R.id.text_order_list_from_to);
            textTime = (TextView) itemView.findViewById(R.id.text_order_list_time);
            textContact = (TextView)itemView.findViewById(R.id.text_order_list_contact);
            textNote = (TextView)itemView.findViewById(R.id.text_order_list_note);
            textState = (TextView)itemView.findViewById(R.id.text_order_list_state);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_order_list, parent, false);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,3,0,3);
        v.setLayoutParams(layoutParams);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.textFromTo.setText(dataSet.get(position).getOrigin() + " to " + dataSet.get(position).getDestination());
        viewHolder.textTime.setText(new SimpleDateFormat("yyyy-MM-dd HH-mm").format(dataSet.get(position).getTime()));
        viewHolder.textContact.setText(dataSet.get(position).getContact());
        viewHolder.textNote.setText(dataSet.get(position).getNote());

        Order.OrderState orderState = dataSet.get(position).getOrderState();
        if(orderState == Order.OrderState.PENDING){
            viewHolder.textState.setVisibility(View.VISIBLE);
            viewHolder.textState.setText(dataSet.get(position).getOrderState().toString());
            viewHolder.textState.setTextColor(Color.RED);
        }
        else if(orderState == Order.OrderState.ACCEPTED){
            viewHolder.textState.setVisibility(View.VISIBLE);
            viewHolder.textState.setText(dataSet.get(position).getOrderState().toString());
            viewHolder.textState.setTextColor(Color.GREEN);
        }
        viewHolder.textState.setText(orderState.toString());
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
