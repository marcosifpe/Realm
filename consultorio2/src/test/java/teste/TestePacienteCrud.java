package teste;

import consultoriodescorp.Paciente;
import consultoriodescorp.TipoPlanoSaude;
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
public class TestePacienteCrud extends TesteBase{

    @Test
    public void t01_persistirPaciente() {
        logger.info("Executando t01: persistir paciente");

        Paciente paciente = new Paciente();

        paciente.setNome("Maria Joana");
        paciente.setLogin("mariajoana");
        paciente.setEmail("mariaj@gmail.com");
        paciente.setSenha("mariaj123");
        paciente.setSexo("F");
        paciente.setPlano(TipoPlanoSaude.HAPVIDA);

        em.persist(paciente);
        em.flush();
        assertNotNull(paciente.getId());
        logger.log(Level.INFO, "Funcionario {0} incluída com sucesso.", paciente);
    }

    @Test
    public void t02_atualizarpaciente() {
        logger.info("Executando t02: atualizar paciente");
        Paciente paciente;
        String consulta = "SELECT p FROM Paciente p WHERE  p.senha=?1 ";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, "joao123");
        paciente = (Paciente) query.getSingleResult();
        paciente.setSenha("MacacosMeMordam");
        em.flush();
        query.setParameter(1, "MacacosMeMordam");
        paciente = (Paciente) query.getSingleResult();
        
        assertEquals("MacacosMeMordam", paciente.getSenha());

    }

    @Test
    public void t03_atualizarPacienteMerge() {
        logger.info("Executando t03: atualizar paciente com Merge");
        Paciente paciente;
        String consulta = "SELECT p FROM Paciente p WHERE p.login=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, "mariajoaquina");
        paciente = (Paciente) query.getSingleResult();
        assertNotNull(paciente);
        em.clear();
        paciente.setLogin("mariaj");
        em.merge(paciente);
        em.flush();
        query.setParameter(1, "mariaj");
        paciente = (Paciente) query.getSingleResult();
        assertEquals("mariaj", paciente.getLogin());
    }

    @Test
    public void t04_persistirPaciente() {
        logger.info("Executando t01: persistir Paciente");

        Paciente paciente = new Paciente();
        paciente.setNome("Joao Pedro");
        paciente.setLogin("Joao Pedro");
        paciente.setEmail("joaopedro@gmail.com");
        paciente.setSenha("joao123");
        paciente.setSexo("M");
        paciente.setPlano(TipoPlanoSaude.AMIL);
        em.persist(paciente);
        em.flush();
        assertNotNull(paciente.getId());
        logger.log(Level.INFO, "Paciente {0} incluída com sucesso.", paciente);
    }

    @Test
    public void t05_delete() {
        logger.info("Executando t05: DELETE paciente");
        Paciente paciente;
        String consulta = "SELECT p FROM Paciente p WHERE p.id=?1";
        Query query = em.createQuery(consulta);
        long id = 1;
        query.setParameter(1, id);
        paciente = (Paciente) query.getSingleResult();
        em.remove(paciente);
        em.flush();
        Map map = new HashMap();
        map.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        Paciente deletado = em.find(Paciente.class, id, map);
        assertNull(deletado);

    }

}
