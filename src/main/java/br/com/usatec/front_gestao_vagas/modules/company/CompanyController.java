package br.com.usatec.front_gestao_vagas.modules.company;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @GetMapping("/create")
    public String create() {
        return "company/create";
    }

}
