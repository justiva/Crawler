package Models;

import java.time.LocalDate;

public class FlightsRequest {
    public String departureCityName;
    public String arrivalCityName;
    public String departureAirportCode;
    public String arrivalAirportCode;
    public LocalDate dateFrom;
    public LocalDate dateTo;

    public FlightsRequest(String departureCityName, String arrivalCityName, String departureAirportCode, String arrivalAirportCode, LocalDate dateFrom, LocalDate dateTo) {
        this.departureCityName = departureCityName;
        this.arrivalCityName = arrivalCityName;
        this.departureAirportCode = departureAirportCode;
        this.arrivalAirportCode = arrivalAirportCode;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
