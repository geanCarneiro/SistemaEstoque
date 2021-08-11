package br.com.sistemaestoque.dao;

import br.com.sistemaestoque.models.EntidadeGenerico;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public abstract class DAO<T extends EntidadeGenerico> {


    protected EntityManager entityManager;

    public DAO(){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("perUnit");
        this.entityManager = emf.createEntityManager();
    }


    public T save(T entity){
        if(entity.getId() == -1) {
            return insert(entity);
        } else {
            return update(entity);
        }
    }

    public T insert(T entity) {
        try {
            this.entityManager.getTransaction().begin();
            this.entityManager.persist(entity);
            this.entityManager.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            try {
                this.entityManager.close();
            } catch (Exception e){}
        }

        return entity;
    }

    public abstract T update(T entity);

    public abstract  List<T> findAll(T filter);

    public T remover(T entity){
        entity.setAtivo(false);
        return update(entity);
    }

    public abstract T findById(int id);

}
