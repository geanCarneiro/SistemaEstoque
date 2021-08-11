package br.com.sistemaestoque.service;

import br.com.sistemaestoque.dao.FabricanteDAO;
import br.com.sistemaestoque.dao.ProdutoDAO;
import br.com.sistemaestoque.models.Fabricante;
import br.com.sistemaestoque.models.Produto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fabricante")
public class FabricanteService {

    @PutMapping
    @ResponseBody
    public Fabricante salvar(@RequestBody Fabricante fabricante){
        new FabricanteDAO().save(fabricante);
        return fabricante;
    }

    @PostMapping("findAll")
    @ResponseBody
    public List<Fabricante> findAll(@RequestBody Fabricante filter) {
        return new FabricanteDAO().findAll(filter);
    }

    @GetMapping("findById")
    @ResponseBody
    public Fabricante findById(@RequestParam int id) {
        return new FabricanteDAO().findById(id);
    }

    @DeleteMapping
    @ResponseBody
    public Fabricante remover(@RequestBody Fabricante fabricante) {
        return new FabricanteDAO().remover(fabricante);
    }
}
