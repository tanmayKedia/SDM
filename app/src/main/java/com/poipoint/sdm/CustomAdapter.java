package com.poipoint.sdm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tanmay on 4/25/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<Holder>
{
    private CategoreyActivity context;
    private String[] list;
    public CustomAdapter(String[] list, CategoreyActivity context) {
        this.list = list;
        this.context=context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.custom_list_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int i) {
        holder.mRadioButton.setText(list[i]);
        //Saving the position value in each holder so that we can set it in radioButtonPosition
        holder.position=i;
        holder.newOrderActivity=context;
    }

    @Override
    public int getItemCount() {
        return list.length;
    }

}