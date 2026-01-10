package br.com.alura.adopet.api.controller;

import br.com.alura.adopet.api.dto.abrigo.CadastrarAbrigoDto;
import br.com.alura.adopet.api.dto.abrigo.DetalhesAbrigoDto;
import br.com.alura.adopet.api.dto.pet.CadastrarPetDto;
import br.com.alura.adopet.api.dto.pet.DadosDetalhesPet;
import br.com.alura.adopet.api.exception.ValidacaoException;
import br.com.alura.adopet.api.model.Abrigo;
import br.com.alura.adopet.api.service.AbrigoService;
import br.com.alura.adopet.api.service.PetService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/abrigos")
public class AbrigoController {

    @Autowired
    AbrigoService abrigoService;
    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<DetalhesAbrigoDto>> listar() {
        List<DetalhesAbrigoDto> abrigoDtos = abrigoService.listar();
        return ResponseEntity.ok(abrigoDtos);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<String> cadastrar(@RequestBody @Valid CadastrarAbrigoDto dto) {
        try {
            abrigoService.cadastrar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{idOuNome}/pets")
    public ResponseEntity<List<DadosDetalhesPet>> listarPets(@PathVariable String idOuNome) {
        try {
            List<DadosDetalhesPet> pets = abrigoService.listarPetsDoAbrigo(idOuNome);
            return ResponseEntity.ok(pets);

        } catch (EntityNotFoundException | ValidacaoException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{idOuNome}/pets")
    @Transactional
    public ResponseEntity<String> cadastrarPet(@PathVariable String idOuNome, @RequestBody @Valid CadastrarPetDto pet) {
        try {
            Abrigo abrigo = abrigoService.carregarAbrigo(idOuNome);
            petService.cadastrarPet(abrigo, pet);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException | ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
