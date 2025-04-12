package model;


public class Phase {
    private int phaseID;
    private String namePhase;

    public Phase(int phaseID, String namePhase) {
        this.phaseID = phaseID;
        this.namePhase = namePhase;
    }

    public int getPhaseId() {
        return phaseID;
    }
    public void setPhaseId(int phaseID) {
        this.phaseID = phaseID;
    }
    public String getNamePhase() {
        return namePhase;
    }
    public void setNamePhase(String namePhase) {
        this.namePhase = namePhase;
    }

    @Override
    public String toString() {
        return "Phase{" +
                "phaseID=" + phaseID +
                ", namePhase='" + namePhase + '\'' +
                '}';
    }
}
