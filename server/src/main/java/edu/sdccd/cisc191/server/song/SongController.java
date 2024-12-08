package edu.sdccd.cisc191.server.song;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "http://localhost:3000")
public class SongController {

    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    List<SongInfo> getAllSongs() {
        return songService.getAllSongs();
    }

    @GetMapping("/{id}")
    ResponseEntity<SongInfo> getSongById(@PathVariable Long id) {
        var song = songService.getSongById(id);
        return ResponseEntity.ok(song);
    }

    @PostMapping
    ResponseEntity<Void> createSong(@Valid @RequestBody CreateSongPayload payload) {
        var savedSong = songService.createSong(payload.name(), payload.artist(), payload.genre());
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(savedSong.getId());
        return ResponseEntity.created(url).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSongPayload payload) {
        songService.updateSong(id, payload.name(), payload.artist(), payload.genre());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SongNotFoundException.class)
    ResponseEntity<Void> handle(SongNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    record CreateSongPayload(
            @NotEmpty(message = "Name is required")
            String name,
            @NotEmpty(message = "Artist is required")
            String artist,
            @NotEmpty(message = "Genre is required")
            String genre) {
    }

    record UpdateSongPayload(
            @NotEmpty(message = "Name is required")
            String name,
            @NotEmpty(message = "Artist is required")
            String artist,
            @NotEmpty(message = "Genre is required")
            String genre) {
    }
}
