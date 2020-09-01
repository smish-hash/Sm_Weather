package com.example.smweather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class LocationAdapter extends ListAdapter<Location, LocationAdapter.ViewHolder> {
    private OnLocationClickListener listener;

    public LocationAdapter() {
        super(DIFF_CALLBACK);
    }
    private static final DiffUtil.ItemCallback<Location> DIFF_CALLBACK = new DiffUtil.ItemCallback<Location>() {
        @Override
        public boolean areItemsTheSame(@NonNull Location oldItem, @NonNull Location newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Location oldItem, @NonNull Location newItem) {
            return oldItem.getStrlocation().equals(newItem.getStrlocation());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAdapter.ViewHolder holder, int position) {
        Location currentLocation = getItem(position);
        holder.locationtv.setText(currentLocation.getStrlocation());
    }

    public Location getLocationAt(int position){
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView locationtv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationtv = (TextView) itemView.findViewById(R.id.location_textview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onLocationClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnLocationClickListener{
        void onLocationClick(Location location);
    }

    public void setOnLocationClickListener(OnLocationClickListener listener){
        this.listener = listener;
    }
}
