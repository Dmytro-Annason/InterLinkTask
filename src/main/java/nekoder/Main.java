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

    private static Set<Date> allDates = new TreeSet<>();

    public static void main(String[] args) {
        List<String[]> rows = readFromFile(FILE_PATH_WITH_INPUT_DATA);
        Map<String, WorkTimes> workers = parseData(rows);
        writeInFile(workers);
        System.out.println();
    }

    private static void writeInFile(Map<String, WorkTimes> workers) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(FILE_PATH_WITH_OUTPUT_DATA))) {
            writer.writeNext(createHeader());
            for (String worker : workers.keySet()) {
                StringBuilder res = new StringBuilder(worker + ",");
                WorkTimes workTimes = workers.get(worker);
                for (Date date : allDates) {
                    if (!workTimes.contains(date)) {
                        res.append("0,");
                    } else {
                        res.append(workTimes.getTime(date)).append(",");
                    }
                }
                writer.writeNext(res.deleteCharAt(res.length() - 1).toString().split(","));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static String[] createHeader() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder res = new StringBuilder("Name / Date,");
        for (Date date : allDates) {
            res.append(formatter.format(date)).append(",");
        }
        return res.deleteCharAt(res.length() - 1).toString().split(",");
    }

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
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (d != null)
            allDates.add(d);

        return d;
    }

    private static List<String[]> readFromFile(String fileName) {
        List<String[]> list = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            list = csvReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.remove(0);
        return list;
    }
}
