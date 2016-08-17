package com.sam_chordas.android.stockhawk.rest;

import android.content.ContentProviderOperation;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.data.StockHawkConstants;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam_chordas on 10/8/15.
 */
public class Utils {

  private static String LOG_TAG = Utils.class.getSimpleName();

  public static boolean showPercent = true;

  public static ArrayList quoteJsonToContentVals(String JSON){
    ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
    JSONObject jsonObject = null;
    JSONArray resultsArray = null;
    try{
      jsonObject = new JSONObject(JSON);
      if (jsonObject != null && jsonObject.length() != 0){
        jsonObject = jsonObject.getJSONObject("query");
        int count = Integer.parseInt(jsonObject.getString("count"));
        if (count == 1){
          jsonObject = jsonObject.getJSONObject("results")
              .getJSONObject("quote");
            boolean asknull = jsonObject.get("Ask") !=null;
            Log.v("jsonobject-ask",asknull+"");
            asknull = jsonObject.get("Ask").equals("null");
            Log.v("jsonobject-ask-if",asknull+"");
            Log.v("Ask value",jsonObject.get("Ask").toString());
            if(jsonObject.get("Ask") !=null && !jsonObject.get("Ask").toString().contains("null")  )
                batchOperations.add(buildBatchOperation(jsonObject));
            else{

            }
        } else{
          resultsArray = jsonObject.getJSONObject("results").getJSONArray("quote");

          if (resultsArray != null && resultsArray.length() != 0){
            for (int i = 0; i < resultsArray.length(); i++){
              jsonObject = resultsArray.getJSONObject(i);
                if(!jsonObject.get("Ask").equals("null"))
                {
                    batchOperations.add(buildBatchOperation(jsonObject));
                };
            }
          }
        }
      }
    } catch (JSONException e){
      Log.e(LOG_TAG, "String to JSON failed: " + e);
    }
      Log.v("batchoperations.size()=",batchOperations.size()+"");
    return batchOperations;
  }

  public static String truncateBidPrice(String bidPrice){
    if (!bidPrice.equals("null"))
      bidPrice = String.format("%.2f", Float.parseFloat(bidPrice));
    return bidPrice;
  }

  public static String truncateChange(String change, boolean isPercentChange){
    String weight = change.substring(0,1);
    String ampersand = "";
    if (isPercentChange){
      ampersand = change.substring(change.length() - 1, change.length());
      change = change.substring(0, change.length() - 1);
    }
    change = change.substring(1, change.length());
    double round = 0;
    if (change.matches("\\d*"))
     round= (double) Math.round(Double.parseDouble(change) * 100) / 100;
    change = String.format("%.2f", round);
    StringBuffer changeBuffer = new StringBuffer(change);
    changeBuffer.insert(0, weight);
    changeBuffer.append(ampersand);
    change = changeBuffer.toString();
    return change;
  }

  public static ContentProviderOperation buildBatchOperation(JSONObject jsonObject){
    ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
            QuoteProvider.Quotes.CONTENT_URI);
    try {

        Log.v("buildBatchoperation",jsonObject.toString());
      String change = jsonObject.getString("Change");
      builder.withValue(QuoteColumns.SYMBOL, jsonObject.getString("symbol"));
      builder.withValue(QuoteColumns.BIDPRICE, truncateBidPrice(jsonObject.getString("Bid")));
      builder.withValue(QuoteColumns.PERCENT_CHANGE, truncateChange(
          jsonObject.getString("ChangeinPercent"), true));
      builder.withValue(QuoteColumns.CHANGE, truncateChange(change, false));
      builder.withValue(QuoteColumns.ISCURRENT, 1);
      if (change.charAt(0) == '-'){
        builder.withValue(QuoteColumns.ISUP, 0);
      }else{
        builder.withValue(QuoteColumns.ISUP, 1);
      }

    } catch (JSONException e){
      e.printStackTrace();
    }
    return builder.build();
  }

  public static ILineDataSet getDataSet(JSONObject results) {
    JSONArray quoteArray = null;
    ILineDataSet lineDataSet1 = null;
    try {

      if (results != null) {
        JSONObject query1 = (JSONObject) results.get(StockHawkConstants.JSON_OBJECT_QUERY);
        JSONObject RESULTS = query1.getJSONObject(StockHawkConstants.JSON_OBJECT_RESULTS);
        quoteArray = RESULTS.getJSONArray(StockHawkConstants.JSON_OBJECT_QUOTE);


        ArrayList<ILineDataSet> dataSets = null;
        ArrayList<Entry> valueSet1 = new ArrayList<>();
        for (int i = 0; i < quoteArray.length(); i++) {

          JSONObject quoteValue = quoteArray.getJSONObject(i);
          float datef = 0;
          String date = (String) quoteValue.get(StockHawkConstants.JSON_OBJECT_DATE);
          String value = (String) quoteValue.get(StockHawkConstants.JSON_OBJECT_ADJ_CLOSE);
          String [] dateArray = new String[3] ;
          if (date!= null && date != "" ){
            dateArray = date.split("-");
            datef = Float.parseFloat(dateArray[2]);
          }
          // Here we show the date and the stock value
          Entry v1e1 = new Entry(i,Float.parseFloat(value));
          valueSet1.add(v1e1);
        }


        lineDataSet1 = new LineDataSet(valueSet1, "Brand 1");
        int[] colors = new int[]{Color.RED, Color.RED, Color.BLUE, Color.GREEN, Color.GRAY, Color.RED};
      }
    }catch(JSONException ex){
      ex.printStackTrace();
    }

    return lineDataSet1;
  }



}
