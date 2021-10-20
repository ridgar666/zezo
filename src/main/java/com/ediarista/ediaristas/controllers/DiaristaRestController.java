package com.ediarista.ediaristas.controllers;

import com.ediarista.ediaristas.dtos.DiaristaPagedResponse;
import com.ediarista.ediaristas.repositories.DiaristaRepository;
import com.ediarista.ediaristas.services.ViaCepService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/diarista-cidade")
public class DiaristaRestController {
    

    @Autowired
    private DiaristaRepository repository;

    @Autowired
    private ViaCepService viaCepService;

    @GetMapping
    public DiaristaPagedResponse buscaDiaristasPorCep (@RequestParam String cep){
        var endereco = viaCepService.buscarEnderecoPorCep(cep);
        var codigoIbge = endereco.getIbge();

        var pageable = PageRequest.of(0, 6);
        var diaristas = repository.findByCodigoIbge(codigoIbge, pageable);

        var quantidadeDiaristas =  diaristas.getTotalElements() > 6 ? diaristas.getTotalElements() -6 : 0;

        return new DiaristaPagedResponse(diaristas.getContent(), quantidadeDiaristas);
    }
}
