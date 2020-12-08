package com.WKNS.gather.helperMethods;

import java.util.Date;

public class DateFormatter {

    public static String getFormattedDate(Date date) {
        StringBuilder b = new StringBuilder();

        // Day of the week
        b.append(getFormattedDay(date.getDay()));
        b.append(", ");

        // Month
        b.append(getFormattedMonth(date.getMonth()));
        b.append(" ");

        // Day of the month
        b.append(date.getDate());
        b.append(" ");

        // Time
        b.append(getFormattedTime(date.getHours(), date.getMinutes()));
        b.append(" ");

        // Year
        b.append(date.getYear() + 1900);

        return b.toString();
    }

    private static String getFormattedTime(int hour, int minutes) {
        StringBuilder b = new StringBuilder();

        boolean isPM = (hour >= 13);

        if (hour >= 13) {
            hour -= 12;
        }

        if (hour == 0) {
            b.append("12");
        } else {
            b.append(hour);
        }

        b.append(":");

        if (minutes < 10) {
            b.append("0");
        }
        b.append(minutes);

        if (isPM) {
            b.append("PM");
        } else {
            b.append("AM");
        }

        return b.toString();
    }

    private static String getFormattedDay(int day) {
        String formattedDay = "";

        switch (day) {
            case 0:
                formattedDay = "Sunday";
                break;
            case 1:
                formattedDay = "Monday";
                break;
            case 2:
                formattedDay = "Tuesday";
                break;
            case 3:
                formattedDay = "Wednesday";
                break;
            case 4:
                formattedDay = "Thursday";
                break;
            case 5:
                formattedDay = "Friday";
                break;
            case 6:
                formattedDay = "Saturday";
                break;
            default:
                formattedDay = "???";
        }

        return formattedDay;
    }

    private static String getFormattedMonth(int month) {

        String formattedMonth = "";

        switch (month) {
            case 0:
                formattedMonth = "Jan";
                break;
            case 1:
                formattedMonth = "Feb";
                break;
            case 2:
                formattedMonth = "Mar";
                break;
            case 3:
                formattedMonth = "Apr";
                break;
            case 4:
                formattedMonth = "May";
                break;
            case 5:
                formattedMonth = "Jun";
                break;
            case 6:
                formattedMonth = "Jul";
                break;
            case 7:
                formattedMonth = "Aug";
                break;
            case 8:
                formattedMonth = "Sep";
                break;
            case 9:
                formattedMonth = "Oct";
                break;
            case 10:
                formattedMonth = "Nov";
                break;
            case 11:
                formattedMonth = "Dec";
                break;
            default:
                formattedMonth = "???";
        }

        return formattedMonth;

    }
}
