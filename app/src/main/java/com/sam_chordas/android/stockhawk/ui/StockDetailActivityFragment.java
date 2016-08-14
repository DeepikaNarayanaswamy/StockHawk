package com.sam_chordas.android.stockhawk.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class StockDetailActivityFragment extends Fragment {

    public StockDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        Bundle extras = intent.getExtras();
        String stockSymbol = extras.getString("STOCKNAME");
        View stockDetailView = inflater.inflate(R.layout.fragment_stock_detail, container, false);
        TextView stockName =  (TextView)stockDetailView.findViewById(R.id.stockName);
        stockName.setText(stockSymbol);
        return stockDetailView;
    }
}
