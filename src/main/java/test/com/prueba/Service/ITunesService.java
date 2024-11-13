package test.com.prueba.Service ;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ITunesService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ITunesService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = "searchCache", key = "#name")
    public Map<String, Object> searchTracks(String name) {
        String url = "https://itunes.apple.com/search";
        
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("term", name)
                .queryParam("entity", "musicTrack")
                .queryParam("limit", 25);

        String response = restTemplate.getForObject(uriBuilder.toUriString(), String.class);
        
        try {
            Map<String, Object> resultMap = objectMapper.readValue(response, Map.class);
            return processTracks(resultMap, name);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    private Map<String, Object> processTracks(Map<String, Object> response, String name) {
        if (response == null || !response.containsKey("results")) {
            return Collections.emptyMap();
        }

        List<Map<String, Object>> results = (List<Map<String, Object>>) response.get("results");

        results = results.stream()
                .filter(track -> name.equalsIgnoreCase((String) track.get("artistName")))
                .collect(Collectors.toList());

        Set<String> uniqueAlbums = new HashSet<>();
        List<Map<String, Object>> songs = new ArrayList<>();

        for (Map<String, Object> track : results) {
            uniqueAlbums.add((String) track.get("collectionName"));

            Map<String, Object> songData = new HashMap<>();
            songData.put("cancion_id", track.get("trackId"));
            songData.put("nombre_album", track.get("collectionName"));
            songData.put("nombre_tema", track.get("trackName"));
            songData.put("preview_url", track.get("previewUrl"));
            songData.put("fecha_lanzamiento", track.get("releaseDate"));

            Map<String, Object> priceData = new HashMap<>();
            priceData.put("valor", track.get("trackPrice"));
            priceData.put("moneda", track.get("currency"));
            songData.put("precio", priceData);

            songs.add(songData);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total_albums", uniqueAlbums.size());
        result.put("total_canciones", songs.size());
        result.put("albumes", uniqueAlbums);
        result.put("canciones", songs);

        return result;
    }
}

