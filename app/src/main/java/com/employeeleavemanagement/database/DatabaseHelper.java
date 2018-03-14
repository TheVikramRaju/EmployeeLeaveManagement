package com.employeeleavemanagement.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.employeeleavemanagement.model.Employee;
import com.employeeleavemanagement.model.Leave;

import java.util.ArrayList;

/**
 * Created by Vikram on 8/20/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "emp-leave-management";

    // Table Names
    private static final String TABLE_EMPLOYEE = "tbl_employee";
    private static final String TABLE_LEAVES = "tbl_leaves";


    // TABLE_EMPLOYEE Table - column names
    private static final String KEY_EMP_ID = "empID";
    private static final String KEY_EMP_NAME = "name";
    private static final String KEY_EMP_IMAGE = "image_url";
    private static final String KEY_EMP_AGE = "age";
    private static final String KEY_EMP_GENDER = "gender";
    private static final String KEY_EMP_DESIGNATION = "designation";

    // TABLE_LEAVES Table - column names
    private static final String KEY_LEAVE_ID = "leaveID";
    private static final String KEY_LEAVE_FROM = "leave_from";
    private static final String KEY_SESSION_FROM = "session_from";
    private static final String KEY_LEAVE_TO = "leave_to";
    private static final String KEY_SESSION_TO = "session_to";

    //Employee Table creation query
    private static final String CREATE_TABLE_EMPLOYEE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_EMPLOYEE + "(" + KEY_EMP_ID + " INTEGER PRIMARY KEY, "
            + KEY_EMP_NAME + " TEXT, "
            + KEY_EMP_IMAGE + " TEXT, "
            + KEY_EMP_AGE + " INTEGER, "
            + KEY_EMP_GENDER + " TEXT, "
            + KEY_EMP_DESIGNATION + " TEXT" + ")";

    //Leave Table creation query
    private static final String CREATE_TABLE_LEAVE = "CREATE TABLE IF NOT EXISTS "
            + TABLE_LEAVES + "(" + KEY_LEAVE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_EMP_ID + " INTEGER, "
            + KEY_LEAVE_FROM + " TEXT, "
            + KEY_SESSION_FROM + " INTEGER, "
            + KEY_LEAVE_TO + " TEXT, "
            + KEY_SESSION_TO + " INTEGER" + ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_EMPLOYEE);
        db.execSQL(CREATE_TABLE_LEAVE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LEAVES);
        // create new tables
        onCreate(db);
    }


    public long addEmployee(Employee employeeData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMP_NAME, employeeData.getEmpName());
        values.put(KEY_EMP_IMAGE, employeeData.getEmpImage());
        values.put(KEY_EMP_AGE, employeeData.getEmpAge());
        values.put(KEY_EMP_GENDER, employeeData.getEmpGender());
        values.put(KEY_EMP_DESIGNATION, employeeData.getEmpDesignation());

        // insert row
        long result = db.insert(TABLE_EMPLOYEE, null, values);

        return result;
    }

    public long addEmployeeList(ArrayList<Employee> employeesArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        for (int i = 0; i < employeesArrayList.size(); i++) {
            Employee employeeData = employeesArrayList.get(i);

            ContentValues values = new ContentValues();
            values.put(KEY_EMP_ID, employeeData.getEmpID());
            values.put(KEY_EMP_NAME, employeeData.getEmpName());
            values.put(KEY_EMP_IMAGE, employeeData.getEmpImage());
            values.put(KEY_EMP_AGE, employeeData.getEmpAge());
            values.put(KEY_EMP_GENDER, employeeData.getEmpGender());
            values.put(KEY_EMP_DESIGNATION, employeeData.getEmpDesignation());

            // insert row
            result = db.insert(TABLE_EMPLOYEE, null, values);
        }

        return result;
    }


    public ArrayList<Employee> getEmployeeData() {
        ArrayList<Employee> data = new ArrayList<Employee>();
        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Employee empData = new Employee();
                empData.setEmpID(c.getInt((c.getColumnIndex(KEY_EMP_ID))));
                empData.setEmpName((c.getString(c.getColumnIndex(KEY_EMP_NAME))));
                empData.setEmpImage((c.getString(c.getColumnIndex(KEY_EMP_IMAGE))));
                empData.setEmpAge((c.getInt(c.getColumnIndex(KEY_EMP_AGE))));
                empData.setEmpGender((c.getString(c.getColumnIndex(KEY_EMP_GENDER))));
                empData.setEmpDesignation((c.getString(c.getColumnIndex(KEY_EMP_DESIGNATION))));
                data.add(empData);
            } while (c.moveToNext());
        }
        c.close();
        return data;
    }

    public long addLeaves(Leave leaveData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EMP_ID, leaveData.getEmpID());
        values.put(KEY_LEAVE_FROM, leaveData.getLeaveFrom());
        values.put(KEY_SESSION_FROM, leaveData.getSessionFrom());
        values.put(KEY_LEAVE_TO, leaveData.getLeaveTo());
        values.put(KEY_SESSION_TO, leaveData.getSessionTo());

        // insert row
        long result = db.insert(TABLE_LEAVES, null, values);

        return result;
    }

    public long addLeavesList(ArrayList<Leave> leaveArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = 0;
        for (int i = 0; i < leaveArrayList.size(); i++) {
            Leave leaveData = leaveArrayList.get(i);

            ContentValues values = new ContentValues();
            values.put(KEY_EMP_ID, leaveData.getEmpID());
            values.put(KEY_LEAVE_FROM, leaveData.getLeaveFrom());
            values.put(KEY_SESSION_FROM, leaveData.getSessionFrom());
            values.put(KEY_LEAVE_TO, leaveData.getLeaveTo());
            values.put(KEY_SESSION_TO, leaveData.getSessionTo());
            // insert row
            result = db.insert(TABLE_LEAVES, null, values);
        }
        return result;
    }


    public ArrayList<Leave> getLeaveData(int employeeId) {
        ArrayList<Leave> data = new ArrayList<Leave>();
        String selectQuery = "SELECT  * FROM " + TABLE_LEAVES + " WHERE " + KEY_EMP_ID + " = " + employeeId;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Leave leaveData = new Leave();
                leaveData.setLeaveID(c.getInt((c.getColumnIndex(KEY_LEAVE_ID))));
                leaveData.setEmpID(c.getInt((c.getColumnIndex(KEY_EMP_ID))));
                leaveData.setLeaveFrom((c.getString(c.getColumnIndex(KEY_LEAVE_FROM))));
                leaveData.setSessionFrom((c.getInt(c.getColumnIndex(KEY_SESSION_FROM))));
                leaveData.setLeaveTo((c.getString(c.getColumnIndex(KEY_LEAVE_TO))));
                leaveData.setSessionTo((c.getInt(c.getColumnIndex(KEY_SESSION_TO))));

                data.add(leaveData);
            } while (c.moveToNext());
        }
        c.close();
        return data;
    }


    public void deleteEmployee() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_EMPLOYEE, null, null);
    }

    public void deleteLeaves() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_LEAVES, null, null);
    }

    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
