package profinder.com.br.profindermobile;

class Projeto {
    private String UID;
    private String nome;
    private String area;
    private String coordenador;
    private String descricao;
    private int qntAlunos;

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
