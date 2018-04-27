package profinder.com.br.profindermobile;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String matricula;
    private String type;
    private Map<String, Projeto> projetos;

    public Usuario() {}

    public Usuario(String nome, String email, String senha, String matricula, String type) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.matricula = matricula;
        this.type = type;
        this.projetos = new HashMap<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(Map<String, Projeto> projetos) {
        this.projetos = projetos;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", matricula='" + matricula + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
