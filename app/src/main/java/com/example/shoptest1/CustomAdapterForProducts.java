package com.example.shoptest1;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class CustomAdapterForProducts extends SimpleAdapter {
    String[] photoUrl;
    Context context;

    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public CustomAdapterForProducts(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to,
                                     String[] photoUrl) {
        super(context, data, resource, from, to);
        this.photoUrl = new String[photoUrl.length];
        System.arraycopy(photoUrl, 0, this.photoUrl, 0, photoUrl.length);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ImageView orderPhotoImageView;
        orderPhotoImageView = view.findViewById(R.id.orderPhotoImageView);
        if (!photoUrl[position].equals("")) {
            Picasso.with(context).load(photoUrl[position]).into(orderPhotoImageView);
        }
        return view;
    }
}
