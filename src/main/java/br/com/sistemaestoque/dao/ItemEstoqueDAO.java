package br.com.sistemaestoque.dao;

import br.com.sistemaestoque.models.EntidadeGenerico;
import br.com.sistemaestoque.models.Fabricante;
import br.com.sistemaestoque.models.ItemEstoque;
import br.com.sistemaestoque.models.Produto;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Repository
public class ItemEstoqueDAO extends DAO<ItemEstoque>{

    @Override
    public ItemEstoque update(ItemEstoque entity) {
        try {
            this.entityManager.getTransaction().begin();

            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaUpdate<ItemEstoque> update = builder.createCriteriaUpdate(ItemEstoque.class);
            Root<ItemEstoque> root = update.from(ItemEstoque.class);

            update.set("produto", entity.getProduto());
            update.set("quantidade", entity.getQuantidade());
            update.set("estoqueIdeal", entity.getEstoqueIdeal());
            update.set("ativo", entity.isAtivo());
            update.where(builder.equal(root.get("id"), entity.getId()));

            this.entityManager.createQuery(update).executeUpdate();
            this.entityManager.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            this.entityManager.close();
        }


        return entity;

    }

    @Override
    public List findAll(ItemEstoque filter) {
        List<ItemEstoque> out = new ArrayList<>();

        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<ItemEstoque> query = builder.createQuery(ItemEstoque.class);
            Root<ItemEstoque> root = query.from(ItemEstoque.class);
            root.fetch("produto").fetch("fabricante");

            query.select(root);
            ArrayList<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.equal(root.get("ativo"), true));
            if (filter.getProduto() != null)
                predicates.add(
                        builder.like(
                                builder.upper(root.get("produto").get("nomeCorPredominante")),
                                "%" + filter.getProduto().getNome().toUpperCase() + "%"
                        )
                );

            query.where(predicates.toArray(new Predicate[0]));

            out = this.entityManager.createQuery(query).getResultList();
            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            this.entityManager.close();
        }

        return out;


    }

    @Override
    public ItemEstoque findById(int id) {
        ItemEstoque out = null;
        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<ItemEstoque> query = builder.createQuery(ItemEstoque.class);
            Root<ItemEstoque> root = query.from(ItemEstoque.class);
            root.fetch("produto");

            query.select(root);
            query.where(builder.equal(root.get("id"), id));

            List<ItemEstoque> result = this.entityManager.createQuery(query).getResultList();

            if(!result.isEmpty()) out = result.get(0);
            this.entityManager.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            this.entityManager.close();
        }

        return out;
    }

    public ItemEstoque findByProduto(Produto produto) {
        ItemEstoque estoque = new ItemEstoque();
        estoque.setProduto(produto);

        List<ItemEstoque> result = findAll(estoque);

        return result.isEmpty() ? null : result.get(0);
    }
}
