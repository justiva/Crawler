package Services;

import Models.FlightsRequest;
import Models.TaxRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ResponseService  {

    public String getFlightsResponse(FlightsRequest requestModel) {
        URI uri = getUri(requestModel);
        if (uri == null) {
            return null;
        }

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return null;
        }

        return response.body();
    }

    public String getTaxResponse(TaxRequest taxRequest) {
        URI uri;
        try {
            uri = new URI("https://www.fly540.com/flights/index.php?option=com_travel");
        } catch (URISyntaxException e) {
            return null;
        }

        String requestPostBody = getRequestPostBody(taxRequest);

        HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).build();

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .header("Origin", "https://www.fly540.com")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.82 Safari/537.36")
                .header("Accept-Encoding",  "gzip, deflate, br")
                .header("Accept-Language",  "en,lt;q=0.9,en-US;q=0.8,ru;q=0.7,pl;q=0.6")
                .header("Referer",  "https://www.fly540.com/flights/nairobi-to-mombasa?isoneway=0&currency=KES&depairportcode=NBO&arrvairportcode=MBA&date_from=Sat%2C+26+Feb+2022&date_to=Mon%2C+28+Feb+2022&adult_no=1&children_no=0&infant_no=0&searchFlight=&change_flight=")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(requestPostBody))
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return null;
        }

        return response.body();
    }

    private URI getUri(FlightsRequest requestModel) {
        String formattedDateFrom = getFormattedDate(requestModel.dateFrom);
        String formattedDateTo = getFormattedDate(requestModel.dateTo);

        StringBuilder stringBuilder = new StringBuilder("https://www.fly540.com/flights/");
        stringBuilder.append(requestModel.departureCityName);
        stringBuilder.append("-to-");
        stringBuilder.append(requestModel.arrivalCityName);
        stringBuilder.append("?isoneway=0");
        stringBuilder.append("&depairportcode=");
        stringBuilder.append(requestModel.departureAirportCode);
        stringBuilder.append("&arrvairportcode=");
        stringBuilder.append(requestModel.arrivalAirportCode);
        stringBuilder.append("&date_from=");
        stringBuilder.append(formattedDateFrom);
        stringBuilder.append("&date_to=");
        stringBuilder.append(formattedDateTo);
        stringBuilder.append("&adult_no=1&children_no=0&infant_no=0&currency=KES&searchFlight=");

        try {
            return new URI(stringBuilder.toString());
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private String getFormattedDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy", Locale.getDefault()))
                .replace(",", "%2C")
                .replace(" ", "+");
    }

    private String getRequestPostBody(TaxRequest taxRequest) {
        StringBuilder stringBuilder = new StringBuilder("option=com_travel&task=airbook.addPassengers");
        stringBuilder.append("&outbound_request_id=");
        stringBuilder.append(taxRequest.outboundRequestId);
        stringBuilder.append("&inbound_request_id=");
        stringBuilder.append(taxRequest.inboundRequestId);
        stringBuilder.append("&outbound_solution_id=");
        stringBuilder.append(taxRequest.outboundSolutionId);
        stringBuilder.append("&outbound_cabin_class=0");
        stringBuilder.append("&inbound_solution_id=");
        stringBuilder.append(taxRequest.inboundSolutionId);
        stringBuilder.append("&inbound_cabin_class=0&adults=1&children=0&infants=0&change_flight=");
        return stringBuilder.toString();
    }
}
