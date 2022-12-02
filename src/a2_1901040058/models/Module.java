package a2_1901040058.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Module implements Serializable {
    private String code;
    private String name;
    private int semester;
    private int credits;
    private final String LETTER_MODULE = "M";

    public static Map<Integer, Integer> temp = new HashMap<>();

    public Module(String name, int semester, int credits) throws Exception {
        if (!validateName(name) || !validateCredits(credits) || !validateSemester(semester)) {
            throw new Exception("Invalid module name, credits, semester");
        } else {
            this.name = name;
            this.semester = semester;
            this.credits = credits;
            this.code = generateCode(semester);
        }
    }

    public Module(String code, String name, int semester, int credits) throws Exception {
        if (!validateName(name) || !validateCredits(credits) || !validateSemester(semester)) {
            throw new Exception("Invalid module name, credits, semester");
        } else {
            this.name = name;
            this.semester = semester;
            this.credits = credits;
            this.code = code;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (!validateName(name)) {
            return;
        }
        this.name = name;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        if (!validateSemester(semester)) {
            return;
        }
        this.semester = semester;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        if (!validateCredits(credits)) {
            return;
        }
        this.credits = credits;
    }

    private boolean validateName(String name) {
        return name.length() > 0;
    }

    private boolean validateSemester(int semester) {
        return semester > 0;
    }

    private boolean validateCredits(int credits) {
        return credits > 0;
    }

    private String generateCode(int semester) {
        if (temp.containsKey(semester)) {
            int index = temp.get(semester);
            index++;
            temp.put(semester, index);
        } else {
            temp.put(semester, 1);
        }

        return LETTER_MODULE + (100 * semester + temp.get(semester));
    }

    @Override
    public String toString() {
        return "Module{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", semester=" + semester +
                ", credits=" + credits +
                '}';
    }
}
