package teste;

import consultoriodescorp.Remedio;
import java.util.List;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
//query com sql nativa retornando valor e o objeto
//query have e group by pode ser jpql
/**
 *
 * @author valeria
 */
public class QueryRemedio extends TesteBase {

    @Test
    public void selecionarRemedioPorTratamento() {
        logger.info("Selecionar remedio por tratamento Named query SQL nativo ");
        List<Remedio> remedios;
        TypedQuery<Remedio> query = em.createNamedQuery("Remedio.PorTratamento", Remedio.class);
        query.setParameter(1, "Pressão alta");
        remedios = query.getResultList();
        for (Remedio remedio : remedios) {
            assertEquals("Pressão alta", remedio.getTratamento());
        }
        assertEquals(2, remedios.size());
    }

    @Test
    public void SelecionarTodasAsReceitasQuePossuemRemedioPorNome() {
        Query query; 
        query = em.createNamedQuery("TotalRemediosEmReceitasSQL");
        query.setParameter(1,"Atensina");
        Object[] resultado = (Object[]) query.getSingleResult();
        assertEquals("Atensina", ((Remedio)resultado[0]).getNomeRemedio());
        assertEquals(2L, resultado[1]);
    }

    @Test
    public void selecionarRemedioPorNome() {
        logger.info("Selecionar remedio por nome SQL nativo");
        List<Remedio> remedios;
        String consulta = "SELECT ID_REMEDIO, CL_NOME_REMEDIO, CL_TRATAMENTO, CL_DURACAO  FROM TB_REMEDIO WHERE CL_NOME_REMEDIO LIKE ?";
        Query query = em.createNativeQuery(consulta, Remedio.class);
        query.setParameter(1, "Cetoconazol");
        remedios = query.getResultList();
        for (Remedio remedio : remedios) {
            assertEquals("Cetoconazol", remedio.getNomeRemedio());
        }
        assertEquals(1, remedios.size());
    }

    @Test
    public void selecionarPorId() {
        logger.info("Selecionar remedio por id");
        String consulta = "SELECT r FROM Remedio r WHERE r.id=?1";
        TypedQuery<Remedio> query = em.createQuery(consulta, Remedio.class);
        query.setParameter(1, 3);
        Remedio remedio = query.getSingleResult();
        Assert.assertNotNull(remedio);
    }

    @Test(expected = NoResultException.class)
    public void selecionarPorIdInexistente() {
        logger.info("Selecionar remedio por id inexistente");
        String consulta = "SELECT r FROM Remedio r WHERE r.id=?1";
        TypedQuery<Remedio> query = em.createQuery(consulta, Remedio.class);
        query.setParameter(1, 300);
        query.getSingleResult();
    }

}
