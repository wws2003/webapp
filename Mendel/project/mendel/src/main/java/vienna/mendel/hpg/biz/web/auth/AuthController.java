package vienna.mendel.hpg.biz.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("signin")
    public String signin() {
        return "signin/signin";
    }
}
