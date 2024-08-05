package br.com.usatec.front_gestao_vagas.modules.candidate.service;

import br.com.usatec.front_gestao_vagas.modules.candidate.dto.CreateCandidateDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class CreateCandidateService {

    public void execute(CreateCandidateDTO createCandidateDTO) {
            RestTemplate rt = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<CreateCandidateDTO> request = new HttpEntity<>(createCandidateDTO, headers);

            var result = rt.postForObject("http://localhost:8080/candidate/", request, CreateCandidateDTO.class);
            System.out.println(result);
    }
}