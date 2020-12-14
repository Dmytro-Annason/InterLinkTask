package nekoder.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkTimes {
    private Map<Date, String> allDateTimes = new HashMap<>();

    public WorkTimes() {
    }

    public void add(Date date, String time) {
        allDateTimes.put(date, time);
    }


    public boolean contains(Date date) {
        return allDateTimes.containsKey(date);
    }

    public String getTime(Date date) {
        return allDateTimes.get(date);
    }
}
