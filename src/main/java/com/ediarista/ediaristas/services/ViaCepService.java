package com.ediarista.ediaristas.services;

import com.ediarista.ediaristas.dtos.ViaCepResponse;
import com.ediarista.ediaristas.exceptions.CepInvalidoException;
import com.ediarista.ediaristas.exceptions.CepNaoEncontradoException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    public ViaCepResponse buscarEnderecoPorCep(String cep) {
        System.out.println(cep);
        var url = "https://viacep.com.br/ws/" + cep + "/json/";
        System.out.println(url);
        var clienteHttp = new RestTemplate();
        ResponseEntity<ViaCepResponse> response;

        try {
            response = clienteHttp.getForEntity(url, ViaCepResponse.class);
        } catch (HttpClientErrorException e) {
            throw new CepInvalidoException("Cep Invalido!");
        }


        if (response.getBody().getCep() == null) {
            throw new CepNaoEncontradoException("Cep Não Encontrado!");
            
        }

        return response.getBody();
    }
    
}
