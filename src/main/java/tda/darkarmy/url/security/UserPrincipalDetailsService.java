package tda.darkarmy.url.security;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tda.darkarmy.url.model.User;
import tda.darkarmy.url.repository.UserRepository;

@Slf4j
@Service
@AllArgsConstructor
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(s);
        UserPrincipal userPrincipal = new UserPrincipal(user);

        log.info("Name "+user.getName());

        return userPrincipal;
    }
}
