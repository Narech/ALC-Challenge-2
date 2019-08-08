package com.arsenal_tech.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{

    ArrayList<TravelDeal> deals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    public DealAdapter(){

        //FirebaseUtil.openFbReference("traveldeals",this.);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        deals = FirebaseUtil.mDeals;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.e("Deal",td.getTitle());
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row, viewGroup, false);

        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder dealViewHolder, int i) {
            TravelDeal deal = deals.get(i);
            dealViewHolder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle,tvDescription,tvPrice;
        ImageView image;


        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView)itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            image = (ImageView) itemView.findViewById(R.id.imageDeal);
            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal deal){
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
            showImage(deal.getImageUrl());

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.e("click",String.valueOf(position));
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(v.getContext(), DealActivity.class);
            intent.putExtra("Deal",selectedDeal);
            v.getContext().startActivity(intent);

        }


        private void showImage(String url){
            if( url != null && url.isEmpty()==false){
                Log.e("Image",url);
                Picasso.with(image.getContext())
                        .load(url)
                        .resize(160, 160)
                        .centerCrop()
                        .into(image);
            }
        }
    }

}
