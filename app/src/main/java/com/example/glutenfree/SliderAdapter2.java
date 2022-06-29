package com.example.glutenfree;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter2 extends SliderViewAdapter<SliderAdapter.Holder> {

    int[] images;

    public SliderAdapter2(int[] images) {

        this.images = images;

    }

    @Override
    public SliderAdapter.Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item, parent, false);
        return new SliderAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapter.Holder viewHolder, int position) {
        viewHolder.imageView.setImageResource(images[position]);
    }


    @Override
    public int getCount() {
        return images.length;
    }

    public class Holder extends ViewHolder {

        ImageView imageView;

        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }

}
