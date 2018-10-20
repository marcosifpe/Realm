package teste;

import consultoriodescorp.Funcionario;
import consultoriodescorp.Paciente;
import consultoriodescorp.Receita;
import consultoriodescorp.Remedio;
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
public class TesteReceitaCrud extends TesteBase {

    @Test
    public void t01_persistiReceita() {
        logger.info("Executando t01: persistir receita");

        Receita receita = new Receita();
        Calendar c = Calendar.getInstance();
        String consulta = "SELECT f FROM Funcionario f WHERE f.id=?1";
        Query query = em.createQuery(consulta);
        query.setParameter(1, 9);
        Funcionario funcionario;
        funcionario = (Funcionario) query.getSingleResult();
        funcionario.getId();
        receita.setMedico(funcionario);

        String consulta1 = "SELECT p FROM Paciente p WHERE p.id=?2";
        Query query1 = em.createQuery(consulta1);
        query1.setParameter(2, 4);
        Paciente paciente;
        paciente = (Paciente) query1.getSingleResult();
        paciente.getId();
        receita.setPaciente(paciente);

        String consulta2 = "SELECT r FROM Remedio r WHERE r.id=?2";
        Query query2 = em.createQuery(consulta2);
        query2.setParameter(2, 5);
        Remedio remedio;
        remedio = (Remedio) query2.getSingleResult();
        remedio.getId();
        remedio.adicionarReceitas(receita);
        receita.setRemedio(remedio);

        c.set(2018, Calendar.OCTOBER, 28);
        receita.setDataReceita(c.getTime());

        em.persist(receita);
        em.flush();
        assertNotNull(receita.getId());
        logger.log(Level.INFO, "Receita {0} incluído com sucesso.", receita);
    }

    @Test
    public void t02_atualizarReceita() {
        logger.info("Executando t02: atualizar Receita ");
        Receita receita;
        String consulta = "SELECT r FROM Receita r WHERE  r.id=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);

        query.setParameter(1, 3);
        receita = (Receita) query.getSingleResult();
        receita.setDataReceita(getData(9, Calendar.OCTOBER, 2018));

        em.flush();
        query.setParameter(1, 3);
        receita = (Receita) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataReceita = dateFormat.format((Date) receita.getDataReceita());

        assertEquals("09-10-2018", dataReceita);

    }

    @Test
    public void t03_atualizarReceitaMerge() {
        logger.info("Executando t03: atualizar Receita com Merge");
        Receita receita;
        String consulta = "SELECT r FROM Receita r WHERE r.id=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, 5);
        receita = (Receita) query.getSingleResult();
        em.clear();
        receita.setDataReceita(getData(7, Calendar.OCTOBER, 2018));
        em.merge(receita);
        em.flush();

        query.setParameter(1, 5);
        receita = (Receita) query.getSingleResult();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dataReceita = dateFormat.format((Date) receita.getDataReceita());
        assertEquals("07-10-2018", dataReceita);
    }

    @Test
    public void t04_delete() {
        logger.info("Executando t04: DELETE Receita");
        Receita receita;
        String consulta = "SELECT r FROM  Receita r WHERE r.id=?1";
        Query query = em.createQuery(consulta);
        long id = 2;
        query.setParameter(1, id);
        receita = (Receita) query.getSingleResult();
        em.remove(receita);
        em.flush();
        Map map = new HashMap();
        map.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        Receita deletado = em.find(Receita.class, id, map);
        assertNull(deletado);

    }

}
