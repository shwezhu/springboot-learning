package david.zhu;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

// 每个自定义Authentication都会有一个对应的自定义AuthenticationProvider
// RobotAuthenticationProvider,DavidAuthenticationProvider分别对应DavidAuthentication,RobotAuthentication
// 只不过 DavidAuthentication由UsernamePasswordAuthenticationToken代表
// 我们可以观察AuthenticationProvider的supports()方法来看其所操控的Authentication类型
public class DavidAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var username = authentication.getName();
        if ("david".equals(username)) {
            return UsernamePasswordAuthenticationToken.authenticated(
                    "david",
                    null,
                    AuthorityUtils.createAuthorityList("ROLE_admin")
            );
        }
        // What do we do in case it's not Daniel
        // Well we don't know what to do with this, like the user password this must be handled
        //by some other AuthenticationProvider, so to Signal this, we're returning null
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
