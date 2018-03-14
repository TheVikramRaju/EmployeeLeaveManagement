package com.employeeleavemanagement.ui;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.employeeleavemanagement.R;
import com.employeeleavemanagement.Utils;
import com.employeeleavemanagement.adapter.LeaveAdapter;
import com.employeeleavemanagement.database.DatabaseHelper;
import com.employeeleavemanagement.model.Employee;
import com.employeeleavemanagement.model.Leave;

import java.util.ArrayList;

public class EmployeeLeaveListActivity extends AppCompatActivity {
    Context mContext;
    DatabaseHelper dbHelper;
    Employee currentEmployee;
    RecyclerView rv;
    ArrayList<Leave> leaveArrayList;
    LeaveAdapter leaveAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.leave_list_title);
        mContext = this;
        currentEmployee = Utils.utilsEmployee;
        dbHelper = new DatabaseHelper(getApplicationContext());

        rv = findViewById(R.id.leave_list);
        leaveArrayList = new ArrayList<>();

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);

    }


    @Override
    protected void onStart() {
        super.onStart();
        leaveArrayList = dbHelper.getLeaveData(currentEmployee.getEmpID());
        leaveAdapter = new LeaveAdapter(leaveArrayList, mContext);
        rv.setAdapter(leaveAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
