package com.courses_platform.domain.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "enrollment")
public class Enrollment {

    @EmbeddedId
    private EnrollmentId id;

    @ManyToOne(optional = false)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false)
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    public Enrollment() {}

    public Enrollment(User user, Course course) {
        this.user = user;
        this.course = course;
        this.id = new EnrollmentId(user.getId(), course.getId());
    }

    public EnrollmentId getId() {
        return id;
    }

    public void setId(EnrollmentId id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Enrollment)) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Embeddable
    public static class EnrollmentId implements Serializable {

        private static final long serialVersionUID = 1L;

        @Column(name = "user_id")
        private Long userId;

        @Column(name = "course_id")
        private Long courseId;

        public EnrollmentId() {}

        public EnrollmentId(Long userId, Long courseId) {
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

        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(!(o instanceof EnrollmentId)) return false;
            EnrollmentId that = (EnrollmentId) o;
            return Objects.equals(getUserId(), that.getUserId()) &&
                    Objects.equals(getCourseId(), that.getCourseId());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUserId(), getCourseId());
        }
    }
}
