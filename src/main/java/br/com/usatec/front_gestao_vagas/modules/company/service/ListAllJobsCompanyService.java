package br.com.usatec.front_gestao_vagas.modules.company.service;

import br.com.usatec.front_gestao_vagas.modules.candidate.dto.JobDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ListAllJobsCompanyService {

    @Value("${host.api.gestao.vagas}")
    private String hostApi;

    public List<JobDTO> execute(String token) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var httpEntity = new HttpEntity<>(headers);

        ParameterizedTypeReference<List<JobDTO>> responseType = new ParameterizedTypeReference<List<JobDTO>>() {};

        return rt.exchange(hostApi.concat("/company/job/"), HttpMethod.GET, httpEntity, responseType).getBody();
    }
}
