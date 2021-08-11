package br.com.sistemaestoque.dao;

import br.com.sistemaestoque.models.Usuario;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO extends DAO<Usuario> {

    @Override
    public Usuario update(Usuario entity) {
        try {
           this.entityManager.getTransaction().begin();

           CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
           CriteriaUpdate<Usuario> update = builder.createCriteriaUpdate(Usuario.class);
           Root<Usuario> usuarioRoot = update.from(Usuario.class);

           update.set("nome", entity.getNome())
                   .set("usuario", entity.getUsuario())
                   .set("senha", entity.getSenha())
                   .set("funcao", entity.getFuncao())
                   .set("ativo", entity.isAtivo())
                   .where(
                       builder.equal(usuarioRoot.get("id"), entity.getId())
                   );

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
    public List<Usuario> findAll(Usuario filter) {
        try {
            this.entityManager.getTransaction().begin();

            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
            Root<Usuario> usuarioRoot = query.from(Usuario.class);

            ArrayList<Predicate> predicates = new ArrayList<>();

            predicates.add(builder.isTrue(usuarioRoot.get("ativo")));
            predicates.add(builder.like(builder.upper(usuarioRoot.get("nome")), "%" + filter.getNome().toUpperCase() + "%"));
            predicates.add(builder.like(builder.upper(usuarioRoot.get("usuario")), "%" + filter.getUsuario().toUpperCase() + "%"));
            if (filter.getFuncao() != null)
                predicates.add(builder.equal(builder.upper(usuarioRoot.get("funcao")), filter.getFuncao()));

            query.select(usuarioRoot)
                    .where(predicates.toArray(new Predicate[0]));

            return this.entityManager.createQuery(query).getResultList();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.entityManager.close();
        }

        return new ArrayList<>();
    }

    @Override
    public Usuario findById(int id) {
        try{
            this.entityManager.getTransaction().begin();

            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
            Root<Usuario> usuarioRoot = query.from(Usuario.class);

            query.select(usuarioRoot)
                    .where(builder.equal(usuarioRoot.get("id"), id));

            List<Usuario> result = this.entityManager.createQuery(query).getResultList();

            if(!result.isEmpty()) return result.get(0);
        } catch (Exception e){
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            this.entityManager.close();
        }

        return null;
    }

    public Usuario findByLogin(String usuario, String senha) {
        try{
            this.entityManager.getTransaction().begin();

            CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
            CriteriaQuery<Usuario> query = builder.createQuery(Usuario.class);
            Root<Usuario> usuarioRoot = query.from(Usuario.class);

            query.select(usuarioRoot)
                    .where(builder.and(
                            builder.isTrue(usuarioRoot.get("ativo")),
                            builder.equal(builder.upper(usuarioRoot.get("usuario")), usuario.toUpperCase()),
                            builder.equal(usuarioRoot.get("senha"), senha)
                    ));

            List<Usuario> result = this.entityManager.createQuery(query).getResultList();

            if(!result.isEmpty()) return result.get(0);

            this.entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            this.entityManager.getTransaction().rollback();
        } finally {
            this.entityManager.close();
        }

        return null;
    }
}
