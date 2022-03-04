package Services;

import Models.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractionService {

    public List<CombinedFlight> getListOfFlights(String flightsResponse) {
        flightsResponse = flightsResponse
                .replace("\n", "")
                .replace("\r", "")
                .replace("\r\n", "")
                .replace(" </div>", "</div>")
                .replace("</div> ", "</div>");

        Pattern flightRowPattern = Pattern.compile("<div class=\"fly5-result fly5-result-\\d+\">.{3500,5000}</div></div></div></div></div>");
        Matcher flightRowMatcher = flightRowPattern.matcher(flightsResponse);

        List<String> flightRows = new ArrayList<>();

        while (flightRowMatcher.find()) {
            String flightRow = flightRowMatcher.group();
            flightRows.add(flightRow);
        }

        FlightYears flightYears = getYears(flightsResponse);

        List<FlightOneWay> outboundOneWayFlights = new ArrayList<>();
        List<FlightOneWay> inboundOneWayFlights = new ArrayList<>();

        for (String flightRow: flightRows) {
            boolean isOutbound = isOutbound(flightRow);
            FlightOneWay flightOneWay = new FlightOneWay();
            getDates(flightRow, flightOneWay, flightYears, isOutbound);
            getAirports(flightRow, flightOneWay);
            getPrice(flightRow, flightOneWay);
            getSolutionId(flightRow, flightOneWay);

            if (isOutbound) {
                outboundOneWayFlights.add(flightOneWay);
            } else {
                inboundOneWayFlights.add(flightOneWay);
            }
        }

        FlightRequestIds flightRequestIds = getFlightRequestIds(flightsResponse);
        List<CombinedFlight> combinedFlights = new ArrayList<>();

        for (FlightOneWay outboundFlight: outboundOneWayFlights) {
            for (FlightOneWay inboundFlight: inboundOneWayFlights) {
                CombinedFlight combinedFlight = new CombinedFlight(outboundFlight, inboundFlight, flightRequestIds.outboundRequestId, flightRequestIds.inboundRequestId);
                combinedFlights.add(combinedFlight);
            }
        }

        return combinedFlights;
    }

    public Taxes getTaxes(String taxResponse) {
        Pattern rowTaxesPattern = Pattern.compile("<div>Tax \\(Pax 1 - Adult\\)\\s?<span class=\"num\">\\d+(?:,\\d+)?\\.\\d{2}");
        Matcher rowTaxesMatcher = rowTaxesPattern.matcher(taxResponse);
        String rowTaxesOutboundPrice = "";
        String rowTaxesInboundPrice = "";

        if (rowTaxesMatcher.find()) {
            rowTaxesOutboundPrice = rowTaxesMatcher.group();
        }

        if (rowTaxesMatcher.find()) {
            rowTaxesInboundPrice = rowTaxesMatcher.group();
        }

        String taxesOutboundPrice = getStringByRegex("\\d+(?:,\\d+)?\\.\\d{2}", rowTaxesOutboundPrice).replace(",", "");
        String taxesInboundPrice = getStringByRegex("\\d+(?:,\\d+)?\\.\\d{2}", rowTaxesInboundPrice).replace(",", "");

        BigDecimal outboundTax = new BigDecimal(taxesOutboundPrice);
        BigDecimal inboundTax = new BigDecimal(taxesInboundPrice);
        Taxes taxes = new Taxes(outboundTax.add(inboundTax));
        return taxes;
    }

    private FlightRequestIds getFlightRequestIds(String flightsResponse) {
        String rowOutboundRequestId = getStringByRegex("id=\"outbound_request_id\" value=\"\\d+\"", flightsResponse);
        String outboundRequestId = getStringByRegex("\\d+", rowOutboundRequestId);

        String rowInboundRequestId = getStringByRegex("id=\"inbound_request_id\" value=\"\\d+\"", flightsResponse);
        String inboundRequestId = getStringByRegex("\\d+", rowInboundRequestId);

        FlightRequestIds flightRequestIds = new FlightRequestIds(outboundRequestId, inboundRequestId);
        return flightRequestIds;
    }

    private FlightYears getYears(String flightsResponse) {
        String rowYearsDeparting = getStringByRegex("Departing</span> \\w{3} \\d+, \\w{3} \\d{4}", flightsResponse);
        String yearsDeparting = getStringByRegex("\\d{4}", rowYearsDeparting);

        String rowYearsArriving = getStringByRegex("Returning</span> \\w{3} \\d+, \\w{3} \\d{4}", flightsResponse);
        String yearsArriving = getStringByRegex("\\d{4}", rowYearsArriving);

        FlightYears flightYears = new FlightYears(yearsDeparting, yearsArriving);
        return flightYears;
    }

    private boolean isOutbound(String flightRow) {
        Pattern outboundPattern = Pattern.compile("outbound");
        Matcher outboundMatcher = outboundPattern.matcher(flightRow);
        return outboundMatcher.find();
    }

    private void getDates(String flightRow, FlightOneWay flightOneWay, FlightYears flightYears, boolean isOutbound) {
        Pattern timePattern = Pattern.compile("\\d{1,2}:\\d{2}(?:am|pm)");
        Matcher timeMatcher = timePattern.matcher(flightRow);
        String departureTime = "";
        String arrivalTime = "";

        if (timeMatcher.find()) {
            departureTime = timeMatcher.group();
        }

        if (timeMatcher.find()) {
            arrivalTime = timeMatcher.group();
        }

        Pattern datePattern = Pattern.compile("\\w{3} \\d{1,2}, \\w{3}");
        Matcher dateMatcher = datePattern.matcher(flightRow);
        String departureDate = "";
        String arrivalDate = "";

        if (dateMatcher.find()) {
            departureDate = dateMatcher.group().replace(",", "");
        }

        if (dateMatcher.find()) {
            arrivalDate = dateMatcher.group().replace(",", "");;
        }

        String years;
        if (isOutbound) {
            years = flightYears.outboundYears;
        } else {
            years = flightYears.inboundYears;
        }

        flightOneWay.departureTime = departureTime + " " + departureDate + " " + years;
        flightOneWay.arrivalTime = arrivalTime + " " + arrivalDate + " " + years;
    }

    private void getAirports(String flightRow, FlightOneWay flightOneWay) {
        Pattern rowAirportPattern = Pattern.compile("\\(\\w{3}\\)");
        Matcher rowAirportMatcher = rowAirportPattern.matcher(flightRow);
        String rowDepartureAirport = "";
        String rowArrivalAirport = "";

        if (rowAirportMatcher.find()) {
            rowDepartureAirport = rowAirportMatcher.group();
        }

        if (rowAirportMatcher.find()) {
            rowArrivalAirport = rowAirportMatcher.group();
        }

        String departureAirport = getStringByRegex("\\w{3}", rowDepartureAirport);
        String arrivalAirport = getStringByRegex("\\w{3}", rowArrivalAirport);

        flightOneWay.departureAirport = departureAirport;
        flightOneWay.arrivalAirport = arrivalAirport;
    }

    private void getPrice(String flightRow, FlightOneWay flightOneWay) {
        String rowPrice = getStringByRegex("<span class=\"pkgprice\">\\w{3} \\d+(?:,\\d{3})?", flightRow);
        String price = getStringByRegex("\\d+(?:,\\d{3})?", rowPrice);

        flightOneWay.totalPrice = new BigDecimal(price.replace(",", ""));
    }

    private void getSolutionId(String flightRow, FlightOneWay flightOneWay) {
        String solutionId = getStringByRegex("data-flight-key=\"[^\"]+", flightRow);

        flightOneWay.solutionId = solutionId
                .replace("data-flight-key=\"", "")
                .replace("=", "%3D");
    }

    private String getStringByRegex(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String matchedResponse = "";
        if (matcher.find()) {
            matchedResponse = matcher.group();
        }

        return matchedResponse;
    }
}
