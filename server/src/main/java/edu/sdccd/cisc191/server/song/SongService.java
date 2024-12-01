package edu.sdccd.cisc191.server.song;

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

    public Song createSong(String name, String artist, String genre) {
        var song = new Song();
        song.setName(name);
        song.setArtist(artist);
        song.setGenre(genre);
        song.setCreatedAt(Instant.now());

        return songRepository.save(song);
    }

    public void updateSong(Long id, String name, String artist, String genre) {
        var song = songRepository.findById(id)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));
        song.setName(name);
        song.setArtist(artist);
        song.setGenre(genre);
        song.setUpdatedAt(Instant.now());

        songRepository.save(song);
    }

}
