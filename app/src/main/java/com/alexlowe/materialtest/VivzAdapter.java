package com.alexlowe.materialtest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Alex on 8/18/2015.
 */
public class VivzAdapter extends RecyclerView.Adapter<VivzAdapter.MyViewHolder>{
    private Context context;
    private LayoutInflater inflater;

    List<Item> data = Collections.emptyList();

    public VivzAdapter(Context context, List<Item> data){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position){
        data.remove(position);
        //used with recyclerView instead of notify datasetchanged(), b/c less expensive - all items would be rebound and relaid out
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = inflater.inflate(R.layout.item_view, parent, false);
        Log.d("VIVZ", "onCreateHolder called");
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Item current = data.get(position);
        Log.d("VIVZ", "onBindHolder called");
        holder.title.setText(current.getTitle());
        holder.icon.setImageResource(current.getIconId());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
            icon = (ImageView) itemView.findViewById(R.id.item_icon);
            icon.setOnClickListener(this);
            //title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, "Item clicked at " + getLayoutPosition(), Toast.LENGTH_SHORT).show();
            delete(getLayoutPosition());
        }
    }
}
