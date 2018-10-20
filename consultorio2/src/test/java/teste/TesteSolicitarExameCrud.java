package teste;

import consultoriodescorp.Exame;
import consultoriodescorp.Funcionario;
import consultoriodescorp.Paciente;
import consultoriodescorp.SolicitarExame;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class TesteSolicitarExameCrud extends TesteBase {
    
    @Test
    public void t01_persistirSolicitarExame() {
        logger.info("Executando t01: persistir Solicitar Exame");
        Calendar c = Calendar.getInstance();
        SolicitarExame solicita = new SolicitarExame();
        String consulta = "SELECT f FROM Funcionario f WHERE f.id=?1";
        Query query = em.createQuery(consulta); 
        query.setParameter(1,5);
        Funcionario funcionario;
        funcionario=(Funcionario)query.getSingleResult();
        funcionario.getId();
        solicita.setMedico(funcionario);
        
        String consulta1 = "SELECT p FROM Paciente p WHERE p.id=?2";
        Query query1 = em.createQuery(consulta1); 
        query1.setParameter(2,4);
        Paciente paciente;
        paciente=(Paciente)query1.getSingleResult();
        paciente.getId();
        solicita.setPaciente(paciente);
        
        String consulta2 = "SELECT e FROM Exame e WHERE e.id=?3"; 
        Query query2 = em.createQuery(consulta2);
        query2.setParameter(3, 1);
        Exame exame;
        exame=(Exame)query2.getSingleResult();
        exame.getId();
        solicita.setExame(exame);
        c.set(2018, Calendar.OCTOBER, 01);
        solicita.setDataExame(c.getTime());
        
        em.persist(solicita);
        em.flush();
        assertNotNull(solicita.getId());
        logger.log(Level.INFO, "Marcar Consulta {0} incluída com sucesso.", solicita);
    }

    @Test
    public void t02_atualizarSolicitarExame() {
        logger.info("Executando t02: atualizar Solicitar Exame ");
        SolicitarExame solicita; 
        String consulta = "SELECT se FROM SolicitarExame se WHERE se.id=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1,1);
        solicita = (SolicitarExame) query.getSingleResult();
       solicita.setDataExame(getData(15,Calendar.OCTOBER,2018));
        
        em.flush();
        query.setParameter(1, 1);
        solicita = (SolicitarExame) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataExame = dateFormat.format((Date) solicita.getDataExame());
        assertEquals("15-10-2018", dataExame);

    }
    
    @Test
    public void t03_atualizarSolicitarExameMerge() {
       logger.info("Executando t03: atualizar Solicitar exame com Merge");
        SolicitarExame solicita; 
        String consulta = "SELECT se FROM SolicitarExame se WHERE se.id=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1,3);
        solicita = (SolicitarExame) query.getSingleResult();
        solicita.setDataExame(getData(11,Calendar.NOVEMBER,2018));
        
        query.setParameter(1, 3);
        solicita = (SolicitarExame) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataExame = dateFormat.format((Date) solicita.getDataExame());
        
        assertEquals("11-11-2018", dataExame);
    }

    @Test
    public void t04_delete() {
        logger.info("Executando t05: DELETE Solicitar Exame");
        SolicitarExame solicita;
        String consulta = "SELECT se FROM SolicitarExame se WHERE se.id=?1";
        Query query = em.createQuery(consulta);
        long id = 2;
        query.setParameter(1, id);
        solicita = (SolicitarExame) query.getSingleResult();
        em.remove(solicita);
        em.flush();
        Map map = new HashMap();
        map.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        SolicitarExame deletado = em.find(SolicitarExame.class, id, map);
        assertNull(deletado);

    }

}
