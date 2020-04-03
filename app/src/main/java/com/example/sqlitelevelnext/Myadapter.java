package com.example.sqlitelevelnext;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {

    Context mcontext;
    int mresource;
    ArrayList<UserData> dataArrayList=new ArrayList<>();
    Activity activity;

    public Myadapter(Context mcontext, int mresource, ArrayList<UserData> dataArrayList, Activity activity) {
        this.mcontext = mcontext;
        this.mresource = mresource;
        this.dataArrayList.addAll(dataArrayList);
        this.activity = activity;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(mcontext).inflate(mresource,parent,false);
        ViewHolder holder=new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.email.setText(dataArrayList.get(position).getEmail());
        holder.name.setText(dataArrayList.get(position).getName());

        byte[] image=dataArrayList.get(position).getImageBytes();

        Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
        holder.profile.setImageBitmap(bitmap);
        final String phone=dataArrayList.get(position).getPhone();
        holder.image_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(mcontext, Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
                    Intent intent=new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:"+phone));
                    mcontext.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView email,name;
        CircleImageView profile;
        ImageView image_call;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            email=itemView.findViewById(R.id.email);
            name=itemView.findViewById(R.id.name);
            profile=itemView.findViewById(R.id.profile);
            image_call=itemView.findViewById(R.id.image_call);
        }
    }
}
