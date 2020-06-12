package com.example.alsihadb;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> {
    private List<String> values;
//private List<Entry> values;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView id, name, email;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            id = (TextView) v.findViewById(R.id.id);
            //newtext = (TextView) v.findViewById(R.id.newtext);

        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public WeightAdapter(List<String > myDataset)
    {
        values = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.weightlist, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String  name = values.get(position);

//        final Entry  name = values.get(position);

        holder.id.setText(name//.toString().substring(7)

        );
        //holder.newtext.setText(name);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return values.size();
    }
}

