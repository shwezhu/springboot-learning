package david.zhu;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class RobotAuthentication implements Authentication {
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
        return true;
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
        return AuthorityUtils.createAuthorityList("ROLE_robot");
    }

}
