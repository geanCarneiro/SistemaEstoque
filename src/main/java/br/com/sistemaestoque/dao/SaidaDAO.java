package br.com.sistemaestoque.dao;

import br.com.sistemaestoque.models.Saida;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SaidaDAO extends DAO<Saida> {

    private static ArrayList<Saida> saidas = new ArrayList<>();

    @Override
    public Saida update(Saida entity) {
        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaUpdate<Saida> update = builder.createCriteriaUpdate(Saida.class);
            Root<Saida> root = update.from(Saida.class);

            update.set("produto", entity.getProduto());
            update.set("quantidade", entity.getQuantidade());
            update.set("data", entity.getData());
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
    public List<Saida> findAll(Saida filter) {
        List<Saida> out = new ArrayList<>();
        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Saida> query = builder.createQuery(Saida.class);
            Root<Saida> root = query.from(Saida.class);
            root.fetch("produto").fetch("fabricante");

            query.select(root);

            ArrayList<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.equal(root.get("ativo"), true));
            if(filter.getProduto() != null)
                predicates.add(builder.equal(root.get("produto"), filter.getProduto()));

            query.where(predicates.toArray(new Predicate[0]));
            query.orderBy(builder.desc(root.get("data")));

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
    public Saida findById(int id) {
        Saida out = null;

        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Saida> query = builder.createQuery(Saida.class);
            Root<Saida> root = query.from(Saida.class);

            query.select(root);
            query.where(builder.equal(root.get("id"), id));

            List<Saida> result = this.entityManager.createQuery(query).getResultList();

            if(!result.isEmpty()) out = result.get(0);

            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            this.entityManager.close();
        }

        return out;
    }

    public List<Saida> findAfter(Date date) {
        List<Saida> out = null;

        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Saida> query = builder.createQuery(Saida.class);
            Root<Saida> root = query.from(Saida.class);
            root.fetch("produto");

            query.select(root);
            query.where(builder.and(
                    builder.isTrue(root.get("ativo")),
                    builder.greaterThanOrEqualTo(root.get("data"), date)
            ));

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
}
