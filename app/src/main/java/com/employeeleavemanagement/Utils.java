package com.employeeleavemanagement;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.employeeleavemanagement.model.Employee;

/**
 * Created by VIKRAM R on 13/03/2018.
 */

public class Utils {
    public static Employee utilsEmployee;

    public static boolean isOnline(Context ctx) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
//        return true;
    }
}
