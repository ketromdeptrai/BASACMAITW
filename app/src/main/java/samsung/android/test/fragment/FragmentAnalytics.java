package samsung.android.test.fragment;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import com.example.ketromdeptrai.database.Analytics;
import com.example.ketromdeptrai.database.AnalyticsTableDataAdapter;
import com.example.ketromdeptrai.database.Database;
import com.example.ketromdeptrai.database.SortableAnalyticsTableView;
import com.google.zxing.client.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import samsung.android.test.MainActivity;

/**
 * Created by ketromdeptrai on 4/29/2017.
 */

public class FragmentAnalytics extends Fragment {

    List<Analytics> model = new ArrayList<Analytics>();
//    ListViewAdapter adapter = null;
    String subjectID, classID, date;
    Database db = null;
    public FragmentAnalytics (){

    }
    public FragmentAnalytics(String subjectID, String classID, String date){
        this.subjectID = subjectID;
        this.classID = classID;
        this.date = date;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_ana_test, container, false);
        ((MainActivity) getActivity()).setActionBarTitle(subjectID+" - "+classID);
        ((MainActivity) getActivity()).setActionBarSubtitle(date);
        if (savedInstanceState != null) {
            subjectID = savedInstanceState.getString("subjectID");
            classID = savedInstanceState.getString("classID");
            date = savedInstanceState.getString("date");
        }
        test(v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore some state before we've even inflated our own layout
            // This could be generic things like an ID that our Fragment represents
            subjectID = savedInstanceState.getString("subjectID");
            classID = savedInstanceState.getString("classID");
            date = savedInstanceState.getString("date");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("subjectID", subjectID);
        outState.putString("classID", classID);
        outState.putString("date", date);
    }
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore some state that needs to happen after our own views have had
            // their state restored
            // DON'T try to restore ListViews here because their scroll position will
            // not be restored properly
            subjectID = savedInstanceState.getString("subjectID");
            classID = savedInstanceState.getString("classID");
            date = savedInstanceState.getString("date");
        }
    }

    private void test(View v){
        List<Analytics> analyticsList= getAnalyticArray();
     //   analyticsList.add(new Analytics(1,"20132962", "Nguyễn Minh Phú", "6"));
        final SortableAnalyticsTableView analyticTableView = (SortableAnalyticsTableView) v.findViewById(R.id.tableView);
        if (analyticTableView != null) {
            final AnalyticsTableDataAdapter analyticsTableDataAdapter = new AnalyticsTableDataAdapter(getActivity(), analyticsList, analyticTableView);
            analyticTableView.setDataAdapter(analyticsTableDataAdapter);
            //carTableView.addDataClickListener(new CarClickListener());
           // carTableView.addDataLongClickListener(new CarLongClickListener());
            analyticTableView.setSwipeToRefreshEnabled(true);
            analyticTableView.setSwipeToRefreshListener(new SwipeToRefreshListener() {
                @Override
                public void onRefresh(final RefreshIndicator refreshIndicator) {
                    analyticTableView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                        //    final Analytics randomCar = getRandomCar();
                        //    analyticsTableDataAdapter.getData().add(randomCar);
                            analyticsTableDataAdapter.notifyDataSetChanged();
                            refreshIndicator.hide();
                           // Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                }
            });
        }
    }

    private String swapDateAndYear(String temp){
        StringTokenizer st = new StringTokenizer(temp, "/");
        String date = st.nextToken();
        String month = st.nextElement().toString();
        String year = st.nextElement().toString();
        return (year + "/" + month + "/" + date);
    }

    private  List<Analytics> getAnalyticArray (){
        List<Analytics> data = new ArrayList<Analytics>();
        try{db = new Database(getActivity());
            Cursor c = db.loadStudentList(classID);
            c.moveToFirst();
            int i = 1;
            while (c.isAfterLast() == false){
                if (date.equals("All")){
                    String studentID = c.getString(0);
                    int absent = db.calculateAbsentTimes(studentID,classID);
                    data.add(new Analytics(i, studentID ,db.loadStudent(Integer.parseInt(studentID)), absent+""));
                } else{
                    String studentID = c.getString(0);
                    String check = (db.checkStudentAbsent(classID, studentID, swapDateAndYear(date))) ? "✖":"✔";
                    data.add(new Analytics(i, studentID ,db.loadStudent(Integer.parseInt(studentID)), check));
                }
                i++;
                // Toast.makeText(getActivity(), (c.getInt(0))+"", Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
            c.close();
            db.close();
        }
        catch (Exception exception){
            Toast.makeText(getActivity(), exception+"", Toast.LENGTH_LONG).show();
        }
        finally {
            return data;
        }
    }
}
