package test.com.prueba.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import test.com.prueba.Model.FavoritoRequest;
import test.com.prueba.Service.FavoritoService;
import test.com.prueba.Service.ITunesService;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ITunesController {

    private final ITunesService iTunesService;
    private final FavoritoService favoritoService;


    @Autowired
    public ITunesController(ITunesService iTunesService, FavoritoService favoritoService) {
        this.iTunesService = iTunesService;
        this.favoritoService = favoritoService;
    }

    @GetMapping("/search_tracks")
    public ResponseEntity<Map<String, Object>> searchTracks(@RequestParam String name) {
        Map<String, Object> response = iTunesService.searchTracks(name);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/favoritos")
    public ResponseEntity<String> agregarFavorito(@RequestBody FavoritoRequest favoritoRequest) {
        boolean agregado = favoritoService.agregarFavorito(favoritoRequest);

        if (agregado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Favorito agregado exitosamente.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El favorito ya existe.");
        }
    }

    @GetMapping("/favoritos")
    public ResponseEntity<List<FavoritoRequest>> obtenerFavoritos() {
        return ResponseEntity.ok(favoritoService.obtenerFavoritos());
    }
}

