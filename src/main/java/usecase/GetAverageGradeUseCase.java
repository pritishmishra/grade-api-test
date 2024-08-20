package usecase;

import api.GradeDataBase;

/**
 * GetAverageGradeUseCase class.
 */
public final class GetAverageGradeUseCase {
    private final GradeDataBase gradeDataBase;

    public GetAverageGradeUseCase(GradeDataBase gradeDataBase) {
        this.gradeDataBase = gradeDataBase;
    }

    /**
     * Get the average grade for a course.
     * @param course The course.
     * @return The average grade.
     */
    public float getAverageGrade(String course) {
        // TODO: Get average grade for all students in your team.
        return 0.0f;
    }
}
