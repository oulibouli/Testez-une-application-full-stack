package com.openclassrooms.starterjwt.unitaire.security.services;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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
    @Tag("Security")
    @DisplayName("Test getAuthorities should return an empty collection")
    void testGetAuthorities() {
        // Act: Get the authorities from the UserDetailsImpl instance
        Collection<? extends GrantedAuthority> authorities = userDetailsImpl.getAuthorities();

        // Assert: Verify that the authorities collection is empty
        assertTrue(authorities.isEmpty(), "Authorities should be empty");
        assertEquals(0, authorities.size(), "Authorities size should be 0");
    }

    @Test
    @Tag("Security")
    @DisplayName("Test isAccountNonExpired should return true")
    void testIsAccountNonExpired() {
        // Assert: Verify that the account is not expired
        assertTrue(userDetailsImpl.isAccountNonExpired());
    }

    @Test
    @Tag("Security")
    @DisplayName("Test isAccountNonLocked should return true")
    void testIsAccountNonLocked() {
        // Assert: Verify that the account is not locked
        assertTrue(userDetailsImpl.isAccountNonLocked());
    }

    @Test
    @Tag("Security")
    @DisplayName("Test isCredentialsNonExpired should return true")
    void testIsCredentialsNonExpired() {
        // Assert: Verify that the credentials are not expired
        assertTrue(userDetailsImpl.isCredentialsNonExpired());
    }

    @Test
    @Tag("Security")
    @DisplayName("Test isEnabled should return true")
    void testIsEnabled() {
        // Assert: Verify that the user is enabled
        assertTrue(userDetailsImpl.isEnabled());
    }

    @Test
    @Tag("Equality")
    @DisplayName("Test equals method with same instance should return true")
    void testIsEqual() {
        // Arrange: Create a UserDetailsImpl instance
        UserDetailsImpl user = new UserDetailsImpl(1L, "username", "firstName", "lastName", false, "password");

        // Assert: Verify that the instance is equal to itself
        assertTrue(user.equals(user));
    }

    @Test
    @Tag("Equality")
    @DisplayName("Test equals method with null should return false")
    void testIsNotEqualNull() {
        // Arrange: Create a UserDetailsImpl instance
        UserDetailsImpl user = new UserDetailsImpl(1L, "username", "firstName", "lastName", false, "password");

        // Assert: Verify that the instance is not equal to null
        assertFalse(user.equals(null));
    }

    @Test
    @Tag("Equality")
    @DisplayName("Test equals method with different class should return false")
    void testIsNotEqualDifferentClass() {
        // Arrange: Create a UserDetailsImpl instance
        UserDetailsImpl user = new UserDetailsImpl(1L, "username", "firstName", "lastName", false, "password");

        // Arrange: Create an instance of a different class
        Object differentClassObject = new Object();

        // Assert: Verify that the instance is not equal to an object of a different class
        assertFalse(user.equals(differentClassObject));
    }

    @Test
    @Tag("Equality")
    @DisplayName("Test equals method with different IDs should return false")
    void testIsNotEqualDifferentId() {
        // Arrange: Create two UserDetailsImpl instances with different IDs
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "username1", "firstName1", "lastName1", false, "password1");
        UserDetailsImpl user2 = new UserDetailsImpl(2L, "username2", "firstName2", "lastName2", false, "password2");

        // Assert: Verify that the instances are not equal due to different IDs
        assertFalse(user1.equals(user2));
    }

    @Test
    @Tag("Equality")
    @DisplayName("Test equals method with same IDs should return true")
    void testIsEqualSameId() {
        // Arrange: Create two UserDetailsImpl instances with the same ID but different attributes
        UserDetailsImpl user1 = new UserDetailsImpl(1L, "username1", "firstName1", "lastName1", false, "password1");
        UserDetailsImpl user2 = new UserDetailsImpl(1L, "username2", "firstName2", "lastName2", false, "password2");

        // Assert: Verify that the instances are equal due to the same ID
        assertTrue(user1.equals(user2));
    }
}