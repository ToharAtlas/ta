package com.example.glutenfree;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class businessAdapter extends RecyclerView.Adapter<businessAdapter.viewHolder> {
    private ArrayList<business> businessList;
    private Context context; //מחזיק את שם העמוד-שם המחלקה

    public businessAdapter(ArrayList<business> businessList, Context context) {
        this.businessList = businessList;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(this.context);
        View view = layoutInflater.inflate(R.layout.activity_business_view, viewGroup, false);
        return new businessAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        business b = businessList.get(i);
        viewHolder.businessName.setText(b.getName());
        viewHolder.businessDescription.setText("תיאור: " + b.getDescription());
        viewHolder.businessAddress.setText("כתובת בית העסק:  " + b.getAddress());
        viewHolder.openingHours.setText("שעות פתיחה: " + b.getOpening_hours());
        if (!b.getSite_link().isEmpty()) {
            viewHolder.siteLink.setText("לחץ על מנת להיכנס לאתר בית העסק ");
            viewHolder.siteLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(b.getSite_link()));
                    context.startActivity(intent);
                }
            });
        } else
            viewHolder.siteLink.setText("");
        if (!b.getFacebook_link().isEmpty()) {
            viewHolder.facebookLink.setText(" לחץ על מנת להיכנס לעמוד הפייסבוק של העסק" + b.getFacebook_link());
            viewHolder.facebookLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(b.getFacebook_link()));
                    context.startActivity(intent);
                }
            });
        } else
            viewHolder.facebookLink.setText("");
        if (!b.getPrices().isEmpty())
            viewHolder.prices.setText("טווח מחירים: " + b.getPrices());
        else
            viewHolder.prices.setText("");
        if (b.getDelivery_services().equals("false"))
            viewHolder.delivery.setText("העסק אינו מבצע שירותי משלוחים ");
        else
            viewHolder.delivery.setText("העסק מבצע שירותי משלוחים:" + b.getDelivery_services());
        viewHolder.phone.setText("טלפון בית העסק: " + b.getPrices());
        //דירוג viewHolder.businessDescription.setText("תיאור: " + b.getDescription());
        // viewHolder.sliderView=new SliderView(context,b.getPictures());
        String editedVideo="";

        for (int j = 0; j < b.getVideo().length(); j++) {
            if (b.getVideo().charAt(j) == '=')
                editedVideo = b.getVideo().substring(j + 1);
        }
        String finalEditedVideo = editedVideo;
        viewHolder.youTubePlayerView.addListener(new AbstractYouTubePlayerListener() {
           @Override
           public void onReady(@NonNull YouTubePlayer youTubePlayer) {
               String videoId = finalEditedVideo;
               youTubePlayer.loadVideo(videoId, 0); }
       });
        //תגובות   viewHolder.businessDescription.setText("תיאור: " + b.getDescription());
    }

    @Override
    public int getItemCount() {
        return businessList.size(); //מחזיר את מספר התאים ברשימה לאותו עמוד, adapter, ואחראי בעצם לכמות הפעמים שהוא ירוץ בתוך האדפטר
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView businessName, businessDescription, businessAddress, openingHours, siteLink, facebookLink, prices, delivery, phone, rating;
        SliderView sliderView;
        YouTubePlayer youTubePlayerView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            businessName = itemView.findViewById(R.id.tvBusinessName);
            businessDescription = itemView.findViewById(R.id.tvDescription);
            businessAddress = itemView.findViewById(R.id.tvAddress);
            openingHours = itemView.findViewById(R.id.tvOpeningHours);
            siteLink = itemView.findViewById(R.id.tvSiteLink);
            facebookLink = itemView.findViewById(R.id.tvFacebookLink);
            prices = itemView.findViewById(R.id.tvPrices);
            delivery = itemView.findViewById(R.id.tvDelivery);
            phone = itemView.findViewById(R.id.tvPhone);
            rating = itemView.findViewById(R.id.tvRating);
            sliderView = itemView.findViewById(R.id.image_slider);
            youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
        }
    }
}
