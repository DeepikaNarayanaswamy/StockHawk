package com.sam_chordas.android.stockhawk.ui;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.content.CursorLoader;

import android.content.Loader;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import java.util.ArrayList;

public class StockDetailActivity extends AppCompatActivity{



    public static final String TAG_STOCK_SYMBOL = "STOCK_SYMBOL";
    private static final int STOCKS_LOADER = 1;

    private String currency;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);

        //currency = getIntent().getStringExtra(TAG_STOCK_SYMBOL);

        //setTitle(currency);


        //getLoaderManager().initLoader(STOCKS_LOADER, null, this);
        /*String [] labels = {"a","b","c","d","e"};
        float [] values = new float[5];
        values[0] =16.001f;
        values[1] =15.031f;
        values[2] =15.033f;
        values[3] =15.034f;
        values[4] =15.0989f;
        LineSet dataset = new LineSet(labels, values);
        dataset.setColor(Color.parseColor("#758cbb"))
                .setFill(Color.parseColor("#2d374c"))
                .setDotsColor(Color.parseColor("#758cbb"))
                .setThickness(4)
                .setDashed(new float[]{10f, 10f});

        lineChartView.setBorderSpacing(Tools.fromDpToPx(15))
                .setYLabels(AxisController.LabelPosition.OUTSIDE)
                .setXLabels(AxisController.LabelPosition.NONE)
                .setLabelsColor(Color.parseColor("#6a84c3"))
                .addData(dataset);*/

        /*ValueLineChart mCubicValueLineChart = (ValueLineChart) findViewById(R.id.linechart);

        ValueLineSeries series = new ValueLineSeries();
        series.setColor(0xFF56B7F1);

        series.addPoint(new ValueLinePoint("Jan", 2.4f));
        series.addPoint(new ValueLinePoint("Feb", 3.4f));
        series.addPoint(new ValueLinePoint("Mar", .4f));
        series.addPoint(new ValueLinePoint("Apr", 1.2f));
        series.addPoint(new ValueLinePoint("Mai", 2.6f));
        series.addPoint(new ValueLinePoint("Jun", 1.0f));
        series.addPoint(new ValueLinePoint("Jul", 3.5f));
        series.addPoint(new ValueLinePoint("Aug", 2.4f));
        series.addPoint(new ValueLinePoint("Sep", 2.4f));
        series.addPoint(new ValueLinePoint("Oct", 3.4f));
        series.addPoint(new ValueLinePoint("Nov", .4f));
        series.addPoint(new ValueLinePoint("Dec", 1.3f));

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();
*/
        /*LineChart chart = (LineChart) findViewById(R.id.linechart);

        LineData data = new LineData();

        data.addDataSet(getDataSet());
        chart.setDescription("Stock price over time");
        chart.setData(data);
        setXAxis(chart);

        chart.getAxisRight().setDrawLabels(false);
        //chart.setAutoScaleMinMaxEnabled(true);
        chart.setGridBackgroundColor(128);
        chart.setBorderColor(255);
        chart.setDrawGridBackground(false);
        //chart.setBackgroundColor(0);
        chart.getLegend().setEnabled(false);
        chart.setPinchZoom(true);
        chart.setDescription("");
        chart.setTouchEnabled(true);
        chart.setDoubleTapToZoomEnabled(true);
        chart.animateXY(2000, 2000);
        chart.invalidate();*/


    }
    private ILineDataSet getDataSet() {
        ArrayList<ILineDataSet> dataSets = null;
        ArrayList<Entry> valueSet1 = new ArrayList<>();

        Entry v1e1 = new Entry(10.000f, 0);
        valueSet1.add(v1e1);
        Entry v1e2 = new Entry(20.000f, 4);
        valueSet1.add(v1e2);
        Entry v1e3 = new Entry(30.000f, 5);
        valueSet1.add(v1e3);
        Entry v1e4 = new Entry(40.000f, 6);
        valueSet1.add(v1e4);
        Entry v1e5 = new Entry(50.000f, 7);
        valueSet1.add(v1e5);
        Entry v1e6 = new Entry(60.000f, 8);
        valueSet1.add(v1e6);

        ILineDataSet lineDataSet1 = new LineDataSet(valueSet1, "Brand 1");
        int[] colors  = new int[] {Color.RED  ,Color.RED, Color.BLUE ,Color.GREEN, Color.GRAY, Color.RED};


        return lineDataSet1;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");
        xAxis.add("JUL");
        xAxis.add("AUG");
        xAxis.add("SEPT");
        xAxis.add("OCT");
        xAxis.add("NOV");
        xAxis.add("DEC");
        return xAxis;
    }

    private void setXAxis(LineChart chart){
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.RED);
        //xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
    }

}
