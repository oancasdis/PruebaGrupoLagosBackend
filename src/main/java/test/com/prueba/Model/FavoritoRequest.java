package test.com.prueba.Model;

public class FavoritoRequest {
    private String nombre_banda;
    private int cancion_id;
    private String usuario;
    private String ranking;

    public String getNombre_banda() {
        return nombre_banda;
    }

    public void setNombre_banda(String nombre_banda) {
        this.nombre_banda = nombre_banda;
    }

    public int getCancion_id() {
        return cancion_id;
    }

    public void setCancion_id(int cancion_id) {
        this.cancion_id = cancion_id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }
}
