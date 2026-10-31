package com.registration.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class CourseOffering {
    private final String courseCode;
    private final int capacity;
    private final Set<String> prerequisites = new LinkedHashSet<>();
    private final Set<String> enrolledLearners = new LinkedHashSet<>();
    private final Deque<String> waitlist = new ArrayDeque<>();
    private final ReentrantLock lock = new ReentrantLock();

    public CourseOffering(String courseCode, int capacity) {
        this(courseCode, capacity, List.of());
    }

    public CourseOffering(String courseCode, int capacity, List<String> prerequisites) {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Course code must not be null or blank");
        }
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }
        this.courseCode = courseCode;
        this.capacity = capacity;
        if (prerequisites != null) {
            this.prerequisites.addAll(prerequisites);
        }
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getCapacity() {
        return capacity;
    }

    public Set<String> getPrerequisites() {
        return Collections.unmodifiableSet(prerequisites);
    }

    public boolean isFull() {
        return enrolledLearners.size() >= capacity;
    }

    public boolean isEnrolled(String learnerId) {
        return enrolledLearners.contains(learnerId);
    }

    public boolean isWaitlistEmpty() {
        return waitlist.isEmpty();
    }

    public void addEnrolledLearner(String learnerId) {
        if (learnerId == null || learnerId.isBlank()) {
            throw new IllegalArgumentException("Learner ID must not be null or blank");
        }
        enrolledLearners.add(learnerId);
    }

    public boolean removeEnrolledLearner(String learnerId) {
        return enrolledLearners.remove(learnerId);
    }

    public Set<String> getEnrolledLearners() {
        return Collections.unmodifiableSet(enrolledLearners);
    }

    public void addToWaitlist(String learnerId) {
        if (learnerId == null || learnerId.isBlank()) {
            throw new IllegalArgumentException("Learner ID must not be null or blank");
        }
        if (!waitlist.contains(learnerId)) {
            waitlist.addLast(learnerId);
        }
    }

    public boolean containsWaitlistedLearner(String learnerId) {
        return waitlist.contains(learnerId);
    }

    public String pollWaitlist() {
        return waitlist.pollFirst();
    }

    public Deque<String> getWaitlist() {
        return new ArrayDeque<>(waitlist);
    }

    public ReentrantLock getLock() {
        return lock;
    }
}
