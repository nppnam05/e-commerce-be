package com.e_commerce.e_commerce_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.exception.NotFoundException;
import com.e_commerce.e_commerce_api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String value) throws UsernameNotFoundException {
        return userRepository.findByEmail(value)
                .orElseThrow(() -> new NotFoundException("Not found User Email: " + value));
    }
}