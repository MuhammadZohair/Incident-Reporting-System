package com.lunatialiens.incidentreportingsystem.controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lunatialiens.incidentreportingsystem.R;
import com.lunatialiens.incidentreportingsystem.models.Incident;
import com.lunatialiens.incidentreportingsystem.utils.AppUtils;

import java.util.ArrayList;

/**
 * The type Incident adapter. Adapter is the class which sets the recycler view data that
 * how it will be displayed.
 * THE METHODS WHICH ARE GREYED OUT THEY ARE NOT BEING USED IN THE APP BUT MIGHT BE USEFUL
 * FOR TESTING PURPOSES
 */
public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    private final Context context; // this is the context which we will pass in the constructor
    private final LayoutInflater mInflater; // the inflator
    private ArrayList<Incident> adapterData; // this is the data which we will show in the recyclerview

    /**
     * Instantiates a new Incident adapter.
     *
     * @param context the context
     * @param data    the data
     */
    public IncidentAdapter(Context context, ArrayList<Incident> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.adapterData = data;
    }

    /**
     * This method is called whenever an item in the recycler view is created
     *
     * @param parent   the parent recycler view
     * @param viewType the view Type
     * @return the view holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_incident, parent, false);
        return new ViewHolder(view);
    }

    /**
     * This method is called whenever an item is added in the list. so if there is 20 items in the
     * list then this method will be called 20 times
     *
     * @param holder   the holder
     * @param position the position
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.locationTextView.setText(adapterData.get(position).getLocation()); // here we set the location of the item
        holder.descTextView.setText(adapterData.get(position).getDesc()); // here we set the data about the incident
        holder.timestampTextView.setText(AppUtils.getSimpleTime(adapterData.get(position).getTimestamp())); // here we set the time

    }

    /**
     * This method is called if we need to check how many items are there in the recycler view
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (adapterData != null)
            return adapterData.size();
        else return 0;
    }


    /**
     * Sets adapter data.
     *
     * @param adapterData the adapter data
     */
    public void setAdapterData(ArrayList<Incident> adapterData) {
        this.adapterData = adapterData;
    }

    /**
     * Gets item.
     *
     * @param id the id
     * @return the item
     */
    public Incident getItem(int id) {
        return adapterData.get(id);
    }


    /**
     * The type View holder.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The Location text view.
         */
        TextView locationTextView;
        /**
         * The Desc text view.
         */
        TextView descTextView;
        /**
         * The Timestamp text view.
         */
        TextView timestampTextView;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        ViewHolder(View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.tv_location);
            descTextView = itemView.findViewById(R.id.tv_desc);
            timestampTextView = itemView.findViewById(R.id.tv_timestamp);

        }
    }
}