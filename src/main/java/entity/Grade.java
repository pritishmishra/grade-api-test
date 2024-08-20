package entity;

public class Grade {

    // Refer to the API documentation for the meaning of these fields.
    private String username;
    private String course;
    private int grade;

    public Grade(String utorid, String course, int grade) {
        this.username = utorid;
        this.course = course;
        this.grade = grade;
    }

    public static GradeBuilder builder() {
        return new GradeBuilder();
    }

    public static class GradeBuilder {
        private String username;
        private String course;
        private int grade;

        GradeBuilder() {
        }

        public GradeBuilder username(String username) {
            this.username = username;
            return this;
        }

        public GradeBuilder course(String course) {
            this.course = course;
            return this;
        }

        public GradeBuilder grade(int grade) {
            this.grade = grade;
            return this;
        }

        public Grade build() {
            return new Grade(username, course, grade);
        }
    }

    @Override
    public String toString() {
        return "Grade{" +
                "username='" + username + '\'' +
                ", course='" + course + '\'' +
                ", grade=" + grade +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public String getCourse() {
        return course;
    }

    public int getGrade() {
        return grade;
    }




}
