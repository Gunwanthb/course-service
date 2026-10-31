package com.registration.service;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class Learner {
    private final String learnerId;
    private final Set<String> completedCourses = new LinkedHashSet<>();

    public Learner(String learnerId) {
        if (learnerId == null || learnerId.isBlank()) {
            throw new IllegalArgumentException("Learner ID must not be null or blank");
        }
        this.learnerId = learnerId;
    }

    public String getLearnerId() {
        return learnerId;
    }

    public Set<String> getCompletedCourses() {
        return Collections.unmodifiableSet(completedCourses);
    }

    public boolean hasCompletedCourse(String courseCode) {
        return completedCourses.contains(courseCode);
    }

    public void completeCourse(String courseCode) {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Course code must not be null or blank");
        }
        completedCourses.add(courseCode);
    }
}
