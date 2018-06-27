package vienna.mendel.hpg.biz.web.user;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import vienna.mendel.hpg.model.dto.document.Document;

@Controller
@RequestMapping("user")
class HomeController {

    @ModelAttribute("module")
    public String module() {
        return "home";
    }

    @GetMapping("/myPage")
    public String home(Principal principal, Model model) {
        // TODO Implement properly

        // Almost code now is for investigation purpose
        // Add attribute from model
        model.addAttribute("welcomeMessage", "Indexing your memory");

        Document doc1 = new Document(1L, "My Java Date format note1", "DateTimeFormatter vs SimpleDateFormat");
        Document doc2 = new Document(2L, "My OpenCL note1", "clCreateImage is problematic");
        Document doc3 = new Document(3L, "My OpenCL note2", "Bitonic sort local");
        List<Document> documents = new ArrayList(Arrays.asList(doc1, doc2, doc3));

        model.addAttribute("documents", documents);

        return "home/home";
    }
}
