package com.example.alsihadb;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AddPatientAdpater extends RecyclerView.Adapter<AddPatientAdpater.ViewHolder> {
    private List<PatientItem> values;
    AddPatient act;

    String click;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView id,name;
        Button add;

        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            id = (TextView) v.findViewById(R.id.id);
            name = (TextView) v.findViewById(R.id.name);
            add =  v.findViewById(R.id.add);
        }
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public AddPatientAdpater(List<PatientItem> myDataset,String click,AddPatient act) {
        values = myDataset;
        this.click=click;
        this.act=act;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AddPatientAdpater.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.pat_items
                        , parent, false);
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
        if(click.equals("add"))
        {
            final SharedPreferences sharedPreferences = act.getSharedPreferences("userdata", MODE_PRIVATE);
            holder.add.setText("ADD");
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    act.getData(sharedPreferences.getString("username","null"),name.getId(),"add");
                }
            });

        }
        else
        {
            final SharedPreferences sharedPreferences = act.getSharedPreferences("userdata", MODE_PRIVATE);
            holder.add.setText("DEl");
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    act.getData(sharedPreferences.getString("username","null"),name.getId(),"remove");
                }
            });
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}