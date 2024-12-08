package edu.sdccd.cisc191.server.linkedplaylist;

import edu.sdccd.cisc191.server.song.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class LinkedPlaylistTest {

    private LinkedPlaylist linkedPlaylist;
    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    public void setUp() {
        linkedPlaylist = new LinkedPlaylist();

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
    }

    @Test
    public void testAddSong() {
        assertEquals(0, linkedPlaylist.size());

        linkedPlaylist.addSong(song1);
        linkedPlaylist.addSong(song2);

        assertEquals(2, linkedPlaylist.size());
        assertEquals(song1, linkedPlaylist.get(0));
        assertEquals(song2, linkedPlaylist.get(1));
        assertEquals(song1, linkedPlaylist.getCurrentSong());
    }

    @Test
    public void testRemoveCurrentSong() {
        linkedPlaylist.addSong(song1);
        linkedPlaylist.addSong(song2);

        assertEquals(2, linkedPlaylist.size());
        assertEquals(song1, linkedPlaylist.getCurrentSong());

        linkedPlaylist.removeCurrentSong();

        assertEquals(1, linkedPlaylist.size());
        assertEquals(song2, linkedPlaylist.getCurrentSong());

        linkedPlaylist.removeCurrentSong();

        assertTrue(linkedPlaylist.isEmpty());
        assertNull(linkedPlaylist.getCurrentSong());
    }

    @Test
    public void testNextSong() {
        linkedPlaylist.addSong(song1);
        linkedPlaylist.addSong(song2);
        linkedPlaylist.addSong(song3);

        assertEquals(song1, linkedPlaylist.getCurrentSong());

        assertEquals(song2, linkedPlaylist.nextSong());
        assertEquals(song3, linkedPlaylist.nextSong());
        assertEquals(song1, linkedPlaylist.nextSong());
    }

    @Test
    public void testPreviousSong() {
        linkedPlaylist.addSong(song1);
        linkedPlaylist.addSong(song2);
        linkedPlaylist.addSong(song3);

        assertEquals(song1, linkedPlaylist.getCurrentSong());

        assertEquals(song3, linkedPlaylist.previousSong()); // Loops to the end
        assertEquals(song2, linkedPlaylist.previousSong());
        assertEquals(song1, linkedPlaylist.previousSong());
    }

    @Test
    public void testGetCurrentSong() {
        assertNull(linkedPlaylist.getCurrentSong()); // No songs in the playlist

        linkedPlaylist.addSong(song1);

        assertEquals(song1, linkedPlaylist.getCurrentSong());

        linkedPlaylist.nextSong();
        assertEquals(song1, linkedPlaylist.getCurrentSong());
    }

    @Test
    public void testFindSongByName() {
        linkedPlaylist.addSong(song1);
        linkedPlaylist.addSong(song2);

        Song foundSong = linkedPlaylist.findSongByName("Song 2");

        assertNotNull(foundSong);
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

        linkedPlaylist.addSong(songC);
        linkedPlaylist.addSong(songB);
        linkedPlaylist.addSong(songA);

        linkedPlaylist.sortByName();

        assertEquals(songA.getName(), linkedPlaylist.get(0).getName());
        assertEquals(songB.getName(), linkedPlaylist.get(1).getName());
        assertEquals(songC.getName(), linkedPlaylist.get(2).getName());
    }
}
