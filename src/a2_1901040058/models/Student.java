package a2_1901040058.models;

import java.util.Calendar;
import java.util.regex.Pattern;

public class Student {
    private String id;
    private String name;
    private String dob;
    private String address;
    private String email;
    private final String LETTER_STUDENT = "S";
    private static int year = Calendar.getInstance().get(Calendar.YEAR);

    public Student(String name, String dob, String address, String email) throws Exception {
        if (!validateName(name) || !validateDob(dob) || !validateAddress(address) || !validateEmail(email)) {
            throw new Exception("Invalid name, dob, address, email");
        } else {
            this.name = name;
            this.dob = dob;
            this.address = address;
            this.email = email;
            this.id = generateId();
        }
    }

    private String generateId() {
        return LETTER_STUDENT + year++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        if (!validateDob(dob)) {
            return;
        }
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (!validateAddress(address)) {
            return;
        }
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (!validateEmail(email)) {
            return;
        }
        this.email = email;
    }

    private boolean validateName(String name) {
        return name.length() > 0;
    }

    private boolean validateDob(String dob) {
        return dob.length() > 0;
    }

    private boolean validateAddress(String address) {
        return address.length() > 0;
    }

    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex);

        return emailPattern.matcher(email).matches();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
