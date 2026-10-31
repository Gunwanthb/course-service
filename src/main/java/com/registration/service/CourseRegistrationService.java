package com.registration.service;

import java.util.concurrent.locks.ReentrantLock;

public class CourseRegistrationService {
    private final InMemoryCourseRegistration registration;

    public CourseRegistrationService(InMemoryCourseRegistration registration) {
        if (registration == null) {
            throw new IllegalArgumentException("Registration repository must not be null");
        }
        this.registration = registration;
    }

    public InMemoryCourseRegistration getRegistration() {
        return registration;
    }

    public RegirstrationResult enroll(String courseCode, String learnerId) {
        validateCourseAndLearner(courseCode, learnerId);

        CourseOffering offering = registration.getCourseOffering(courseCode);
        ReentrantLock lock = offering.getLock();
        lock.lock();
        try {
            if (!registration.meetsPrerequisites(learnerId, offering)) {
                return RegirstrationResult.REQUIREMENTS_NOT_MET;
            }
            if (offering.isEnrolled(learnerId)) {
                return RegirstrationResult.ALREADY_ENROLLED;
            }
            if (offering.isFull()) {
                offering.addToWaitlist(learnerId);
                return RegirstrationResult.ADDED_TO_WAITLIST;
            }
            offering.addEnrolledLearner(learnerId);
            return RegirstrationResult.SUCCESSFULLY_ENROLLED;
        } finally {
            lock.unlock();
        }
    }

    public RegistrationResult drop(String courseCode, String learnerId) {
        validateCourseAndLearner(courseCode, learnerId);

        CourseOffering offering = registration.getCourseOffering(courseCode);
        ReentrantLock lock = offering.getLock();
        lock.lock();
        try {
            if (!offering.isEnrolled(learnerId)) {
                return RegistrationResult.NOT_ENROLLED;
            }

            offering.removeEnrolledLearner(learnerId);

            String promotedLearnerId = null;
            while (!offering.isWaitlistEmpty()) {
                String candidate = offering.pollWaitlist();
                if (candidate == null) {
                    break;
                }
                if (registration.meetsPrerequisites(candidate, offering)) {
                    offering.addEnrolledLearner(candidate);
                    promotedLearnerId = candidate;
                    break;
                }
            }
            return RegistrationResult.droppedSuccessfully(promotedLearnerId);
        } finally {
            lock.unlock();
        }
    }

    private void validateCourseAndLearner(String courseCode, String learnerId) {
        if (!registration.hasCourse(courseCode)) {
            throw new IllegalArgumentException("Course not found: " + courseCode);
        }
        if (!registration.hasLearner(learnerId)) {
            throw new IllegalArgumentException("Learner not found: " + learnerId);
        }
    }
}
