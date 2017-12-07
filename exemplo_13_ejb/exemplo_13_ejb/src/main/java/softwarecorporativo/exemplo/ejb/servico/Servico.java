/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb.servico;

import java.lang.reflect.Field;
import java.util.List;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.REQUIRED;
import static javax.ejb.TransactionAttributeType.SUPPORTS;
import static javax.ejb.TransactionManagementType.CONTAINER;
import javax.ejb.TransactionManagement;
import static javax.persistence.PersistenceContextType.TRANSACTION;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import softwarecorporativo.exemplo.ejb.entidade.Entidade;

/**
 *
 * @author marcos
 * @param <T>
 */
@TransactionManagement(CONTAINER)
@TransactionAttribute(SUPPORTS)
public abstract class Servico<T extends Entidade> {

    @PersistenceContext(name = "exemplo_13_ejb", type = TRANSACTION)
    protected EntityManager entityManager;
    protected Class<T> classe;
    protected String queryExistencia;
    protected String queryEntidade;
    protected String queryEntidades;
    protected String atributoExistencia;

    protected void setClasse(Class<T> classe) {
        this.classe = classe;
    }

    protected void setQueryExistencia(String queryExistencia) {
        this.queryExistencia = queryExistencia;
    }

    protected void setQueryEntidade(String queryEntidade) {
        this.queryEntidade = queryEntidade;
    }

    protected void setQueryEntidades(String queryEntidades) {
        this.queryEntidades = queryEntidades;
    }

    protected void setAtributoExistencia(String atributoExistencia) {
        this.atributoExistencia = atributoExistencia;
    }

    public abstract T criar();

    @TransactionAttribute(REQUIRED)
    public void persistir(T entidade) {
        if (!existe(entidade)) {
            entityManager.persist(entidade);
        }
    }

    @TransactionAttribute(REQUIRED)
    public void atualizar(T entidade) {
        if (existe(entidade)) {
            entityManager.merge(entidade);
            entityManager.flush();
        }
    }

    public T get(Long id) {
        return entityManager.find(classe, id);
    }

    public T getEntidade(Object[] parametros) {
        TypedQuery<T> query = entityManager.createNamedQuery(queryEntidade, classe);

        int i = 1;
        for (Object parametro : parametros) {
            query.setParameter(i++, parametro);
        }

        return query.getSingleResult();
    }

    public List<T> getEntidades(Object[] parametros) {
        TypedQuery<T> query = entityManager.createNamedQuery(queryEntidades, classe);

        int i = 1;
        for (Object parametro : parametros) {
            query.setParameter(i++, parametro);
        }

        return query.getResultList();
    }

    private Field getAtributo(Class<?> clazz, String nome) {
        Class<?> atual = clazz;
        Field field = null;
        do {
            try {
                field = atual.getDeclaredField(nome);
                if (field != null) {
                    break;
                }
            } catch (NoSuchFieldException | SecurityException ex) {
            }
        } while ((atual = atual.getSuperclass()) != null);
        return field;
    }

    private Object getValorAtributo(T entidade) {
        try {
            Field field = getAtributo(entidade.getClass(),
                    this.atributoExistencia);
            field.setAccessible(true);
            return field.get(entidade);

        } catch (SecurityException | IllegalArgumentException |
                IllegalAccessException ex) {
            throw new ExcecaoSistema(ex);
        }
    }

    public boolean existe(T entidade) {
        TypedQuery<T> query = entityManager.createNamedQuery(this.queryExistencia, classe);
        query.setParameter(1, getValorAtributo(entidade));
        return !query.getResultList().isEmpty();
    }
}
