package david.zhu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.*;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var authManager = new ProviderManager(new RobotAuthenticationProvider(List.of("beep-boop", "boop-beep")));

        return http
                .authorizeHttpRequests(authorizeConfig -> {
                            authorizeConfig.requestMatchers("/").permitAll();
                            authorizeConfig.requestMatchers("/error").permitAll();
                            authorizeConfig.requestMatchers("/favicon.ico").permitAll();
                            authorizeConfig.anyRequest().authenticated();
                        })
                .formLogin(withDefaults())
                // By calling httpBasic(), an instance of the BasicAuthenticationFilter is added to the filter chain.
                // https://stackoverflow.com/a/57577530/16317008
                // 为了实现通过 curl -u "user:password" http://localhost:8080/private 登录
                .httpBasic(withDefaults())
                .oauth2Login(withDefaults())
                // 注意之前的参数是 new RobotFilter(authenticationManager)
                .addFilterBefore(new RobotFilter(authManager), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(new DavidAuthenticationProvider())
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // We are using in-memory-user demo purpose
        return new InMemoryUserDetailsManager(
                User.builder()
                        .username("user-1")
                        .password("{noop}asd")
                        .authorities("ROLE_user")
                        .build()
        );
    }
}
