package profinder.com.br.profindermobile;

import java.io.Serializable;
import java.util.List;

class Projeto implements Serializable {
    private String id;
    private String UID;
    private String nome;
    private String area;
    private String coordenador;
    private String descricao;
    private int qntAlunos;
    private List<Usuario> alunos;

    public Projeto() {}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(String coordenador) {
        this.coordenador = coordenador;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQntAlunos() {
        return qntAlunos;
    }

    public void setQntAlunos(int qntAlunos) {
        this.qntAlunos = qntAlunos;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Usuario> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Usuario> alunos) {
        this.alunos = alunos;
    }

    @Override
    public String toString() {
        return "Projeto{" +
                "UID='" + UID + '\'' +
                ", nome='" + nome + '\'' +
                ", area='" + area + '\'' +
                ", coordenador='" + coordenador + '\'' +
                ", descricao='" + descricao + '\'' +
                ", qntAlunos=" + qntAlunos +
                '}';
    }
}
