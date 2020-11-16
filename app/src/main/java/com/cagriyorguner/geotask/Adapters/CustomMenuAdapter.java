package com.cagriyorguner.geotask.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cagriyorguner.geotask.Database.RoomDB;
import com.cagriyorguner.geotask.Models.CustomMenuItem;
import com.cagriyorguner.geotask.R;

import java.util.List;

public class CustomMenuAdapter extends RecyclerView.Adapter<CustomMenuAdapter.CustomMenuViewHolder>{

    private List<CustomMenuItem> mCustomMenuItemsList;
    Context mContext;

    private AdapterCallback adapterCallback;

    public CustomMenuAdapter(List<CustomMenuItem> customMenuItemsList, Context context){
        mCustomMenuItemsList = customMenuItemsList;
        mContext = context;
        notifyDataSetChanged();
        try {
            adapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static class CustomMenuViewHolder extends RecyclerView.ViewHolder{
        public TextView bodyTw;
        public ImageView arrowIW;
        public ImageView trashIW;
        public LinearLayout secondLayerLayout;
        public Button showDetailsButton;
        public Button showLocationButton;

        public CustomMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            bodyTw = itemView.findViewById(R.id.custom_menu_adapter_body);
            arrowIW = itemView.findViewById(R.id.custom_menu_adapter_arrow);
            trashIW = itemView.findViewById(R.id.custom_menu_adapter_trash);
            secondLayerLayout = itemView.findViewById(R.id.custom_menu_adapter_second_layer);
            showDetailsButton = itemView.findViewById(R.id.custom_menu_adapter_button_1);
            showLocationButton = itemView.findViewById(R.id.custom_menu_adapter_button_2);
        }
    }

    @NonNull
    @Override
    public CustomMenuAdapter.CustomMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_menu_adapter_view_layout, parent, false);
        return new CustomMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomMenuAdapter.CustomMenuViewHolder holder, int position) {
        final CustomMenuItem customMenuItem = mCustomMenuItemsList.get(position);

        holder.bodyTw.setText(customMenuItem.getTaskName() + " - " + customMenuItem.getLocationName());

        //onClickListeners

        //to make the second layer appear which consists 'detayı göster' and 'konumu göster' buttons.
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.arrowIW.getTag().equals("1")){
                    holder.arrowIW.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                    holder.secondLayerLayout.setVisibility(View.VISIBLE);
                    holder.arrowIW.setTag("2");
                }
                else if (holder.arrowIW.getTag().equals("2")){
                    holder.arrowIW.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24);
                    holder.secondLayerLayout.setVisibility(View.GONE);
                    holder.arrowIW.setTag("1");
                }
            }
        };

        holder.bodyTw.setOnClickListener(onClickListener);
        holder.arrowIW.setOnClickListener(onClickListener);

        holder.showDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.onMethodCallbackButton1(customMenuItem);
            }
        });

        holder.showLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.onMethodCallbackButton2(customMenuItem);
            }
        });

        holder.trashIW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RoomDB database = RoomDB.getInstance(mContext);
                database.mainDao().deleteCustomMenuItem(customMenuItem);
                adapterCallback.onMethodCallbackButton3();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCustomMenuItemsList.size();
    }

    public interface AdapterCallback {
        void onMethodCallbackButton1(CustomMenuItem customMenuItem);
        void onMethodCallbackButton2(CustomMenuItem customMenuItem);
        void onMethodCallbackButton3();
    }
}
