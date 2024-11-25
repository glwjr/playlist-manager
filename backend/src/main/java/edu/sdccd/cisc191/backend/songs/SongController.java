package edu.sdccd.cisc191.backend.songs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/songs")
public class SongController {
    private final SongRepository songRepository;

    SongController(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @GetMapping
    List<SongInfo> getSongs() {
        return songRepository.findAllByOrderByCreatedAtDesc();
    }

    @GetMapping("/{id}")
    ResponseEntity<SongInfo> getSongById(@PathVariable Long id) {
        var song = songRepository.findSongById(id).orElseThrow(() -> new SongNotFoundException("Song not found"));
        return ResponseEntity.ok(song);
    }

    record CreateSongPayload(
            @NotEmpty(message = "Title is required")
            String title,
            @NotEmpty(message = "Artist is required")
            String artist) {}

    @PostMapping
    ResponseEntity<Void> createSong(
            @Valid @RequestBody CreateSongPayload payload) {
        var song = new Song();
        song.setTitle(payload.title);
        song.setArtist(payload.artist);
        song.setCreatedAt(Instant.now());
        var savedSong = songRepository.save(song);
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
        var song = songRepository.findById(id).orElseThrow(() -> new SongNotFoundException("Song not found"));
        song.setTitle(payload.title);
        song.setArtist(payload.artist);
        song.setUpdatedAt(Instant.now());
        songRepository.save(song);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SongNotFoundException.class)
    ResponseEntity<Void> handle(SongNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
