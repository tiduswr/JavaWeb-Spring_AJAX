package spring_ajax.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import spring_ajax.domain.Promocao;
import spring_ajax.repository.PromocaoRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

public class PromocaoDataTablesService {

    private final String[] cols = {
        "id", "titulo", "site", "linkPromocao", "descricao",
        "linkImagem", "preco", "likes", "dtCadastro", "categoria.titulo"
    };

    public Map<String, Object> execute(PromocaoRepository repository, HttpServletRequest request){

        //Pegando informações datatable
        int start = Integer.parseInt(request.getParameter("start"));
        int length = Integer.parseInt(request.getParameter("length"));
        int draw = Integer.parseInt(request.getParameter("draw"));
        int current = currentPage(start, length);
        String colProperties = colName(request);
        String search = searchBy(request);
        Map<String, Object> json = new LinkedHashMap<>();
        Sort.Direction direction = orderBy(request);

        //Regras de paginação
        Pageable pageable = PageRequest.of(current, length, direction, colProperties);

        //Gerando pagina da tabela
        Page<Promocao> page = queryBy(search, repository, pageable);

        json.put("draw", draw);
        json.put("recordsTotal", page.getTotalElements());
        json.put("recordsFiltered", page.getTotalElements());
        json.put("data", page.getContent());

        return json;
    }

    private String searchBy(HttpServletRequest request) {
        String search = request.getParameter("search[value]");
        return search.isEmpty() ? "" : search;
    }

    private Page<Promocao> queryBy(String search, PromocaoRepository repository, Pageable pageable) {
        if(search.isEmpty()) return repository.findAll(pageable);
        return repository.findByTituloOrSiteOrCategoria(search, pageable);
    }

    private Sort.Direction orderBy(HttpServletRequest request) {
        String order = request.getParameter("order[0][dir]");
        return order.equalsIgnoreCase("DESC") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
    }

    private String colName(HttpServletRequest request) {
        int col = Integer.parseInt(request.getParameter("order[0][column]"));
        return cols[col];
    }

    private int currentPage(int start, int length) {
        //00
        //0-9 | 10-19 | 20-29
        return start/length;
    }

}
