package edu.sdccd.cisc191.server.playlist;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

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

    record CreatePlaylistPayload(
            @NotEmpty(message = "Name is required")
            String name) {}

    @PostMapping
    ResponseEntity<Void> createPlaylist(@Valid @RequestBody CreatePlaylistPayload payload) {
        var savedPlaylist = playlistService.createPlaylist(payload.name());
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .build(savedPlaylist.getId());
        return ResponseEntity.created(url).build();
    }
}
