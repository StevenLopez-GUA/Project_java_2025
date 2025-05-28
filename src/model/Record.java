package model;

public class Record {

    private int recordId;
    private String serviceTag;
    private Integer phaseId;
    private Integer technicalId;
    private String entryDate;
    private String departureDate;
    private String details;

    public Record(int recordId, String serviceTag, Integer phaseId,
                  Integer technicalId, String entryDate, String departureDate, String details) {
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

    public Integer getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Integer phaseId) {
        this.phaseId = phaseId;
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

    public Integer getTechnicalId() {
        return technicalId;
    }

    public void setTechnicalId(Integer technicalId) {
        this.technicalId = technicalId;
    }
}
