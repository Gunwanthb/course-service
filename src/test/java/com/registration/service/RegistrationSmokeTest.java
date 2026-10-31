package com.registration.service;

import java.util.ArrayList;
import java.util.List;

public class RegistrationSmokeTest {
    public static void main(String[] args) {
        List<String> historyLog = new ArrayList<>();
        List<String> addedLearners = new ArrayList<>();

        InMemoryCourseRegistration repository = new InMemoryCourseRegistration();
        CourseOffering cs101 = new CourseOffering("CS101", 2);
        CourseOffering advanced = new CourseOffering("CS201", 1, List.of("CS101"));

        Learner learner1 = new Learner("L1");
        Learner learner2 = new Learner("L2");
        learner1.completeCourse("CS101");

        repository.registerCourseOffering(cs101);
        repository.registerCourseOffering(advanced);
        repository.registerLearner(learner1);
        repository.registerLearner(learner2);
        historyLog.add("Setup complete: offerings [CS101, CS201], learners [L1, L2]");

        CourseRegistrationService service = new CourseRegistrationService(repository);
        RegirstrationResult advancedAttempt = service.enroll("CS201", "L2");
        historyLog.add("enroll(CS201, L2) -> " + advancedAttempt);
        if (advancedAttempt != RegirstrationResult.REQUIREMENTS_NOT_MET) {
            throw new AssertionError("Prerequisite check failed");
        }

        RegirstrationResult firstEnroll = service.enroll("CS101", "L1");
        historyLog.add("enroll(CS101, L1) -> " + firstEnroll);
        if (firstEnroll != RegirstrationResult.SUCCESSFULLY_ENROLLED) {
            throw new AssertionError("Basic enrollment failed");
        }
        addedLearners.add("L1 (enrolled in CS101)");

        RegirstrationResult secondEnroll = service.enroll("CS101", "L2");
        historyLog.add("enroll(CS101, L2) -> " + secondEnroll);
        if (secondEnroll != RegirstrationResult.SUCCESSFULLY_ENROLLED) {
            throw new AssertionError("Second enrollment failed");
        }
        addedLearners.add("L2 (enrolled in CS101)");

        RegirstrationResult duplicateEnroll = service.enroll("CS101", "L2");
        historyLog.add("enroll(CS101, L2) duplicate -> " + duplicateEnroll);
        if (duplicateEnroll != RegirstrationResult.ALREADY_ENROLLED) {
            throw new AssertionError("Already-enrolled check failed");
        }

        Learner learner3 = new Learner("L3");
        repository.registerLearner(learner3);
        historyLog.add("registerLearner(L3)");

        RegirstrationResult waitlistEnroll = service.enroll("CS101", "L3");
        historyLog.add("enroll(CS101, L3) -> " + waitlistEnroll);
        if (waitlistEnroll != RegirstrationResult.ADDED_TO_WAITLIST) {
            throw new AssertionError("Waitlist enrollment failed");
        }
        addedLearners.add("L3 (waitlisted for CS101)");

        RegistrationResult dropped = service.drop("CS101", "L1");
        historyLog.add("drop(CS101, L1) -> " + dropped.getStatus() +
                ", promoted=" + dropped.getPromotedLearnerId());
        if (dropped.getStatus() != RegistrationResult.Status.DROPPED_SUCCESSFULLY) {
            throw new AssertionError("Drop failed or promoted learner not expected");
        }
        if (dropped.getPromotedLearnerId() != null) {
            addedLearners.add(dropped.getPromotedLearnerId() + " (promoted from waitlist to enrolled)");
        }

        System.out.println("=== Added Learners Group ===");
        for (String added : addedLearners) {
            System.out.println("- " + added);
        }

        System.out.println("\n=== History Log Group ===");
        for (String event : historyLog) {
            System.out.println("- " + event);
        }

        System.out.println("Smoke tests passed");
    }
}
