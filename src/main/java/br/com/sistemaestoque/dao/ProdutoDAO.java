package br.com.sistemaestoque.dao;

import br.com.sistemaestoque.models.Fabricante;
import br.com.sistemaestoque.models.Produto;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProdutoDAO extends DAO<Produto> {

    @Override
    public Produto update(Produto entity) {

       try {
            this.entityManager.getTransaction().begin();

            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaUpdate<Produto> update = builder.createCriteriaUpdate(Produto.class);
            Root<Produto> produtoRoot = update.from(Produto.class);

            update.set("nome", entity.getNome());
            update.set("fabricante", entity.getFabricante());
            update.set("corPredominante", entity.getCorPredominante());
            update.set("descricao", entity.getDescricao());
            update.set("ativo", entity.isAtivo());
            update.where(builder.equal(produtoRoot.get("id"), entity.getId()));

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
    public List<Produto> findAll(@RequestParam("filter") Produto filter) {
        List<Produto> out = new ArrayList();
        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

            CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
            Root<Produto> r = cq.from(Produto.class);
            r.fetch("fabricante");

            cq.select(r);
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isTrue(r.get("ativo")));
            predicates.add(cb.or(
                        cb.like(cb.upper(r.get("nome")), "%" + filter.getNome().toUpperCase() + "%"),
                        cb.like(cb.upper(r.get("nomeCorPredominante")), "%" + filter.getNome().toUpperCase() + "%")
                    ));
            if(filter.getCorPredominante() != null)
                predicates.add(cb.like(cb.upper(r.<String>get("corPredominante")), "%" + filter.getCorPredominante().toUpperCase() + "%"));
            if(filter.getFabricante() != null)
                predicates.add(cb.equal(r.get("fabricante"), filter.getFabricante()));

            cq.where(cb.and(predicates.toArray(new Predicate[0])));
            cq.orderBy(cb.asc(r.get("nome")));

            out = this.entityManager.createQuery(cq).getResultList();

            this.entityManager.getTransaction().commit();

        } catch (Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            try {
                this.entityManager.close();
            } catch (Exception e) { }
        }


        return out;
    }

    public List<Produto> findAllIgnoraAtivo(@RequestParam("filter") Produto filter) {
        List<Produto> out = new ArrayList();
        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

            CriteriaQuery<Produto> cq = cb.createQuery(Produto.class);
            Root<Produto> r = cq.from(Produto.class);
            r.fetch("fabricante");

            cq.select(r);
            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(cb.upper(r.<String>get("nome")), "%" + filter.getNome().toUpperCase() + "%"));
            predicates.add(cb.like(cb.upper(r.<String>get("corPredominante")), "%" + filter.getCorPredominante().toUpperCase() + "%"));
            if(filter.getFabricante() != null)
                predicates.add(cb.equal(r.get("fabricante"), filter.getFabricante()));

            cq.where(cb.and(predicates.toArray(new Predicate[0])));

            out = this.entityManager.createQuery(cq).getResultList();

            this.entityManager.getTransaction().commit();

        } catch (Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            try {
                this.entityManager.close();
            } catch (Exception e) { }
        }


        return out;
    }

    @Override
    public Produto findById(int id) {
        Produto out = new Produto();
        try {

            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Produto> query = builder.createQuery(Produto.class);
            Root<Produto> r = query.from(Produto.class);
            r.fetch("fabricante");

            query.select(r)
                    .where(builder.and(
                            builder.equal(r.get("ativo"), true),
                            builder.equal(r.get("id"), id)
                    ));

            this.entityManager.getTransaction().begin();
            List<Produto> result = this.entityManager.createQuery(query)
                    .getResultList();

            if (result.size() > 0) out = result.get(0);
            this.entityManager.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            try {
                this.entityManager.close();
            } catch (Exception e) { }
        }


        return out;
    }
}
