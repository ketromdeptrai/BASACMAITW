package samsung.android.test.fragment;


import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ketromdeptrai.database.Database;
import com.google.zxing.client.android.R;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import samsung.android.test.MainActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTable extends Fragment {
    private static final int PICKFILE_REQUEST_CODE = 1;
    Database db = null;
    List<String> listClass = new ArrayList<String>();
    List<String> listDate = new ArrayList<String>();
    ArrayAdapter adapter_class, adapter_date;
    String classID, subjectID, date;
    Spinner sp_date;

    public FragmentTable() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_table, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Statistics");
        ((MainActivity) getActivity()).setActionBarSubtitle(null);
        List<String> listSubject = getStringArrayForSpinnerSubject();
        final Spinner sp_subject = (Spinner) v.findViewById(R.id.sub);
        final Spinner sp_class = (Spinner) v.findViewById(R.id.grade);
        ArrayAdapter adapter_subject = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listSubject);
        adapter_class = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listClass);
        sp_class.setAdapter(adapter_class);
        sp_subject.setAdapter(adapter_subject);
        sp_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String temp = sp_subject.getSelectedItem().toString();
                StringTokenizer st = new StringTokenizer(temp, "-");
                subjectID = st.nextToken().trim();
                //  Toast.makeText(getActivity(), subjectID, Toast.LENGTH_LONG).show();
                //  listClass.clear();
                listClass = getStringArrayForSpinnerClass(subjectID);
                adapter_class = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listClass);
                sp_class.setAdapter(adapter_class);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sp_date = (Spinner) v.findViewById(R.id.day);
        sp_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classID = sp_class.getSelectedItem().toString().trim();
                listDate = getStringArrayForDateClass(classID);
                adapter_date = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, listDate);
                sp_date.setAdapter(adapter_date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn_tk = (Button) v.findViewById(R.id.btn_tk);
        btn_tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date = sp_date.getSelectedItem().toString().trim();
                FragmentAnalytics fr_tk = new FragmentAnalytics(subjectID, classID, date);
                FragmentTransaction frag = getFragmentManager().beginTransaction();
                frag.replace(R.id.frame, fr_tk, "fragment_analytics");
                frag.addToBackStack("Fragment_blank");
                frag.commit();
            }
        });
        Button btn_export = (Button) v.findViewById(R.id.btn_export);
        btn_export.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    /*Intent intent = new Intent(getActivity(), DirectoryChooserActivity.class);
                    *//*intent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                    intent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                    intent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
                    //  intent.setType("file*//**//*");
                    intent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());*//*

                    startActivityForResult(intent, PICKFILE_REQUEST_CODE);*/
                    writeXls();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e + "", Toast.LENGTH_LONG).show();
                }

            }
        });
        return v;
    }


    private List<String> getStringArrayForSpinnerSubject() {
        db = new Database(getActivity());
        Cursor c = db.loadSubjectList();
        c.moveToFirst();
        List<String> data = new ArrayList<String>();
        while (c.isAfterLast() == false) {
            data.add(c.getString(0) + " - " + c.getString(1));
            c.moveToNext();
        }
        c.close();
        db.close();
        return data;
    }

    private List<String> getStringArrayForSpinnerClass(String subjectID) {
        List<String> data = new ArrayList<String>();
        try {
            db = new Database(getActivity());
            Cursor c = db.loadClassList(subjectID);
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                data.add((c.getInt(0)) + "");
                // Toast.makeText(getActivity(), (c.getInt(0))+"", Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
            c.close();
            db.close();
        } catch (Exception exception) {
            Toast.makeText(getActivity(), exception + "", Toast.LENGTH_LONG).show();
        } finally {
            return data;
        }
    }

    private List<String> getStringArrayForDateClass(String classID) {
        List<String> data = new ArrayList<String>();
        try {
            db = new Database(getActivity());
            Cursor c = db.loadAttendTimesList(classID);
            c.moveToFirst();
            data.add("All");
            while (c.isAfterLast() == false) {
                data.add(swapDateAndYear(c.getString(1)));
                // Toast.makeText(getActivity(), (c.getInt(0))+"", Toast.LENGTH_LONG).show();
                c.moveToNext();
            }
            c.close();
            db.close();
        } catch (Exception exception) {
            Toast.makeText(getActivity(), exception + "", Toast.LENGTH_LONG).show();
        } finally {
            return data;
        }
    }

    private String swapDateAndYear(String temp) {
        StringTokenizer st = new StringTokenizer(temp, "/");
        String date = st.nextToken();
        String month = st.nextElement().toString();
        String year = st.nextElement().toString();
        return (year + "/" + month + "/" + date);
    }

    private void loadRow(Sheet sheet, int colCount) {
        db = new Database(getActivity());
        Cursor c = db.loadStudentList(classID);
        c.moveToFirst();
        int i = 1;
        while (c.isAfterLast() == false) {
            Row row = sheet.createRow(i);
            String studentID = c.getString(0);
            String studentName = db.loadStudent(Integer.parseInt(studentID));
            Cell cell = row.createCell(0);
            cell.setCellValue(i + "");
            cell = row.createCell(1);
            cell.setCellValue(studentID);
            cell = row.createCell(2);
            cell.setCellValue(studentName);
            for (int j = 3; j < (colCount - 1); j++) {
                cell = row.createCell(j);
                String date_attend = sp_date.getItemAtPosition(j - 2).toString();
                String check = (db.checkStudentAbsent(classID, studentID, swapDateAndYear(date_attend))) ? "x" : "";
                cell.setCellValue(check);
            }
            cell = row.createCell(colCount - 1);
            int absent = db.calculateAbsentTimes(studentID, classID);
            cell.setCellValue(absent + "");
            i++;
            c.moveToNext();
            //int absent = db.calculateAbsentTimes(studentID,classID);
        }
        c.close();
        db.close();
    }

    private void writeXls() {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        //Title
        Row row = sheet.createRow(0);
        int colCount = sp_date.getCount() - 1 + 4;
        Cell cell = row.createCell(0);
        cell.setCellValue("id");
        cell = row.createCell(1);
        cell.setCellValue("studentID");
        cell = row.createCell(2);
        cell.setCellValue("studentName");
        for (int i = 3; i < (colCount - 1); i++) {
            cell = row.createCell(i);
            cell.setCellValue(sp_date.getItemAtPosition(i - 2).toString());
        }
        cell = row.createCell(colCount - 1);
        cell.setCellValue("absentTimes");
        loadRow(sheet, colCount);
        String path = "/storage/sdcard1/" + subjectID + "_" + classID + ".xls";
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
            Toast.makeText(getActivity(), "Data saved in " + path, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            path = "/storage/emulated/0/" + subjectID + "_" + classID + ".xls";
            try (FileOutputStream outputStream = new FileOutputStream(path)) {
                wb.write(outputStream);
                outputStream.flush();
                outputStream.close();
                Toast.makeText(getActivity(), "Data saved in " + path, Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            new MediaScannerHelper().addFile(path);
        }
    }

    /*public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        try {
            if (requestCode == PICKFILE_REQUEST_CODE) {

            }

        } catch (Exception e) {
            Toast.makeText(getActivity(), e + "", Toast.LENGTH_LONG).show();
        }
    }*/

    public class MediaScannerHelper implements MediaScannerConnection.MediaScannerConnectionClient {

        public void addFile(String filename)
        {
            String [] paths = new String[1];
            paths[0] = filename;
            MediaScannerConnection.scanFile(getActivity(), paths, null, this);
        }

        public void onMediaScannerConnected() {
        }

        public void onScanCompleted(String path, Uri uri) {
        }
    }

}
