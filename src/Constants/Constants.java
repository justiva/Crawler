package Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constants {
    public static final Map<String, String> airportInfo = new HashMap<>();
    public static final List<String> excelColumnNames = new ArrayList<>();

    private Constants() {}

    static {
        airportInfo.put("NBO", "nairobi");
        airportInfo.put("MBA", "mombasa");
        airportInfo.put("WIL", "nairobi");
        airportInfo.put("UKA", "nairobi");
        airportInfo.put("EDL", "eldoret");
        airportInfo.put("KIS", "kisumu");
        airportInfo.put("MYD", "malidi");
        airportInfo.put("LAU", "lamu%20island");
        airportInfo.put("LOK", "lodwar");
        airportInfo.put("JUB", "juba");
        airportInfo.put("ZNZ", "zanzibar");

        excelColumnNames.add("outbound_departure_airport");
        excelColumnNames.add("outbound_arrival_airport");
        excelColumnNames.add("outbound_departure_time");
        excelColumnNames.add("outbound_arrival_time");
        excelColumnNames.add("inbound_departure_airport");
        excelColumnNames.add("inbound_arrival_airport");
        excelColumnNames.add("inbound_departure_time");
        excelColumnNames.add("inbound_arrival_time");
        excelColumnNames.add("total_price");
        excelColumnNames.add("taxes");
    }
}
