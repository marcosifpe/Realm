package consultoriodescorp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import javax.validation.constraints.NotBlank;

/**
 *
 * @author valeria
 */
@Entity
@Table(name = "TB_EXAME")
public class Exame implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_EXAME")
    private Long id;
   // @NotBlank
    @Column(name = "CL_NOME_EXAME")
    private String nome;
    
    @OneToMany(mappedBy = "exame",fetch=FetchType.LAZY)
    private List<SolicitarExame> solicitarExames;
   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<SolicitarExame> getSolicitarExames() {
        return solicitarExames;
    }

    public void adicionarSolicitarExames(SolicitarExame solicitarExame) {
        if(this.solicitarExames == null){
            this.solicitarExames = new ArrayList<>();
        }
        this.solicitarExames.add(solicitarExame);
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Exame other = (Exame) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
