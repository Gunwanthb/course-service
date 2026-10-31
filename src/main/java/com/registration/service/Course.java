package com.registration.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Course {
    private final String courseCode;
    private final List<String> prerequisites;

    public Course(String courseCode) {
        this(courseCode, Collections.emptyList());
    }

    public Course(String courseCode, List<String> prerequisites) {
        if (courseCode == null || courseCode.isBlank()) {
            throw new IllegalArgumentException("Course code must not be null or blank");
        }
        this.courseCode = courseCode;
        this.prerequisites = new ArrayList<>(prerequisites == null ? Collections.emptyList() : prerequisites);
    }

    public String getCourseCode() {
        return courseCode;
    }

    public List<String> getPrerequisites() {
        return Collections.unmodifiableList(prerequisites);
    }
}
