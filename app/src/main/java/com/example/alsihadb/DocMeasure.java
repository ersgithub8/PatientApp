package com.example.alsihadb;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.example.alsihadb.JSON.CustomRequest;
import com.example.alsihadb.JSON.MySingleton;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DocMeasure extends AppCompatActivity implements
        OnChartValueSelectedListener {

    Toolbar toolbar;
    private LineChart chart;
    String addrs;
    boolean isgetaddress=false;
    protected Typeface tfLight;
    RecyclerView weight;
    WeightAdapter adapter;
    List<String> values=new ArrayList<>();
    String patid=null;
    final Handler ha=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_measure);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Measure ECG");

        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");

        patid=getIntent().getStringExtra("id");

        weight=findViewById(R.id.weight);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        weight.setLayoutManager(layoutManager);
        weight.setHasFixedSize(true);
        adapter=new WeightAdapter(values);
        weight.setAdapter(adapter);

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);

        // enable description text
        chart.getDescription().setEnabled(true);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        chart.setData(data);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTypeface(tfLight);
        l.setTextColor(Color.WHITE);

        XAxis xl = chart.getXAxis();
        xl.setTypeface(tfLight);
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaximum(100f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);




    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            ha.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //call function


                    ha.postDelayed(this, 2000);
                    getData2(getIntent().getStringExtra("id"));
                }
            }, 2000);
        }catch (Exception e)
        {

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ha.removeCallbacksAndMessages(null);
    }

    private void addEntry(int value) {

        LineData data = chart.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addEntry(new Entry(set.getEntryCount(), value), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            chart.notifyDataSetChanged();

            // limit the number of visible entries
            chart.setVisibleXRangeMaximum(120);
            // chart.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            chart.moveViewToX(data.getEntryCount());

            // this automatically refreshes the chart (calls invalidate())
            // chart.moveViewTo(data.getXValCount()-7, 55f,
            // AxisDependency.LEFT);
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setCircleRadius(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);
        return set;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    ////////
    private void getData2(final String patid) {



        Map<String, String> params = new Hashtable<String, String>();
        params.put("patid", patid);

        CustomRequest jsonRequest = new CustomRequest(Request.Method.POST, "http://alseha.martoflahore.com/getECGData.php", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                Toast.makeText(DocMeasure.this, ""+response, Toast.LENGTH_SHORT).show();
                parseData2(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Something went wrong"+error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


        jsonRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonRequest);
    }

    private void parseData2(JSONObject response) {
        JSONArray students = null;
        try {
            students = response.getJSONArray("data");
        } catch (JSONException e) {
        }

        assert students != null;

        for (int i = 0; i < students.length(); i++) {

            try {
                JSONObject student = students.getJSONObject(i);

                addEntry(Integer.valueOf(student.getString("ecg")));
                values.add((student.getString("loadd")));
                adapter.notifyDataSetChanged();
                weight.scrollToPosition(adapter.getItemCount() - 1);



            } catch (JSONException e) {

            }

        }

        ///////////
    }
}
