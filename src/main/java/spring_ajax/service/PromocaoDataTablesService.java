package spring_ajax.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

        int start = Integer.parseInt(request.getParameter("start"));
        int length = Integer.parseInt(request.getParameter("length"));
        int draw = Integer.parseInt(request.getParameter("draw"));
        int current = currentPage(start, length);
        String colProperties = colName(request);
        Map<String, Object> json = new LinkedHashMap<>();
        Sort.Direction direction = orderBy(request);

        Pageable pageable = PageRequest.of(current, length, direction, colProperties);

        json.put("draw", null);
        json.put("recordsTotal", 0);
        json.put("recordsFiltered", 0);
        json.put("data", null);

        return json;
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
