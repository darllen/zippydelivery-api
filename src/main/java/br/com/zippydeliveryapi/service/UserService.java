package br.com.zippydeliveryapi.service;

import br.com.zippydeliveryapi.model.User;
import br.com.zippydeliveryapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public User save(User user) {
        user.setEnabled(Boolean.TRUE);
        user.setUsername(user.getUsername());
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    @Transactional
    public User findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return this.userRepository.findById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User usuario = this.userRepository.findByUsername(username);
        if (usuario == null || !usuario.getEnabled()) {
            throw new UsernameNotFoundException("User not found or disabled");
        }
        return new org.springframework.security.core.userdetails.User(usuario.getUsername(), usuario.getPassword(), new ArrayList<>());
    }

}
