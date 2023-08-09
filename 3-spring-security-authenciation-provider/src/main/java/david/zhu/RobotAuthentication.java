package david.zhu;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// 该类主要由 RobotAuthenticationProvider 来操控
// 同 DavidAuthentication 一样, 由DavidAuthenticationProvider 来操控
// 只不过 DavidAuthentication 是由 UsernamePasswordAuthenticationToken 代表,
// 因为是账号密码登录,所以没必要新建一个 DavidAuthentication
// 因此你知道了, 每个自定义 Authentication 都会有一个对应的自定义AuthenticationProvider
// 至于怎么看该AuthenticationProvider是操控哪个类型Authentication的，可以看其 support 方法
public class RobotAuthentication implements Authentication {

    private final boolean isAuthenticated;
    private final List<GrantedAuthority> authorities;
    private final String password;

    // This is too hard for my users to understand, so usually,
    // and this is what is done in Spring Security you create static methods for this,
    // unauthenticated(), and authenticated(), like factory method
    private RobotAuthentication(List<GrantedAuthority> authorities, String password) {
        this.password = password;
        this.authorities = authorities;
        this.isAuthenticated = password == null;
    }

    // 用来代表 Authentication Request, 因为需要验证, 所以要提供密码
    public static RobotAuthentication unauthenticated(String password) {
        return new RobotAuthentication(Collections.emptyList(), password);
    }

    // 用来代表 Authentication Object, 若验证成功, 返回一个 Authenticated Object,
    // 因为已经验证成功了, 就没必要保存密码了
    public static RobotAuthentication authenticated() {
        return new RobotAuthentication(AuthorityUtils.createAuthorityList("ROLE_robot"), null);
    }

    @Override
    public String getName() {
        return "Mr Robot 🤖️";
    }

    @Override
    public Object getPrincipal() {
        // Birthday is also fine
        return getName();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // Again the same pattern immutability, so for compatibility and legacy reasons
        // we don't change authentication object, we create a new one instead,
        throw new IllegalArgumentException("Don't");
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        // Usually password, unknown for developer, so null
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

}
