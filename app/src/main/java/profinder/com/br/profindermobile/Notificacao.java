package profinder.com.br.profindermobile;

public class Notificacao {
    private boolean isRead;
    private Projeto projeto;
    private Usuario usuario;

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
                '}';
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
