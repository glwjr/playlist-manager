package edu.sdccd.cisc191.server.playlist;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public List<PlaylistInfo> getAllPlaylists() {
        return playlistRepository.findAllByOrderByCreatedAtDesc();
    }

    public PlaylistInfo getPlaylistById(Long id) {
        return playlistRepository.findPlaylistById(id)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
    }

    public PlaylistInfo getPlaylistByName(String name) {
        return playlistRepository.findPlaylistByName(name)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
    }

    public Playlist createPlaylist(String name) {
        var playlist = new Playlist();
        playlist.setName(name);
        playlist.setCreatedAt(Instant.now());

        return playlistRepository.save(playlist);
    }

    public void updatePlaylist(Long id, String name) {
        var playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        playlist.setName(name);
        playlistRepository.save(playlist);
    }

    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }

    public void addSongToPlaylistArray(Long playlistId, int row, String name, String artist, String genre) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        playlist.addSongToArray(row, name, artist, genre);
    }

    public Playlist persistSongFromPlaylistArray(Long playlistId, int row) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        playlist.persistSongFromArray(row);

        return playlistRepository.save(playlist);
    }

    public Playlist persistAllSongsFromPlaylistArray(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));

        for (int i = 0; i < playlist.getSongsArray().length; i++) {
            playlist.persistSongFromArray(i);
        }

        return playlistRepository.save(playlist);
    }
}
