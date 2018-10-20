package teste;

import consultoriodescorp.Funcionario;
import consultoriodescorp.TipoFuncionario;
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
public class TesteFuncionarioCrud extends TesteBase{

    @Test
    public void t01_persistirFuncionario() {
        logger.info("Executando t01: persistir funcionario");

        Funcionario funcionario = new Funcionario();

        funcionario.setNome("Joaquina Baquita");
        funcionario.setLogin("joaquinabaquita");
        funcionario.setEmail("joaquinabaq@gmail.com");
        funcionario.setSenha("joaquina123");
        funcionario.setSexo("F");
        funcionario.setTipo(TipoFuncionario.ATENDENTE);
        funcionario.setEscolaridade("ENSINO SUPERIOR INCOMPLETO");
        funcionario.setEspecialidade("TECNICO EM ADMINISTRAÇÃO");

        em.persist(funcionario);
        em.flush();
        assertNotNull(funcionario.getId());
        logger.log(Level.INFO, "Funcionario {0} incluída com sucesso.", funcionario);
    }

    @Test
    public void t02_atualizarFuncionario() {
        logger.info("Executando t02: atualizar funcionario");
        Funcionario funcionario;
        String consulta = "SELECT f FROM Funcionario f WHERE f.email=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, "Joseluiz@gmail.com");
        funcionario = (Funcionario) query.getSingleResult();
        funcionario.setEmail("joaquinabaquita@gmail.com");
        em.flush();
        query.setParameter(1, "joaquinabaquita@gmail.com");
        funcionario = (Funcionario) query.getSingleResult();
        assertEquals("joaquinabaquita@gmail.com", funcionario.getEmail());

    }

    @Test
    public void t03_atualizarFuncionarioMerge() {
        logger.info("Executando t03: atualizar funcionario com Merge");
        Funcionario funcionario;
        String consulta = "SELECT f FROM Funcionario f WHERE f.login=?1";
        Query query = em.createQuery(consulta);
        query.setHint("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        query.setParameter(1, "anaclara");
        funcionario = (Funcionario) query.getSingleResult();
        assertNotNull(funcionario);
        em.clear();
        funcionario.setLogin("joaquinabaq");
        em.merge(funcionario);
        em.flush();
        query.setParameter(1, "joaquinabaq");
        funcionario = (Funcionario) query.getSingleResult();
        assertEquals("joaquinabaq", funcionario.getLogin());
    }

    @Test
    public void t04_persistirFuncionario() {
        logger.info("Executando t01: persistir funcionario");

        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Maria Izabel");
        funcionario.setLogin("mariaiza");
        funcionario.setEmail("mariaiza@gmail.com");
        funcionario.setSenha("mariaiza123");
        funcionario.setSexo("F");
        funcionario.setTipo(TipoFuncionario.MEDICO);
        funcionario.setEscolaridade("ENSINO SUPERIOR COMPLETO");
        funcionario.setEspecialidade("ORTOPEDISTA");
        em.persist(funcionario);
        em.flush();
        assertNotNull(funcionario.getId());
        logger.log(Level.INFO, "Funcionario {0} incluída com sucesso.", funcionario);
    }

    @Test
    public void t05_delete() {
        logger.info("Executando t05: DELETE funcionario");
        Funcionario funcionario;
        String consulta = "SELECT f FROM  Funcionario f WHERE f.id=?7";
        Query query = em.createQuery(consulta);
        long id = 7;
        query.setParameter(7, id);
        funcionario = (Funcionario) query.getSingleResult();
        em.remove(funcionario);
        em.flush();
        Map map = new HashMap();
        map.put("javax.persistence.cache.retrieveMode", CacheRetrieveMode.BYPASS);
        Funcionario deletado = em.find(Funcionario.class, id, map);
        assertNull(deletado);

    }

}
