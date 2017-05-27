package com.example.ketromdeptrai.database;

/**
 * Created by ketromdeptrai on 4/29/2017.
 */

public class Analytics {
    private int id;
    private String studentID;
    private String studentName;
    private String col4;

    public Analytics(){

    }
    public Analytics(int id, String studentID, String studentName, String col4){
        this.id = id;
        this.studentID = studentID;
        this.studentName = studentName;
        this.col4 = col4;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getCol4() {
        return col4;
    }

    public void setCol4(String col4) {
        this.col4 = col4;
    }

}

