package com.example.cs_2340_student_scheduler_app;

public class ManipulateData {
    public static boolean validateDate(String s) {
        if (s.charAt(2) != '/') return false;
        if (s.charAt(5) != '/') return false;
        if (s.length() != 10) return false;
        if (Integer.parseInt(s.substring(0, 2)) > 12
                || Integer.parseInt(s.substring(0, 2)) < 1) return false;
        if (s.charAt(2) != '/') return false;
        return Integer.parseInt(s.substring(3, 5)) <= 31 && Integer.parseInt(s.substring(3, 5)) > 0;
    }

    public static boolean validateTime(String s) {
        if(s.length() > 5) return false;
        if (s.length() < 4) return false;
        return s.charAt(s.length() - 3) == ':';
    }

    public static boolean validateDayOfWeek(String s) {
        String[] strs = s.split(",");
        for (int i = 0; i < strs.length; i++) {
            if (!validDay(strs[i])) return false;
        }
        return true;
    }

    public static boolean validDay(String s) {
        if (s.equalsIgnoreCase("sunday")) return true;
        if (s.equalsIgnoreCase("monday")) return true;
        if (s.equalsIgnoreCase("tuesday")) return true;
        if (s.equalsIgnoreCase("wednesday")) return true;
        if (s.equalsIgnoreCase("thursday")) return true;
        if (s.equalsIgnoreCase("friday")) return true;
        return s.equalsIgnoreCase("saturday");

    }


}
