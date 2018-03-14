package com.employeeleavemanagement;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.employeeleavemanagement.adapter.EmployeeAdapter;
import com.employeeleavemanagement.database.DatabaseHelper;
import com.employeeleavemanagement.model.Employee;
import com.employeeleavemanagement.model.Leave;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    Dialog dialog;
    TextView info;
    ArrayList<Employee> employeeArrayList;
    ArrayList<Leave> leaveArrayList;
    RecyclerView rv;

    //Permision code that will be checked in the method onRequestPermissionsResult
    private int STORAGE_PERMISSION_CODE = 23;
    //Firebase objects
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
    StorageReference islandRef;

    File localFile;

    DatabaseHelper dbHelper;

    EmployeeAdapter employeeAdapter;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    public boolean isFileDownloaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = this;

        sharedpreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        isFileDownloaded = sharedpreferences.getBoolean("isFileDownloaded", false); // getting boolean

        dbHelper = new DatabaseHelper(getApplicationContext());
        rv = findViewById(R.id.emp_list);

        employeeArrayList = new ArrayList<>();

        storageRef = storage.getReferenceFromUrl(getString(R.string.firebase_url));
        islandRef = storageRef.child(getString(R.string.firebase_file_name));

        getSupportActionBar().setTitle(R.string.emp_list_title);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
    }

    @Override
    protected void onStart() {
        super.onStart();
        callLoadData();
    }

    private void callLoadData() {
        if (!isFileDownloaded) {
            if (Utils.isOnline(mContext)) {
                if (isReadStorageAllowed()) {
                    showProgressDialog((String) getText(R.string.downloading));
                    DownloadFile();
                } else {
                    //If the app has not the permission then asking for the permission
                    requestStoragePermission();
                }
            } else {
                Toast.makeText(this, R.string.network_error, Toast.LENGTH_LONG).show();
            }
        } else {
            showProgressDialog((String) getText(R.string.fetching_data));
            employeeArrayList = dbHelper.getEmployeeData();
            loadDataToRecyclerView(employeeArrayList);
        }
    }


    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Explain here why you need this permission
            Toast.makeText(this, R.string.explanation_for_permission, Toast.LENGTH_LONG).show();
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showProgressDialog((String) getText(R.string.downloading));
                DownloadFile();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, R.string.denial_of_permission, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showProgressDialog(String information) {
        dialog = rotateimageWithDesc(mContext);
        info.setText(information);
        dialog.show();
    }

    private void hideProgressDialog() {
        if (dialog.isShowing())
            dialog.hide();
    }

    /*
    * Dialog with rotating icon to show the progress
    * */
    public Dialog rotateimageWithDesc(Context mContext) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rotating_progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView imgRotationClock = dialog.findViewById(R.id.img_rotation_clock);
        dialog.setCancelable(false);
        info = dialog.findViewById(R.id.info_text);
        Animation rotationClock = AnimationUtils.loadAnimation(mContext, R.anim.rotator);
        rotationClock.setRepeatCount(Animation.INFINITE);
        imgRotationClock.startAnimation(rotationClock);

        return dialog;
    }

    /*
    * Download initiation of file
    * */
    private void DownloadFile() {
        File rootPath = new File(getString(R.string.file_location));
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        localFile = new File(rootPath, getString(R.string.file_name));

        //Downloading file from Firebase
        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // change state to true in shared preference
                editor.putBoolean("isFileDownloaded", true);
                editor.commit();
                info.setText(R.string.downloading_success);
                readData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // change state to false in shared preference
                editor.putBoolean("isFileDownloaded", false);
                editor.commit();
                info.setText(R.string.downloading_failure);
                hideProgressDialog();
            }
        });

    }

    /*
    *reading data from downloaded file
    * */
    private void readData() {
        info.setText(R.string.reading_file);
        File file = new File(getString(R.string.saved_file_path));
        StringBuilder text = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            // do exception handling
        } finally {
            try {
                br.close();
            } catch (Exception e) {
            }
        }
        String finaltext = text.toString();
        try {
            JSONObject obj = new JSONObject(finaltext);
            storeDataToLocal(obj);
        } catch (Throwable t) {

        }
    }

    /*
    * Formatting to Json object and insertion to DB
    * */
    private void storeDataToLocal(JSONObject obj) {
        info.setText(R.string.data_construction);
        employeeArrayList = new ArrayList<>();
        leaveArrayList = new ArrayList<>();
        JSONObject parentObj = obj;
        try {
            JSONArray employeeArray = parentObj.getJSONArray("EmployeeList");
            if (employeeArray.length() > 0) {
                for (int i = 0; i < employeeArray.length(); i++) {
                    JSONObject employeeObj = employeeArray.getJSONObject(i);
                    Employee employee = new Employee();

                    employee.setEmpID(employeeObj.optInt("empId"));
                    employee.setEmpName(employeeObj.optString("empName"));
                    employee.setEmpImage(employeeObj.optString("empImage"));
                    employee.setEmpAge(employeeObj.optInt("empAge"));
                    employee.setEmpGender(employeeObj.optString("empGender"));
                    employee.setEmpDesignation(employeeObj.optString("empDesignation"));
                    employeeArrayList.add(employee);

                    JSONArray leaveArray = employeeObj.getJSONArray("Leaves");
                    if (leaveArray.length() > 0) {
                        for (int j = 0; j < leaveArray.length(); j++) {
                            JSONObject leaveObj = leaveArray.getJSONObject(j);
                            Leave leave = new Leave();
                            leave.setEmpID(leaveObj.getInt("empId"));
                            leave.setLeaveFrom(leaveObj.getString("leaveFrom"));
                            leave.setSessionFrom(leaveObj.getInt("fromSession"));
                            leave.setLeaveTo(leaveObj.getString("leaveTo"));
                            leave.setSessionTo(leaveObj.getInt("toSession"));
                            leaveArrayList.add(leave);
                        }
                    }
                }
            }

            if (employeeArrayList.size() > 0) {
                dbHelper.deleteEmployee();
                long employeeInsertedCount = dbHelper.addEmployeeList(employeeArrayList);
                loadDataToRecyclerView(employeeArrayList);
            }

            if (leaveArrayList.size() > 0) {
                dbHelper.deleteLeaves();
                long leavesInsertedCount = dbHelper.addLeavesList(leaveArrayList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            hideProgressDialog();
        }

    }

    /*
    * Loading data to RecyclerView*/
    private void loadDataToRecyclerView(ArrayList<Employee> employeeArrayList) {
        employeeAdapter = new EmployeeAdapter(employeeArrayList, mContext);
        rv.setAdapter(employeeAdapter);
        hideProgressDialog();
    }
}
