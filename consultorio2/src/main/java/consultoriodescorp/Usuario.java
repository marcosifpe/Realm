
package consultoriodescorp;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;


/**
 *
 * @author valeria
 */
@Entity
@Table(name="TB_USUARIO")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="DISC_USUARIO",discriminatorType=DiscriminatorType.STRING,length=1)
@Access(AccessType.FIELD)
public abstract class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID_USUARIO")
    private Long id;
    @NotBlank
    @Size(max=40)
    @Pattern(regexp = "\\p{Upper}{1}\\p{Lower}+", message = "{consultoriodescorp.Usuario.nome}")
    @Column(name="CL_NOME")
    private String nome;
    @NotBlank
    @Column(name="CL_LOGIN")
    @Pattern(regexp = "([^\\s])\\W +", message = "{consultoriodescorp.Usuario.login}")
    private String login;
    @NotBlank
    @Column(name="CL_SENHA")
    @Pattern(regexp = "((?=.*\\p{Digit})(?=.*\\p{Lower})(?=.*\\p{Upper}))",message="{consultoriodescorp.Usuario.senha}")
    @Size(min=6,max=20)
    private String senha;
    @NotBlank
    @Email
    @Column(name="CL_EMAIL")
    private String email;
    @NotBlank
    @Column(name="CL_SEXO")
    private String sexo;

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash  += (id != null ? id.hashCode() : 0);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
   
    
}
