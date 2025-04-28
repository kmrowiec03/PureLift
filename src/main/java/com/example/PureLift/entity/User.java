package com.example.PureLift.entity;

import com.example.PureLift.AppUserRole;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Setter
@Getter
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private AppUserRole appuserRole;
    private boolean enabled;
    private boolean locked;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TrainingPlan> trainingPlans;

    public User(boolean locked,
                boolean enabled,
                AppUserRole appuserRole,
                String password,
                String email,
                String username,
                String name) {
        this.locked = locked;
        this.enabled = enabled;
        this.appuserRole = appuserRole;
        this.password = password;
        this.email = email;
        this.username = username;
        this.name = name;
    }

    public User() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(appuserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}
