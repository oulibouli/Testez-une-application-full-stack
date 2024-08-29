package com.openclassrooms.starterjwt.unitaire.security.services;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ExtendWith(MockitoExtension.class)
public class UserDetailsImplTest {
    @InjectMocks
    private UserDetailsImpl userDetailsImpl;

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetailsImpl.getAuthorities();

        assertTrue(authorities.isEmpty(), "Authorities should be empty");
        assertEquals(0, authorities.size(), "Authorities size should be 0");
    }

    @Test
    void testIsAccoundNonExpired() {
        assertTrue(userDetailsImpl.isAccountNonExpired());
    }
    @Test
    void testIsAccountNonLocked() {
        assertTrue(userDetailsImpl.isAccountNonLocked());
    }
    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(userDetailsImpl.isCredentialsNonExpired());
    }
    @Test
    void testIsEnabled() {
        assertTrue(userDetailsImpl.isEnabled());
    }

    @Test
    void testIsEqual() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "username", "firstName", "lastName", false, "password");
        assertTrue(user.equals(user));
    }
    @Test
    void testIsNotEqual() {
        UserDetailsImpl user = new UserDetailsImpl(1L, "username", "firstName", "lastName", false, "password");
        assertFalse(user.equals(null));
    }
}
