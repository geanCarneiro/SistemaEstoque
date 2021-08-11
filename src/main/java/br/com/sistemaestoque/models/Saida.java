package br.com.sistemaestoque.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
public class Saida extends EntidadeGenerico{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUTO_ID")
    private Produto produto;
    private int quantidade;
    private Date data;

}
