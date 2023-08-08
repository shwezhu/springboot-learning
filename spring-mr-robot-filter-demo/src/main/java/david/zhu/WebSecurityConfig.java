package david.zhu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // authorizeHttpRequests: Allows restricting access based upon the HttpServletRequest using RequestMatcher implementations (i.e. via URL patterns).
                .authorizeHttpRequests(authorizeConfig -> {
                            // permitAll() specifies that access to the root URL should be permitted for all users without requiring authentication.
                            // So, the authentication object will not be created when access this url,
                            // you can try to inject authentication object into the publicPage Controller, and you will get null pointer exception
                            authorizeConfig.requestMatchers("/").permitAll();
                            authorizeConfig.requestMatchers("/error").permitAll();
                            authorizeConfig.requestMatchers("/favicon.ico").permitAll();
                            // Method authenticated(): Specify that URLs are allowed by any authenticated user.
                            authorizeConfig.anyRequest().authenticated();
                        })
                // Enable form login, if I need login, show me a form
                // without this, when you access page that needs authentication will get Whitelabel Error Page with code 403
                .formLogin(withDefaults())
                .oauth2Login(withDefaults())
                // https://docs.spring.io/spring-security/reference/servlet/architecture.html#servlet-security-filters
                .addFilterBefore(new RobotFilter(), UsernamePasswordAuthenticationFilter.class)
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
