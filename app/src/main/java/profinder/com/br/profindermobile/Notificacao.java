package profinder.com.br.profindermobile;

public class Notificacao {
    private boolean isRead;
    private Usuario usuario;

    public Notificacao() {
    }

    public Notificacao(Usuario usuario) {
        this.usuario = usuario;
        this.isRead = false;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "isRead=" + isRead +
                ", usuario=" + usuario +
                '}';
    }
}
