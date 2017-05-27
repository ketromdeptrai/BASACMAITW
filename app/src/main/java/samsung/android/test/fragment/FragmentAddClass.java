package samsung.android.test.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ketromdeptrai.database.Database;
import com.google.zxing.client.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import samsung.android.test.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddClass extends Fragment {
  //  String [] arr_subject ={" "};
    Database db = null;
    public FragmentAddClass() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_addclass, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Add Class");
        ((MainActivity) getActivity()).setActionBarSubtitle(null);
       List<String> list = getStringArrayForSpinner();
        final Spinner sp_sub = (Spinner) view.findViewById(R.id.spinner_sub);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1, list);
        sp_sub.setAdapter(adapter);
        //
        Button button = (Button) view.findViewById(R.id.btnAddClass);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                db = new Database(getActivity());
                String addClassID = ((EditText)view.findViewById(R.id.addclass_classID)).getText()+"";
                String temp = sp_sub.getSelectedItem().toString();
                StringTokenizer st = new StringTokenizer(temp, "-");
                String subjectID = st.nextToken().trim();
                //  Toast.makeText(getActivity(), db.ERROR_REASON, Toast.LENGTH_LONG).show();
                String msg = db.insertClass(subjectID, Integer.parseInt(addClassID));
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                db.close();
            }
        });
        return view;
    }

    private List<String> getStringArrayForSpinner(){
        db = new Database(getActivity());
        Cursor c = db.loadSubjectList();
        c.moveToFirst();
        List<String> data = new ArrayList<String>();
        while (c.isAfterLast() == false){
            data.add(c.getString(0) + " - " + c.getString(1));
            c.moveToNext();
        }
        c.close();
        db.close();
        return data;
    }

}
