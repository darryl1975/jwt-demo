package sg.edu.nus.iss.JWT.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import lombok.RequiredArgsConstructor;
import sg.edu.nus.iss.JWT.demo.filter.JwtAuthenticationFilter;
// import org.springframework.security.web.authentication.logout.LogoutHandler;
// import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    // private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/test/all").permitAll()
                .requestMatchers(toH2Console()).permitAll()
                // .requestMatchers("/api/test/mod").hasRole("ROLE_MODERATOR")
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
                // .logout();
                // .logoutUrl("/api/auth/logout");
                // .addLogoutHandler(logoutHandler)
                // .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        // fix H2 database console: Refused to display ' in a frame because it set
        // 'X-Frame-Options' to 'deny'
        http.headers().frameOptions().sameOrigin();

        return http.build();
    }
}
