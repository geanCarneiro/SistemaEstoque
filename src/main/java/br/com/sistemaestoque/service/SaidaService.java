package br.com.sistemaestoque.service;

import br.com.sistemaestoque.dao.ItemEstoqueDAO;
import br.com.sistemaestoque.dao.SaidaDAO;
import br.com.sistemaestoque.models.ItemEstoque;
import br.com.sistemaestoque.models.Saida;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saida")
public class SaidaService {

    @PutMapping
    @ResponseBody
    public Saida salvar(@RequestBody Saida saida){

        new SaidaDAO().save(saida);

        return saida;
    }

    @PostMapping("findAll")
    @ResponseBody
    public List<Saida> findAll(@RequestBody Saida filter) {
        return new SaidaDAO().findAll(filter);
    }

    @GetMapping("findById")
    @ResponseBody
    public Saida findById(@RequestParam int id) {
        return new SaidaDAO().findById(id);
    }

    @DeleteMapping
    @ResponseBody
    public Saida remover(@RequestBody Saida saida) {
        return new SaidaDAO().remover(saida);
    }

    @PostMapping("cancelarSaida")
    @ResponseBody
    public void cancelarSaida(@RequestBody Saida saida) {
        ItemEstoque itemEstoque = new ItemEstoqueDAO().findByProduto(saida.getProduto());
        itemEstoque.setQuantidade(itemEstoque.getQuantidade() + saida.getQuantidade());
        new ItemEstoqueDAO().update(itemEstoque);

        new SaidaDAO().remover(saida);
    }


}
