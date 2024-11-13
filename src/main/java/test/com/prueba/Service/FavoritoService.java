package test.com.prueba.Service;

import org.springframework.stereotype.Service;
import test.com.prueba.Model.FavoritoRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoritoService {

    private final List<FavoritoRequest> favoritos = new ArrayList<>();

    public boolean agregarFavorito(FavoritoRequest favorito) {
        boolean existe = favoritos.stream()
                .anyMatch(f -> f.getCancion_id() == favorito.getCancion_id() && 
                               f.getNombre_banda().equalsIgnoreCase(favorito.getNombre_banda()));

        if (!existe) {
            favoritos.add(favorito);
            return true;
        } else {
            return false; 
        }
    }

    public List<FavoritoRequest> obtenerFavoritos() {
        return favoritos;
    }
}

