package model;



public class Computer {
    private String serviceTag;
    private int clientId;  // Relación con Cliente
    private String problemDescription;
    private String receptionDate; // En formato YYYY-MM-DD

    // Constructor
    public Computer(String serviceTag, int clientId, String problemDescription, String receptionDate) {
        this.serviceTag = serviceTag;
        this.clientId = clientId;
        this.problemDescription = problemDescription;
        this.receptionDate = receptionDate;
    }

    // Getters y Setters
    public String getServiceTag() {
        return serviceTag;
    }
    public void setServiceTag(String serviceTag) {
        this.serviceTag = serviceTag;
    }
    public int getClientId() {
        return clientId;
    }
    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
    public String getProblemDescription() {
        return problemDescription;
    }
    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }
    public String getReceptionDate() {
        return receptionDate;
    }
    public void setReceptionDate(String receptionDate) {
        this.receptionDate = receptionDate;
    }

    @Override
    public String toString() {
        return "Computer{" +
                "serviceTag='" + serviceTag + '\'' +
                ", clientId=" + clientId +
                ", problemDescription='" + problemDescription + '\'' +
                ", receptionDate='" + receptionDate + '\'' +
                '}';
    }
}
