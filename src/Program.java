import Constants.Constants;
import Models.*;
import Services.CsvService;
import Services.ExtractionService;
import Services.ResponseService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Program {
    public static void main(String[] args) {
        ResponseService responseService = new ResponseService();
        ExtractionService extractionService = new ExtractionService();
        CsvService csvService = new CsvService();

        String departureCityName = Constants.airportInfo.get("NBO");
        String arrivalCityName = Constants.airportInfo.get("MBA");

        LocalDate dateNow = LocalDate.now();

        FlightsRequest flightsRequestDay10 = new FlightsRequest(departureCityName, arrivalCityName, "NBO", "MBA", dateNow.plusDays(10), dateNow.plusDays(17));
        String flightsResponseDay10 = responseService.getFlightsResponse(flightsRequestDay10);
        List<CombinedFlight> flightsOfDay10 = extractionService.getListOfFlights(flightsResponseDay10);

        FlightsRequest flightsRequestDay20 = new FlightsRequest(departureCityName, arrivalCityName, "NBO", "MBA", dateNow.plusDays(20), dateNow.plusDays(27));
        String flightsResponseDay20 = responseService.getFlightsResponse(flightsRequestDay20);
        List<CombinedFlight> flightsOfDay20 = extractionService.getListOfFlights(flightsResponseDay20);

        List<FlightsWithTaxes> allFlightsWithTaxes = new ArrayList<>();

        for (CombinedFlight combinedFlight : flightsOfDay10) {
            TaxRequest taxRequest = new TaxRequest(combinedFlight.outboundRequestId, combinedFlight.inboundRequestId, combinedFlight.outbound.solutionId, combinedFlight.inbound.solutionId);
            String taxResponse = responseService.getTaxResponse(taxRequest);
            Taxes taxes = extractionService.getTaxes(taxResponse);
            allFlightsWithTaxes.add(new FlightsWithTaxes(combinedFlight, taxes));
        }

        for (CombinedFlight combinedFlight : flightsOfDay20) {
            TaxRequest taxRequest = new TaxRequest(combinedFlight.outboundRequestId, combinedFlight.inboundRequestId, combinedFlight.outbound.solutionId, combinedFlight.inbound.solutionId);
            String taxResponse = responseService.getTaxResponse(taxRequest);
            Taxes taxes = extractionService.getTaxes(taxResponse);
            allFlightsWithTaxes.add(new FlightsWithTaxes(combinedFlight, taxes));
        }

        csvService.writeToCsvFile(allFlightsWithTaxes);
    }
}
