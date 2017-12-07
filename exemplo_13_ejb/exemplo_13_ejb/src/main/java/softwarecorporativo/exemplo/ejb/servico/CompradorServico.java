/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb.servico;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
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
        super.setQueryExistencia(Comprador.COMPRADOR_POR_CPF);
        super.setQueryEntidade(Comprador.COMPRADOR_POR_CPF);
        super.setQueryEntidades(Comprador.COMPRADOR_POR_CARTAO);
        super.setAtributoExistencia("cpf");
    }

    @Override
    public Comprador criar() {
        return new Comprador();
    }
}
