package com.pokemonreview.api.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pokemonreview.api.models.Role;
import com.pokemonreview.api.models.UserEntity;
import com.pokemonreview.api.repository.UserRepository;


@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO - here the correct is receive by parameter the GrantendAuthorits, so you dont need look for this all the time
        // TODO - but to the firts time, this is the correct way, bacause you need charge this informations to put this on the jwt
        UserEntity userEntity = userRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException("Usernname not found"));
        return new User(userEntity.getName(), userEntity.getPassword(), mapRolesToGrantedAuthority(userEntity.getRoles()));
    }

    public Collection<GrantedAuthority> mapRolesToGrantedAuthority(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
