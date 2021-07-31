package io.ipinfo.api.model;

public class MapResponse {
    private final String reportUrl;
    private final String status;

    public MapResponse(
            String reportUrl,
            String status
    ) {
        this.reportUrl = reportUrl;
        this.status = status;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "MapResponse{" +
                "reportUrl='" + reportUrl + '\'' +
                ", status=" + status +
                '}';
    }
}
