/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package softwarecorporativo.exemplo.ejb.servico;

import javax.ejb.ApplicationException;

/**
 *
 * @author marcos
 */
@ApplicationException(rollback = true)
public class ExcecaoSistema extends RuntimeException {
    public ExcecaoSistema(Exception causa) {
        super(causa.getMessage(), causa);
    }
}
