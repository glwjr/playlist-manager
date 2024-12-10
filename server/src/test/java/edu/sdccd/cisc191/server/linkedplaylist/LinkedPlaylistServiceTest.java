package edu.sdccd.cisc191.server.linkedplaylist;

import edu.sdccd.cisc191.server.song.Song;
import edu.sdccd.cisc191.server.song.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LinkedPlaylistServiceTest {

    @Autowired
    private LinkedPlaylistService linkedPlaylistService;

    @Autowired
    private SongRepository songRepository;

    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    public void setUp() {
        linkedPlaylistService = new LinkedPlaylistService(songRepository);

        song1 = new Song();
        song1.setName("Song 1");
        song1.setArtist("Artist 1");
        song1.setGenre("Genre 1");
        song1.setCreatedAt(Instant.now());

        song2 = new Song();
        song2.setName("Song 2");
        song2.setArtist("Artist 2");
        song2.setGenre("Genre 2");
        song2.setCreatedAt(Instant.now());

        song3 = new Song();
        song3.setName("Song 3");
        song3.setArtist("Artist 3");
        song3.setGenre("Genre 3");
        song3.setCreatedAt(Instant.now());

        songRepository.saveAll(List.of(song1, song2, song3));
    }

    @Test
    public void testAddSong() {
        Song addedSong = linkedPlaylistService.addSong(song1.getId());

        assertNotNull(addedSong);
        assertEquals("Song 1", addedSong.getName());
    }

    @Test
    public void testRemoveCurrentSong() {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());
        linkedPlaylistService.addSong(song3.getId());

        assertEquals(song1.getId(), linkedPlaylistService.getCurrentSong().getId());

        linkedPlaylistService.removeCurrentSong();

        assertEquals(song2.getId(), linkedPlaylistService.getCurrentSong().getId());
    }

    @Test
    public void testPlayNextSong() {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());

        assertEquals(song1.getId(), linkedPlaylistService.getCurrentSong().getId());
        assertEquals(song2.getId(), linkedPlaylistService.playNextSong().getId());
        assertEquals(song1.getId(), linkedPlaylistService.playNextSong().getId());
    }

    @Test
    public void testPlayPreviousSong() {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());

        assertEquals(song1.getId(), linkedPlaylistService.getCurrentSong().getId());
        assertEquals(song2.getId(), linkedPlaylistService.playPreviousSong().getId());
        assertEquals(song1.getId(), linkedPlaylistService.playPreviousSong().getId());
    }

    @Test
    public void testGetCurrentSong() {
        assertNull(linkedPlaylistService.getCurrentSong());

        linkedPlaylistService.addSong(song1.getId());

        assertEquals(song1.getId(), linkedPlaylistService.getCurrentSong().getId());
    }

    @Test
    public void testGetAllSongs() {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());
        linkedPlaylistService.addSong(song3.getId());

        List<Song> songs = linkedPlaylistService.getAllSongs();

        assertEquals(3, songs.size());
        assertEquals(song1.getId(), songs.get(0).getId());
        assertEquals(song2.getId(), songs.get(1).getId());
        assertEquals(song3.getId(), songs.get(2).getId());
    }

    @Test
    public void testFindSongByName() {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());
        linkedPlaylistService.addSong(song3.getId());

        Song foundSong = linkedPlaylistService.findSongByName("Song 2");

        assertNotNull(foundSong);
        assertEquals(song2.getId(), foundSong.getId());
        assertEquals(song2.getName(), foundSong.getName());
    }

    @Test
    public void testSortByName() {
        Song songC = new Song();
        songC.setName("Song C");
        songC.setArtist("Artist C");
        songC.setGenre("Genre C");
        songC.setCreatedAt(Instant.now());

        Song songB = new Song();
        songB.setName("Song B");
        songB.setArtist("Artist B");
        songB.setGenre("Genre B");
        songB.setCreatedAt(Instant.now());

        Song songA = new Song();
        songA.setName("Song A");
        songA.setArtist("Artist A");
        songA.setGenre("Genre A");
        songA.setCreatedAt(Instant.now());

        songRepository.saveAll(List.of(songC, songB, songA));

        linkedPlaylistService.addSong(songC.getId());
        linkedPlaylistService.addSong(songB.getId());
        linkedPlaylistService.addSong(songA.getId());

        linkedPlaylistService.sortByName();

        List<Song> songs = linkedPlaylistService.getAllSongs();

        assertEquals(3, songs.size());
        assertEquals(songA.getId(), songs.get(0).getId());
        assertEquals(songB.getId(), songs.get(1).getId());
        assertEquals(songC.getId(), songs.get(2).getId());
    }

    @Test
    public void testClearPlaylist() {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());
        linkedPlaylistService.addSong(song3.getId());

        linkedPlaylistService.clearPlaylist();

        assertNull(linkedPlaylistService.getCurrentSong());
        assertNull(linkedPlaylistService.playNextSong());
        assertNull(linkedPlaylistService.playPreviousSong());
    }

    @Test
    public void testPopulatePlaylist() {
        linkedPlaylistService.populatePlaylist();

        assertEquals(3, linkedPlaylistService.getAllSongs().size());
        assertEquals(song1.getId(), linkedPlaylistService.getCurrentSong().getId());
    }
}
