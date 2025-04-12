package model;


public class Technical {
    private int technicalId;
    private String nameTechnical;

    public Technical(int technicalId, String nameTechnical) {
        this.technicalId = technicalId;
        this.nameTechnical = nameTechnical;
    }

    public int getTechnicalId() {
        return technicalId;
    }
    public void setTechnicalId(int technicalId) {
        this.technicalId = technicalId;
    }
    public String getNameTechnical() {
        return nameTechnical;
    }
    public void setNameTechnical(String nameTechnical) {
        this.nameTechnical = nameTechnical;
    }

    @Override
    public String toString() {
        return "Technical{" +
                "technicalId=" + technicalId +
                ", nameTechnical='" + nameTechnical + '\'' +
                '}';
    }
}
