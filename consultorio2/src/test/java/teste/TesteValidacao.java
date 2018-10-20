package teste;

import consultoriodescorp.Paciente;
import consultoriodescorp.TipoPlanoSaude;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author valeria
 */
public class TesteValidacao extends TesteBase {

    @Test
    public void t01_CriarPacienteInvalido() {
        Paciente p = new Paciente();
        p.setNome("CAROLINA");/* nome invalido*/
        p.setEmail("carolina#teste");/* e-mail invalido*/
        p.setLogin(" Carolina Maria ");/* login com espaço*/
        p.setSexo("F");
        p.setPlano(TipoPlanoSaude.UNIMED);
        p.setSenha("CarolinaMaria123");

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        assertEquals(false, validator.validate(p).isEmpty());
        Set<ConstraintViolation<Paciente>> constraintViolations = validator.validate(p);
        for (ConstraintViolation violation : constraintViolations) {
            String mensagemErro = violation.getRootBeanClass() + "." + violation.getPropertyPath() + ": " + violation.getMessage();
            System.out.println("*****************   " + mensagemErro);
            assertThat(mensagemErro,
                    CoreMatchers.anyOf(
                            startsWith("class consultoriodescorp.Paciente.nome: Deve possuir uma única letra maiúscula, seguida por letras minúsculas."),
                            startsWith("class consultoriodescorp.Paciente.email: Não é um endereço de e-mail"),
                            startsWith("class consultoriodescorp.Paciente.senha: A senha deve possuir pelo menos um caracter : maiúsculo, minúsculo e número."),
                            startsWith("class consultoriodescorp.Paciente.login: Não deve conter espaços")
                    )
            );
        }

        assertEquals(4, constraintViolations.size());
    }
    
    //Teste apenas para verificar funcionamento do Bean Validation
    //Fazer como no exemplo12
    //Verificar os demais testes pois estão estourando erros de validação
    @Test(expected = ConstraintViolationException.class)
    public void criarPacienteInvalido() {
        Paciente p = new Paciente();
        p.setNome("CAROLINA");/* nome invalido*/
        p.setEmail("carolina#teste");/* e-mail invalido*/
        p.setLogin(" Carolina Maria ");/* login com espaço*/
        p.setSexo("F");
        p.setPlano(TipoPlanoSaude.UNIMED);
        p.setSenha("CarolinaMaria123");

        em.persist(p);
    }    

}
