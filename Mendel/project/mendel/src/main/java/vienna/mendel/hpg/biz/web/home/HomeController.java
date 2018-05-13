package vienna.mendel.hpg.biz.web.home;

import java.security.Principal;
import java.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
class HomeController {

    @ModelAttribute("module")
    String module() {
        return "home";
    }

    @GetMapping("/")
    String index(Principal principal, Model model) {
        // Add attribute from model
        model.addAttribute("welcomeMessage", "Indexing your memory");
        model.addAttribute("documentNames", Arrays.asList("My Java Date format note1",
                "My OpenCL note",
                "My Oracle PL/SQL note"));

        return "home/home";
        //model.addAttribute("springVersion", SpringVersion.getVersion());
        //return principal != null ? "home/homeSignedIn" : "home/homeNotSignedIn";
    }
}
