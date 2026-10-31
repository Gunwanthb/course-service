package com.registration.service;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCourseRegistration {
    private final Map<String, CourseOffering> courseOfferings = new HashMap<>();
    private final Map<String, Learner> learners = new HashMap<>();

    public void registerCourseOffering(CourseOffering offering) {
        if (offering == null) {
            throw new IllegalArgumentException("Offering must not be null");
        }
        courseOfferings.put(offering.getCourseCode(), offering);
    }

    public void registerLearner(Learner learner) {
        if (learner == null) {
            throw new IllegalArgumentException("Learner must not be null");
        }
        learners.put(learner.getLearnerId(), learner);
    }

    public boolean hasCourse(String courseCode) {
        return courseCode != null && courseOfferings.containsKey(courseCode);
    }

    public boolean hasLearner(String learnerId) {
        return learnerId != null && learners.containsKey(learnerId);
    }

    public CourseOffering getCourseOffering(String courseCode) {
        return courseOfferings.get(courseCode);
    }

    public Learner getLearner(String learnerId) {
        return learners.get(learnerId);
    }

    public boolean meetsPrerequisites(String learnerId, CourseOffering courseOffering) {
        if (courseOffering == null || learnerId == null) {
            return false;
        }
        Learner learner = learners.get(learnerId);
        if (learner == null) {
            return false;
        }
        for (String prerequisite : courseOffering.getPrerequisites()) {
            if (!learner.hasCompletedCourse(prerequisite)) {
                return false;
            }
        }
        return true;
    }
}
