package com.ediarista.ediaristas.controllers;


import java.io.IOException;

import javax.validation.Valid;

import com.ediarista.ediaristas.models.Diarista;
import com.ediarista.ediaristas.repositories.DiaristaRepository;
import com.ediarista.ediaristas.services.FileService;
import com.ediarista.ediaristas.services.ViaCepService;
import com.ediarista.ediaristas.validators.CepValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/admin/diaristas")
public class DiaristaController {

    @Autowired
    private FileService fileService;

    @Autowired
    private DiaristaRepository repository;

    @Autowired
    private ViaCepService viaCepService;

    @Autowired
    private CepValidator cepValidator;

    @InitBinder("diarista")
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(cepValidator);
    }

    @GetMapping
    public ModelAndView listar() {
        var modelAndView = new ModelAndView("admin/diaristas/listar");

        modelAndView.addObject("diaristas", repository.findAll());

        return modelAndView;
    }

    @GetMapping("/cadastrar")
    public ModelAndView cadastrar() {
        var modelAndView = new ModelAndView("admin/diaristas/form");

        modelAndView.addObject("diarista", new Diarista());

        return modelAndView;
    }
    
    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam MultipartFile imagens, @Valid Diarista diarista, BindingResult result) throws IOException{
        if(result.hasErrors()) {
            return "admin/diaristas/form";
        }

        var filename = fileService.salvar(imagens);
        diarista.setFoto(filename);

        var cep = diarista.getCep();
        var endereco = viaCepService.buscarEnderecoPorCep(cep);
        var codigoIbge = endereco.getIbge();
        diarista.setCodigoIbge(codigoIbge);

        repository.save(diarista);

        return "redirect:/admin/diaristas";
    }


    @GetMapping("/{id}/editar")
    public ModelAndView editar(@PathVariable Long id){
        var modelAndView = new ModelAndView ("admin/diaristas/form");

        modelAndView.addObject("diarista", repository.getById(id));

        return modelAndView;

    }

    @PostMapping("/{id}/editar")
    public String editar(@RequestParam MultipartFile imagens,@PathVariable Long id, @Valid Diarista diarista, BindingResult result) throws IOException{
        if(result.hasErrors()){

        return "admin/diaristas/form";
        }

        var diaristaAtual = repository.getById(id);
     
        if(imagens.isEmpty()) {
            diarista.setFoto(diaristaAtual.getFoto());

        } else {
            var filename = fileService.salvar(imagens);
            diarista.setFoto(filename);
        }

        var cep = diarista.getCep();
        var endereco = viaCepService.buscarEnderecoPorCep(cep);
        var codigoIbge = endereco.getIbge();
        diarista.setCodigoIbge(codigoIbge);

        repository.save(diarista);

        return "redirect:/admin/diaristas";
    }


    @GetMapping("/{id}/excluir")
    public String excluir(@PathVariable Long id){
        repository.deleteById(id);

        return "redirect:/admin/diaristas";
    }
 
}
