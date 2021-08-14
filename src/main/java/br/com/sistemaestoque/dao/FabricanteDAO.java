package br.com.sistemaestoque.dao;

import br.com.sistemaestoque.models.EntidadeGenerico;
import br.com.sistemaestoque.models.Fabricante;
import br.com.sistemaestoque.models.Produto;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FabricanteDAO extends DAO<Fabricante> {

    @Override
    public Fabricante update(Fabricante entity) {
        try {
            this.entityManager.getTransaction().begin();

            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();
            CriteriaUpdate<Fabricante> update = cb.createCriteriaUpdate(Fabricante.class);
            Root<Fabricante> fabricanteRoot = update.from(Fabricante.class);

            update.set("nome", entity.getNome())
                    .set("ativo", entity.isAtivo())
                    .where(cb.equal(fabricanteRoot.get("id"), entity.getId()));

            this.entityManager.createQuery(update).executeUpdate();
            this.entityManager.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            try {
                this.entityManager.close();
            } catch (Exception e) { }
        }

        return entity;
    }

    @Override
    public List findAll(Fabricante filter) {
        List<Fabricante> out = new ArrayList();
        try {
            this.entityManager.getTransaction().begin();
            CriteriaBuilder cb = this.entityManager.getCriteriaBuilder();

            CriteriaQuery<Fabricante> cq = cb.createQuery(Fabricante.class);
            Root<Fabricante> r = cq.from(Fabricante.class);
            cq.select(r);
            cq.where(cb.and(
                    cb.isTrue(r.get("ativo")),
                    cb.like(cb.upper(r.<String>get("nome")), "%" + filter.getNome().toUpperCase() + "%")
            ));
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

    @Override
    public Fabricante findById(int id) {
        Fabricante out = new Fabricante();
        try {
            this.entityManager.getTransaction().begin();

            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Fabricante> query = builder.createQuery(Fabricante.class);
            Root<Fabricante> fabricanteRoot = query.from(Fabricante.class);

            query.select(fabricanteRoot)
                    .where(builder.equal(fabricanteRoot.get("id"), id));

            List<Fabricante> result = this.entityManager.createQuery(query).getResultList();

            if (result.size() > 0) out = result.get(0);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                this.entityManager.close();
            } catch (Exception e) { }
        }


        return out;
    }
}
