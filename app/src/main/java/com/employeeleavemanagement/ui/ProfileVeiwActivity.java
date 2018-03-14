package com.employeeleavemanagement.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.employeeleavemanagement.R;
import com.employeeleavemanagement.Utils;
import com.employeeleavemanagement.database.DatabaseHelper;
import com.employeeleavemanagement.model.Employee;

public class ProfileVeiwActivity extends AppCompatActivity {
    Employee currentEmployee;
    ImageView empImage;
    TextView empName, empUsrId, empAge, empGender, empDesignation;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_veiw);
        context = this;
        currentEmployee = Utils.utilsEmployee;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        initUI();
    }

    private void initUI() {
        empImage = findViewById(R.id.emp_image);
        empName = findViewById(R.id.emp_name);
        empUsrId = findViewById(R.id.emp_id);
        empAge = findViewById(R.id.emp_age);
        empGender = findViewById(R.id.emp_gender);
        empDesignation = findViewById(R.id.emp_designation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Glide.with(context).load(currentEmployee.getEmpImage()).into(empImage);
        empName.setText(currentEmployee.getEmpName());
        empUsrId.setText("( User ID : " + currentEmployee.getEmpID() + " )");
        empGender.setText(currentEmployee.getEmpGender());
        empAge.setText(currentEmployee.getEmpAge() + " Years");
        empDesignation.setText(currentEmployee.getEmpDesignation());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.option_list:
                intent = new Intent(context, EmployeeLeaveListActivity.class);
                startActivity(intent);
                return true;
            case R.id.option_sanction_leave:
                intent = new Intent(context, AddLeaveActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
