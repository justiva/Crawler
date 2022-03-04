package Services;

import Constants.Constants;
import Models.FlightsWithTaxes;

import java.io.PrintWriter;
import java.util.List;

public class CsvService {
    public void writeToCsvFile(List<FlightsWithTaxes> list) {
        try (PrintWriter writer = new PrintWriter("Flights.csv")) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < Constants.excelColumnNames.size(); i++) {
                sb.append(Constants.excelColumnNames.get(i));

                if (i != Constants.excelColumnNames.size() - 1) {
                    sb.append(",");
                }
            }

            sb.append('\n');

            for (FlightsWithTaxes flight : list) {
                String fullPrice = flight.combinedFlight.outbound.totalPrice.add(flight.combinedFlight.inbound.totalPrice).toString();

                sb.append(flight.combinedFlight.outbound.departureAirport);
                sb.append(",");
                sb.append(flight.combinedFlight.outbound.arrivalAirport);
                sb.append(",");
                sb.append(flight.combinedFlight.outbound.departureTime);
                sb.append(",");
                sb.append(flight.combinedFlight.outbound.arrivalTime);
                sb.append(",");
                sb.append(flight.combinedFlight.inbound.departureAirport);
                sb.append(",");
                sb.append(flight.combinedFlight.inbound.arrivalAirport);
                sb.append(",");
                sb.append(flight.combinedFlight.inbound.departureTime);
                sb.append(",");
                sb.append(flight.combinedFlight.inbound.arrivalTime);
                sb.append(",");
                sb.append(fullPrice);
                sb.append(",");
                sb.append(flight.taxes.totalTax);
                sb.append('\n');
            }

            writer.write(sb.toString());
        } catch (Exception e) {
            return;
        }
    }
}