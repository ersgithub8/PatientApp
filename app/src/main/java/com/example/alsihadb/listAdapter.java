package com.example.alsihadb;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder> {
    private List<PatientItem> values;
    Context context;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView id,name,email;
        LinearLayout rl;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            id = (TextView) v.findViewById(R.id.id);
            name = (TextView) v.findViewById(R.id.name);
            email = (TextView) v.findViewById(R.id.email);
            rl=v.findViewById(R.id.rl);

        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public listAdapter(List<PatientItem> myDataset, Context context) {
        values = myDataset;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public listAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final PatientItem name = values.get(position);

        holder.id.setText(name.getId());
        holder.name.setText(name.getUsername());
        holder.email.setText(name.getEmail());
        holder.rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(context,DocMeasure.class);
                i.putExtra("id",name.getId());
                context.startActivity(i);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}