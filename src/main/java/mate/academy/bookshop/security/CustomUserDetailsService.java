package mate.academy.bookshop.security;

import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.exceptions.EntityNotFoundException;
import mate.academy.bookshop.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find user by email: " + email));
    }
}
