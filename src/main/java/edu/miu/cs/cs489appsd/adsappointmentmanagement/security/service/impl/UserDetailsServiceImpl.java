package edu.miu.cs.cs489appsd.adsappointmentmanagement.security.service.impl;

import edu.miu.cs.cs489appsd.adsappointmentmanagement.model.User;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.repository.UserRepository;
import edu.miu.cs.cs489appsd.adsappointmentmanagement.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return new CustomUserDetails(user);
    }
}