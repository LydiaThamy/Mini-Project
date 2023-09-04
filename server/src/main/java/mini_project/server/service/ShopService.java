package mini_project.server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mini_project.server.repository.SqlRepository;

@Service
public class ShopService {
    
    @Autowired
    private SqlRepository sqlRepo;

    public List<String> getCategories() {
        return sqlRepo.getCategories();
    }
}
