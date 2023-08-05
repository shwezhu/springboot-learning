package david.zhu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // authorizeHttpRequests: Allows restricting access based upon the HttpServletRequest using RequestMatcher implementations (i.e. via URL patterns).
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("/").permitAll();
                            authorizeHttp.requestMatchers("/error").permitAll();
                            authorizeHttp.requestMatchers("/favicon.ico").permitAll();
                            // Method authenticated(): Specify that URLs are allowed by any authenticated user.
                            authorizeHttp.anyRequest().authenticated();
                        })
                // Enable form login, if I need login, show me a form
                // without this, when you access page that needs authentication will get Whitelabel Error Page with code 403
                .formLogin(withDefaults())
                .oauth2Login(withDefaults())
                .build();
    }

    // UserDetailsService, an interface, is used to retrieve user-related data.
    // It is used to retrieve user-related data from a data source, such as a database or a file for authentication and authorization purposes.
    // e.g., It has one method named loadUserByUsername() which can be overridden to customize the process of finding the user.
    // It is used by the DaoAuthenticationProvider to load details about the user during authentication.
    @Bean
    public UserDetailsService userDetailsService() {
        // We are using in memory user demo purpose
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("david")
                        // The {noop} prefix is a marker indicates that the password should be treated as plain text.
                        .password("{noop}asd")
                        .authorities("ROLE_user")
                        .build()
        );
    }
}
