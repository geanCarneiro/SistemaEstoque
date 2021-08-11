package br.com.sistemaestoque.service;

import br.com.sistemaestoque.dao.FabricanteDAO;
import br.com.sistemaestoque.dao.ProdutoDAO;
import br.com.sistemaestoque.models.Fabricante;
import br.com.sistemaestoque.models.Produto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto")
public class ProdutoService {

    @PutMapping
    @ResponseBody
    public Produto salvar(@RequestBody Produto produto){

        new ProdutoDAO().save(produto);

        return produto;
    }

    @PostMapping("findAll")
    @ResponseBody
    public List<Produto> findAll(@RequestBody Produto filter) {
        return new ProdutoDAO().findAll(filter);
    }

    @GetMapping("findById")
    @ResponseBody
    public Produto findById(@RequestParam int id) {
        return new ProdutoDAO().findById(id);
    }

    @DeleteMapping
    @ResponseBody
    public Produto remover(@RequestBody Produto produto) {
        return new ProdutoDAO().remover(produto);
    }
}