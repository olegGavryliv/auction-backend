package com.havryliv.auction.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"id", "username"}, callSuper = false)
@Builder
@Entity
@Table(name = "users")
public class User extends BaseEntityAudit implements UserDetails {

    @Id
    @GeneratedValue(generator = "users_generator",  strategy = GenerationType.AUTO )
    @SequenceGenerator(
            name = "users_generator",
            sequenceName = "users_sequence",
            allocationSize=1
    )
    @Column(nullable = false)
    protected Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role")
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Product> products = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "bidder", cascade = CascadeType.REMOVE)
    private List<Bid> bids = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "reviewer", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorities == null && userRoles != null) {
            List<SimpleGrantedAuthority> grantedAuthorityList = userRoles.stream()
                    .map(r -> new SimpleGrantedAuthority(r.name().toUpperCase()))
                    .collect(Collectors.toList());
            return grantedAuthorityList;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public String getUsername() {
        return username;
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
