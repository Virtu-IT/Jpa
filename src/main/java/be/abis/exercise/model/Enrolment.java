package be.abis.exercise.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ENROLMENTS")
@NamedQuery(name = "Enrolment.findEnrolmentsForPerson",
        query = "select e.enrollee, e.session.startDate, e.session.course from Enrolment e " +
                "where e.enrollee.personId = :personId and e.cancelFlag is null " +
                "order by e.session.startDate")
// BEFORE: a plain class with the fields "Session session; int enrolmentInSession; Person enrollee;
// double pricePerDayPayed; Company enrolmentCompany; boolean cancelled;" and no mapping.
// NOW: session + enrolmentInSession form the composite key (EnrolmentId); cancelled became cancelFlag.
public class Enrolment {

    @EmbeddedId
    private EnrolmentId id;
    @MapsId("sessionId")
    @ManyToOne
    @JoinColumn(name = "E_SNO")
    private Session session;
    @ManyToOne
    @JoinColumn(name = "E_PNO")
    private Person enrollee;
    @Column(name = "EPAY")
    private double pricePerDayPayed;
    @ManyToOne
    @JoinColumn(name = "E_CONO")
    private Company enrolmentCompany;
    // the DB marks a cancelled enrolment with a letter, a non-cancelled one has NULL
    @Column(name = "ECANCEL")
    private String cancelFlag;

    public EnrolmentId getId() {
        return id;
    }

    public void setId(EnrolmentId id) {
        this.id = id;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public int getEnrolmentInSession() {
        return id.getEnrolmentInSession();
    }

    public Person getEnrollee() {
        return enrollee;
    }

    public void setEnrollee(Person enrollee) {
        this.enrollee = enrollee;
    }

    public double getPricePerDayPayed() {
        return pricePerDayPayed;
    }

    public void setPricePerDayPayed(double pricePerDayPayed) {
        this.pricePerDayPayed = pricePerDayPayed;
    }

    public Company getEnrolmentCompany() {
        return enrolmentCompany;
    }

    public void setEnrolmentCompany(Company enrolmentCompany) {
        this.enrolmentCompany = enrolmentCompany;
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
}
