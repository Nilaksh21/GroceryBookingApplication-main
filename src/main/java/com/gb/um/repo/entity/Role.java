package com.gb.um.repo.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gb.um.repo.entity.Permission.*;


@RequiredArgsConstructor
public enum Role {

    USER(
            Set.of(
                    USER_READ,
                    USER_WRITE
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_WRITE,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    USER_READ,
                    USER_WRITE
            )
    );

    @Getter
    public final Set<Permission> permissions;

    /**
     * Get all authorities assigned to an role
     * @return List of SimpleGrantedAuthorities from given role and permissions
     */
    public List<SimpleGrantedAuthority> getAuthorities() {
        //convert permissions and roles to Simple Granted Authority
        List<SimpleGrantedAuthority> grantedAuthorities =
                this.permissions.stream()
                        .map(permission -> new SimpleGrantedAuthority(permission.getPermissions()))
                        .collect(Collectors.toList());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
