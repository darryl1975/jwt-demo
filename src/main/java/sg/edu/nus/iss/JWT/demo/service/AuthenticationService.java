package sg.edu.nus.iss.JWT.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sg.edu.nus.iss.JWT.demo.model.ERole;
import sg.edu.nus.iss.JWT.demo.model.Role;
import sg.edu.nus.iss.JWT.demo.model.User;
import sg.edu.nus.iss.JWT.demo.model.MyPrincipal;
import sg.edu.nus.iss.JWT.demo.payload.AuthenticationRequest;
import sg.edu.nus.iss.JWT.demo.payload.AuthenticationResponse;
import sg.edu.nus.iss.JWT.demo.payload.RegisterRequest;
import sg.edu.nus.iss.JWT.demo.repository.UserRepository;
import sg.edu.nus.iss.JWT.demo.repository.RoleRepository;

import java.util.Set;
import java.util.HashSet;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {

                Set<String> strRoles = request.getRoles();
                Set<Role> roles = new HashSet<>();

                if (strRoles == null) {
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                } else {
                        strRoles.forEach(role -> {
                                switch (role) {
                                        case "ROLE_USERADMIN":
                                                Role adminRole = roleRepository.findByName(ERole.ROLE_USERADMIN)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Error: Role is not found."));
                                                roles.add(adminRole);

                                                break;
                                        case "ROLE_MODERATOR":
                                                Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Error: Role is not found."));
                                                roles.add(modRole);

                                                break;
                                        default:
                                                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                                                .orElseThrow(() -> new RuntimeException(
                                                                                "Error: Role is not found."));
                                                roles.add(userRole);
                                }
                        });
                }

                var user = User.builder()
                                .username(request.getUsername())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(roles)
                                .build();
                repository.save(user);
                MyPrincipal myPrincipal = new MyPrincipal(user);

                var jwtToken = jwtService.generateToken(myPrincipal);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) throws UsernameNotFoundException {
                System.out.println("AuthenticationService (request): " + request);
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
                UserDetails user = repository.findByUsername(request.getUsername()).map(MyPrincipal::new)
                                .orElseThrow(() -> new UsernameNotFoundException("Error: User Not Found!!!"));
                System.out.println("AuthenticationService: " + user);

                var jwtToken = "";
                if (user != null) {
                        jwtToken = jwtService.generateToken(user);
                }

                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }
}
