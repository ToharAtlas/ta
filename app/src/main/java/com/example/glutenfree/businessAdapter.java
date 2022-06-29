package com.example.glutenfree;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class businessAdapter extends RecyclerView.Adapter<businessAdapter.viewHolder> {
    private boolean doNotifyDataSetChangedOnce = false;
    private ArrayList<business> businessList;
    private Context context; //מחזיק את שם העמוד-שם המחלקה
    StorageReference storageReference;
    ArrayList<Uri> p;
    Thread thread;
    String editedVideo = "";
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    SharedPreferences sharedPreferences;
    boolean multipleCities = false;
    int count = 0;


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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull viewHolder viewHolder, int i) {
        if (doNotifyDataSetChangedOnce) {
            doNotifyDataSetChangedOnce = false;
            notifyDataSetChanged();
        }
        business b = businessList.get(i);
        p = new ArrayList<>();
        viewHolder.businessName.setText(b.getName());
        viewHolder.businessDescription.setText("תיאור: " + b.getDescription());
        for (int j = 0; j < b.getAddressCity().length(); j++) {
            if (b.getAddressCity().charAt(j) == ',')
                multipleCities = true;
            if (b.getAddressCity().charAt(j) == ' ')
                count++;
        }
        if (multipleCities != true || count < 6) {
            viewHolder.businessAddress.setText("כתובת בית העסק:  " + b.getAddress());
            viewHolder.businessAddress.setTextColor(R.color.blue);
            viewHolder.businessAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Address = b.getAddress().trim();
                    DisplayAddress(Address);
                }

                private void DisplayAddress(String address) {

                    try {
                        Uri uri = Uri.parse("https://www.google.co.in/maps/place/" + address);
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);
                        i.setPackage("com.google.android.apps.maps");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(b.getAddress()));
                        context.startActivity(intent);
                    }
                }

            });
        } else {
            viewHolder.businessAddress.setText("לחץ על מנת לצפות ברשימת הסניפים ");
            viewHolder.businessAddress.setTextColor(R.color.blue);
            viewHolder.businessAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(b.getAddress()));
                    context.startActivity(intent);
                }
            });
        }
        viewHolder.openingHours.setText("שעות פתיחה: " + b.getOpening_hours());
        if (!b.getPictures().isEmpty()) {
            viewHolder.gallery.setVisibility(View.VISIBLE);
            viewHolder.sliderView.setVisibility(View.VISIBLE);
            thread = new Thread() {
                public void run() {
                    try {
                        synchronized (context) {
                            for (int i1 = 0; i1 < b.getPictures().size(); i1++) {
                                storageReference = FirebaseStorage.getInstance().getReference("business/" + b.getName());
                                storageReference = storageReference.child(b.getPictures().get(i1));
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        p.add(uri);
                                    }
                                });
                            }
                            context.wait(2000);
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SliderAdapter sliderAdapter = new SliderAdapter(p, context);
                                    viewHolder.sliderView.setSliderAdapter(sliderAdapter);
                                    viewHolder.sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                                    viewHolder.sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                                    viewHolder.sliderView.startAutoCycle();
                                }
                            });


                        }
                    } catch (InterruptedException e) {
                    }


                }
            };


            thread.start();
        }


        if (!b.getSite_link().equals("")) {
            viewHolder.siteLink.setVisibility(View.VISIBLE);
            viewHolder.siteLink.setText("לחץ על מנת להיכנס לאתר בית העסק ");
            viewHolder.siteLink.setTextColor(R.color.blue);
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
        }
        if (!b.getFacebook_link().equals("")) {
            viewHolder.facebookLink.setVisibility(View.VISIBLE);
            viewHolder.facebookLink.setText(" לחץ על מנת להיכנס לעמוד הפייסבוק של העסק");
            viewHolder.facebookLink.setTextColor(R.color.blue);
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
        }
        if (!b.getPrices().equals("")) {
            viewHolder.prices.setVisibility(View.VISIBLE);
            viewHolder.prices.setText("טווח מחירים: " + b.getPrices());
        }
        if (b.getDelivery_services().equals("false"))
            viewHolder.delivery.setText("העסק אינו מבצע שירותי משלוחים ");
        else
            viewHolder.delivery.setText("העסק מבצע שירותי משלוחים:" + b.getDelivery_services());
        viewHolder.phone.setText("טלפון בית העסק: " + b.getPhone());
        viewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + b.getPhone()));
                context.startActivity(intent);
            }
        });


        if (!b.getVideo().equals("")) {
            viewHolder.vid.setVisibility(View.VISIBLE);
            viewHolder.youTubePlayerView.setVisibility(View.VISIBLE);
            for (int j = 0; j < b.getVideo().length(); j++) {
                if (b.getVideo().charAt(j) == '=')
                    editedVideo = b.getVideo().substring(j + 1);


                viewHolder.youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        youTubePlayer.loadVideo(editedVideo, 0);
                        youTubePlayer.pause();
                    }
                });
            }
        }
        sharedPreferences = context.getSharedPreferences("remember_me1", 0);
        if (sharedPreferences.getBoolean("remember_me", false)) {
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
            if (b.getReviews() != null) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, R.layout.reviews_layout, b.getReviews());
                viewHolder.listView.setAdapter(arrayAdapter);
            }

            viewHolder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArrayList<String> stringArrayList;
                    if (b.getReviews() != null) {
                        stringArrayList = b.getReviews();
                    } else {
                        stringArrayList = new ArrayList<>();
                    }
                    stringArrayList.add(sharedPreferences.getString("firstName", "") + " " + sharedPreferences.getString("lastName", "") + "- " + viewHolder.etEditTextReviews.getText().toString());
                    b.setReviews(stringArrayList);
                    firebaseDatabase.getReference("business").child(b.getKey()).child("reviews").setValue(stringArrayList);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.reviews_layout, b.getReviews());
                    viewHolder.listView.setAdapter(arrayAdapter);

                }
            });
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return this.businessList.size(); //מחזיר את מספר התאים ברשימה לאותו עמוד, adapter, ואחראי בעצם לכמות הפעמים שהוא ירוץ בתוך האדפטר
    }

    public void setBusinessList(ArrayList<business> businessList) {
        this.businessList = businessList;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView businessName, businessDescription, businessAddress, openingHours, siteLink, facebookLink, prices, delivery, phone, gallery, vid;
        EditText etEditTextReviews;
        RatingBar ratingBar;
        Button add;
        SliderView sliderView;
        com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView youTubePlayerView;
        ListView listView;
        LinearLayout linearLayout;

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
            gallery = itemView.findViewById(R.id.tvGallery);
            vid = itemView.findViewById(R.id.tvVid);
            sliderView = itemView.findViewById(R.id.image_slider);
            youTubePlayerView = (com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView) itemView.findViewById(R.id.youtube_player_view);
            etEditTextReviews = itemView.findViewById(R.id.etEditTextReviews);
            listView = itemView.findViewById(R.id.list_view);
            add = itemView.findViewById(R.id.add);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }
}