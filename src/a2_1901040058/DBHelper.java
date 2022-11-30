package a2_1901040058;

import a2_1901040058.models.CompulsoryModule;
import a2_1901040058.models.ElectiveModule;
import a2_1901040058.models.Student;
import a2_1901040058.models.Module;

import java.sql.*;

public class DBHelper {
    private static Connection connection;
    public static Connection getConnection() throws Exception {
        connection = DriverManager.getConnection("jdbc:sqlite:database.sqlite3");
        return connection;
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
}
