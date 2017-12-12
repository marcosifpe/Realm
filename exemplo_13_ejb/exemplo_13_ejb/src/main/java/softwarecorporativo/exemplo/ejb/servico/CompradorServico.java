/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb.servico;

import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import static javax.ejb.TransactionAttributeType.SUPPORTS;
import javax.persistence.TypedQuery;
import softwarecorporativo.exemplo.ejb.entidade.Comprador;

/**
 *
 * @author marcos
 */
@Stateless(name = "ejb/CompradorServico")
@LocalBean
public class CompradorServico extends Servico<Comprador> {

    @PostConstruct
    public void init() {
        super.setClasse(Comprador.class);
    }

    @Override
    public Comprador criar() {
        return new Comprador();
    }

    @Override
    public boolean existe(Comprador entidade) {
        TypedQuery<Comprador> query
                = entityManager.createNamedQuery(Comprador.COMPRADOR_POR_CPF, classe);
        query.setParameter(1, entidade.getCpf());
        return !query.getResultList().isEmpty();
    }

    @TransactionAttribute(SUPPORTS)
    public List<Comprador> consultarCompradores(String nomeCartao) {
        return super.consultarEntidades(new Object[] {nomeCartao}, Comprador.COMPRADOR_POR_CARTAO);
    }
    
    @TransactionAttribute(SUPPORTS)
    public Comprador consultarPorCPF(String cpf) {
        return super.consultarEntidade(new Object[] {cpf}, Comprador.COMPRADOR_POR_CPF);
    }    
}
