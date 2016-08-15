package com.sam_chordas.android.stockhawk.ui;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class StockDetailActivityFragment extends Fragment {

    public StockDetailActivityFragment() {
    }
    LineChart chart ;        JSONObject results = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        String stockSymbol = extras.getString("STOCKNAME");
        View stockDetailView = inflater.inflate(R.layout.fragment_stock_detail, container, false);
        TextView stockName =  (TextView)stockDetailView.findViewById(R.id.stockName);
        stockName.setText(stockSymbol);
        chart = (LineChart) stockDetailView.findViewById(R.id.linechart);
        FetchHistoricalDataTask asyncTask = new FetchHistoricalDataTask();
        asyncTask.execute(stockSymbol);
        return stockDetailView;
    }


    private class FetchHistoricalDataTask extends AsyncTask<String,Void,Void>
    {
        String url="https://query.yahooapis.com/v1/public/yql";
        String Search="format";
        String SearchVal="json";
        String QueryKey="q";
        String Diag ="diagnostics";
        String DiagVal="true";
        String Env="env";
        String EnvVal="store://datatables.org/alltableswithkeys";
        String Call="callback";
        String CallVal="";

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            String stockName = params[0];
            String query="Select * from yahoo.finance.historicaldata where symbol ='"
                    + stockName + "' and startDate = '2016-01-01' and endDate = '2016-01-25'";


            Log.v("OUTPUT", "ENTERING doINbackground method");
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                Uri buildUri;

                buildUri=Uri.parse(url).buildUpon()
                        .appendQueryParameter(QueryKey,query)
                        .appendQueryParameter(Search,SearchVal)
                        .appendQueryParameter(Diag,DiagVal)
                        .appendQueryParameter(Env,EnvVal)
                        .appendQueryParameter(Call,CallVal)
                        .build();
                Log.v("OUTPUT",buildUri.toString());
                // Create the request to OpenWeatherMap, and open the connection
                URL url = new URL(buildUri.toString());
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                Log.v("OUTPUT","CONNECTED !!!");
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                Log.v("OUTPUT","READING");
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                results = new JSONObject(forecastJsonStr);



            } catch (JSONException ex){
                ex.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            return null;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

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
            chart.invalidate();
        }
    }
    private ILineDataSet getDataSet() {
        JSONArray quoteArray = null;
        ILineDataSet lineDataSet1 = null;
        try {
            JSONObject query1 = (JSONObject) results.get("query");
            JSONObject RESULTS = query1.getJSONObject("results");
            quoteArray = RESULTS.getJSONArray("quote");


        ArrayList<ILineDataSet> dataSets = null;
        ArrayList<Entry> valueSet1 = new ArrayList<>();
        for (int i=0;i<quoteArray.length();i++){

            JSONObject quoteValue = quoteArray.getJSONObject(i);
            String date = null;
             date = (String)quoteValue.get("Date");
            String value = (String)quoteValue.get("Adj_Close");
            SimpleDateFormat formattedDate = new SimpleDateFormat(date);
            Entry v1e1 = new Entry(i,Float.parseFloat(value));
                    valueSet1.add(v1e1);
        }


          lineDataSet1 = new LineDataSet(valueSet1, "Brand 1");
        int[] colors  = new int[] {Color.RED  ,Color.RED, Color.BLUE ,Color.GREEN, Color.GRAY, Color.RED};

        }catch(JSONException ex){
            ex.printStackTrace();
        }

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
