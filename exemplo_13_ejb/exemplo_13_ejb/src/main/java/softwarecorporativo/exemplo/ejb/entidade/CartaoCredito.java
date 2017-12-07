package softwarecorporativo.exemplo.ejb.entidade;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author marcos
 */
@Entity
@Table(name = "TB_CARTAO_CREDITO")
@Access(AccessType.FIELD)
public class CartaoCredito extends Entidade implements Serializable {
    @NotNull
    @OneToOne(mappedBy = "cartaoCredito", optional = false)
    private Comprador dono;
    @NotBlank
    @Size(max = 15)
    @Column(name = "TXT_BANDEIRA")
    private String bandeira;
    @NotNull
    @CreditCardNumber
    @Column(name = "TXT_NUMERO")
    private String numero;
    @NotNull
    @Future    
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EXPIRACAO", nullable = false)
    private Date dataExpiracao;

    public Comprador getDono() {
        return dono;
    }

    public void setDono(Comprador dono) {
        this.dono = dono;
    }

    public String getBandeira() {
        return bandeira;
    }

    public void setBandeira(String bandeira) {
        this.bandeira = bandeira;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }    
}
