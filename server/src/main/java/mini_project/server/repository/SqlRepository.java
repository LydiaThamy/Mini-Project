package mini_project.server.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mini_project.server.model.Business;
import mini_project.server.model.Review;
import mini_project.server.model.Search;
import mini_project.server.model.Service;

@Repository
public class SqlRepository {

    @Autowired
    private JdbcTemplate template;

    // autocomplete
    public static final String GET_NAMES_SQL_BY_KEYWORD = "select distinct business_name from business where business_name like ?";
    public static final String GET_ADDRESSES_SQL_BY_KEYWORD = "select distinct address from business where address like ?";
    // category
    public static final String GET_CATEGORIES_SQL = "select distinct category from business";
    // search by category
    public static final String GET_BUSINESSES_SQL_BY_CATEGORY = "select * from business where category = ?";
    // search by keyword
    public static final String GET_BUSINESSES_SQL = "select * from business where business_name like ? or address like ?";
    // get business by id
    public static final String GET_BUSINESS_SQL_BY_ID = "select * from business where business_id = ?";
    public static final String GET_SERVICES_SQL_BY_BUSINESS_ID = "select * from service where business_id = ?";
    public static final String GET_REVIEWS_SQL_BY_BUSINESS_ID = "select * from review where business_id = ?";

    public List<String> autocompleteKeyword(String keyword) {

        String wildcard = "%" + keyword + "%";
        List<String> autocomplete = new LinkedList<>();

        // name
        List<String> names = template.queryForList(GET_NAMES_SQL_BY_KEYWORD, String.class, wildcard);
        if (!names.isEmpty())
            autocomplete.addAll(names);

        // address
        List<String> addresses = template.queryForList(GET_ADDRESSES_SQL_BY_KEYWORD, String.class, wildcard);
        if (!addresses.isEmpty())
            autocomplete.addAll(addresses);

        return autocomplete;
    }

    public List<String> getCategories() {
        return template.queryForList(GET_CATEGORIES_SQL, String.class);
    }

    public Optional<List<Business>> getBusinessesByCategory(String category) {

        List<Business> result;

        result = template.query(GET_BUSINESSES_SQL_BY_CATEGORY, new BeanPropertyRowMapper<>(Business.class),
                category);

        if (result.isEmpty())
            return Optional.empty();

        return Optional.of(result);
    }

    public Optional<List<Business>> getBusinesses(Search search) {

        StringBuilder sqlBuilder = new StringBuilder(GET_BUSINESSES_SQL); 
        
        String keyword = "%" + search.getKeyword() + "%";
        String[] category = search.getCategory();
        String[] region = search.getRegion();

        // if (search.getCategory().length > 0) {
           for (String c:category) {
            sqlBuilder.append(" and category = " + c);
           }
        // }

        // if (search.getRegion().length > 0) {
           for (String r:region) {
            sqlBuilder.append(" and region = " + r);
           }
        // }
        
        List<Business> result = template.query(sqlBuilder.toString(), new BeanPropertyRowMapper<>(Business.class), keyword, keyword);

        if (result.isEmpty())
            return Optional.empty();

        return Optional.of(result);
    }

    public Optional<Business> getBusinessById(Integer id) {
        return Optional.ofNullable(
            template.query(GET_BUSINESS_SQL_BY_ID, new BeanPropertyRowMapper<>(Business.class), id).get(0)
        );
    }

    public List<Service> getServicesByBusinessId(Integer id) {
        return template.query(GET_SERVICES_SQL_BY_BUSINESS_ID, new BeanPropertyRowMapper<>(Service.class), id);
    }

    public List<Review> getReviewsByBusinessId(Integer id) {
        return template.query(GET_REVIEWS_SQL_BY_BUSINESS_ID, new BeanPropertyRowMapper<>(Review.class), id);
    }

}
