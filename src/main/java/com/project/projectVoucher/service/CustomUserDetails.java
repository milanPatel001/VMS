package com.project.projectVoucher.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class CustomUserDetails implements UserDetails{
    private String apiKey;

    private List<String> permissions;
    private boolean isAdmin;
    private String id;

    public CustomUserDetails(){}

    public CustomUserDetails(String apiKey, List<String> permissions, boolean isAdmin, String id) {
        this.apiKey = apiKey;

        this.permissions = permissions;
        this.isAdmin = isAdmin;
        this.id = id;
    }


    private Collection<GrantedAuthority> mapPermissionsToAuthorities(Collection<String> permissions) {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(isAdmin){
            List<GrantedAuthority> l = new ArrayList<>();
            l.add(new SimpleGrantedAuthority("admin"));
            return l;
        }
        return mapPermissionsToAuthorities(permissions);
    }


    public String getApiKey() {
        return apiKey;
    }


    public boolean isAdmin(){
        return isAdmin;
    }

    public String getId(){
        return id;
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

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return apiKey;
    }

}
