package model;

public class Record {
    private int recordId;
    private String serviceTag;
    private int phaseId;
    private Integer technicalId; // Puede ser null si no se asigna técnico
    private String entryDate; // Usaremos formato ISO 8601 por simplicidad
    private String departureDate; // Puede ser null hasta que la fase se complete
    private String details;

    public Record(int recordId, String serviceTag, int phaseId, Integer technicalId, String entryDate,
            String departureDate, String details) {
        this.recordId = recordId;
        this.serviceTag = serviceTag;
        this.phaseId = phaseId;
        this.technicalId = technicalId;
        this.entryDate = entryDate;
        this.departureDate = departureDate;
        this.details = details;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getServiceTag() {
        return serviceTag;
    }

    public void setServiceTag(String serviceTag) {
        this.serviceTag = serviceTag;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    public Integer getTechnicalId() {
        return technicalId;
    }

    public void setTechnicalId(Integer technicalId) {
        this.technicalId = technicalId;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Record{" +
                "recordId=" + recordId +
                ", serviceTag='" + serviceTag + '\'' +
                ", phaseId=" + phaseId +
                ", technicalId=" + technicalId +
                ", entryDate='" + entryDate + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
