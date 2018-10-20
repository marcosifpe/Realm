package teste;

import consultoriodescorp.Exame;
import consultoriodescorp.Funcionario;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author valeria
 */
public class TesteUpdateDelete extends TesteBase{
    @Test
    public void QueryUpdate(){
        logger.info("Efetuando Update");
        Query update = em.createQuery("UPDATE Funcionario AS f SET f.senha=?1 WHERE f.id=?2 ");
        update.setParameter(1, "BolaQuadrada123");
        update.setParameter(2, 5);
        update.executeUpdate();
        String consulta = "SELECT f FROM Funcionario f WHERE f.id=?1";
        TypedQuery<Funcionario> query=em.createQuery(consulta, Funcionario.class);
        query.setParameter(1, 5);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        Funcionario funcionario =(Funcionario) query.getSingleResult();
        
        assertEquals("BolaQuadrada123", funcionario.getSenha());
    }
    
    @Test(expected = NoResultException.class)
    public void QueryDelete(){
        logger.info("Efetuando Delete");
        Query delete = em.createQuery("DELETE FROM Exame AS e WHERE e.id = ?1");
        delete.setParameter(1, 1);
        delete.executeUpdate();
        String consulta = "SELECT e FROM Exame e WHERE e.id = ?1";
        TypedQuery<Exame> query = em.createQuery(consulta, Exame.class);
        query.setParameter(1, 1);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.getSingleResult();
              
       
    }
}
