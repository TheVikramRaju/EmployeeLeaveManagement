package com.employeeleavemanagement.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.employeeleavemanagement.R;
import com.employeeleavemanagement.Utils;
import com.employeeleavemanagement.database.DatabaseHelper;
import com.employeeleavemanagement.model.Employee;
import com.employeeleavemanagement.model.Leave;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddLeaveActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    Employee currentEmployee;
    ImageView empImage;
    TextView empName, empDesignation, leaveCount;
    TextInputEditText fromDate, toDate;
    Context context;
    Spinner sessionFrom, sessionTo;
    Button btnSave;

    DatabaseHelper dbHelper;
    Calendar date = Calendar.getInstance();
    private DatePickerDialog picker;
    boolean isValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leave);
        context = this;
        dbHelper = new DatabaseHelper(getApplicationContext());
        currentEmployee = Utils.utilsEmployee;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.add_leave_title);
        initUI();
    }

    private void initUI() {
        empImage = findViewById(R.id.emp_image);
        empName = findViewById(R.id.emp_name);
        empDesignation = findViewById(R.id.emp_designation);
        leaveCount = findViewById(R.id.number_of_days);
        fromDate = findViewById(R.id.dateFromInput);
        toDate = findViewById(R.id.dateToInput);
        btnSave = findViewById(R.id.btn_submit);

        sessionFrom = findViewById(R.id.from_spinner);
        sessionTo = findViewById(R.id.to_spinner);
        loadDate();
        setOnClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Glide.with(context).load(currentEmployee.getEmpImage()).into(empImage);
        empName.setText(currentEmployee.getEmpName());
        empDesignation.setText(currentEmployee.getEmpDesignation());
    }

    private void loadDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.date_format));
        String today = dateFormat.format(date);
        fromDate.setText(today);
        toDate.setText(today);
    }

    private void setOnClickListener() {
        fromDate.setOnClickListener(this);
        toDate.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        sessionFrom.setOnItemSelectedListener(this);
        sessionTo.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dateFromInput:
                ShowCalender(getString(R.string.startdate));
                break;
            case R.id.dateToInput:
                ShowCalender(getString(R.string.enddate));
                break;
            case R.id.btn_submit:
                Leave leave = new Leave();
                leave.setEmpID(currentEmployee.getEmpID());
                leave.setLeaveFrom(fromDate.getText().toString());
                leave.setSessionFrom(sessionFrom.getSelectedItemPosition());
                leave.setLeaveTo(toDate.getText().toString());
                leave.setSessionTo(sessionTo.getSelectedItemPosition());

                long result = dbHelper.addLeaves(leave);

                if (result > 0) {
                    Toast.makeText(context, R.string.add_leave_success, Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                break;
        }
    }

    private void ShowCalender(String name) {
        //name = name;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        if (name.equals(getString(R.string.enddate))) {
            calendar = date;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
        }
        final String finalName = name;

        picker = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (finalName.equals(getString(R.string.startdate))) {
                            String date_1 = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            fromDate.setText(date_1);
                            date.set(year, monthOfYear, dayOfMonth);
                            toDate.setText(date_1);

                        } else if (finalName.equals(getString(R.string.enddate))) {
                            String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            toDate.setText(date);

                            calculateDate();
                        }
                    }
                }, year, month, day);
        if (name.equals(getString(R.string.enddate))) {
            picker.getDatePicker().setMinDate(date.getTimeInMillis());
        } else {
            picker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        }

        picker.show();
    }

    private void calculateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format), Locale.ENGLISH);
        Date firstDate = null;
        Date secondDate = null;

        int fromSession = sessionFrom.getSelectedItemPosition();
        int toSession = sessionTo.getSelectedItemPosition();
        try {
            firstDate = sdf.parse(fromDate.getText().toString());
            secondDate = sdf.parse(toDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Get the represented date in milliseconds
        long millis1 = firstDate.getTime();
        long millis2 = secondDate.getTime();

        // Calculate difference in milliseconds
        long diff = millis2 - millis1;

        // Calculate difference in days
        double diffDays = diff / (24 * 60 * 60 * 1000);

        isValid = true;

        if (diffDays == 0 && fromSession == toSession) {
            diffDays = 0.5;
        } else if (diffDays == 0 && fromSession < toSession) {
            diffDays = 1;
        } else if (diffDays > 0 && fromSession == toSession) {
            diffDays = diffDays + 0.5;
        } else if (diffDays > 0 && fromSession < toSession) {
            diffDays = diffDays + 1;
        } else if (diffDays > 0 && fromSession > toSession) {
            diffDays = diffDays + 0;
        } else {
            Toast.makeText(context, R.string.invalid_session, Toast.LENGTH_SHORT).show();
            isValid = false;
        }


        if (!isValid)
            btnSave.setVisibility(View.GONE);
        else btnSave.setVisibility(View.VISIBLE);

        leaveCount.setText(diffDays + getString(R.string.days));
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        calculateDate();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
