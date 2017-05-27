package com.example.ketromdeptrai.database;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.client.android.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;


public class AnalyticsTableDataAdapter extends LongPressAwareTableDataAdapter<Analytics> {

    private static final int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public AnalyticsTableDataAdapter(final Context context, final List<Analytics> data, final TableView<Analytics> tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Analytics analytics = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderID(analytics, parentView);
                break;
            case 1:
                renderedView = renderStudentID(analytics);
                break;
            case 2:
                renderedView = renderStudentName(analytics, parentView);
                break;
            case 3:
                renderedView = renderCol4(analytics);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        /*final Analytics analytics = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableCatName(analytics);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;*/
        return getDefaultCellView(rowIndex, columnIndex, parentView);
    }

    /*private View renderEditableCatName(final Analytics analytics) {
        final EditText editText = new EditText(getContext());
        editText.setText(analytics.getName());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new AnalyticsNameUpdater(analytics));
        return editText;
    }*/

    private View renderCol4(final Analytics analytics) {
       /* final String priceString = PRICE_FORMATTER.format(analytics.getPrice()) + " √";

        final TextView textView = new TextView(getContext());
        textView.setText(priceString);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);

        if (analytics.getPrice() < 50000) {
            textView.setTextColor(0xFF2E7D32);
        } else if (analytics.getPrice() > 100000) {
            textView.setTextColor(0xFFC62828);
        }

        return textView;*/
        return renderString(analytics.getCol4());
    }

    private View renderStudentName(final Analytics analytics, final ViewGroup parentView) {
   //     final View view = getLayoutInflater().inflate(R.layout.table_cell_power, parentView, false);
//        final TextView kwView = (TextView) view.findViewById(R.id.kw_view);
//        final TextView psView = (TextView) view.findViewById(R.id.ps_view);

//        kwView.setText(format(Locale.ENGLISH, "%d %s", analytics.getKw(), getContext().getString(R.string.kw)));
//        psView.setText(format(Locale.ENGLISH, "%d %s", analytics.getPs(), getContext().getString(R.string.ps)));

       // return view;
        return renderString(analytics.getStudentName());
    }

    private View renderStudentID(final Analytics analytics) {
        return renderString(analytics.getStudentID());
    }

    private View renderID(final Analytics analytics, final ViewGroup parentView) {
       /* final View view = getLayoutInflater().inflate(R.layout.table_cell_image, parentView, false);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(analytics.getProducer().getLogo());*/
        return renderString(analytics.getId()+"");
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        if (value.equals("✔")){
            textView.setTextColor(Color.GREEN);
        } else if (value.equals("✖")){
            textView.setTextColor(Color.RED);
        }
        return textView;
    }

   /* private static class AnalyticsNameUpdater implements TextWatcher {

        private Analytics analyticsToUpdate;

        public AnalyticsNameUpdater(Analytics analyticsToUpdate) {
            this.analyticsToUpdate = analyticsToUpdate;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            analyticsToUpdate.setName(s.toString());
        }
    }*/

}
