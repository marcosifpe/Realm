/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import softwarecorporativo.exemplo.ejb.entidade.Comprador;
import softwarecorporativo.exemplo.ejb.servico.CompradorServico;

/**
 *
 * @author marcos
 */
public class CompradorTeste {

    private static EJBContainer container;
    private static Logger logger;
    private CompradorServico compradorServico;

    @BeforeClass
    public static void setUpClass() {
        logger = Logger.getGlobal();
        logger.setLevel(Level.INFO);
        container = EJBContainer.createEJBContainer();
        DbUnitUtil.inserirDados();
    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    @Before
    public void setUp() throws NamingException {
        compradorServico = (CompradorServico) container.getContext().lookup("java:global/classes/ejb/CompradorServico!softwarecorporativo.exemplo.ejb.servico.CompradorServico");
    }

    @After
    public void tearDown() {
        compradorServico = null;
    }

    @Test
    public void existeComprador() {
        Comprador comprador = compradorServico.criar();
        comprador.setCpf("740.707.044-00");
        assertTrue(compradorServico.existe(comprador));
    }

    @Test
    public void getCompradorPorId() {
        assertNotNull(compradorServico.consultarPorId(new Long(2)));
    }

    @Test
    public void atualizarInvalido() {
        Comprador comprador = compradorServico.consultarPorId(new Long(2));
        comprador.setSenha("123"); //Senha inválida
        try {
            compradorServico.atualizar(comprador);
            assertTrue(false);
        } catch (EJBException ex) {
            assertTrue(ex.getCause() instanceof ConstraintViolationException);
            ConstraintViolationException causa =
                    (ConstraintViolationException) ex.getCause();
            for (ConstraintViolation erroValidacao: causa.getConstraintViolations()) {
                assertThat(erroValidacao.getMessage(), CoreMatchers.anyOf(startsWith("Senha fraca"), startsWith("tamanho deve estar entre 6 e 20")));
            }
        }

    }

    @Test
    public void getCompradorPorCPF() {
        assertNotNull(compradorServico.consultarPorCPF("808.257.284-10"));
    }

    @Test
    public void getCompradoresPorCartao() {
        List<Comprador> compradores = compradorServico.consultarCompradores("VISA");
        assertEquals(compradores.size(), 2);
    }
}
