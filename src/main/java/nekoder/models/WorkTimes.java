package nekoder.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkTimes {
    private Map<Date, String> allDateTimes = new HashMap<>();

    public WorkTimes() { }

    /**
     * Add new date and count work hours
     *
     * @param date date, when worker is working
     * @param time count hours of work in certain date
     */
    public void add(Date date, String time) {
        allDateTimes.put(date, time);
    }

    /**
     * Check, if contains certain date in list with work times
     *
     * @param date certain date
     */
    public boolean contains(Date date) {
        return allDateTimes.containsKey(date);
    }

    /**
     * Return count hours which worker spent at work on certain date
     *
     * @param date certain date
     * @return count hours
     */
    public String getTime(Date date) {
        return allDateTimes.get(date);
    }
}
