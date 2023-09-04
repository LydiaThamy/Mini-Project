package mini_project.server.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SqlRepository {
    
    @Autowired
    private JdbcTemplate template;

    public static final String GET_CATEGORIES_SQL = "select category from category";

    public List<String> getCategories() {
        return template.queryForList(GET_CATEGORIES_SQL, String.class);
    }
}
