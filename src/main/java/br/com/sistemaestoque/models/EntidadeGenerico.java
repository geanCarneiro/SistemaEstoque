package br.com.sistemaestoque.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@MappedSuperclass
public class EntidadeGenerico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id = -1;

    @Column
    @ColumnDefault("true")
    private boolean ativo = true;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

}
