package softwarecorporativo.exemplo.ejb.entidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author marcos
 */
@Entity
@Table(name = "TB_ITEM")
@Access(AccessType.FIELD)
@NamedQueries(
        {
            @NamedQuery(
                    name = Item.ITEM_POR_CATEGORIA,
                    query = "SELECT i FROM Item i, Categoria c WHERE c.nome like ?1 AND c MEMBER OF i.categorias"
            ),
            @NamedQuery(
                    name = Item.ITEM_POR_TITULO,
                    query = "SELECT i FROM Item i WHERE i.titulo like ?1"
            )
        }
)
public class Item extends Entidade implements Serializable {

    public static final String ITEM_POR_CATEGORIA = "ItemPorCategoria";
    public static final String ITEM_POR_TITULO = "ItemPorTitulo";

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Oferta> ofertas;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_ITENS_CATEGORIAS", joinColumns = {
        @JoinColumn(name = "ID_ITEM")},
            inverseJoinColumns = {
                @JoinColumn(name = "ID_CATEGORIA")})
    private List<Categoria> categorias;
    @NotBlank
    @Size(max = 150)
    @Column(name = "TXT_TITULO")
    private String titulo;
    @NotBlank
    @Size(max = 500)
    @Column(name = "TXT_DESCRICAO")
    private String descricao;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ID_VENDEDOR", referencedColumnName = "ID_USUARIO")
    private Vendedor vendedor;
    @Column(name = "NUM_VALOR_MINIMO")
    private BigDecimal valorMinimo;

    public Item() {
        this.ofertas = new ArrayList<>();
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public List<Oferta> getOfertas() {
        return ofertas;
    }

    public boolean possuiOfertas() {
        return !this.ofertas.isEmpty();
    }

    public boolean adicionar(Oferta oferta) {
        if (valorMinimo != null
                && oferta.getValor().compareTo(valorMinimo) == -1) {
            return false;
        }

        for (Oferta ofertaAnterior : ofertas) {
            //A oferta atual tem que superior a todas as outras
            if (ofertaAnterior.compareTo(oferta) != -1) {
                return false;
            }
        }

        oferta.setItem(this);
        return this.ofertas.add(oferta);
    }

    public boolean remover(Oferta oferta) {
        return ofertas.remove(oferta);
    }

    public void adicionar(Categoria categoria) {
        if (this.categorias == null) {
            this.categorias = new ArrayList<>();
        }

        categorias.add(categoria);
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorMinimo() {
        return valorMinimo;
    }

    public void setValorMinimo(BigDecimal valorMinimo) {
        this.valorMinimo = valorMinimo;
    }
}
