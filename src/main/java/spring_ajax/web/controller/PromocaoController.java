package spring_ajax.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import spring_ajax.domain.Categoria;
import spring_ajax.domain.Promocao;
import spring_ajax.repository.CategoriaRepository;
import spring_ajax.repository.PromocaoRepository;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/list")
    public String listarOfertas(ModelMap map){
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.ASC, "dtCadastro"));
        map.addAttribute("promocoes", promoRepository.findAll(pageRequest));
        return "promo-list";
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<?> adicionarLikes(@PathVariable("id") Long id){
        promoRepository.updateSomarLikes(id);
        int likes = promoRepository.findLikesById(id);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/site")
    public ResponseEntity<?> autocompleteByTermo(@RequestParam("termo") String termo){
        List<String> sites = promoRepository.findSitesByTermo(termo);
        return ResponseEntity.ok(sites);
    }

    @GetMapping("/site/list")
    public String listarPorSite(@RequestParam("site") String site, ModelMap map){
        PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.ASC, "dtCadastro"));
        map.addAttribute("promocoes", promoRepository.findBySite(site, pageRequest));
        return "promo-card";
    }

    @GetMapping("/list/ajax")
    public String listarCards(@RequestParam(name = "page", defaultValue = "1") int page,
                              @RequestParam(name = "site", defaultValue = "") String site,
                              ModelMap map){
        PageRequest pageRequest = PageRequest.of(page, 8,
                Sort.by(Sort.Direction.ASC, "dtCadastro"));
        if(site.isEmpty()){
            map.addAttribute("promocoes",
                    promoRepository.findAll(pageRequest));
        }else{
            map.addAttribute("promocoes",
                    promoRepository.findBySite(site,pageRequest));
        }
        return "promo-card";
    }

    @PostMapping("/save")
    public ResponseEntity<?> salvarPromocao(@Valid Promocao promo, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
            return ResponseEntity.unprocessableEntity().body(errors);
        }

        promo.setDtCadastro(LocalDateTime.now().toLocalDate());
        promoRepository.save(promo);
        return ResponseEntity.ok().build();
    }

    @ModelAttribute("categorias")
    public List<Categoria> getCategorias(){
        return categoriaRepository.findAll();
    }

}
