package mini_project.server.repository;

import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MongoRespository {
    
    @Autowired
    private MongoTemplate template;

    public final static String C_CARTS = "carts";

    public Optional<Document> getCart(String customerId) {
        return Optional.ofNullable(template.findById(customerId, Document.class, C_CARTS));
    }
}
