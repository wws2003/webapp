/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vienna.mendel.hpg.biz.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author wws2003
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    /**
     * Controller for administrator landing page
     *
     * @return
     */
    @GetMapping("/")
    public String admin() {
        return "admin/admin";
    }
}
