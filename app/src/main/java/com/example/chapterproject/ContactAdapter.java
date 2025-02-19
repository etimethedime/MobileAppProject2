package com.example.chapterproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList<String> contactData;
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContactName;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContactName = itemView.findViewById(R.id.nameText);
        }
        public TextView getTvContactName() {
            return tvContactName;
        }
    }
    public ContactAdapter(ArrayList<String> arrayList){
        contactData = arrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_item_view, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactViewHolder viewHolder = (ContactViewHolder) holder;
        viewHolder.getTvContactName().setText(contactData.get(position));
    }

    @Override
    public int getItemCount() {
        return contactData.size();
    }
}
