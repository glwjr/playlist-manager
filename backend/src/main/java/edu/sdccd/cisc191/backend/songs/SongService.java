package edu.sdccd.cisc191.backend.songs;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class SongService {
    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<SongInfo> getAllSongs() {
        return songRepository.findAllByOrderByCreatedAtDesc();
    }

    public SongInfo getSongById(Long id) {
        return songRepository.findSongById(id)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));
    }

    public Song createSong(String title, String artist) {
        var song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setCreatedAt(Instant.now());
        return songRepository.save(song);
    }

    public void updateSong(Long id, String title, String artist) {
        var song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));
        song.setTitle(title);
        song.setArtist(artist);
        song.setUpdatedAt(Instant.now());
        songRepository.save(song);
    }
}
