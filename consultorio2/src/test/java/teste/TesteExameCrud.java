package teste;

import consultoriodescorp.Exame;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import javax.persistence.CacheRetrieveMode;
import javax.persistence.Query;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Valéria
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings("JPQLValidation")
public class TesteExameCrud extends TesteBase {

    @Test
    public void t01_persistiExame() {
        logger.info("Executando t01: persistir exame");

        Exame exame = new Exame();
        exame.setNome("Ecocardiograma");
        
        em.persist(exame);
        em.flush();
        assertNotNull(exame.getId());
        logger.log(Level.INFO, "Exame {0} incluído com sucesso.", exame);
    }

    @Test
    public void t02_atualizarExame() {
        logger.info("Executando t02: atualizar Exame ");
        Exame exame;
        String consulta = "SELECT e FROM Exame e WHERE e.nome=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, "Ultrasom do torax");
        exame = (Exame) query.getSingleResult();
        exame.setNome("Tomografia do torax");

        em.flush();
        query.setParameter(1, "Tomografia do torax");
        exame = (Exame) query.getSingleResult();

        assertEquals("Tomografia do torax", exame.getNome());

    }

    @Test
    public void t03_atualizarExameMerge() {
        logger.info("Executando t03: atualizar exame com Merge");
        Exame exame;
        String consulta = "SELECT e FROM Exame e WHERE e.id=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, 5);
        exame = (Exame) query.getSingleResult();
        em.clear();
        exame.setNome("Tomografia da face");
        em.merge(exame);
        em.flush();

        query.setParameter(1, 5);
        exame = (Exame) query.getSingleResult();

        assertEquals("Tomografia da face", exame.getNome());
    }

    @Test
    public void t04_delete() {
        logger.info("Executando t05: DELETE exame");
        Exame exame;
        String consulta = "SELECT e FROM  Exame e WHERE e.id=?1";
        Query query = em.createQuery(consulta);
        long id = 4;
        query.setParameter(1, id);
        exame = (Exame) query.getSingleResult();
        em.remove(exame);
        em.flush();
        Map map = new HashMap();
        map.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        Exame deletado = em.find(Exame.class, id, map);
        assertNull(deletado);

    }

}
