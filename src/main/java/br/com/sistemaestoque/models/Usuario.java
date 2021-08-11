package br.com.sistemaestoque.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Usuario extends EntidadeGenerico{

    @Column
    private String nome;
    @Column(unique = true)
    private String usuario;
    @Column
    private String senha;
    @Column
    @Enumerated(EnumType.STRING)
    private Funcao funcao;

}
