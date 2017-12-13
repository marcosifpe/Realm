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
import javax.validation.constraints.NotNull;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;
import softwarecorporativo.exemplo.ejb.entidade.Comprador;

/**
 *
 * @author marcos
 */
@Stateless(name = "ejb/CompradorServico")
@LocalBean
@ValidateOnExecution(type = ExecutableType.ALL)
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
    public boolean existe(@NotNull Comprador entidade) {
        TypedQuery<Comprador> query
                = entityManager.createNamedQuery(Comprador.COMPRADOR_POR_CPF, classe);
        query.setParameter(1, entidade.getCpf());
        return !query.getResultList().isEmpty();
    }

    @TransactionAttribute(SUPPORTS)
    public List<Comprador> consultarCompradores(@NotBlank String nomeCartao) {
        return super.consultarEntidades(new Object[] {nomeCartao}, Comprador.COMPRADOR_POR_CARTAO);
    }
    
    @TransactionAttribute(SUPPORTS)
    public Comprador consultarPorCPF(@CPF String cpf) {
        return super.consultarEntidade(new Object[] {cpf}, Comprador.COMPRADOR_POR_CPF);
    }    
}
