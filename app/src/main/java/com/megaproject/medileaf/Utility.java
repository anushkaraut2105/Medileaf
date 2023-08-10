package com.megaproject.medileaf;

import java.time.LocalDateTime;

public class Utility {


    public static String getMessage() {

        int hourOfDay = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            hourOfDay = LocalDateTime.now().getHour();
        }

        if (0 <= hourOfDay && hourOfDay < 12) {
            return "Good Morning";
        } else if(12 <= hourOfDay && hourOfDay < 16) {
            return "Good Afternoon";
        } else {
            return "Good Evening";
        }
    }

}
