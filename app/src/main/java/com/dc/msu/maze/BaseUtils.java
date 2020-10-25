package com.dc.msu.maze;

import android.text.TextUtils;
import android.util.Patterns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BaseUtils {

    public static boolean isLogged = false;
    //- Scope static to store and match on temp basis
    public static String Email = "";
    public static String Password = "";

    public static String mineId = "";

    public boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static String formatDate(String date, String pattern){
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(pattern, Locale.getDefault()).format(new Date(Long.parseLong(date)));
    }

    public static boolean equalLists(List<String> one, List<String> two){
        if (one == null && two == null){
            return true;
        }

        if((one == null && two != null)
                || one != null && two == null
                || one.size() != two.size()){
            return false;
        }

        //to avoid messing the order of the lists we will use a copy
        //as noted in comments by A. R. S.
        one = new ArrayList<String>(one);
        two = new ArrayList<String>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }
}