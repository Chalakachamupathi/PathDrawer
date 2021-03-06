package com.example.chalaka.myapplication;

/**
 * Created by chalaka on 4/3/2016.
 */
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {
    private ArrayList<String> mListItems;
    private LayoutInflater mLayoutInflater;

    public MyCustomAdapter(Context context, ArrayList<String> arrayList){

        mListItems = arrayList;
        Log.d("TCP", "A");
        //get the layout inflater
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("TCP", "B");

    }

    @Override
    public int getCount() {
        //getCount() represents how many items are in the list
        return mListItems.size();
    }

    @Override
    //get the data of an item from a specific position
    //i represents the position of the item in the list
    public Object getItem(int i) {
        return null;
    }

    @Override
    //get the position id of the item from the list
    public long getItemId(int i) {
        return 0;
    }

    @Override

    public View getView(int position, View view, ViewGroup viewGroup) {
        Log.d("TCP", "C");
        //check to see if the reused view is null or not, if is not null then reuse it
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.list_item, null);
            Log.d("TCP", "D");
        }

        //get the string item from the position "position" from array list to put it on the TextView
        String stringItem = mListItems.get(position);
        Log.d("TCP", "E");
        if (stringItem != null) {
            Log.d("TCP", "F");
            TextView itemName = (TextView) view.findViewById(R.id.list_item_text_view);
            Log.d("TCP", "G");
            if (itemName != null) {
                //set the item name on the TextView
                itemName.setText(stringItem);
                Log.d("TCP", "H");
            }else{
                Log.d("TCP", "iteamname is null");
            }
        }

        //this method must return the view corresponding to the data at the specified position.
        return view;

    }
}
