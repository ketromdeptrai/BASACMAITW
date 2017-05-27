package samsung.android.test.fragment;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ketromdeptrai.database.Database;
import com.google.zxing.client.android.R;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.StringTokenizer;

import samsung.android.test.MainActivity;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAddXls extends Fragment {
    private static final int REQUEST_PATH = 1;
    String curFilePath, curFileName, subjectID, classID;
    String curFileExtension;
    EditText edittext;


    public FragmentAddXls() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_xls, container, false);
        ((MainActivity) getActivity()).setActionBarTitle("Import Database");
        ((MainActivity) getActivity()).setActionBarSubtitle(null);
        Button btn_nhap = (Button) v.findViewById(R.id.btn_nhap_csv);
        edittext = (EditText) v.findViewById(R.id.editText);
        Button btn_chon = (Button) v.findViewById(R.id.btn_chon);

        btn_chon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent1 = new Intent(getActivity(), FileChooserActivity.class);
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("gagt/sdf");

                try {
                    startActivityForResult(intent1, REQUEST_PATH);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No activity can handle picking a file. Showing alternatives.", Toast.LENGTH_LONG).show();
                }
            }

        });


        btn_nhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputStream inStream;
                Workbook wb = null;
                int colStudentID = -1, colStudentName = -1, colAbsentTimes = -1;
                try {
                    if (curFileExtension.equalsIgnoreCase("xls")) {
                        Database db = new Database(getActivity());
                        db.clearClass(classID);
                        addClassFromFileName(curFileName);
                        inStream = new FileInputStream(curFilePath);
                        wb = new HSSFWorkbook(inStream);
                        Sheet ws = wb.getSheetAt(0);
                        int rowNum = ws.getLastRowNum() + 1;
                        int colNum = ws.getRow(0).getLastCellNum();
                        for (int i = 0; i < colNum; i++) {
                            Row row = ws.getRow(0);
                            Cell cell = row.getCell(i);
                            switch (cell.toString()) {
                                case "studentID":
                                    colStudentID = i;
                                    break;
                                case "studentName":
                                    colStudentName = i;
                                    break;
                                case "absentTimes":
                                    colAbsentTimes = i;
                                    break;
                                default:
                                    try {

                                       // String msg = db.ERROR_REASON;

                                        db.insertAttendTimes(Integer.parseInt(classID), swapDateAndYear(cell.toString()));

                                        break;
                                    } catch (Exception e) {
                                     //   Toast.makeText(getActivity(), e + "", Toast.LENGTH_LONG).show();
                                    }
                            }

                        }
                        db.close();
                        addStudent(ws, rowNum, colStudentID, colStudentName, colAbsentTimes);
                        inStream.close();
                        Toast.makeText(getActivity(), "Successful", Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getActivity(), "Input file must be *.xls", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                  //  Toast.makeText(getActivity(), e + "", Toast.LENGTH_LONG).show();
                }

            }
        });
        return v;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        if (requestCode == REQUEST_PATH) {
            if (resultCode == RESULT_OK) {
                curFilePath = data.getData().getPath();
                curFileName = curFilePath.substring(curFilePath.lastIndexOf("/") + 1);
                //  curFileName = data.getStringExtra("GetPath");
                curFileExtension = curFileName.substring(curFileName.lastIndexOf(".") + 1);
             //   Toast.makeText(getActivity(), curFileExtension, Toast.LENGTH_LONG).show();
                edittext.setText(curFileName);
            }
        }
    }

    private void addClassFromFileName(String fileName) { //FileNameFormat: subjectID_classID.xls
        Database db = new Database(getActivity());
        StringTokenizer st = new StringTokenizer(fileName, "_.");
        subjectID = st.nextToken().trim();
        classID = st.nextToken().trim();
           Toast.makeText(getActivity(), subjectID, Toast.LENGTH_LONG).show();
           Toast.makeText(getActivity(), classID, Toast.LENGTH_LONG).show();
        db.insertSubject(subjectID, " ");
        String msg = db.insertClass(subjectID, Integer.parseInt(classID));
        db.close();
    }

    private String swapDateAndYear(String temp) {
        StringTokenizer st = new StringTokenizer(temp, "/");
        String date = st.nextToken();
        String month = st.nextElement().toString();
        String year = st.nextElement().toString();
        return (year + "/" + month + "/" + date);
    }

    private void addStudent(Sheet ws, int rowNum, int colStudentID, int colStudentName, int colAbsentTimes) {
        Database db = new Database(getActivity());
        for (int i = 1; i < rowNum; i++) {
            Row row = ws.getRow(i);
            String studentID = (new DataFormatter().formatCellValue(row.getCell(colStudentID))).toString().trim();
       //     Toast.makeText(getActivity(), studentID, Toast.LENGTH_LONG).show();
            String studentName = row.getCell(colStudentName).toString().trim();
            String absentTimes = (new DataFormatter().formatCellValue(row.getCell(colAbsentTimes))).toString().trim();
            db.insertStudentToList(Integer.parseInt(studentID), Integer.parseInt(classID), Integer.parseInt(absentTimes));
            db.insertStudent(Integer.parseInt(studentID), studentName);
            for (int j = (colStudentName + 1); j < colAbsentTimes; j++) {
                if ((row.getCell(j) == null || row.getCell(j).getCellType() == Cell.CELL_TYPE_BLANK)) {
                    db.insertAttendanceList(Integer.parseInt(classID), Integer.parseInt(studentID), swapDateAndYear(ws.getRow(0).getCell(j).toString().trim()));
                    // Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
               //     Toast.makeText(getActivity(), swapDateAndYear(ws.getRow(0).getCell(j).toString()), Toast.LENGTH_LONG).show();
                }
            }
        }
        db.close();

    }
}