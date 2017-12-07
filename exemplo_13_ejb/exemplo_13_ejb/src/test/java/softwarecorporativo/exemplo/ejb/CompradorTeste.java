/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb;

import java.util.List;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
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
    private CompradorServico compradorServico;

    @BeforeClass
    public static void setUpClass() {
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
        assertNotNull(compradorServico.get(new Long(2)));
    }

    @Test
    public void getCompradorPorCPF() {
        assertNotNull(compradorServico.getEntidade(new String[]{"808.257.284-10"}));
    }
    
    @Test
    public void getCompradoresPorCartao() {
        List<Comprador> compradores = compradorServico.getEntidades(new Object[] {"VISA"});
        assertEquals(compradores.size(), 2);
    }
}
