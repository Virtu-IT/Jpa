package be.abis.exercise.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EnrolmentId implements Serializable {

    @Column(name = "E_SNO")
    private int sessionId;
    @Column(name = "ENO")
    private int enrolmentInSession;

    public EnrolmentId() {}

    public EnrolmentId(int sessionId, int enrolmentInSession) {
        this.sessionId = sessionId;
        this.enrolmentInSession = enrolmentInSession;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getEnrolmentInSession() {
        return enrolmentInSession;
    }

    public void setEnrolmentInSession(int enrolmentInSession) {
        this.enrolmentInSession = enrolmentInSession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnrolmentId that)) return false;
        return sessionId == that.sessionId && enrolmentInSession == that.enrolmentInSession;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, enrolmentInSession);
    }
}
