package edu.sdccd.cisc191.server.playlist;

import edu.sdccd.cisc191.server.song.Song;
import edu.sdccd.cisc191.server.song.SongNotFoundException;
import edu.sdccd.cisc191.server.song.SongRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    private final SongRepository songRepository;

    public PlaylistService(PlaylistRepository playlistRepository, SongRepository songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
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

    public Set<Song> getSongs(Long playlistId) {
        PlaylistInfo playlist = playlistRepository.findPlaylistById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        return playlist.getSongs();
    }

    public Playlist addSongToPlaylist(Long playlistId, Long songId) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        var song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        playlist.addSong(song);

        return playlistRepository.save(playlist);
    }

    public void deleteSongFromPlaylist(Long playlistId, Long songId) {
        var playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
        var song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        playlist.deleteSong(song.getId());

        playlistRepository.save(playlist);
    }

    public List<Song> filterAndSortSongsByGenre(Long playlistId, String genre) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));

        return playlist.getSongs().stream()
                .filter(song -> song.getGenre().equalsIgnoreCase(genre))
                .sorted((song1, song2) -> song1.getName().compareToIgnoreCase(song2.getName()))
                .collect(Collectors.toList());
    }
}
