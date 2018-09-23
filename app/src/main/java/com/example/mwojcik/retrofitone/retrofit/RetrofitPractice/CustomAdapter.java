package com.example.mwojcik.retrofitone.retrofit.RetrofitPractice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mwojcik.retrofitone.R;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private List<RetroPhoto> dataList;
    private Context context;


    public CustomAdapter(Context context, List<RetroPhoto> dataList){
        this.dataList = dataList;
        this.context = context;
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load("https://via.placeholder.com/150/92c952")
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titleTV.setText(dataList.get(position).getTitle());

        //DLACZEGO TAK? ANO BO JAKBYSMY TU TWORZYLI NOWEGO PICASSO DLA KAZDEGO ITEMA,
        //TO MIELIBYSMYT MEMORYLEAKSY I WYWALALO BY NAM APKE.
        //TO TAK SWOJA DROGA - W TUTORIALU BYL BLAD, KOMU UDALO SIE
        //NA TO WPASC TO DOBRZE :)
        Picasso.with(context).load(dataList.get(position).getThumbnailUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleTV;
        ImageView coverImage;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.titleTV);
            coverImage = itemView.findViewById(R.id.coverImage);
        }

    }
}
