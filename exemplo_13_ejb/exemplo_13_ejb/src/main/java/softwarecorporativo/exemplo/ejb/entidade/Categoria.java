package softwarecorporativo.exemplo.ejb.entidade;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(name = "TB_CATEGORIA")
@Access(AccessType.FIELD)
@NamedQueries(
        {
            @NamedQuery(
                    name = "Categoria.PorNome",
                    query = "SELECT c FROM Categoria c WHERE c.nome LIKE :nome ORDER BY c.id"
            )
        }
)
@NamedNativeQueries(
        {
            @NamedNativeQuery(
                    name = "Categoria.PorNomeSQL",
                    query = "SELECT id, txt_nome, id_categoria_mae FROM tb_categoria WHERE txt_nome LIKE ? ORDER BY id",
                    resultClass = Categoria.class
            ),
            @NamedNativeQuery(
                    name = "Categoria.QuantidadeItensSQL",
                    query = "SELECT c.ID, c.TXT_NOME, c.ID_CATEGORIA_MAE, count(ic.ID_ITEM) as total_itens from tb_categoria c, tb_itens_categorias ic where c.TXT_NOME LIKE ? and c.ID = ic.ID_CATEGORIA GROUP BY c.id",
                    resultSetMapping = "Categoria.QuantidadeItens"
            )
        }
)
@SqlResultSetMapping(
        name = "Categoria.QuantidadeItens",
        entities = {
            @EntityResult(entityClass = Categoria.class)},
        columns = {
            @ColumnResult(name = "total_itens", type = Long.class)}
)
public class Categoria extends Entidade implements Serializable {
    @NotBlank
    @Size(max = 100)
    @Column(name = "TXT_NOME")
    private String nome;
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "ID_CATEGORIA_MAE", referencedColumnName = "ID")
    private Categoria mae;
    @OneToMany(mappedBy = "mae", orphanRemoval = true)
    private List<Categoria> filhas;

    public Categoria getMae() {
        return mae;
    }

    public void setMae(Categoria mae) {
        this.mae = mae;
    }

    public List<Categoria> getFilhas() {
        return filhas;
    }

    public boolean adicionar(Categoria categoria) {
        categoria.setMae(this);
        return filhas.add(categoria);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
