package com.obushko.numismatistapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {


    private Context context;
    private final ArrayList<ListItem> lists;



    public ListAdapter(Context context, ArrayList<ListItem> lists) {
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_item, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        ListItem currentItem = lists.get(position);

        String urlImage = currentItem.getUrlImage();
        String title = currentItem.getTitle();
        String price = currentItem.getPrice();
        boolean currentTheme = currentItem.getCurrentTheme();

        Picasso.get().load(urlImage).into(holder.imageView);
        holder.textViewTitle.setText(title);
        holder.textViewPrice.setText(price);


    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textViewTitle;
        TextView textViewPrice;


        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positionIndex = getAdapterPosition();
                    int[] imageResources = { R.drawable.back_1, R.drawable.back_2, R.drawable.back_3, R.drawable.back_1,
                    R.drawable.back_4, R.drawable.back_4, R.drawable.back_5, R.drawable.back_6, R.drawable.back_7,
                    R.drawable.back_8, R.drawable.back_9, R.drawable.back_10, R.drawable.back_11, R.drawable.back_12,
                    R.drawable.back_13, R.drawable.back_14, R.drawable.back_14, R.drawable.back_15, R.drawable.back_16, R.drawable.back_17, R.drawable.back_17,
                    R.drawable.back_18, R.drawable.back_19, R.drawable.back_20, R.drawable.back_21, R.drawable.back_21, R.drawable.back_22,
                    R.drawable.back_23, R.drawable.back_1, R.drawable.back_1, R.drawable.back_25};
                    int imageResourceId = imageResources[positionIndex];

                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.alpha_in);
                    imageView.startAnimation(animation);
                    imageView.setImageResource(imageResourceId);


                }
            });

        }
    }

    interface Listener{
        void onClickListItem(ListItem listItem, int pos);

    }

}
