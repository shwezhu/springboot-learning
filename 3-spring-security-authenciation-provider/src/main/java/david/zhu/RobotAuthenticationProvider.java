package david.zhu;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import java.util.List;

// 每个自定义Authentication都会有一个对应的自定义AuthenticationProvider
// RobotAuthenticationProvider,DavidAuthenticationProvider分别对应DavidAuthentication,RobotAuthentication
// 只不过 DavidAuthentication由UsernamePasswordAuthenticationToken代表
// 我们可以观察AuthenticationProvider的supports()方法来看其所操控的Authentication类型
public class RobotAuthenticationProvider implements AuthenticationProvider {
    private final List<String> passwords;

    public RobotAuthenticationProvider(List<String> password) {
        this.passwords = password;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var authRequest = (RobotAuthentication) authentication;
        var password = authRequest.getPassword();
        if (!this.passwords.contains(password)) {
           throw new BadCredentialsException("You are not Mr Robot 🤖️");
        }
        // 可以发现, 实现了Authentication接口的类一般有个 authenticated() 静态方法, 也可称为factory method
        // 比如我们的自定义RobotAuthentication,再比如UsernamePasswordAuthenticationToken
        return RobotAuthentication.authenticated();
    }

    // If this method return false, then you will get:
    // No AuthenticationProvider found for david.zhu.RobotAuthentication
    // at the output: curl localhost:8080/private -H "x-robot-password: beep-boo" -v
    @Override
    public boolean supports(Class<?> authentication) {
        return RobotAuthentication.class.isAssignableFrom(authentication);
    }
}
