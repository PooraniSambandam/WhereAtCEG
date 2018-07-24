package com.example.whereatcegfinal;

/**
 * Created by POORANI on 27-Sep-17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {

    public ArrayList<String> MainList;

    public ArrayList<String> PlacesListTemp;

    public ListAdapter.SubjectDataFilter placeDataFilter;

    public ListAdapter(Context context, int id, ArrayList<String> placesArrayList) {

        super(context, id, placesArrayList);

        this.PlacesListTemp = new ArrayList<String>();

        this.PlacesListTemp.addAll(placesArrayList);

        this.MainList = new ArrayList<String>();

        this.MainList.addAll(placesArrayList);
    }

    @Override
    public Filter getFilter() {

        if (placeDataFilter == null) {

            placeDataFilter = new ListAdapter.SubjectDataFilter();
        }
        return placeDataFilter;
    }


    public class ViewHolder {

        TextView locationName;
        //TextView Number;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListAdapter.ViewHolder holder = null;

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.custom_layout, null);

            holder = new ListAdapter.ViewHolder();

            holder.locationName = (TextView) convertView.findViewById(R.id.textviewName);

            //holder.Number = (TextView) convertView.findViewById(R.id.textviewPhoneNumber);

            convertView.setTag(holder);

        } else {

            holder = (ListAdapter.ViewHolder) convertView.getTag();
        }

        String placeName = PlacesListTemp.get(position);

        holder.locationName.setText(placeName);

        //holder.Number.setText(student.getNumber());

        return convertView;

    }

    private class SubjectDataFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

            charSequence = charSequence.toString().toLowerCase();

            FilterResults filterResults = new FilterResults();

            if (charSequence != null && charSequence.toString().length() > 0) {

                ArrayList<String> arrayList1 = new ArrayList<String>();

                for (int i = 0, l = MainList.size(); i < l; i++) {
                    String subject = MainList.get(i);

                    if (subject.toString().toLowerCase().contains(charSequence))

                        arrayList1.add(subject);
                }
                filterResults.count = arrayList1.size();

                filterResults.values = arrayList1;
            } else {
                synchronized (this) {
                    filterResults.values = MainList;

                    filterResults.count = MainList.size();
                }
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            PlacesListTemp = (ArrayList<String>) filterResults.values;

            notifyDataSetChanged();

            clear();

            for (int i = 0, l = PlacesListTemp.size(); i < l; i++)
                add(PlacesListTemp.get(i));

            notifyDataSetInvalidated();
        }
    }
}