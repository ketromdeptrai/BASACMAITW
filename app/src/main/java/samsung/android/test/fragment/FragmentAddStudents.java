package samsung.android.test.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class FragmentAddStudents extends Fragment {
  //  String[] arr_class;
  //  String[] arr_subject;
    Database db = null;
    List<String> listClass = new ArrayList<String>();
    ArrayAdapter adapter_class;
    public FragmentAddStudents() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_addstudents, container, false);
        //arr_class = getResources().getStringArray(R.array.string_array_class);
        ((MainActivity) getActivity()).setActionBarTitle("Add Student");
        ((MainActivity) getActivity()).setActionBarSubtitle(null);

      //  arr_subject = getResources().getStringArray(R.array.string_array_subject);
        List<String> listSubject = getStringArrayForSpinnerSubject();
        final Spinner sp_subject = (Spinner)view.findViewById(R.id.spinnersub);
        final Spinner sp_class = (Spinner)view.findViewById(R.id.spinner_class2);
        ArrayAdapter adapter_subject = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,listSubject);
        adapter_class= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,listClass);
        sp_class.setAdapter(adapter_class);
        sp_subject.setAdapter(adapter_subject);
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = sp_subject.getSelectedItem().toString();
                StringTokenizer st = new StringTokenizer(temp, "-");
                String subjectID = st.nextToken().trim();
              //  Toast.makeText(getActivity(), subjectID, Toast.LENGTH_LONG).show();
              //  listClass.clear();
                listClass = getStringArrayForSpinnerClass(subjectID);
                adapter_class= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,listClass);
                sp_class.setAdapter(adapter_class);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button button = (Button) view.findViewById(R.id.btnAddStudent);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                db = new Database(getActivity());
                String addStudentID = (((EditText)view.findViewById(R.id.addstudent_studentID)).getText()+"").trim();
                String addStudentName = (((EditText)view.findViewById(R.id.addstudent_studentName)).getText()+"").trim();
                String classID = sp_class.getSelectedItem().toString().trim();
                String msg = db.insertStudent(Integer.parseInt(addStudentID), addStudentName);
                //  Toast.makeText(getActivity(), db.ERROR_REASON, Toast.LENGTH_LONG).show();
               // String msg = db.insertClass(subjectID, Integer.parseInt(addClassID));
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                msg = db.insertStudentToList(Integer.parseInt(addStudentID),Integer.parseInt(classID), 0);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                db.close();
            }
        });
        return view;
    }

    private List<String> getStringArrayForSpinnerSubject(){
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

    private  List<String> getStringArrayForSpinnerClass (String subjectID){
        List<String> data = new ArrayList<String>();
        try{db = new Database(getActivity());
        Cursor c = db.loadClassList(subjectID);
        c.moveToFirst();
        while (c.isAfterLast() == false){
            data.add((c.getInt(0))+"");
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
