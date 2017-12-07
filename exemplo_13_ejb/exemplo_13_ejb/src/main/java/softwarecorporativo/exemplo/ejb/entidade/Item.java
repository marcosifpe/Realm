package softwarecorporativo.exemplo.ejb.entidade;

import java.io.Serializable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TB_ITEM")
@Access(AccessType.FIELD)
public class Item extends Entidade implements Serializable {
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
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ID_VENDEDOR", referencedColumnName = "ID_USUARIO")
    private Vendedor vendedor;

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public List<Oferta> getOfertas() {
        return ofertas;
    }

    public boolean temOfertas() {
        return !this.ofertas.isEmpty();
    }

    public void adicionar(Oferta oferta) {
        if (this.ofertas == null) {
            this.ofertas = new ArrayList<>();
        }

        for (Oferta ofertaAnterior : ofertas) {
            //A oferta atual tem que superior a todas as outras
            if (ofertaAnterior.compareTo(oferta) != -1) {
                return;
            }
        }
        
        this.ofertas.add(oferta);
        oferta.setItem(this);
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
}
