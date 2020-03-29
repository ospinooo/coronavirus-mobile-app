package com.ospino.coronavirus.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ospino.coronavirus.R;
import com.ospino.coronavirus.activities.CountryDataActivity;
import com.ospino.coronavirus.models.Breakdown;

import java.util.List;

public class MainListAdapter extends ArrayAdapter<Breakdown> {

    private List<Breakdown> list;
    private final Activity context;

    public MainListAdapter (Activity context, List<Breakdown> list) {
        super(context, R.layout.activity_main_list_view_item, list);
        this.context = context;
        this.list = list;
    }

    static class ViewHolder {
        protected TextView textViewConfirmed;
        protected TextView textViewNewCases;
        protected TextView textCountry;
        protected TextView textRecovered;
        protected TextView textDeaths;

        protected ImageView imageViewFlag;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = context.getLayoutInflater();
        View view = inflator.inflate(R.layout.activity_main_list_view_item, null);
        final ViewHolder viewHolder = new ViewHolder();

        viewHolder.textViewConfirmed = (TextView) view.findViewById(R.id.text_confirmed);
        viewHolder.textViewNewCases = (TextView) view.findViewById(R.id.text_new_cases);
        viewHolder.textCountry = (TextView) view.findViewById(R.id.text_country);
        viewHolder.imageViewFlag = (ImageView) view.findViewById(R.id.image_flag);
        viewHolder.textDeaths = view.findViewById(R.id.text_deaths);
        viewHolder.textRecovered = view.findViewById(R.id.text_recovered);

        view.setTag(viewHolder);

        Breakdown item = list.get(position);
        System.out.println(item.getNewlyConfirmedCases());
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.textViewConfirmed.setText(String.valueOf(item.getTotalConfirmedCases()));
        holder.textViewNewCases.setText(String.valueOf(item.getNewlyConfirmedCases()));
        holder.textCountry.setText(String.valueOf(item.getLocation().getIsoCode()));
        holder.imageViewFlag.setImageResource(context.getResources()
                .getIdentifier("drawable/ic_list_country_" + item.getLocation().getIsoCode().toLowerCase()
                        ,null, context.getPackageName()));
        holder.textDeaths.setText(String.valueOf(item.getTotalDeaths()));
        holder.textRecovered.setText(String.valueOf(item.getTotalRecoveredCases()));


        view.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, CountryDataActivity.class);
            intent.putExtra("country", item);
            context.startActivity(intent);
        });

        return view;
    }
}
