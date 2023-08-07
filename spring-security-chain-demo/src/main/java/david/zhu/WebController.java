package david.zhu;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
public class WebController {
    @GetMapping("/")
    public String publicPage() {
        return "Hello David~";
    }

    @GetMapping("/private")
    public String privatePage(Authentication authentication) {
        return "Hi ~["
                + getName(authentication)
                + "]~, you've logged in. 🎉";
    }

    private static String getName(Authentication authentication) {
        return Optional.of(authentication.getPrincipal())
                .filter(OidcUser.class::isInstance)
                .map(OidcUser.class::cast)
                .map(OidcUser::getEmail)
                .orElseGet(authentication::getName);
    }
}
