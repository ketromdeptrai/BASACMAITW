package com.example.ketromdeptrai.database;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.google.zxing.client.android.R;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import de.codecrafters.tableview.toolkit.SortStateViewProviders;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;


/**
 * An extension of the {@link SortableTableView} that handles {@link Analytics}s.
 *
 * @author ISchwarz
 */
public class SortableAnalyticsTableView extends SortableTableView<Analytics> {

    public SortableAnalyticsTableView(final Context context) {
        this(context, null);
    }

    public SortableAnalyticsTableView(final Context context, final AttributeSet attributes) {
        this(context, attributes, android.R.attr.listViewStyle);
    }

    public SortableAnalyticsTableView(final Context context, final AttributeSet attributes, final int styleAttributes) {
        super(context, attributes, styleAttributes);

        final SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(context, "ID", "Student ID", "StudentName", "Absent");
        simpleTableHeaderAdapter.setTextColor(ContextCompat.getColor(context, R.color.table_header_text));
        simpleTableHeaderAdapter.setTextSize(14);
        setHeaderAdapter(simpleTableHeaderAdapter);

        final int rowColorEven = ContextCompat.getColor(context, R.color.table_data_row_even);
        final int rowColorOdd = ContextCompat.getColor(context, R.color.table_data_row_odd);
        setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(rowColorEven, rowColorOdd));
        setHeaderSortStateViewProvider(SortStateViewProviders.brightArrows());

        final TableColumnWeightModel tableColumnWeightModel = new TableColumnWeightModel(4);
        tableColumnWeightModel.setColumnWeight(0, 2);
        tableColumnWeightModel.setColumnWeight(1, 3);
        tableColumnWeightModel.setColumnWeight(2, 4);
        tableColumnWeightModel.setColumnWeight(3, 3);
        setColumnModel(tableColumnWeightModel);

        setColumnComparator(0, AnalyticsComparator.getAnalyticsIDComparator());
        setColumnComparator(1, AnalyticsComparator.getAnalyticsStudentIDComparator());
        setColumnComparator(2, AnalyticsComparator.getAnalyticsStudentNameComparator());
        setColumnComparator(3, AnalyticsComparator.getAnalyticsCol4Comparator());
    }

}
