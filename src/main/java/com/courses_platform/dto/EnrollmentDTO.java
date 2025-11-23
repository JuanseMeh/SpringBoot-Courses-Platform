package com.courses_platform.dto;

public class EnrollmentDTO {
    private Long userId;
    private Long courseId;

    public EnrollmentDTO() {}

    public EnrollmentDTO(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }
}
