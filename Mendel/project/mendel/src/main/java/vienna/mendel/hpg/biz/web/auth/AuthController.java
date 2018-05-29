package vienna.mendel.hpg.biz.web.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public class AuthController {

    /**
     * Login page for user
     *
     * @return
     */
    @GetMapping("userLogin")
    public String userLogin() {
        return "auth/userLogin";
    }

    @GetMapping("adminLogin")
    public String adminLogin() {
        return "auth/adminLogin";
    }
}
