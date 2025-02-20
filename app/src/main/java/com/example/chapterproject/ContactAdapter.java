package com.example.chapterproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {
    private ArrayList<Contact> contactData;
    private static View.OnClickListener mOnItemClickListener;
    private boolean isDeleting;
    private Context parentContext;
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

    public ContactAdapter(ArrayList<Contact> contactData, Context context) {
        this.contactData = contactData;
        parentContext = context;
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

        if (isDeleting) {
            viewHolder.deleteContactButton.setVisibility(View.VISIBLE);
            viewHolder.deleteContactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = viewHolder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        deleteItem(currentPosition);
                    }
                }
            });
        } else {
            viewHolder.deleteContactButton.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return contactData.size();
    }
    public void setDelete(boolean delete){
        isDeleting = delete;
    }
    private void deleteItem(int position){
        Contact contact = contactData.get(position);
        ContactDataSource ds = new ContactDataSource(parentContext);
        try{
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getId());
            ds.close();
            if(didDelete){
                contactData.remove(position);
                notifyDataSetChanged();
            }
            else{
                Toast.makeText(parentContext, "Delete Failed", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(parentContext, "Delete Failed", Toast.LENGTH_SHORT).show();
        }
    }

}
