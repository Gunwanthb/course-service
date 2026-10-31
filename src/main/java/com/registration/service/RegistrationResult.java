package com.registration.service;

public class RegistrationResult {
    public static final RegistrationResult NOT_ENROLLED = new RegistrationResult(Status.NOT_ENROLLED, null);
    public static final RegistrationResult DROPPED_SUCCESSFULLY = new RegistrationResult(Status.DROPPED_SUCCESSFULLY, null);

    public enum Status {
        NOT_ENROLLED,
        DROPPED_SUCCESSFULLY
    }

    private final Status status;
    private final String learnerId;

    public RegistrationResult(Status status, String learnerId) {
        this.status = status;
        this.learnerId = learnerId;
    }

    public static RegistrationResult droppedSuccessfully(String learnerId) {
        return new RegistrationResult(Status.DROPPED_SUCCESSFULLY, learnerId);
    }

    public Status getStatus() {
        return status;
    }

    public String getLearnerId() {
        return learnerId;
    }

    public String getPromotedLearnerId() {
        return learnerId;
    }

    @Override
    public String toString() {
        return "RegistrationResult{" +
                "status=" + status +
                ", learnerId='" + learnerId + '\'' +
                '}';
    }
}
