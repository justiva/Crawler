package Models;

public class TaxRequest {
    public String outboundRequestId;
    public String inboundRequestId;
    public String outboundSolutionId;
    public String inboundSolutionId;

    public TaxRequest(String outboundRequestId, String inboundRequestId, String outboundSolutionId, String inboundSolutionId) {
        this.outboundRequestId = outboundRequestId;
        this.inboundRequestId = inboundRequestId;
        this.outboundSolutionId = outboundSolutionId;
        this.inboundSolutionId = inboundSolutionId;
    }
}
