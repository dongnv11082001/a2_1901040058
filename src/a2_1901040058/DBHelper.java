package a2_1901040058;

import a2_1901040058.models.CompulsoryModule;
import a2_1901040058.models.ElectiveModule;
import a2_1901040058.models.Enrolment;
import a2_1901040058.models.Module;
import a2_1901040058.models.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBHelper {
    private static Connection connection;

    public static Connection getConnection() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:src/a2_1901040058/database.sqlite3");
        if (connection.isClosed()) {
            System.out.println("DB disconnected");
        } else {
            System.out.println("DB connected");
        }
        return connection;
    }

    public static List<Student> getAllStudents() throws Exception {
        String sql = "SELECT * FROM students";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        ArrayList<Student> listStudent = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("id");
            String name = rs.getString("name");
            String address = rs.getString("address");
            String email = rs.getString("email");
            String dob = rs.getString("dob");
            listStudent.add(new Student(id, name, dob, address, email));
        }

        return listStudent;
    }

    public static List<Module> getAllModules() throws Exception {
        String sql = "SELECT * FROM modules";
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(sql);

        ArrayList<Module> listModule = new ArrayList<>();
        while (rs.next()) {
            String code = rs.getString("code");
            String name = rs.getString("name");
            int credits = rs.getInt("credits");
            int semester = rs.getInt("semester");
            listModule.add(new Module(code, name, semester, credits));
        }
        return listModule;
    }

    public static void createStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students(id,name,address, email, dob) VALUES(?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, student.getId());
        ps.setString(2, student.getName());
        ps.setString(3, student.getAddress());
        ps.setString(4, student.getEmail());
        ps.setString(5, student.getDob());
        ps.executeUpdate();
    }

    public static void createElectiveModule(ElectiveModule module) throws SQLException {
        String sql = "INSERT INTO modules(code,name,semester,credits,department) VALUES(?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, module.getCode());
        ps.setString(2, module.getName());
        ps.setInt(3, module.getSemester());
        ps.setInt(4, module.getCredits());
        ps.setString(5, module.getDepartmentName());
        ps.executeUpdate();
    }

    public static void createCompulsoryModule(CompulsoryModule module) throws SQLException {
        String sql = "INSERT INTO modules(code,name,semester,credits) VALUES(?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, module.getCode());
        ps.setString(2, module.getName());
        ps.setInt(3, module.getSemester());
        ps.setInt(4, module.getCredits());
        ps.executeUpdate();
    }

    public static void createEnrolment(Enrolment enrolment) throws SQLException {
        String sql = "INSERT INTO enrolments(s_id,s_name,m_code,m_name,internal,examination,finalGrade) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, enrolment.getStudent().getId());
        ps.setString(2, enrolment.getStudent().getName());
        ps.setString(3, enrolment.getModule().getCode());
        ps.setString(4, enrolment.getModule().getName());
        ps.setDouble(5, enrolment.getInternalMark());
        ps.setDouble(6, enrolment.getExaminationMark());
        ps.setString(7, String.valueOf(enrolment.getFinalGrade()));
        ps.executeUpdate();
    }
}
