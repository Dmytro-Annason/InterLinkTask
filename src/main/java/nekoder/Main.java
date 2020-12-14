package nekoder;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import nekoder.models.WorkTimes;

import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {


    private static final String FILE_PATH_WITH_INPUT_DATA = "acme_worksheet.csv";
    private static final String FILE_PATH_WITH_OUTPUT_DATA = "acme_worksheet_final.csv";

    /*All dates for creating file`s header*/
    private static Set<Date> allDates = new TreeSet<>();

    public static void main(String[] args) {
        List<String[]> rows = readFromFile(FILE_PATH_WITH_INPUT_DATA);
        Map<String, WorkTimes> workers = parseData(rows);
        writeInFile(workers);
    }

    /**
     * Write all workers into output file
     *
     * @param workers all workers with their work times
     */
    private static void writeInFile(Map<String, WorkTimes> workers) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH_WITH_OUTPUT_DATA))) {
            writer.writeNext(createHeader());
            for (String worker : workers.keySet()) {
                StringBuilder res = new StringBuilder(worker + ",");
                WorkTimes workTimes = workers.get(worker);
                for (Date date : allDates) {
                    //if worker don`t have work time in certain day, method add '0' to row
                    if (!workTimes.contains(date))
                        res.append("0,");
                    else
                        res.append(workTimes.getTime(date)).append(",");
                }
                writer.writeNext(res.deleteCharAt(res.length() - 1).toString().split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Create header int output file (with all dates, which contains input file)
     *
     * @return header row
     */
    private static String[] createHeader() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder res = new StringBuilder("Name / Date,");
        for (Date date : allDates) {
            res.append(formatter.format(date)).append(",");
        }
        return res.deleteCharAt(res.length() - 1).toString().split(",");
    }

    /**
     * Parse data from input file and group workers with their work times
     *
     * @param rows all rows from input file
     * @return map (keys - names of workers, values - their work times)
     */
    private static Map<String, WorkTimes> parseData(List<String[]> rows) {
        Map<String, WorkTimes> workers = new HashMap<>();
        for (String[] row : rows) {
            if (!workers.containsKey(row[0])) {
                workers.put(row[0], new WorkTimes());
            }
            workers.get(row[0]).add(parseDate(row[1]), row[2]);
        }
        return workers;
    }

    /**
     * Parse date from file
     *
     * @param date date from file
     * @return date (class java.util.Date)
     */
    private static Date parseDate(String date) {
        DateFormatSymbols symbols = new DateFormatSymbols();
        symbols.setShortMonths(new String[]{"Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"});
        SimpleDateFormat format = new SimpleDateFormat("MMM dd yyyy", symbols);
        Date d = null;
        try {
            d = format.parse(date);
            allDates.add(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * Read data from CSV file
     *
     * @param fileName name of input file
     * @return list with all rows from file
     */
    private static List<String[]> readFromFile(String fileName) {
        List<String[]> list = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            list = csvReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //delete header from input file
        list.remove(0);
        return list;
    }
}
