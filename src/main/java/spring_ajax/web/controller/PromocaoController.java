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
import spring_ajax.dto.PromocaoDTO;
import spring_ajax.repository.CategoriaRepository;
import spring_ajax.repository.PromocaoRepository;
import spring_ajax.service.PromocaoDataTablesService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("/tabela")
    public String showTabela(){
        return "promo-datatables";
    }

    @GetMapping("/datatables/server")
    public ResponseEntity<?> datatables(HttpServletRequest request){
        Map<String, Object> data = new PromocaoDataTablesService().execute(promoRepository, request);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> excluirPromocao(@PathVariable("id") Long id){
        promoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<?> preEditarPromocao(@PathVariable("id") Long id){
        Optional<Promocao> promo = promoRepository.findById(id);
        return promo.isPresent() ? ResponseEntity.ok(promo.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editarPromocao(@Valid PromocaoDTO dto, BindingResult result){
        if(result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors()
                    .stream()
                    .collect(Collectors
                            .toMap(e -> e.getField(), e -> e.getDefaultMessage()));
            return ResponseEntity.unprocessableEntity().body(errors);
        }

        Optional<Promocao> promoOpt = promoRepository.findById(dto.getId());
        if(!promoOpt.isPresent()) return ResponseEntity.notFound().build();

        Promocao promo = promoOpt.get();

        promo.setCategoria(dto.getCategoria());
        promo.setDescricao(dto.getDescricao());
        promo.setLinkImagem(dto.getLinkImagem());
        promo.setPreco(dto.getPreco());
        promo.setTitulo(dto.getTitulo());

        promoRepository.save(promo);

        return ResponseEntity.ok().build();
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
