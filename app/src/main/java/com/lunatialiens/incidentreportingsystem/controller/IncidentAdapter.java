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
 * The type Incident adapter.
 */
public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    private final Context context;
    private final LayoutInflater mInflater;
    private ArrayList<Incident> adapterData;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_incident, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.locationTextView.setText(adapterData.get(position).getLocation());
        holder.descTextView.setText(adapterData.get(position).getDesc());
        holder.timestampTextView.setText(AppUtils.getSimpleTime(adapterData.get(position).getTimestamp()));

    }

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