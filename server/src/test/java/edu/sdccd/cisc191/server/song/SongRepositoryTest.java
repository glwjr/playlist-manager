package edu.sdccd.cisc191.server.song;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SongRepositoryTest {

    @Autowired
    private SongRepository songRepository;

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
    public void testSave() {
        Song newSong = new Song();
        newSong.setName("Saved Song");
        newSong.setArtist("Saved Song Artist");
        newSong.setGenre("Saved Song Genre");
        newSong.setCreatedAt(Instant.now());

        songRepository.save(newSong);

        Long songId = newSong.getId();

        Song foundSong = songRepository.findById(songId).orElseThrow();

        assertEquals(newSong.getId(), foundSong.getId());
        assertEquals(newSong.getName(), foundSong.getName());
        assertEquals(newSong.getArtist(), foundSong.getArtist());
        assertEquals(newSong.getGenre(), foundSong.getGenre());
    }

    @Test
    public void testUpdate() {
        song.setName("Updated Song Name");
        songRepository.save(song);

        SongInfo updatedSong = songRepository.findSongById(song.getId()).orElse(null);

        assert updatedSong != null;
        assertEquals("Updated Song Name", updatedSong.getName());
    }

    @Test
    public void testFindSongByName() {
        SongInfo song = songRepository.findSongByName("Song 1")
                .orElseThrow(() -> new SongNotFoundException("Song 1 not found"));

        assert (song.getArtist().equals("Artist 1"));
    }

    @Test
    public void testFindAll() {
        Song newSong1 = new Song();
        newSong1.setName("New Song 1");
        newSong1.setArtist("New Song 1 Artist");
        newSong1.setGenre("New Song 1 Genre");
        newSong1.setCreatedAt(Instant.now());

        Song newSong2 = new Song();
        newSong2.setName("New Song 2");
        newSong2.setArtist("New Song 2 Artist");
        newSong2.setGenre("New Song 2 Genre");
        newSong2.setCreatedAt(Instant.now());

        songRepository.saveAll(List.of(newSong1, newSong2));

        List<Song> foundSongs = songRepository.findAll();

        // Data seed and initial mocked song adds 11 entries to these initial 2
        assertEquals(13, foundSongs.size());
    }

    @Test
    public void testFindAllByName() {
        Song song1 = new Song();
        song1.setName("Song Name");
        song1.setArtist("Artist 1");
        song1.setGenre("Genre 1");
        song1.setCreatedAt(Instant.now());

        Song song2 = new Song();
        song2.setName("Song Name");
        song2.setArtist("Artist 2");
        song2.setGenre("Genre 2");
        song2.setCreatedAt(Instant.now());

        Song song3 = new Song();
        song3.setName("Different Song Name");
        song3.setArtist("Artist 3");
        song3.setGenre("Genre 3");
        song3.setCreatedAt(Instant.now());

        songRepository.saveAll(List.of(song1, song2, song3));

        List<SongInfo> foundSongs = songRepository.findAllByName("Song Name");

        assertEquals(2, foundSongs.size());
    }

    @Test
    public void testFindAllByArtist() {
        Song song1 = new Song();
        song1.setName("Song 1");
        song1.setArtist("Artist Name");
        song1.setGenre("Genre 1");
        song1.setCreatedAt(Instant.now());

        Song song2 = new Song();
        song2.setName("Song 2");
        song2.setArtist("Artist Name");
        song2.setGenre("Genre 2");
        song2.setCreatedAt(Instant.now());

        Song song3 = new Song();
        song3.setName("Song 3");
        song3.setArtist("Different Artist Name");
        song3.setGenre("Genre 3");
        song3.setCreatedAt(Instant.now());

        songRepository.saveAll(List.of(song1, song2, song3));

        List<SongInfo> foundSongs = songRepository.findAllByArtist("Artist Name");

        assertEquals(2, foundSongs.size());
    }

    @Test
    public void testFindAllByGenre() {
        Song song1 = new Song();
        song1.setName("Song 1");
        song1.setArtist("Artist 1");
        song1.setGenre("Genre Name");
        song1.setCreatedAt(Instant.now());

        Song song2 = new Song();
        song2.setName("Song 2");
        song2.setArtist("Artist 2");
        song2.setGenre("Genre Name");
        song2.setCreatedAt(Instant.now());

        Song song3 = new Song();
        song3.setName("Song 3");
        song3.setArtist("Artist 3");
        song3.setGenre("Different Genre");
        song3.setCreatedAt(Instant.now());

        songRepository.saveAll(List.of(song1, song2, song3));

        List<SongInfo> foundSongs = songRepository.findAllByGenre("Genre Name");

        assertEquals(2, foundSongs.size());
    }

    @Test
    public void testDeleteById() {
        Song song1 = new Song();
        song1.setName("Song 1");
        song1.setArtist("Artist 1");
        song1.setGenre("Genre 1");
        song1.setCreatedAt(Instant.now());

        songRepository.save(song1);

        Long savedSongId = song1.getId();

        songRepository.deleteById(savedSongId);

        Optional<Song> result = songRepository.findById(savedSongId);
        assertTrue(result.isEmpty());
    }

}
