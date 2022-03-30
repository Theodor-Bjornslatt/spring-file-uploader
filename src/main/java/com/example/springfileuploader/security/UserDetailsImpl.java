package com.example.springfileuploader.security;

import com.example.springfileuploader.repository.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A customized implementation of the Spring Security UserDetails.
 * Some unused values have been defaulted to always be true due to the simplicity of the application.
 */
public class UserDetailsImpl implements UserDetails {
    private final User user;

    public UserDetailsImpl(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach((role) -> authorities.add(new SimpleGrantedAuthority(role)));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * @return the email of the user.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
