package vienna.mendel.hpg.biz.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @GetMapping("signin")
    public String signin() {
        return "signin/signin";
    }

    @PostMapping("authenticate")
    public String authenticate() {
        // TODO Implement
        return "home/home";
    }
}
