package edu.sdccd.cisc191.server.song;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SongServiceTest {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongService songService;

    private Song song;

    @BeforeEach
    public void setUp() {
        song = new Song();
        song.setName("Song 1");
        song.setArtist("Artist 1");
        song.setGenre("Genre 1");
        song.setCreatedAt(Instant.now());

        songRepository.save(song);
    }

    @AfterEach
    public void tearDown() {
        songRepository.delete(song);
    }

    @Test
    public void testGetAll() {
        List<SongInfo> songs = songService.getAllSongs();

        assert songs != null;
        // Database is already seeded with 10 songs
        assertEquals(11, songs.size());
    }

    @Test
    public void testGetSongById() {
        SongInfo foundSong = songService.getSongById(song.getId());

        assertEquals(song.getName(), foundSong.getName());
        assertEquals(song.getArtist(), foundSong.getArtist());
        assertEquals(song.getGenre(), foundSong.getGenre());
        assertEquals(song.getCreatedAt(), foundSong.getCreatedAt());
    }

    @Test
    public void testCreate() {
        Song newSong = songService.createSong("Song Name", "Artist Name", "Genre Name");

        Optional<Song> createdSong = songRepository.findById(newSong.getId());

        assert createdSong.isPresent();
        assertEquals(newSong.getName(), createdSong.get().getName());
    }

    @Test
    public void testUpdate() {
        songService.updateSong(song.getId(), "New Title", "New Artist", "New Genre");

        Optional<Song> updatedSong = songRepository.findById(song.getId());

        assert updatedSong.isPresent();
        assertEquals("New Title", updatedSong.get().getName());
    }

    @Test
    public void testDelete() {
        songService.deleteSong(song.getId());

        Optional<Song> deletedSong = songRepository.findById(song.getId());
        assert deletedSong.isEmpty();
    }

}
