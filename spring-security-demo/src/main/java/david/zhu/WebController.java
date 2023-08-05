package david.zhu;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
    @GetMapping("/")
    public String publicPage() {
        return "Hello David~";
    }

    @GetMapping("/private")
    public String privatePage(Authentication authentication) {
        return "Hi ~["
                + authentication.getName()
                + "]~, you've logged in. 🎉";
    }
}
