package com.example.myweather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {

    Context context;
    ArrayList<model> arr;

    public RecycleViewAdapter(Context context, ArrayList arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.recycleview_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Changing Date format
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
        try {
            Date timefmt = input.parse(arr.get(position).time);
            holder.layout_time.setText(output.format(timefmt));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.layout_temp.setText(arr.get(position).temp);

        Log.d("Gaurav","Image Url : "+arr.get(position).img_url);
        Picasso.get().load("https:"+arr.get(position).img_url).into(holder.layout_img);
        holder.layout_windspd.setText(arr.get(position).wind_speed+"kmph");
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView layout_time,layout_temp,layout_windspd;
        ImageView layout_img;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);



            layout_time  = itemView.findViewById(R.id.layout_time);
            layout_temp  = itemView.findViewById(R.id.layout_temp);
            layout_img = itemView.findViewById(R.id.layout_img);
            layout_windspd = itemView.findViewById(R.id.layout_windspd);
        }
    }
}
