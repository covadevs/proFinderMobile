package profinder.com.br.profindermobile;

public class Notificacao {
    private boolean isRead;
    private Projeto projeto;
    private Usuario usuario;
    private String id;

    public Notificacao() {
        this.isRead = false;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "isRead=" + isRead +
                ", projeto=" + projeto +
                ", usuario=" + usuario +
                ", id='" + id + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

}
