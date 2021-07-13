package io.ipinfo.api.model;

public class MapResponse {
    private final String reportUrl;
    private final String status;

    public MapResponse(String reportUrl, String status) {
        this.reportUrl = reportUrl;
        this.status = status;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public String getStatus() {
        return status;
    }

    public String toString() {
        return "IPResponse{" +
                "reportUrl='" + reportUrl + '\'' +
                ", status=" + status +
                '}';
    }
}
