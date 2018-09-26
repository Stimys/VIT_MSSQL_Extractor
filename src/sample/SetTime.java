package sample;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class SetTime {
    private Calendar c;
    private Date date;
    private int i;
    private LocalDate localDate;


    public LocalDate setRealTime(){
        c = Calendar.getInstance();
        date = c.getTime();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return localDate;
    }

    public LocalDate setPrivWeekStart(){
        Date date = new Date();
        c = Calendar.getInstance();
        c.setTime(date);
        i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i - 7);
        date = c.getTime();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return localDate;
    }

    public LocalDate setPrivWeekEnd(){
        c.add(Calendar.DATE, 6);
        date = c.getTime();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return localDate;
    }

    public LocalDate setPrivMonthStart(){
        c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DATE, 1);
        date = c.getTime();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return localDate;
    }

    public LocalDate setPrivMonthStartEnd(){
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        date = c.getTime();
        localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return localDate;
    }
}
