package a2_1901040058.models;

public class Enrolment implements Comparable<Enrolment> {
    private Student student;
    private Module module;
    private float internalMark;
    private float examinationMark;
    private char finalGrade;

    public Enrolment(Student student, Module module, float internalMark, float examinationMark) {
        if (!validateStudent(student) || !validateModule(module) || !validateInternalMark(internalMark) || !validateExaminationMark(examinationMark)) {
            System.err.println("Invalid student, module, internal mark, examination mark");
        } else {
            this.student = student;
            this.module = module;
            this.internalMark = internalMark;
            this.examinationMark = examinationMark;
            this.finalGrade = generateGrade();
        }
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        if (!validateStudent(student)) {
            return;
        }
        this.student = student;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        if (!validateModule(module)) {
            return;
        }
        this.module = module;
    }

    public float getInternalMark() {
        return internalMark;
    }

    public void setInternalMark(float internalMark) {
        if (!validateInternalMark(internalMark)) {
            return;
        }
        this.internalMark = internalMark;
    }

    public float getExaminationMark() {
        return examinationMark;
    }

    public void setExaminationMark(float examinationMark) {
        if (!validateExaminationMark(examinationMark)) {
            return;
        }
        this.examinationMark = examinationMark;
    }

    public char getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(char finalGrade) {
        this.finalGrade = finalGrade;
    }

    private boolean validateStudent(Student student) {
        return student != null;
    }

    private boolean validateModule(Module module) {
        return module != null;
    }

    private boolean validateInternalMark(float internalMark) {
        return internalMark > 0;
    }

    private boolean validateExaminationMark(float examinationMark) {
        return examinationMark > 0;
    }

    private char generateGrade() {
        double aggregateMark = 0.4 * internalMark + 0.6 * examinationMark;

        if (aggregateMark > 8) {
            return 'E';
        } else if (aggregateMark > 5) {
            return 'G';
        } else if (aggregateMark == 5) {
            return 'P';
        } else {
            return 'F';
        }
    }

    @Override
    public String toString() {
        return "Enrolment{" +
                "Student=" + student +
                ", Module=" + module +
                ", Internal Mark=" + internalMark +
                ", Examination Mark=" + examinationMark +
                ", Final Grade=" + finalGrade +
                '}';
    }

    @Override
    public int compareTo(Enrolment o) {
        return 0;
    }
}
