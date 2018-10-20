package teste;

import consultoriodescorp.Paciente;
import consultoriodescorp.TipoPlanoSaude;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
public class QueryPaciente extends TesteBase {

    @Test
    public void t01_listarPacientes() {
        logger.info("Executando t01: listar pacientes");

        List<Paciente> pacientes;
        Query query = em.createNamedQuery("Listar.Paciente", Paciente.class);
        pacientes = query.getResultList();

        assertEquals(5, pacientes.size());
    }

    @Test
    public void t02_listarPacientesPorPlano() {
        logger.info("Executando t02: listar pacientes por plano HAPVIDA");
        List<Paciente> pacientes;
        TypedQuery<Paciente> query = em.createNamedQuery("Listar.Paciente.Planos", Paciente.class);
        query.setParameter(1, TipoPlanoSaude.HAPVIDA);
        pacientes = query.getResultList();

        for (Paciente paciente : pacientes) {
            assertEquals(TipoPlanoSaude.HAPVIDA, paciente.getPlano());
        }
        assertEquals(2, pacientes.size());
    }

    @Test
    public void t03_SelecionarDiferentesPlanosDePaciente() {
        logger.info("Executando t03: Selecionar Diferente Planos de Paciente");
        List<String> pacientes;
        TypedQuery<String> query = em.createQuery("SELECT DISTINCT(p.plano) FROM Paciente p ORDER BY p.plano", String.class);
        pacientes = query.getResultList();
        assertEquals(3, pacientes.size());
    }

    @Test
    public void t4_SelecionarPacientePorNome() {
        logger.info("Executando t4: Selecionar Paciente por nome");
        List<Paciente> pacientes;
        String consulta = "SELECT p FROM Paciente p WHERE p.nome LIKE :nome";
        Query query = em.createQuery(consulta);
        query.setParameter("nome", "Maria%");
        pacientes = query.getResultList();
        for (Paciente paciente : pacientes) {
            assertTrue(paciente.getNome().startsWith("Maria"));
        }
        assertEquals(2, pacientes.size());
    }

    @Test
    public void t5_SelecionarTodosOsPacientesPossuemGmail() {
        logger.info("Executando t5: selecionar os pacientes que possuem gmail");
        List<Paciente> pacientes;
        TypedQuery<Paciente> query = em.createQuery("SELECT p FROM Paciente p WHERE p.email LIKE :email", Paciente.class);
        query.setParameter("email", "%@gmail.com");
        pacientes = query.getResultList();
        for (Paciente paciente : pacientes) {
            assertTrue(paciente.getEmail().endsWith("@gmail.com"));
        }
        assertEquals(5, pacientes.size());
    }
    @Test
    public void t6_ContarTodosPacientesQuePossuemPlanoUnimedHAVING(){
        logger.info("contar os pacientes que possuem plano unimed e ordenar por id");
        List<Paciente>pacientes;
        String consulta = "SELECT p FROM Paciente p, Usuario u WHERE p.plano=?1 group by p.id HAVING COUNT(u.id)>1";
        Query query = em.createQuery(consulta);
        query.setParameter(1, TipoPlanoSaude.UNIMED);
        pacientes = query.getResultList();
        for(Paciente paciente:pacientes){
            assertEquals(TipoPlanoSaude.UNIMED, paciente.getPlano());
        }
        assertEquals(2, pacientes.size());
    
    }   

}
