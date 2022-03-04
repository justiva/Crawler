package Models;

public class FlightsWithTaxes {
    public CombinedFlight combinedFlight;
    public Taxes taxes;

    public FlightsWithTaxes(CombinedFlight combinedFlight, Taxes taxes) {
        this.combinedFlight = combinedFlight;
        this.taxes = taxes;
    }
}
