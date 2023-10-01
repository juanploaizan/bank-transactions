package com.transaccionesbancarias.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transaccionesbancarias.model.ERole;
import com.transaccionesbancarias.model.Role;
import com.transaccionesbancarias.model.User;
import com.transaccionesbancarias.payload.request.SignupRequest;
import com.transaccionesbancarias.payload.response.MessageResponse;
import com.transaccionesbancarias.repository.RoleRepository;
import com.transaccionesbancarias.repository.UserRepository;
import com.transaccionesbancarias.security.jwt.JwtUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class AuthControllerTest {
    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(true);
        RoleRepository roleRepository = mock(RoleRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(new HashSet<>());
        signUpRequest.setUsername("janedoe");
        ResponseEntity<?> actualRegisterUserResult = authController.registerUser(signUpRequest);
        assertTrue(actualRegisterUserResult.hasBody());
        assertEquals(400, actualRegisterUserResult.getStatusCodeValue());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
        assertEquals("Error: Username is already taken!",
                ((MessageResponse) actualRegisterUserResult.getBody()).getMessage());
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser2() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        RoleRepository roleRepository = mock(RoleRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(new HashSet<>());
        signUpRequest.setUsername("janedoe");
        ResponseEntity<?> actualRegisterUserResult = authController.registerUser(signUpRequest);
        assertTrue(actualRegisterUserResult.hasBody());
        assertEquals(400, actualRegisterUserResult.getStatusCodeValue());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
        assertEquals("Error: Email is already in use!",
                ((MessageResponse) actualRegisterUserResult.getBody()).getMessage());
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser3() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByUsername(Mockito.<String>any()))
                .thenThrow(new RuntimeException("User registered successfully!"));
        RoleRepository roleRepository = mock(RoleRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(new HashSet<>());
        signUpRequest.setUsername("janedoe");
        assertThrows(RuntimeException.class, () -> authController.registerUser(signUpRequest));
        verify(userRepository).existsByUsername(Mockito.<String>any());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser4() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);
        RoleRepository roleRepository = mock(RoleRepository.class);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(new HashSet<>());
        signUpRequest.setUsername("janedoe");
        ResponseEntity<?> actualRegisterUserResult = authController.registerUser(signUpRequest);
        assertTrue(actualRegisterUserResult.hasBody());
        assertEquals(200, actualRegisterUserResult.getStatusCodeValue());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
        assertEquals("User registered successfully!",
                ((MessageResponse) actualRegisterUserResult.getBody()).getMessage());
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser5() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_USER);
        Optional<Role> ofResult = Optional.of(role);
        RoleRepository roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByName(Mockito.<ERole>any())).thenReturn(ofResult);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        HashSet<String> role2 = new HashSet<>();
        role2.add("User registered successfully!");

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(role2);
        signUpRequest.setUsername("janedoe");
        ResponseEntity<?> actualRegisterUserResult = authController.registerUser(signUpRequest);
        assertTrue(actualRegisterUserResult.hasBody());
        assertEquals(200, actualRegisterUserResult.getStatusCodeValue());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
        assertEquals("User registered successfully!",
                ((MessageResponse) actualRegisterUserResult.getBody()).getMessage());
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(userRepository).save(Mockito.<User>any());
        verify(roleRepository).findByName(Mockito.<ERole>any());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser6() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        RoleRepository roleRepository = mock(RoleRepository.class);
        Optional<Role> emptyResult = Optional.empty();
        when(roleRepository.findByName(Mockito.<ERole>any())).thenReturn(emptyResult);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        HashSet<String> role = new HashSet<>();
        role.add("User registered successfully!");

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(role);
        signUpRequest.setUsername("janedoe");
        assertThrows(RuntimeException.class, () -> authController.registerUser(signUpRequest));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(roleRepository).findByName(Mockito.<ERole>any());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser7() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setId(1);
        user.setPassword("iloveyou");
        user.setRoles(new HashSet<>());
        user.setUsername("janedoe");
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user);

        Role role = new Role();
        role.setId(1);
        role.setName(ERole.ROLE_USER);
        Optional<Role> ofResult = Optional.of(role);
        RoleRepository roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByName(Mockito.<ERole>any())).thenReturn(ofResult);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        HashSet<String> role2 = new HashSet<>();
        role2.add("admin");

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(role2);
        signUpRequest.setUsername("janedoe");
        ResponseEntity<?> actualRegisterUserResult = authController.registerUser(signUpRequest);
        assertTrue(actualRegisterUserResult.hasBody());
        assertEquals(200, actualRegisterUserResult.getStatusCodeValue());
        assertTrue(actualRegisterUserResult.getHeaders().isEmpty());
        assertEquals("User registered successfully!",
                ((MessageResponse) actualRegisterUserResult.getBody()).getMessage());
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(userRepository).save(Mockito.<User>any());
        verify(roleRepository).findByName(Mockito.<ERole>any());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser8() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        RoleRepository roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByName(Mockito.<ERole>any())).thenThrow(new RuntimeException("admin"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        HashSet<String> role = new HashSet<>();
        role.add("User registered successfully!");

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(role);
        signUpRequest.setUsername("janedoe");
        assertThrows(RuntimeException.class, () -> authController.registerUser(signUpRequest));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(roleRepository).findByName(Mockito.<ERole>any());
    }

    /**
     * Method under test: {@link AuthController#registerUser(SignupRequest)}
     */
    @Test
    void testRegisterUser9() {

        ArrayList<AuthenticationProvider> providers = new ArrayList<>();
        providers.add(new RunAsImplAuthenticationProvider());
        ProviderManager authenticationManager = new ProviderManager(providers);
        UserRepository userRepository = mock(UserRepository.class);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(false);
        when(userRepository.existsByUsername(Mockito.<String>any())).thenReturn(false);
        RoleRepository roleRepository = mock(RoleRepository.class);
        when(roleRepository.findByName(Mockito.<ERole>any())).thenThrow(new RuntimeException("admin"));
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AuthController authController = new AuthController(authenticationManager, userRepository, roleRepository, encoder,
                new JwtUtils());

        HashSet<String> role = new HashSet<>();
        role.add("admin");

        SignupRequest signUpRequest = new SignupRequest();
        signUpRequest.setEmail("jane.doe@example.org");
        signUpRequest.setPassword("iloveyou");
        signUpRequest.setRole(role);
        signUpRequest.setUsername("janedoe");
        assertThrows(RuntimeException.class, () -> authController.registerUser(signUpRequest));
        verify(userRepository).existsByEmail(Mockito.<String>any());
        verify(userRepository).existsByUsername(Mockito.<String>any());
        verify(roleRepository).findByName(Mockito.<ERole>any());
    }
}

