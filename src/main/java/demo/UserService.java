package demo;

import java.util.HashMap;
import java.util.Map;

public class UserService {

    private final Map<Long, String> users = new HashMap<>();

    public String findName(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is required");
        }
        String name = users.get(id);
        if (name == null) {
            throw new IllegalStateException("user not found: " + id);
        }
        return name;
    }

    public void save(Long id, String name) {
        try {
            users.put(id, name.trim());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("name is required", e);
        }
    }
}
