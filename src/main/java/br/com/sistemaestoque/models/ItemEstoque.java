package br.com.sistemaestoque.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ITEM_ESTOQUE")
@Getter
@Setter
public class ItemEstoque extends EntidadeGenerico{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUTO_ID")
    private Produto produto;

    @Column
    private int quantidade;

    @Column(name = "ESTOQUE_IDEAL")
    private int estoqueIdeal;

    public ItemEstoque(){
        this.setId(-1);
        this.setProduto(null);
        this.setQuantidade(0);
        this.setEstoqueIdeal(0);
        this.setAtivo(true);
    }

    public ItemEstoque(ItemEstoque itemEstoque, Produto produto){
        this.setId(itemEstoque.getId());
        this.setProduto(produto);
        this.setQuantidade(itemEstoque.getQuantidade());
        this.setEstoqueIdeal(itemEstoque.getEstoqueIdeal());
        this.setAtivo(itemEstoque.isAtivo());
    }

    public ItemEstoque(int id){
        this();
        this.setId(id);
    }

    public ItemEstoque(Produto produto){
        this();
        this.setProduto(produto);
    }


}
