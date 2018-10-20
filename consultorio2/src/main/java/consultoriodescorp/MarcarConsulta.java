package consultoriodescorp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
//import javax.validation.constraints.NotBlank;

/**
 *
 * @author valeria
 */
@Entity
@Table(name = "TB_MARCAR_CONSULTA")
public class MarcarConsulta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID_CONSULTA")
    private Long id;
    //@NotBlank
    @Column(name = "CL_DATA_CONS")
    @Temporal(TemporalType.DATE)
    private Date dataConsuta;
    //s@NotBlank
    @Column(name = "CL_HORA")
    private String hora;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "TB_CONSULTA_MEDICOS", joinColumns = {
        @JoinColumn(name = "ID_CONSULTA")},
            inverseJoinColumns = {
                @JoinColumn(name = "ID_FUNCIONARIO")})
    private List<Funcionario> medicos;
 
    @JoinColumn(name="ID_PACIENTE")
    @OneToOne(fetch = FetchType.LAZY)
    //@NotBlank
    private Paciente paciente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataConsuta() {
        return dataConsuta;
    }

    public void setDataConsuta(Date dataConsuta) {
        this.dataConsuta = dataConsuta;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public List<Funcionario> getMedicos() {
        return medicos;
        
    }
    public void adicionarMedico(Funcionario medico) {
        
        if (this.medicos == null) {
            this.medicos = new ArrayList<>();
        }
        this.medicos.add(medico);
        
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
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
        final MarcarConsulta other = (MarcarConsulta) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}