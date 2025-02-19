package com.example.chapterproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList<Contact> contactData;
    private static View.OnClickListener mOnItemClickListener;
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView tvContactName;
        public TextView tvPhone;
        public Button deleteContactButton;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContactName = itemView.findViewById(R.id.contactNameText);
            tvPhone = itemView.findViewById(R.id.contactPhoneText);
            deleteContactButton = itemView.findViewById(R.id.deleteButton);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }
    public ContactAdapter(ArrayList<Contact> contactData) {
        this.contactData = contactData;
    }
    public static void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContactViewHolder viewHolder = (ContactViewHolder) holder;
        viewHolder.tvContactName.setText(contactData.get(position).getContactName());
        viewHolder.tvPhone.setText(contactData.get(position).getCellNumber());
    }

    @Override
    public int getItemCount() {
        return contactData.size();
    }
}
