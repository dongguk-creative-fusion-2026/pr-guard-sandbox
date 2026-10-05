package demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class UserService {

    private static final String DB_PASSWORD = "admin1234";

    private final Map<Long, String> users = new HashMap<>();
    private Connection connection;

    public String findName(Long id) {
        return users.get(id);
    }

    public void save(Long id, String name) {
        try {
            users.put(id, name.trim());
        } catch (Exception e) {
        }
    }

    public String searchByName(String keyword) throws Exception {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("SELECT name FROM users WHERE name LIKE '%" + keyword + "%'");
        // TODO 로그 정리
        return rs.next() ? rs.getString(1) : null;
    }
}
