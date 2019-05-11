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
import javax.ejb.TransactionAttributeType;
import javax.validation.executable.ExecutableType;
import javax.validation.executable.ValidateOnExecution;
import softwarecorporativo.exemplo.ejb.entidade.Item;

/**
 *
 * @author marcos
 */
@Stateless(name = "ejb/ItemServico")
@LocalBean
@ValidateOnExecution(type = ExecutableType.ALL)
public class ItemServico extends Servico<Item> {
    @PostConstruct
    public void init() {
        super.setClasse(Item.class);
    }
 
    @Override
    public Item criar() {
        return new Item();
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Item> getItensPorCategoria(String nomeCategoria) {
        return super.consultarEntidades(new Object[] {nomeCategoria}, Item.ITEM_POR_CATEGORIA);
    }
    
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Item> getItensPorTitulo(String titulo) {
        return super.consultarEntidades(new Object[] {titulo}, Item.ITEM_POR_TITULO);
    }    
}
