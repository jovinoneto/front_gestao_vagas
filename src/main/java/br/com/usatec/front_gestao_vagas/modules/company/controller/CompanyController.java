package br.com.usatec.front_gestao_vagas.modules.company.controller;

import br.com.usatec.front_gestao_vagas.utils.FormatErrorMessage;
import br.com.usatec.front_gestao_vagas.modules.company.dto.CreateCompanyDTO;
import br.com.usatec.front_gestao_vagas.modules.company.dto.CreateJobsDTO;
import br.com.usatec.front_gestao_vagas.modules.company.service.CreateCompanyService;
import br.com.usatec.front_gestao_vagas.modules.company.service.CreateJobService;
import br.com.usatec.front_gestao_vagas.modules.company.service.ListAllJobsCompanyService;
import br.com.usatec.front_gestao_vagas.modules.company.service.LoginCompanyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CreateCompanyService createCompanyService;

    @Autowired
    private LoginCompanyService loginCompanyService;

    @Autowired
    private CreateJobService createJobService;

    @Autowired
    private ListAllJobsCompanyService listAllJobsCompanyService;

    @GetMapping("/login")
    private String login() { return "company/login"; }

    @PostMapping("/signIn")
    public String signIn(RedirectAttributes redirectAttributes, HttpSession session, String username, String password) {

        try {
            var token = this.loginCompanyService.execute(username, password);
            var grants = token.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())).toList();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(null, null, grants);
            auth.setDetails(token.getAccess_token());

            SecurityContextHolder.getContext().setAuthentication(auth);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            session.setAttribute("token", token);

            return "redirect:/company/jobs";
        } catch (HttpClientErrorException e) {
            redirectAttributes.addFlashAttribute("error_message", "User/password incorrect");

            return "redirect:/company/jobs";
        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("company", new CreateCompanyDTO());

        return "company/create";
    }

    @PostMapping("/create")
    public String save(Model model, CreateCompanyDTO createCompanyDTO) {
        try {
            this.createCompanyService.execute(createCompanyDTO);
            model.addAttribute("company", new CreateCompanyDTO());
        } catch (HttpClientErrorException e) {
            model.addAttribute("error_message", FormatErrorMessage.formatErrorMessage(e.getResponseBodyAsString()));
            model.addAttribute("company", createCompanyDTO);
        }

        return "company/create";
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public String jobs(Model model) {
        model.addAttribute("jobs", new CreateJobsDTO());

        return "company/jobs";
    }

    @PostMapping("/jobs")
    @PreAuthorize("hasRole('COMPANY')")
    public String createJobs(CreateJobsDTO jobs) {
        var result = this.createJobService.execute(jobs, getToken());

        return "redirect:/company/jobs/list";
    }

    @GetMapping("/jobs/list")
    @PreAuthorize("hasRole('COMPANY')")
    public String list(Model model) {
        var result = this.listAllJobsCompanyService.execute(getToken());
        model.addAttribute("jobs", result);

        return "company/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("SPRING_SECURITY_CONTEXT");
        session.removeAttribute("token");

        return "redirect:/company/login";
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getDetails().toString();
    }
}
