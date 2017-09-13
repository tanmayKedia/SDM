package com.poipoint.sdm;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;

class Holder extends RecyclerView.ViewHolder
{   RadioButton mRadioButton;
   int position;
   CategoreyActivity newOrderActivity;
   public Holder(View itemView) {
       super(itemView);
       mRadioButton=(RadioButton)itemView.findViewById(R.id.radio_button_item);
       mRadioButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(newOrderActivity.lastCheckedRadioButton==null) {
                   newOrderActivity.lastCheckedRadioButton=mRadioButton;
                   newOrderActivity.lastCheckedRadioButton.setChecked(true);
                   newOrderActivity.radioButtonPosition=position;
               }
               else if(newOrderActivity.lastCheckedRadioButton!=null)
               {
                   newOrderActivity.lastCheckedRadioButton.setChecked(false);
                   newOrderActivity.lastCheckedRadioButton=mRadioButton;
                   newOrderActivity.lastCheckedRadioButton.setChecked(true);
                   newOrderActivity.radioButtonPosition=position;
               }
           }
       });
   }
}