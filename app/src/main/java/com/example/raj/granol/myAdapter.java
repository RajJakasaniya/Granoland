package com.example.raj.granol;

/**
 * Created by raj on 8/5/17.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by Belal on 2/23/2017.
 */

public class myAdapter extends RecyclerView.Adapter<myAdapter.ViewHolder> {

    private Context context;
    private List<Upload> uploads;
    Upload upload;

    public myAdapter(Context context, List<Upload> uploads) {
        this.uploads = uploads;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_images, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        upload = uploads.get(position);

        //holder.textViewName.setText(upload.getName());

        Log.e("Yoo",upload.getUrl());
        Glide.with(context).load(upload.getUrl()).placeholder(R.drawable.dc)
                .error(R.drawable.final_logo).fitCenter().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //public TextView textViewName;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            //textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }

}