package mini_project.server.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mini_project.server.model.User;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate template;

    public static final String INSERT_NEW_USER = "insert into user values (?, ?, ?)";
    public static final String GET_USER_BY_ID = "select * from user where user_id = ?";

    public Integer saveUser(User user) {
        // public Integer saveUser(String userId, String email, String username) {
        return template.update(INSERT_NEW_USER, user.getUserId(), user.getUsername(), user.getEmail());
    }

    public Optional<User> getUser(String userId) {
        System.out.println("User Id: " + userId);

        List<User> result = template.query(GET_USER_BY_ID, new BeanPropertyRowMapper<>(User.class), userId);

        if (result.isEmpty())
            return Optional.empty();

        return Optional.of(result.get(0));
    }
}
