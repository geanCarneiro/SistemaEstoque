package br.com.sistemaestoque.models;

import br.com.sistemaestoque.dao.FabricanteDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@Setter
@Getter
public class Produto extends EntidadeGenerico {

    @Column
    private String nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FABRICANTE_ID")
    private Fabricante fabricante;

    @Column(name = "COR_PREDOMINANTE")
    private String corPredominante;

    @Column
    private String descricao;

    @Formula(value = "NOME || ' ' || COR_PREDOMINANTE")
    private String nomeCorPredominante;

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<ItemEstoque> estoque;

    @OneToMany(mappedBy = "produto", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Saida> saidas;

    public Produto(){
        this.setNome("");
        this.setCorPredominante("");
        this.setFabricante(null);
        this.setDescricao("");
    }

    public Produto(int id){
        this();
        this.setId(id);
    }

    public Produto(int id, String desc, int idFabricante){
        this(id);
        this.setNome(desc);
        this.setFabricante(new FabricanteDAO().findById(idFabricante));
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
