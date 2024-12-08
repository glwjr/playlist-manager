package edu.sdccd.cisc191.server.playlist;

import edu.sdccd.cisc191.server.song.Song;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/playlists")
@CrossOrigin(origins = "http://localhost:3000")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping
    List<PlaylistInfo> getPlaylists() {
        return playlistService.getAllPlaylists();
    }

    @GetMapping("/{id}")
    ResponseEntity<PlaylistInfo> getPlaylistById(@PathVariable Long id) {
        var playlist = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlist);
    }

    @PostMapping
    ResponseEntity<Void> createPlaylist(@Valid @RequestBody CreatePlaylistPayload payload) {
        var savedPlaylist = playlistService.createPlaylist(payload.name());
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(savedPlaylist.getId());
        return ResponseEntity.created(url).build();
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updatePlaylist(@PathVariable Long id, @Valid @RequestBody UpdatePlaylistPayload payload) {
        playlistService.updatePlaylist(id, payload.name());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/songs")
    ResponseEntity<List<Song>> getSongsByPlaylistId(@PathVariable Long id) {
        var playlist = playlistService.getPlaylistById(id);
        Set<Song> songs = playlist.getSongs();
        return ResponseEntity.ok(songs.stream().toList());
    }

    @PostMapping("/{id}/songs")
    ResponseEntity<Void> addSongToPlaylist(@PathVariable Long id, @Valid @RequestBody AddSongToPlaylistPayload payload) {
        var updatedPlaylist = playlistService.addSongToPlaylist(id, payload.songId);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(updatedPlaylist.getId());
        return ResponseEntity.created(url).build();
    }

    @DeleteMapping("/{id}/songs/{songId}")
    ResponseEntity<Void> deleteSongFromPlaylist(@PathVariable Long id, @PathVariable Long songId) {
        playlistService.deleteSongFromPlaylist(id, songId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/songs/{genre}")
    ResponseEntity<List<Song>> filterAndSortSongsByGenre(@PathVariable Long id, @PathVariable String genre) {
        List<Song> filteredSongs = playlistService.filterAndSortSongsByGenre(id, genre);
        return ResponseEntity.ok(filteredSongs);
    }

    @ExceptionHandler(PlaylistNotFoundException.class)
    ResponseEntity<Void> handle(PlaylistNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    record CreatePlaylistPayload(
            @NotEmpty(message = "Name is required")
            String name) {
    }

    record UpdatePlaylistPayload(
            @NotEmpty(message = "Name is required")
            String name) {
    }

    record AddSongToPlaylistPayload(
            @NotNull(message = "Song ID is required")
            Long songId) {
    }
}
