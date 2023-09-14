package mini_project.server.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import mini_project.server.model.User;
import mini_project.server.model.UserPrincipal;
import mini_project.server.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepo.getUserByUsername(username);
        if (user.isPresent()) 
            return new UserPrincipal(user.get());
        
        throw new UsernameNotFoundException("%s not found".formatted(username));
    }

}
