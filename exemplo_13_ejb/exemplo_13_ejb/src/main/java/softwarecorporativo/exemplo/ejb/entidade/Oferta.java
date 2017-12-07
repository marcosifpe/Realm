package softwarecorporativo.exemplo.ejb.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TB_OFERTA")
@Access(AccessType.FIELD)
public class Oferta extends Entidade implements Serializable, Comparable<Oferta> {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_ITEM", referencedColumnName = "ID")
    private Item item;
    @DecimalMin("0.1")
    @NotNull
    @Column(name = "NUM_VALOR")
    private BigDecimal valor;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_OFERTA")
    private Date data;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_COMPRADOR", referencedColumnName = "ID_USUARIO")
    private Comprador comprador;
    @Column(name = "FLAG_VENCEDORA")
    private boolean vencedora;    
    
    public Oferta() {
        this.vencedora = false;
    }

    @PrePersist
    public void set() {
        this.setData(new Date());
    }
    
    public Comprador getComprador() {
        return comprador;
    }

    public void setComprador(Comprador comprador) {
        this.comprador = comprador;
    }    

    public boolean isVencedora() {
        return vencedora;
    }

    public void setVencedora(boolean vencedora) {
        this.vencedora = vencedora;
    }
    
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    @Override
    public int compareTo(Oferta oferta) {
        return this.valor.compareTo(oferta.valor);
    }
    
}
