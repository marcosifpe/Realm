/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.validation.ConstraintViolationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import softwarecorporativo.exemplo.ejb.entidade.Comprador;
import softwarecorporativo.exemplo.ejb.entidade.Item;
import softwarecorporativo.exemplo.ejb.entidade.Oferta;
import softwarecorporativo.exemplo.ejb.servico.CompradorServico;
import softwarecorporativo.exemplo.ejb.servico.ItemServico;

/**
 *
 * @author marcos
 */
public class ItemTeste extends Teste {

    private ItemServico itemServico;
    private CompradorServico compradorServico;

    @Before
    public void setUp() throws NamingException {
        itemServico = (ItemServico) container.getContext().lookup("java:global/classes/ejb/ItemServico!softwarecorporativo.exemplo.ejb.servico.ItemServico");
        compradorServico = (CompradorServico) container.getContext().lookup("java:global/classes/ejb/CompradorServico!softwarecorporativo.exemplo.ejb.servico.CompradorServico");
    }

    @After
    public void tearDown() {
        itemServico = null;
    }

    @Test
    public void consultarItensPorCategoria() {
        assertEquals(3, itemServico.getItensPorCategoria("Pedais").size());
    }

    @Test
    public void consultarItensPorTitulo() {
        assertEquals(1, itemServico.getItensPorTitulo("boss DM-2").size());
    }

    @Test
    public void adicionarOferta() {
        Item item = itemServico.consultarPorId(new Long(6));
        assertNotNull(item);
        Comprador comprador = compradorServico.consultarPorId(new Long(2));
        Oferta oferta = item.criarOferta();
        oferta.setComprador(comprador);
        oferta.setValor(BigDecimal.valueOf(500.00));
        assertFalse(item.adicionar(oferta)); //Oferta menor que valor mínimo
        oferta.setValor(BigDecimal.valueOf(750.00));
        assertTrue(item.adicionar(oferta)); //Oferta igual ao valor mínimo
        comprador = compradorServico.consultarPorId(new Long(1));
        oferta = item.criarOferta();
        oferta.setComprador(comprador);
        oferta.setValor(BigDecimal.valueOf(745.00));
        assertFalse(item.adicionar(oferta)); //Oferta menor que oferta maior oferta
        oferta.setValor(BigDecimal.valueOf(770.00));
        assertTrue(item.adicionar(oferta)); //Oferta maior que oferta atual)
    }

}
