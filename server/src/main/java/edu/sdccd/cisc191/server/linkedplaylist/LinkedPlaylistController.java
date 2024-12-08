package edu.sdccd.cisc191.server.linkedplaylist;

import edu.sdccd.cisc191.server.song.Song;
import edu.sdccd.cisc191.server.song.SongNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/linked-playlist")
@CrossOrigin(origins = "http://localhost:3000")
public class LinkedPlaylistController {
    private final LinkedPlaylistService linkedPlaylistService;

    public LinkedPlaylistController(LinkedPlaylistService linkedPlaylistService) {
        this.linkedPlaylistService = linkedPlaylistService;
    }

    @GetMapping
    List<Song> getAllSongs() {
        return linkedPlaylistService.getAllSongs();
    }

    @GetMapping("/current")
    ResponseEntity<Song> getCurrentSong() {
        var song = linkedPlaylistService.getCurrentSong();
        if (song == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(song);
    }

    @PostMapping("/add")
    ResponseEntity<Song> addSong(@Valid @RequestBody AddSongPayload payload) {
        linkedPlaylistService.addSong(payload.songId);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/current")
                .build()
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/remove")
    ResponseEntity<Void> removeCurrentSong() {
        linkedPlaylistService.removeCurrentSong();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/next")
    ResponseEntity<Song> playNextSong() {
        var nextSong = linkedPlaylistService.playNextSong();
        return ResponseEntity.ok(nextSong);
    }

    @PutMapping("/previous")
    ResponseEntity<Song> playPreviousSong() {
        var previousSong = linkedPlaylistService.playPreviousSong();
        return ResponseEntity.ok(previousSong);
    }

    @GetMapping("/search")
    ResponseEntity<Song> findSongByName(@RequestParam String name) {
        var song = linkedPlaylistService.findSongByName(name);
        if (song != null) {
            return ResponseEntity.ok(song);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/sort-by-name")
    ResponseEntity<Void> sortByName() {
        linkedPlaylistService.sortByName();
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(SongNotFoundException.class)
    ResponseEntity<Void> handle(SongNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    record AddSongPayload(
            @NotNull(message = "Song ID is required")
            Long songId) {
    }

}
