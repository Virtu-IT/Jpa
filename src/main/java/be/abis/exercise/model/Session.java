package be.abis.exercise.model;

import jakarta.persistence.*;

import java.time.LocalDate;

// BEFORE: a plain class with "private boolean cancelled;" and no mapping.
// NOW: the DB stores a letter (or NULL), so it is mapped as cancelFlag; isCancelled() is derived from it.
@Entity
@Table(name = "SESSIONS")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SNO")
    private int sessionId;
    @Column(name = "SDATE")
    private LocalDate startDate;
    @ManyToOne
    @JoinColumn(name = "SINS_PNO")
    private Person instructor;
    @ManyToOne
    @JoinColumn(name = "SLOC_CONO")
    private Company location;
    @Column(name = "SKIND")
    private String kind;
    @Column(name = "SINCOMES")
    private double income;
    // the DB marks a cancelled session with a letter, a non-cancelled one has NULL
    @Column(name = "SCANCEL")
    private String cancelFlag;
    @ManyToOne
    @JoinColumn(name = "S_CID")
    private Course course;

    public Session(){}

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Company getLocation() {
        return location;
    }

    public void setLocation(Company location) {
        this.location = location;
    }

    public Person getInstructor() {
        return instructor;
    }

    public void setInstructor(Person instructor) {
        this.instructor = instructor;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public boolean isCancelled() {
        return cancelFlag != null && !cancelFlag.isBlank();
    }

    public void setCancelled(boolean cancelled) {
        this.cancelFlag = cancelled ? "C" : null;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
