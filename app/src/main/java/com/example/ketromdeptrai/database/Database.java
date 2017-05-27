package com.example.ketromdeptrai.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ketromdeptrai on 4/18/2017.
 */

public class Database extends SQLiteOpenHelper{
    public String ERROR_REASON = "";
    private static final String DATABASE_NAME="attendance.db";
    private static final int SCHEMA_VERSION=1;
    public Database(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String absent = "CREATE TABLE IF NOT EXISTS absent ("+
                "studentID INTEGER NOT NULL, "+
                "classID INTEGER NOT NULL, "+
                "absentTimes INTEGER NOT NULL DEFAULT 0, "+
                "PRIMARY KEY(studentID, classID))";
        String attend_times = "CREATE TABLE IF NOT EXISTS attend_times (" +
                "classID INTEGER NOT NULL," +
                "attendanceDate TEXT NOT NULL," +
                "PRIMARY KEY(classID, attendanceDate))";
        String attendance_list = "CREATE TABLE IF NOT EXISTS attendance_list(" +
                "classID INTEGER NOT NULL, " +
                "studentID INTEGER NOT NULL, " +
                "attendanceDate TEXT NOT NULL, " +
                "PRIMARY KEY(classID, studentID, attendanceDate))";
        String tbclass = "CREATE TABLE IF NOT EXISTS class (" +
                "classID INTEGER NOT NULL, " +
                "subjectID TEXT NOT NULL, " +
                "PRIMARY KEY(classID))";
        String student = "CREATE TABLE IF NOT EXISTS student (" +
                "studentID INTEGER NOT NULL, " +
                "studentName TEXT NOT NULL, " +
                "PRIMARY KEY(studentID))";
        String subject = "CREATE TABLE IF NOT EXISTS subject (" +
                "subjectID TEXT NOT NULL, " +
                "subjectName TEXT NOT NULL, " +
                "PRIMARY KEY(subjectID))";
        try {
            db.execSQL(absent);
            db.execSQL(attend_times);
            db.execSQL(attendance_list);
            db.execSQL(tbclass);
            db.execSQL(student);
            db.execSQL(subject);
        } catch (SQLException exception) {
        //    Toast.makeText(this, "Create table false", Toast.LENGTH_LONG).show();
            ERROR_REASON = "Create Table False";
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public synchronized void close() {
        super.close();
    }

    public String insertSubject(String subjectID, String subjectName){
        ContentValues values=new ContentValues();
        values.put("subjectID", subjectID);
        values.put("subjectName", subjectName);
        String msg="";
        if(getWritableDatabase().insertWithOnConflict("subject", null, values, SQLiteDatabase.CONFLICT_REPLACE)==-1){
            msg="Failed to insert subject";
        }
        else{
            msg="Insert subject is successful";
        }
     //   Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        ERROR_REASON = msg;
        return msg;
    };
    public String insertClass(String subjectID, int classID){
        ContentValues values=new ContentValues();
        values.put("subjectID", subjectID);
        values.put("classID", classID);
        String msg="";
        if(getWritableDatabase().insert("class", null, values)==-1){
            msg="Failed to insert class";
        }
        else{
            msg="Insert class is successful";
        }
    //    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        ERROR_REASON = msg;
        return msg;
    };
    public String insertStudent(int studentID, String studentName){
        ContentValues values=new ContentValues();
        values.put("studentID", studentID);
        values.put("studentName", studentName);
        String msg="";
        if(getWritableDatabase().insertWithOnConflict("student", null, values, SQLiteDatabase.CONFLICT_REPLACE)==-1){
            msg="Failed to insert student";
        }
        else{
            msg="Insert student is successful";
        }
    //    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        ERROR_REASON = msg;
        return msg;
    };
    public String insertStudentToList(int studentID, int classID, int absentTimes){
        ContentValues values=new ContentValues();
        values.put("studentID", studentID);
        values.put("classID", classID);
        values.put("absentTimes", absentTimes);
        String msg="";
        if(getWritableDatabase().insertWithOnConflict("absent", null, values, SQLiteDatabase.CONFLICT_REPLACE)==-1){
            msg="Failed to insert student to list";
        }
        else{
            msg="Insert student to list is successful";
        }
   //     Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        ERROR_REASON = msg;
        return msg;
    };
    public void insertAttendTimes(int classID, String attendanceDate){
        ContentValues values=new ContentValues();
        values.put("classID", classID);
        values.put("attendanceDate", attendanceDate);
        String msg="";
        if(getWritableDatabase().insertWithOnConflict("attend_times", null, values, SQLiteDatabase.CONFLICT_REPLACE)==-1){
            msg="Failed to insert AttendTimes";
        }
        else{
            msg="Insert AttendTimes is successful";
        }
   //     Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        ERROR_REASON = msg;
    };
    public boolean insertAttendanceList(int classID, int studentID, String attendanceDate) {
        ContentValues values=new ContentValues();
        values.put("attendanceDate", attendanceDate);
        values.put("studentID", studentID);
        values.put("classID", classID);
        if(getWritableDatabase().insert("attendance_list", null, values)==-1){
            ERROR_REASON ="failed";
            return false;

        }
        else{
            ERROR_REASON ="insert attendance_list is successful";
            return true;
        }
    //    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();

    }

    public Cursor loadSubjectList(){
        String table = "subject";
        String[] columns = {"subjectID", "subjectName"};
        String selection = null;//"column3 =?";
        String[] selectionArgs = null;//{"apple"};
        String groupBy = null;
        String having = null;
        String orderBy = "subjectID ASC";
        String limit = null;
        return getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }


    public Cursor loadClassList(String subjectID){
        String table = "class";
        String[] columns = {"classID", "subjectID"};
        String selection = "subjectID =?";
        String[] selectionArgs = {subjectID};
        String groupBy = null;
        String having = null;
        String orderBy = "classID ASC";
        String limit = null;
        return getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor loadAttendTimesList(String classID){
        String table = "attend_times";
        String[] columns = {"classID", "attendanceDate"};
        String selection = "classID =?";
        String[] selectionArgs = {classID};
        String groupBy = null;
        String having = null;
        String orderBy = "attendanceDate ASC";
        String limit = null;
        return getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor checkStudentAvailable(int studentID, int classID){
        String table = "absent";
        String[] columns = {"studentID", "classID", "absentTimes"};
        String selection = "studentID =? AND classID =?";
        String[] selectionArgs = {studentID+"",classID+""};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        return getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public String loadStudent(int studentID){
        String table = "student";
        String[] columns = {"studentID", "studentName"};
        String selection = "studentID =?";
        String[] selectionArgs = {studentID+""};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        Cursor c = getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (c!= null && c.moveToFirst() ) {
            return c.getString(1);
        } else return "";
    }

    public boolean checkStudentAbsent(String classID, String studentID,String attendanceDate){
        String table = "attendance_list";
        String[] columns = {"classID", "studentID", "attendanceDate"};
        String selection = "classID =? AND studentID =? AND attendanceDate =?";
        String[] selectionArgs = {classID, studentID, attendanceDate};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        Cursor c = getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        if (c==null || c.getCount() <= 0){
            return true;
        } else return false;
    }

    public Cursor loadStudentList(String classID){
        String table = "absent";
        String[] columns = {"studentID", "classID", "absentTimes"};
        String selection = "classID =?";
        String[] selectionArgs = {classID};
        String groupBy = null;
        String having = null;
        String orderBy = "studentID ASC";
        String limit = null;
        return getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public int calculateAbsentTimes(String studentID, String classID){
        Cursor c1 = loadAttendTimesList(classID);
        String table = "attendance_list";
        String[] columns = {"classID", "studentID", "attendanceDate"};
        String selection = "classID =? AND studentID =?";
        String[] selectionArgs = {classID, studentID};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        Cursor c = getReadableDatabase().query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        return c1.getCount() - c.getCount();
    }

    public void clearClass(String classID){
        try {
            String whereClause = "classID =?";
            String[] whereArgs = {classID};
            getWritableDatabase().delete("absent", whereClause, whereArgs);
            getWritableDatabase().delete("attend_times", whereClause, whereArgs);
            getWritableDatabase().delete("attendance_list", whereClause, whereArgs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
