package br.com.sistemaestoque.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Fabricante extends EntidadeGenerico {

    @Column
    private String nome;


    @OneToMany(mappedBy = "fabricante", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Produto> produtos;

    public Fabricante(int id){
        this.setId(id);
    }

    public Fabricante(int id, String nome){
        this(id);
        this.setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
