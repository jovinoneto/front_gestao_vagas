package br.com.usatec.front_gestao_vagas.modules.candidate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUserDTO {

    private String email;
    private UUID id;
    private String description;
    private String username;
    private String name;

}