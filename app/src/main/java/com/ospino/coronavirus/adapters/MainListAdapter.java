package com.ospino.coronavirus.adapters;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ospino.coronavirus.R;
import com.ospino.coronavirus.activities.CountryDataActivity;
import com.ospino.coronavirus.models.Breakdown;
import com.ospino.coronavirus.models.Country;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class MainListAdapter extends ArrayAdapter<Breakdown> implements Filterable {

    private final static String TAG = "MainListAdapter";
    private List<Breakdown> list;
    private List<Breakdown> filterList = null;
    private final Activity context;
    private CountrySortFields currentSortField = CountrySortFields.COUNTRY_ISO;

    public MainListAdapter (Activity context, List<Breakdown> list) {
        super(context, R.layout.activity_main_list_view_item, list);
        this.context = context;
        this.list = list;
    }

    public CountrySortFields getCurrentSortField() {return currentSortField;}

    public void setCurrentSortField(CountrySortFields countrySortFields) { currentSortField = countrySortFields; }

    public enum CountrySortFields {CONFIRMED, NEW_CASES, RECOVERED, DEATHS, COUNTRY_ISO}


    static class ViewHolder {
        protected TextView textViewConfirmed;
        protected TextView textViewNewCases;
        protected TextView textCountry;
        protected TextView textRecovered;
        protected TextView textDeaths;
        protected ImageView imageViewFlag;
    }

    public void sortByCurrentField() {
        sortByField(currentSortField);
    }

    public void sortByField(CountrySortFields sortField) {
        currentSortField = sortField; // Update the current sort field
        switch (sortField) {
            case COUNTRY_ISO:
                sort((breakdown, t1) -> breakdown.getLocation().getIsoCode().compareTo(t1.getLocation().getIsoCode()));
                break;
            case CONFIRMED:
                sort((breakdown, t1) -> t1.getTotalConfirmedCases() - breakdown.getTotalConfirmedCases());
                break;
            case NEW_CASES:
                sort(((breakdown, t1) -> t1.getNewlyConfirmedCases() - breakdown.getNewlyConfirmedCases()));
                break;
            case DEATHS:
                sort((breakdown, t1) -> t1.getTotalDeaths() - breakdown.getTotalDeaths());
                break;
            case RECOVERED:
                sort((breakdown, t1) -> t1.getTotalRecoveredCases() - breakdown.getTotalRecoveredCases());
                break;
            default:
                Log.v(TAG, "No Country field implemented");
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = context.getLayoutInflater();

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflator.inflate(R.layout.activity_main_list_view_item, null);
            viewHolder.textViewConfirmed = convertView.findViewById(R.id.text_confirmed);
            viewHolder.textViewNewCases = convertView.findViewById(R.id.text_new_cases);
            viewHolder.textCountry = convertView.findViewById(R.id.text_country);
            viewHolder.imageViewFlag = convertView.findViewById(R.id.image_flag);
            viewHolder.textDeaths = convertView.findViewById(R.id.text_deaths);
            viewHolder.textRecovered = convertView.findViewById(R.id.text_recovered);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Breakdown item = list.get(position);
        System.out.println(item.getNewlyConfirmedCases());

        viewHolder.textCountry.setText(String.valueOf(item.getLocation().getIsoCode()));

        viewHolder.imageViewFlag.setImageResource(context.getResources()
                .getIdentifier("drawable/ic_list_country_" + item.getLocation().getIsoCode().toLowerCase()
                        ,null, context.getPackageName()));

        viewHolder.textViewConfirmed.setText(String.format("%,d", item.getTotalConfirmedCases()));
        viewHolder.textViewNewCases.setText(String.format("%,d",item.getNewlyConfirmedCases()));
        viewHolder.textDeaths.setText(String.format("%,d",item.getTotalDeaths()));
        viewHolder.textRecovered.setText(String.format("%,d",item.getTotalRecoveredCases()));

        convertView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, CountryDataActivity.class);
            intent.putExtra("country", item);
            context.startActivity(intent);
            context.overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
        });

        return convertView;
    }

    public List<Breakdown> getList() {
        return list;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                list = (List<Breakdown>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();  // Holds the results of a filtering operation in values
                List<Breakdown> FilteredArrList = new ArrayList<>();

                if (filterList == null) {
                    filterList = new ArrayList<Breakdown>(list); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = filterList.size();
                    results.values = filterList;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < filterList.size(); i++) {
                        Breakdown data = filterList.get(i);

                        if (data.getLocation().getCountryOrRegion().toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
    }
}
