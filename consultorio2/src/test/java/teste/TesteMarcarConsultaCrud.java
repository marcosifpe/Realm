package teste;

import consultoriodescorp.Funcionario;
import consultoriodescorp.MarcarConsulta;
import consultoriodescorp.Paciente;
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
public class TesteMarcarConsultaCrud extends TesteBase {
    
    @Test
    public void t01_persistirMarcarConsulta() {
        logger.info("Executando t01: persistir Marcar consulta");
        Calendar c = Calendar.getInstance();
        MarcarConsulta marcar = new MarcarConsulta();
        String consulta = "SELECT f FROM Funcionario f WHERE f.id=?1";
        Query query = em.createQuery(consulta); 
        query.setParameter(1,5);
        Funcionario funcionario;
        funcionario=(Funcionario)query.getSingleResult();
        funcionario.getId();
        marcar.adicionarMedico(funcionario);
        
        String consulta1 = "SELECT p FROM Paciente p WHERE p.id=?2";
        Query query1 = em.createQuery(consulta1); 
        query1.setParameter(2,4);
        Paciente paciente;
        paciente=(Paciente)query1.getSingleResult();
        paciente.getId();
        marcar.setPaciente(paciente);
      
        c.set(2018, Calendar.OCTOBER, 01);
        marcar.setDataConsuta(c.getTime());
        marcar.setHora("14:00H");
        
        em.persist(marcar);
        em.flush();
        assertNotNull(marcar.getId());
        logger.log(Level.INFO, "Marcar Consulta {0} incluída com sucesso.", marcar);
    }

    @Test
    public void t02_atualizarMarcarConsulta() {
        logger.info("Executando t02: atualizar Marcar Consulta ");
        MarcarConsulta marcar;
        String consulta = "SELECT mc FROM MarcarConsulta mc WHERE mc.id=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, 1);
        marcar = (MarcarConsulta) query.getSingleResult();
        marcar.setHora("15:00H");
       
        em.flush();
        query.setParameter(1, 1);
        marcar = (MarcarConsulta) query.getSingleResult();
        
        assertEquals("15:00H", marcar.getHora());

    }
    
    @Test
    public void t03_atualizarMarcarConsultaMerge() {
        logger.info("Executando t03: atualizar MarcarConsulta com Merge");
        MarcarConsulta marcar;
        String consulta = "SELECT mc FROM MarcarConsulta mc WHERE mc.id=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1 ,1);
        marcar = (MarcarConsulta) query.getSingleResult();
        em.clear();
        marcar.setDataConsuta(getData(05,Calendar.OCTOBER,2018));
        em.merge(marcar);
        em.flush();
        
        query.setParameter(1 ,1);
        marcar = (MarcarConsulta) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataConsulta = dateFormat.format((Date) marcar.getDataConsuta());
        assertEquals("05-10-2018", dataConsulta);
    }

    @Test
    public void t04_delete() {
        logger.info("Executando t05: DELETE cosulta marcada");
        MarcarConsulta marcar;
        String consulta = "SELECT mc FROM MarcarConsulta mc WHERE mc.id=?1";
        Query query = em.createQuery(consulta);
        long id = 2;
        query.setParameter(1, id);
        marcar = (MarcarConsulta) query.getSingleResult();
        em.remove(marcar);
        em.flush();
        Map map = new HashMap();
        map.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        MarcarConsulta deletado = em.find(MarcarConsulta.class, id, map);
        assertNull(deletado);

    }

}
