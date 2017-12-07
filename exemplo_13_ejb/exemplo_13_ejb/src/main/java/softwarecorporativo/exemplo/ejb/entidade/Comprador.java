package softwarecorporativo.exemplo.ejb.entidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;

/**
 *
 * @author marcos
 */
@Entity
@Table(name = "TB_COMPRADOR")
@Access(AccessType.FIELD)
@DiscriminatorValue(value = "C")
@PrimaryKeyJoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
@NamedQueries(
        {
            @NamedQuery(
                    name = Comprador.COMPRADOR_POR_CPF,
                    query = "SELECT c FROM Comprador c WHERE c.cpf = ?1"
            ),
            @NamedQuery(
                    name = Comprador.COMPRADOR_POR_CARTAO,
                    query = "SELECT c FROM Comprador c WHERE c.cartaoCredito IS NOT NULL AND c.cartaoCredito.bandeira LIKE ?1"
            )
        }
)
public class Comprador extends Usuario implements Serializable {

    public static final String COMPRADOR_POR_CPF = "CompradorPorCPF";
    public static final String COMPRADOR_POR_CARTAO = "CompradorPorCartao";
    @Valid
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "ID_CARTAO_CREDITO", referencedColumnName = "ID")
    private CartaoCredito cartaoCredito;
    @Valid
    @OneToMany(mappedBy = "comprador", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Oferta> ofertas;

    public Comprador() {
        super();
        this.ofertas = new ArrayList<>();
    }

    public CartaoCredito getCartaoCredito() {
        return cartaoCredito;
    }

    public void setCartaoCredito(CartaoCredito cartaoCredito) {
        this.cartaoCredito = cartaoCredito;
        this.cartaoCredito.setDono(this);
    }

    public List<Oferta> getOfertas() {
        return ofertas;
    }

    public boolean adicionar(Oferta oferta) {
        oferta.setComprador(this);
        return ofertas.add(oferta);
    }

    @Override
    public String toString() {
        return "exemplo.jpa.Comprador[ id=" + id + " ]";
    }

}
