package br.com.usatec.front_gestao_vagas.modules.company.service;

import br.com.usatec.front_gestao_vagas.modules.company.dto.CreateCompanyDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CreateCompanyService {

    @Value("${host.api.gestao.vagas}")
    private String hostApi;

    public String execute(CreateCompanyDTO createCompanyDTO) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreateCompanyDTO> request = new HttpEntity<>(createCompanyDTO, headers);

        return rt.postForObject(hostApi.concat("/company/"), request, String.class);
    }

}
