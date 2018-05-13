/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vienna.mendel.hpg.biz.web.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for debugging purpose
 *
 * @author wws2003
 */
@Controller
@RequestMapping(path = "debug")
public class DebugController {

    public String xx1() {
        return "";
    }
}
