package samsung.android.test.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ketromdeptrai.database.Database;
import com.example.ketromdeptrai.database.QustomDialogBuilder;
import com.google.zxing.client.android.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import samsung.android.test.MainActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCamera extends Fragment {
    Button btn_diemdanh;
    List<String> listClass = new ArrayList<String>();
    ArrayAdapter adapter_class;
    Database db = null;
    String classID,tim;
    public FragmentCamera() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_camera, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Check Attendance");
        ((MainActivity) getActivity()).setActionBarSubtitle(null);
        btn_diemdanh = (Button)v.findViewById(R.id.btn_diemdanh);
        String expectedPattern = "yyyy/MM/dd";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern);
        Date time = new Date();
        tim = formatter.format(time);
        try {
            loadSpinner(v);
        }catch (Exception e){
            Toast.makeText(getActivity(), e+"", Toast.LENGTH_LONG).show();
        }
        btn_diemdanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Database db = new Database(getActivity());
                 //   Toast.makeText(getActivity(), classID + " " +tim, Toast.LENGTH_LONG).show();
                    db.insertAttendTimes(Integer.parseInt(classID), tim);
                    db.close();
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    startActivityForResult(intent, 0);
                }catch (Exception e){
                    Toast.makeText(getActivity(), e+"", Toast.LENGTH_LONG).show();
                }
            }
        });

        return v;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            if (requestCode == 0) {
                if (resultCode == RESULT_OK) {
                    String studentID = intent.getStringExtra("SCAN_RESULT");
                    Database db = new Database(getActivity());
                    String studentName = db.loadStudent(Integer.parseInt(studentID));
                    Cursor c = db.checkStudentAvailable(Integer.parseInt(studentID), Integer.parseInt(classID));
                    c.moveToFirst();
                    QustomDialogBuilder builder = new QustomDialogBuilder(getActivity());
                    builder.setTitleColor("#1abc9c");
                    builder.setDividerColor("#1abc9c");
                    if (c.getCount() <= 0) {//student not available
                        builder.setTitle("ERROR!!!");
                        builder.setMessage(studentName + " (" + studentID + ") is not in this class");
                    } else {
                        boolean boo = db.insertAttendanceList(Integer.parseInt(classID), Integer.parseInt(studentID), tim);
                        if (boo) {
                            builder.setTitle("Success!!!");
                            builder.setMessage(studentName + " (" + studentID + ") has successfully attended");
                            int absentTimes = c.getInt(2);
                            db.insertStudentToList(Integer.parseInt(studentID), Integer.parseInt(classID),(absentTimes+1));
                        } else {
                            builder.setTitle("ERROR!!!");
                            builder.setMessage(studentName + " (" + studentID + ") has already attended today");
                        }
                    }
                    builder.setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btn_diemdanh.performClick();
                        }
                    });
                    c.close();
                    db.close();

                    builder.show();
                    // Handle successful scan
                } else if (resultCode == RESULT_CANCELED) {

                    //Toast.makeText(getActivity(),"Back lại làm méo gì",Toast.LENGTH_LONG).show();
                }
            }
        }catch(NumberFormatException e){
            QustomDialogBuilder builder = new QustomDialogBuilder(getActivity());
            builder.setTitleColor("#1abc9c");
            builder.setDividerColor("#1abc9c");
            builder.setTitle("ERROR!!!");
            builder.setMessage("This is not a barcode of student card");
            builder.setNegativeButton("QUIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    btn_diemdanh.performClick();
                }
            });
            /*AlertDialog dialog = builder.create();
            Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            //Set negative button background
            nbutton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background));
            //Set negative button text color
           // nbutton.setTextColor(Color.YELLOW);
            Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            //Set positive button background
            pbutton.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.background));
            //Set positive button text color
           // pbutton.setTextColor(Color.MAGENTA);
            dialog.show();*/
            builder.show();
        }
        catch(Exception e){
            Toast.makeText(getActivity(), e+"", Toast.LENGTH_LONG).show();
        }
    }

    private void loadSpinner(View view){
        List<String> listSubject = getStringArrayForSpinnerSubject();
        final Spinner sp_subject = (Spinner)view.findViewById(R.id.sp_subject);
        final Spinner sp_class = (Spinner)view.findViewById(R.id.sp_class);
        ArrayAdapter adapter_subject = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,listSubject);
       // adapter_class= new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,listClass);
        sp_subject.setAdapter(adapter_subject);
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = sp_subject.getSelectedItem().toString();
                StringTokenizer st = new StringTokenizer(temp, "-");
                String subjectID = st.nextToken().trim();
                //  Toast.makeText(getActivity(), subjectID, Toast.LENGTH_LONG).show();
                //  listClass.clear();
                try {
                    listClass = getStringArrayForSpinnerClass(subjectID);
                    adapter_class = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listClass);
                    sp_class.setAdapter(adapter_class);
                    classID = sp_class.getSelectedItem().toString();
                }catch (Exception e){
                    Toast.makeText(getActivity(), e+"", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
