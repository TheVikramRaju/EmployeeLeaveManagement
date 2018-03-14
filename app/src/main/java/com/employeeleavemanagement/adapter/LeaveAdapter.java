package com.employeeleavemanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.employeeleavemanagement.R;
import com.employeeleavemanagement.model.Leave;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by VIKRAM R on 13/03/2018.
 */

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {
    private List<Leave> leaveList;
    private Context context;

    public LeaveAdapter(List<Leave> leaveList, Context context) {
        this.leaveList = leaveList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Leave c = leaveList.get(position);
        holder.leaveFrom.setText(c.getLeaveFrom());
        if (c.getSessionFrom() == 0)
            holder.sessionFrom.setText("Forenoon");
        else
            holder.sessionFrom.setText("Afternoon");

        if (c.getSessionTo() == 0)
            holder.sessionTo.setText("Forenoon");
        else
            holder.sessionTo.setText("Afternoon");

        holder.leaveTo.setText(c.getLeaveTo());
        holder.leaveCount.setText(calculateDate(c.getLeaveFrom(), c.getSessionFrom(), c.getLeaveTo(), c.getSessionTo()));
    }

    @Override
    public int getItemCount() {
        return leaveList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.leave_item, parent, false);
        return new MyViewHolder(v);
    }


    /**
     * View holder class
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView leaveFrom;
        TextView leaveTo;
        TextView sessionFrom;
        TextView sessionTo;
        TextView leaveCount;

        public MyViewHolder(View view) {
            super(view);
            leaveFrom = itemView.findViewById(R.id.leave_from);
            leaveTo = itemView.findViewById(R.id.leave_to);
            sessionFrom = itemView.findViewById(R.id.session_from);
            sessionTo = itemView.findViewById(R.id.session_to);
            leaveCount = itemView.findViewById(R.id.leave_count);
        }
    }

    private String calculateDate(String leaveFrom, int sessionFrom, String leaveTo, int sessionTo) {
        SimpleDateFormat sdf = new SimpleDateFormat(context.getString(R.string.date_format), Locale.ENGLISH);
        Date firstDate = null;
        Date secondDate = null;

        int fromSession = sessionFrom;
        int toSession = sessionTo;
        try {
            firstDate = sdf.parse(leaveFrom);
            secondDate = sdf.parse(leaveTo);
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
        }
        return diffDays + "";
    }
}