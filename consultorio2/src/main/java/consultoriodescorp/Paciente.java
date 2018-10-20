package consultoriodescorp;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
//import javax.validation.constraints.NotBlank;

/**
 *
 * @author valeria
 */
@Entity
@Table(name = "TB_PACIENTE")
@NamedQueries({
    @NamedQuery(
    name = "Listar.Paciente",
    query = "SELECT p FROM Paciente p"),
    @NamedQuery(
    name="Listar.Paciente.Planos",
    query="SELECT p FROM Paciente p WHERE p.plano=?1"        
    )
    
})
@DiscriminatorValue(value = "P")
@PrimaryKeyJoinColumn(name = "ID_PACIENTE",referencedColumnName ="ID_USUARIO")
public class Paciente extends Usuario implements Serializable {
    
    @Enumerated(EnumType.STRING)
    @Column(name = "CL_PLANO")
    private TipoPlanoSaude plano;

    public TipoPlanoSaude getPlano() {
        return plano;
    }

    public void setPlano(TipoPlanoSaude plano) {
        this.plano = plano;
    }

}
