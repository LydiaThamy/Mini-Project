package mini_project.server.repository;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mini_project.server.model.Business;

@Repository
public class SqlRepository {

    @Autowired
    private JdbcTemplate template;

    public static final String GET_NAMES_SQL_BY_KEYWORD = "select distinct business_name from business where business_name like ?";
    public static final String GET_ADDRESSES_SQL_BY_KEYWORD = "select distinct address from business where address like ?";
    public static final String GET_REGIONS_SQL_BY_KEYWORD = "select distinct region from business where region like ?";
    public static final String GET_CATEGORIES_SQL = "select distinct category from business";
    public static final String GET_BUSINESSES_SQL = "select * from business where business_name like ? or address like ? or region like ?";
    public static final String GET_BUSINESSES_SQL_BY_CATEGORY = "select * from business where category = ?";
    // public static final String GET_BUSINESSES_SQL_BY_REGION = "select * from business where category = ? and region = ?";

    public Optional<List<String>> autocompleteKeyword(String keyword) {

        String wildcard = "%" + keyword + "%";

        List<String> autocomplete = new LinkedList<>();

        // name
        List<String> names = template.queryForList(GET_NAMES_SQL_BY_KEYWORD, String.class, wildcard);
        if (!names.isEmpty())
            autocomplete.addAll(names);

        // template.queryForStream(GET_NAMES_SQL_BY_KEYWORD, new
        // BeanPropertyRowMapper<>(String.class), keyword)
        // .forEach(data -> autocomplete.add(data));

        // address
        List<String> addresses = template.queryForList(GET_ADDRESSES_SQL_BY_KEYWORD, String.class, wildcard);
        if (!addresses.isEmpty())
            autocomplete.addAll(addresses);

        // template.queryForStream(GET_ADDRESSES_SQL_BY_KEYWORD, new
        // BeanPropertyRowMapper<>(String.class), keyword)
        // .forEach(data -> autocomplete.add(data));

        // region
        List<String> regions = template.queryForList(GET_REGIONS_SQL_BY_KEYWORD, String.class, wildcard);
        if (!regions.isEmpty())
            autocomplete.addAll(regions);

        // template.queryForStream(GET_REGIONS_SQL_BY_KEYWORD, new
        // BeanPropertyRowMapper<>(String.class), keyword)
        // .forEach(data -> autocomplete.add(data));

        if (autocomplete.isEmpty())
            return Optional.empty();

        return Optional.of(autocomplete);
    }

    public List<String> getCategories() {
        return template.queryForList(GET_CATEGORIES_SQL, String.class);
    }

    public Optional<List<Business>> getBusinesses(String keyword) {

        String wildcard = "%" + keyword + "%";

        List<Business> result = template.query(GET_BUSINESSES_SQL, new BeanPropertyRowMapper<>(Business.class), wildcard, wildcard, wildcard);

        if (result.isEmpty())
            return Optional.empty();

        return Optional.of(result);
    }

    public Optional<List<Business>> getBusinessesByCategory(String category) {

        List<Business> result;

            result = template.query(GET_BUSINESSES_SQL_BY_CATEGORY, new BeanPropertyRowMapper<>(Business.class),
                    category);

        if (result.isEmpty())
            return Optional.empty();

        return Optional.of(result);
    }

    // public Optional<List<Business>> getBusinesses(String category, Optional<String> region) {

    //     List<Business> result;

    //     if (region.isEmpty()) {
    //         result = template.query(GET_BUSINESSES_SQL_BY_CATEGORY, new BeanPropertyRowMapper<>(Business.class),
    //                 category);

    //     } else {
    //         result = template.query(GET_BUSINESSES_SQL_BY_REGION, new BeanPropertyRowMapper<>(Business.class), category,
    //                 region.get());
    //     }

    //     if (result.isEmpty())
    //         return Optional.empty();

    //     return Optional.of(result);
    // }
}
