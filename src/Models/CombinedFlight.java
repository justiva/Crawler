package Models;

public class CombinedFlight {
    public FlightOneWay outbound;
    public FlightOneWay inbound;
    public String outboundRequestId;
    public String inboundRequestId;

    public CombinedFlight(FlightOneWay outbound, FlightOneWay inbound, String outboundRequestId, String inboundRequestId) {
        this.outbound = outbound;
        this.inbound = inbound;
        this.outboundRequestId = outboundRequestId;
        this.inboundRequestId = inboundRequestId;
    }
}
