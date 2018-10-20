package teste;

import consultoriodescorp.Remedio;
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
public class TesteRemedioCrud extends TesteBase{

    @Test
    public void t01_persistiRemedio() {
        logger.info("Executando t01: persistir remedio");
        Remedio remedio = new Remedio();
        
        remedio.setNomeRemedio("Buzonid");
        remedio.setTratamento("Descongestionante");
        remedio.setDuracao("7 dias");
       
        em.persist(remedio);
        em.flush();
        assertNotNull(remedio.getId());
        logger.log(Level.INFO, "receita {0} incluído com sucesso.", remedio);
    }

    @Test
    public void t02_atualizarRemedio() {
        logger.info("Executando t02: atualizar Remedio ");
        Remedio remedio;
        String consulta = "SELECT r FROM Remedio r WHERE r.tratamento=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1,"Queimadura");
        remedio = (Remedio) query.getSingleResult();
        remedio.setTratamento("Queimadura e assadura");
       
        em.flush();
        query.setParameter(1, "Queimadura e assadura");
        remedio = (Remedio) query.getSingleResult();
        
        assertEquals("Queimadura e assadura", remedio.getTratamento());

    } 
    
    @Test
    public void t03_atualizarRemedioMerge() {
        logger.info("Executando t03: atualizar Remedio com Merge");
        Remedio remedio;
        String consulta = "SELECT r FROM Remedio r WHERE r.nomeRemedio=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1 ,"Aerolim");
        remedio = (Remedio) query.getSingleResult();
        em.clear();
        remedio.setNomeRemedio("Melxi Xarope");
        em.merge(remedio);
        em.flush();
        
        query.setParameter(1 ,"Melxi Xarope");
        remedio = (Remedio) query.getSingleResult();
        
        assertEquals("Melxi Xarope", remedio.getNomeRemedio());
    }
    
    @Test
    public void t04_delete() {
        logger.info("Executando t04: DELETE Remedio");
        Remedio remedio;
        String consulta = "SELECT r FROM  Remedio r WHERE r.id=?1";
        Query query = em.createQuery(consulta);
        long id = 2;
        query.setParameter(1, id);
        remedio = (Remedio) query.getSingleResult();
        em.remove(remedio);
        em.flush();
        Map map = new HashMap();
        map.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        Remedio deletado = em.find(Remedio.class, id, map);
        assertNull(deletado);

    }

}
