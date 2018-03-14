package com.employeeleavemanagement.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.employeeleavemanagement.R;
import com.employeeleavemanagement.Utils;
import com.employeeleavemanagement.model.Employee;
import com.employeeleavemanagement.ui.ProfileVeiwActivity;

import java.util.List;

/**
 * Created by VIKRAM R on 13/03/2018.
 */

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.MyViewHolder> {
    private List<Employee> employeeList;
    private Context context;

    public EmployeeAdapter(List<Employee> employeeList, Context context) {
        this.employeeList = employeeList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Employee employee = employeeList.get(position);
        holder.empName.setText(employee.getEmpName());
        Glide.with(context).load(employee.getEmpImage()).into(holder.empProfile);
        holder.empView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.utilsEmployee = employee;
                Intent intent = new Intent(context, ProfileVeiwActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emp_item, parent, false);
        return new MyViewHolder(v);
    }


    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView empProfile;
        TextView empName;
        View empView;

        public MyViewHolder(View view) {
            super(view);
            empName = itemView.findViewById(R.id.emp_name);
            empProfile = itemView.findViewById(R.id.emp_image);
            empView = view;


        }
    }
}