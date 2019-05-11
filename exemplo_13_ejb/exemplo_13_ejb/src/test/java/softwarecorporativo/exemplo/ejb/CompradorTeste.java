/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJBException;
import javax.naming.NamingException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.startsWith;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import softwarecorporativo.exemplo.ejb.entidade.CartaoCredito;
import softwarecorporativo.exemplo.ejb.entidade.Comprador;
import softwarecorporativo.exemplo.ejb.entidade.Endereco;
import softwarecorporativo.exemplo.ejb.servico.CompradorServico;

/**
 *
 * @author marcos
 */
public class CompradorTeste extends Teste {

    private CompradorServico compradorServico;
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
    public void getCompradorPorCPF() {
        Comprador comprador = compradorServico.consultarPorCPF("808.257.284-10");
        assertNotNull(comprador);
        assertEquals("Fulano", comprador.getPrimeiroNome());
    }

    @Test(expected = EJBException.class)
    public void consultarCompradorCPFInvalido() {
        try {
            compradorServico.consultarPorCPF("222.111.444-98");
        } catch (EJBException ex) {
            assertTrue(ex.getCause() instanceof ConstraintViolationException);
            throw ex;
        }
    }

    @Test
    public void getCompradoresPorCartao() {
        List<Comprador> compradores = compradorServico.consultarCompradores("VISA");
        assertEquals(compradores.size(), 2);
    }

    @Test
    public void getCompradorPorId() {
        assertNotNull(compradorServico.consultarPorId(new Long(2)));
    }

    @Test
    public void persistir() {
        Comprador comprador = compradorServico.criar();
        comprador.addTelefone("(81)995907123");
        comprador.addTelefone("(81)30463812");
        comprador.setCpf("212.762.055-03");
        comprador.setDataNascimento(getData(20, Calendar.OCTOBER, 1986));
        comprador.setEmail("jose@gmail.com");
        comprador.setLogin("joses");
        comprador.setPrimeiroNome("Jose");
        comprador.setUltimoNome("Silva");
        comprador.setSenha("!teStE123@");

        Endereco endereco = comprador.criarEndereco();
        endereco.setBairro("Engenho do Meio");
        endereco.setCep("50.640-120");
        endereco.setCidade("Recife");
        endereco.setComplemento("Apto 301");
        endereco.setEstado("PE");
        endereco.setLogradouro("Rua da Igreja");
        endereco.setNumero(120);

        CartaoCredito cartaoCredito = comprador.criarCartaoCredito();
        cartaoCredito.setBandeira("MASTERCARD");
        cartaoCredito.setNumero("5462014816361274");
        cartaoCredito.setDataExpiracao(getData(15, Calendar.FEBRUARY, 2022));

        compradorServico.persistir(comprador);
        assertNotNull(comprador.getId());
    }

    private Date getData(int dia, int mes, int ano) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, dia);
        calendar.set(Calendar.MONTH, mes);
        calendar.set(Calendar.YEAR, ano);
        return calendar.getTime();
    }

    @Test
    public void atualizar() {
        Comprador comprador = compradorServico.consultarPorId(new Long(2));
        comprador.setSenha("!Nov@400"); //Senha válida
        compradorServico.atualizar(comprador);
        comprador = compradorServico.consultarPorId(new Long(2));
        assertEquals("!Nov@400", comprador.getSenha());
    }

    @Test(expected = EJBException.class)
    public void atualizarInvalido() {
        Comprador comprador = compradorServico.consultarPorId(new Long(2));
        comprador.setSenha("123"); //Senha inválida
        try {
            compradorServico.atualizar(comprador);
        } catch (EJBException ex) {
            assertTrue(ex.getCause() instanceof ConstraintViolationException);
            ConstraintViolationException causa
                    = (ConstraintViolationException) ex.getCause();
            for (ConstraintViolation erroValidacao : causa.getConstraintViolations()) {
                assertThat(erroValidacao.getMessage(),
                        CoreMatchers.anyOf(startsWith("Senha fraca"),
                                startsWith("tamanho deve estar entre 6 e 20")));
            }
            
            throw ex;
        }
    }
}
