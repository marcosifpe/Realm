/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb;

import javax.naming.NamingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import softwarecorporativo.exemplo.ejb.servico.ItemServico;

/**
 *
 * @author marcos
 */
public class ItemTeste extends Teste {

    private ItemServico itemServico;

    @Before
    public void setUp() throws NamingException {
        itemServico = (ItemServico) container.getContext().lookup("java:global/classes/ejb/ItemServico!softwarecorporativo.exemplo.ejb.servico.ItemServico");
    }

    @After
    public void tearDown() {
        itemServico = null;
    }

    @Test
    public void consultarItensPorCategoria() {
        assertEquals(3, itemServico.getItensPorCategoria("Pedais").size());
    }

}
