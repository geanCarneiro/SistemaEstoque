package br.com.sistemaestoque.service;

import br.com.sistemaestoque.dao.UsuarioDAO;
import br.com.sistemaestoque.models.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioService {

    @PutMapping
    @ResponseBody
    public Usuario salvar(@RequestBody Usuario usuario){
        return new UsuarioDAO().save(usuario);
    }

    @PostMapping("findAll")
    @ResponseBody
    public List<Usuario> findAll(@RequestBody Usuario filter) {
        return new UsuarioDAO().findAll(filter);
    }

    @GetMapping("findById")
    @ResponseBody
    public Usuario findById(@RequestParam int id) {
        return new UsuarioDAO().findById(id);
    }

    @DeleteMapping
    @ResponseBody
    public Usuario remover(@RequestBody Usuario usuario) {
        return new UsuarioDAO().remover(usuario);
    }

    @PostMapping
    @ResponseBody
    public Usuario usuarioExistente(@RequestBody Usuario usuario){
        return new UsuarioDAO().findByLogin(usuario.getUsuario(), usuario.getSenha());
    }


}
