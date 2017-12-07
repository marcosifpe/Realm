package softwarecorporativo.exemplo.ejb.entidade;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "TB_USUARIO")
@Access(AccessType.FIELD)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DISC_USUARIO",
        discriminatorType = DiscriminatorType.STRING, length = 1)
public abstract class Usuario extends Entidade implements Serializable {
    @Valid
    @Embedded
    protected Endereco endereco;
    @Size(max = 3)
    @ElementCollection
    @CollectionTable(name = "TB_TELEFONE",
            joinColumns = @JoinColumn(name = "ID_USUARIO"))
    @Column(name = "TXT_NUM_TELEFONE")
    protected Collection<String> telefones;
    @NotNull
    @CPF
    @Column(name = "TXT_CPF")
    protected String cpf;
    @NotBlank
    @Size(max = 20)
    @Column(name = "TXT_LOGIN")
    protected String login;
    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "\\p{Upper}{1}\\p{Lower}+", message = "{exemplo.jpa.Usuario.nome}")
    @Column(name = "TXT_PRIMEIRO_NOME")
    protected String primeiroNome;
    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "\\p{Upper}{1}\\p{Lower}+", message = "{exemplo.jpa.Usuario.nome}") 
    @Column(name = "TXT_ULTIMO_NOME")
    protected String ultimoNome;
    @NotNull
    @Email
    @Column(name = "TXT_EMAIL", length = 30, nullable = false)
    protected String email;
    @NotBlank
    @Size(min = 6, max = 20)
    @Pattern(regexp = "((?=.*\\p{Digit})(?=.*\\p{Lower})(?=.*\\p{Upper})(?=.*\\p{Punct}).{6,20})", 
            message = "{exemplo.jpa.Usuario.senha}")
    @Column(name = "TXT_SENHA")
    protected String senha;
    @Past
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_NASCIMENTO")
    protected Date dataNascimento;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DT_CRIACAO")
    protected Date dataCriacao;    
    
    @PrePersist
    public void setDataCriacao() {
        this.setDataCriacao(new Date());
    }
    
    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    
    public Endereco criarEndereco() {
        this.setEndereco(new Endereco());
        return getEndereco();
    }

    public Collection<String> getTelefones() {
        return telefones;
    }

    public void addTelefone(String telefone) {
        if (telefones == null) {
            telefones = new HashSet<>();
        }
        telefones.add(telefone);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getPrimeiroNome() {
        return primeiroNome;
    }

    public void setPrimeiroNome(String primeiroNome) {
        this.primeiroNome = primeiroNome;
    }

    public String getUltimoNome() {
        return ultimoNome;
    }

    public void setUltimoNome(String ultimoNome) {
        this.ultimoNome = ultimoNome;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append(", ");
        sb.append(this.primeiroNome);
        sb.append(", ");
        sb.append(this.login);
        sb.append(", ");
        sb.append(this.cpf);

        for (String telefone : this.telefones) {
            sb.append(", ");
            sb.append(telefone);
        }

        return sb.toString();
    }
}
