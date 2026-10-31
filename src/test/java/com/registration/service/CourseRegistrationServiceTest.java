package com.registration.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CourseRegistrationServiceTest {

    @Test
    void enrollShouldSucceedWhenEligibleAndSpaceAvailable() {
        InMemoryCourseRegistration repository = new InMemoryCourseRegistration();
        CourseOffering offering = new CourseOffering("CS101", 2, List.of());
        Learner learner = new Learner("L1");

        repository.registerCourseOffering(offering);
        repository.registerLearner(learner);

        CourseRegistrationService service = new CourseRegistrationService(repository);

        RegirstrationResult result = service.enroll("CS101", "L1");

        assertEquals(RegirstrationResult.SUCCESSFULLY_ENROLLED, result);
        assertEquals(1, offering.getEnrolledLearners().size());
    }

    @Test
    void enrollShouldRejectWhenPrerequisitesNotMet() {
        InMemoryCourseRegistration repository = new InMemoryCourseRegistration();
        CourseOffering offering = new CourseOffering("CS201", 2, List.of("CS101"));
        Learner learner = new Learner("L2");

        repository.registerCourseOffering(offering);
        repository.registerLearner(learner);

        CourseRegistrationService service = new CourseRegistrationService(repository);

        RegirstrationResult result = service.enroll("CS201", "L2");

        assertEquals(RegirstrationResult.REQUIREMENTS_NOT_MET, result);
    }

    @Test
    void enrollShouldAddToWaitlistWhenCourseIsFull() {
        InMemoryCourseRegistration repository = new InMemoryCourseRegistration();
        CourseOffering offering = new CourseOffering("CS301", 1, List.of());
        Learner firstLearner = new Learner("L1");
        Learner secondLearner = new Learner("L2");

        repository.registerCourseOffering(offering);
        repository.registerLearner(firstLearner);
        repository.registerLearner(secondLearner);

        CourseRegistrationService service = new CourseRegistrationService(repository);
        service.enroll("CS301", "L1");

        RegirstrationResult result = service.enroll("CS301", "L2");

        assertEquals(RegirstrationResult.ADDED_TO_WAITLIST, result);
        assertEquals(1, offering.getEnrolledLearners().size());
        assertEquals(1, offering.getWaitlist().size());
    }

    @Test
    void dropShouldReturnNotEnrolledWhenLearnerIsNotCurrentlyEnrolled() {
        InMemoryCourseRegistration repository = new InMemoryCourseRegistration();
        CourseOffering offering = new CourseOffering("CS401", 2, List.of());
        Learner learner = new Learner("L3");

        repository.registerCourseOffering(offering);
        repository.registerLearner(learner);

        CourseRegistrationService service = new CourseRegistrationService(repository);

        RegistrationResult result = service.drop("CS401", "L3");

        assertEquals(RegistrationResult.Status.NOT_ENROLLED, result.getStatus());
    }

    @Test
    void dropShouldPromoteEligibleWaitlistedLearner() {
        InMemoryCourseRegistration repository = new InMemoryCourseRegistration();
        CourseOffering offering = new CourseOffering("CS500", 1, List.of());
        Learner learnerOne = new Learner("L1");
        Learner learnerTwo = new Learner("L2");
        learnerTwo.completeCourse("CS101");

        repository.registerCourseOffering(offering);
        repository.registerLearner(learnerOne);
        repository.registerLearner(learnerTwo);

        CourseRegistrationService service = new CourseRegistrationService(repository);
        service.enroll("CS500", "L1");
        service.enroll("CS500", "L2");

        RegistrationResult result = service.drop("CS500", "L1");

        assertEquals(RegistrationResult.Status.DROPPED_SUCCESSFULLY, result.getStatus());
        assertNotNull(result.getPromotedLearnerId());
        assertEquals("L2", result.getPromotedLearnerId());
        assertEquals(1, offering.getEnrolledLearners().size());
        assertEquals("L2", offering.getEnrolledLearners().iterator().next());
    }
}
