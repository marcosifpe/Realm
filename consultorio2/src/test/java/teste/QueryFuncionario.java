package teste;

import consultoriodescorp.Funcionario;
import consultoriodescorp.TipoFuncionario;
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
public class QueryFuncionario extends TesteBase {

    @Test
    public void t01_listarTipoFuncionario() {
        logger.info("Executando t01: listar tipo funcionario");

        List<Funcionario> funcionarios;
        Query query = em.createNamedQuery("TipoFuncionario", Funcionario.class);
        query.setParameter(1, TipoFuncionario.SECRETARIA);
        funcionarios = query.getResultList();
        for (Funcionario funcionario : funcionarios) {
            assertEquals(TipoFuncionario.SECRETARIA,funcionario.getTipo());
        }
        assertEquals(1, funcionarios.size());
    }

    @Test
    public void t02_contarQuantosFuncionariosSaoMedicos() {
        logger.info("Executando t02: contar quantos funcionario são medicos");
        Long resTipo;
        
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(f) FROM Funcionario f WHERE f.tipo=?1", Long.class);
        query.setParameter(1, TipoFuncionario.MEDICO);
        resTipo = query.getSingleResult();

        assertEquals(new Long(3), resTipo);
    }

    @Test
    public void t03_SelecionarFuncionarioPorEspecialidade() {
        logger.info("Executando t03: Selecionar Funcionario por especialidade");
        List<Funcionario> funcionarios;
        String consulta = "SELECT f FROM Funcionario f WHERE f.especialidade=?1";
        Query query = em.createQuery(consulta);
        query.setParameter(1, "HOTORRINOLARINGOLOGISTA");
        funcionarios = query.getResultList();
        for (Funcionario funcionario : funcionarios) {
           assertEquals("HOTORRINOLARINGOLOGISTA",funcionario.getEspecialidade()) ;
        }
        assertEquals(1, funcionarios.size());

    }

    @Test
    public void t4_ContarQuantosFuncionariosPossuemNivelSuperior() {
        logger.info("Executando t4: Contar quantos funcinarios possuem nivel superior");
        Long resEscolaridade;
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(f) FROM Funcionario f WHERE f.escolaridade=?1", Long.class);
        query.setParameter(1, "ENSINO SUPERIOR COMPLETO");
        resEscolaridade = query.getSingleResult();
        assertEquals(new Long(4), resEscolaridade);
    }

    @Test
    public void t5_ContarTotalFuncionarios() {
        logger.info("Executando t5: contar o total de funcionarios existente");
        Long resFuncionario;
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(f) FROM Funcionario f", Long.class);
        resFuncionario = query.getSingleResult();
        assertEquals(new Long(5) , resFuncionario);
    }

}
