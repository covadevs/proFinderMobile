package profinder.com.br.profindermobile;

class Projeto {
    private String nome;

    public Projeto() {}

    public Projeto(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Projeto{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
