package com.example.motus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    public SpinnerAdapter(Context context, ArrayList<SpinnerItem> itemList){
        super(context, 0, itemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if (convertView == null){
            convertView= LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_layout, parent, false
            );
        }
        TextView textViewName = convertView.findViewById(R.id.text_view_category);

        SpinnerItem currentItem = getItem(position);

        if (currentItem != null) {
            textViewName.setText(currentItem.getItem());
        }

        return convertView;
    }
}
