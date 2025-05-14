package model;


public class Technical {
    private int technicalId;
    private String nameTechnical;
    private String email;
    private String passwordHash;

    public Technical(int technicalId, String nameTechnical, String email, String passwordHash) {
        this.technicalId = technicalId;
        this.nameTechnical = nameTechnical;
        this.email       = email;
        this.passwordHash = passwordHash;
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
    public String getEmailTechnical() {
        return email;
    }
    public void setEmailTechnical(String email) {
        this.email = email;
    }
    public String getPassword() {
        return passwordHash;
    }
    public void setPassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public String toString() {
        return "Technical{" +
                "technicalId=" + technicalId +
                ", nameTechnical='" + nameTechnical + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                '}';
    }
}
