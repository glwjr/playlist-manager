package edu.sdccd.cisc191.backend.songs;

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
    List<SongInfo> getSongs() {
        return songService.getAllSongs();
    }

    @GetMapping("/{id}")
    ResponseEntity<SongInfo> getSongById(@PathVariable Long id) {
        var song = songService.getSongById(id);
        return ResponseEntity.ok(song);
    }

    record CreateSongPayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "Artist is required")
            String artist) {}

    @PostMapping
    ResponseEntity<Void> createSong(@Valid @RequestBody CreateSongPayload payload) {
        var savedSong = songService.createSong(payload.title(), payload.artist());
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(savedSong.getId());
        return ResponseEntity.created(url).build();
    }

    record UpdateSongPayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "Artist is required")
            String artist) {}

    @PutMapping("/{id}")
    ResponseEntity<Void> updateSong(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSongPayload payload) {
        songService.updateSong(id, payload.title(), payload.artist());
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SongNotFoundException.class)
    ResponseEntity<Void> handle(SongNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
