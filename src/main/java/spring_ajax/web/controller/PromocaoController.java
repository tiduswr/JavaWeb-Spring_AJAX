package spring_ajax.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spring_ajax.domain.Categoria;
import spring_ajax.domain.Promocao;
import spring_ajax.repository.CategoriaRepository;
import spring_ajax.repository.PromocaoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/promocao")
public class PromocaoController {

    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private PromocaoRepository promoRepository;

    @GetMapping("/add")
    public String abrirCadastro(){
        return "promo-add";
    }

    @PostMapping("/save")
    public ResponseEntity<Promocao> salvarPromocao(Promocao promo){
        promo.setDtCadastro(LocalDateTime.now().toLocalDate());
        promoRepository.save(promo);
        return ResponseEntity.ok().build();
    }

    @ModelAttribute("categorias")
    public List<Categoria> getCategorias(){
        return categoriaRepository.findAll();
    }

}
