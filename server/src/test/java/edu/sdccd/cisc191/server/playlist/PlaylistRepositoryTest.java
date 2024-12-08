package edu.sdccd.cisc191.server.playlist;

import edu.sdccd.cisc191.server.song.Song;
import edu.sdccd.cisc191.server.song.SongRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlaylistRepositoryTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    private Playlist playlist;

    private Song song;

    @BeforeEach
    public void setUp() {
        playlist = new Playlist();
        playlist.setName("Test Playlist");
        playlist.setCreatedAt(Instant.now());

        playlistRepository.save(playlist);

        song = new Song();
        song.setName("Test Song");
        song.setArtist("Test Artist");
        song.setGenre("Test Genre");
        song.setCreatedAt(Instant.now());

        songRepository.save(song);
    }

    @AfterEach
    public void tearDown() {
        playlistRepository.delete(playlist);
        songRepository.delete(song);
    }

    @Test
    public void testSave() {
        Playlist newPlaylist = new Playlist();
        newPlaylist.setName("Test Playlist 2");
        newPlaylist.setCreatedAt(Instant.now());

        playlistRepository.save(newPlaylist);

        Long playlistId = newPlaylist.getId();

        PlaylistInfo foundPlaylist = playlistRepository.findPlaylistById(playlistId)
                .orElseThrow(() -> new PlaylistNotFoundException("Could not find playlist with id: " + playlistId));

        assertEquals(newPlaylist.getId(), foundPlaylist.getId());
        assertEquals(newPlaylist.getName(), foundPlaylist.getName());
    }

    @Test
    public void testUpdate() {
        playlist.setName("Updated Playlist Name");
        playlistRepository.save(playlist);

        PlaylistInfo updatedPlaylist = playlistRepository.findPlaylistById(playlist.getId())
                .orElseThrow(() -> new PlaylistNotFoundException("Could not find playlist with id: " + playlist.getId()));

        assert updatedPlaylist != null;
        assertEquals("Updated Playlist Name", updatedPlaylist.getName());
    }

    @Test
    public void testFindPlaylistByName() {
        PlaylistInfo playlist = playlistRepository.findPlaylistByName("Test Playlist")
                .orElseThrow(() -> new PlaylistNotFoundException("Test Playlist not found"));

        assert (playlist.getName().equals("Test Playlist"));
    }

    @Test
    public void testFindAllByOrderByCreatedAtDesc() {
        Playlist newPlaylist = new Playlist();
        newPlaylist.setName("Test Playlist 2");
        newPlaylist.setCreatedAt(Instant.now());

        playlistRepository.save(newPlaylist);

        List<PlaylistInfo> playlists = playlistRepository.findAllByOrderByCreatedAtDesc();

        assertFalse(playlists.isEmpty());
        assertEquals(2, playlists.size());
    }

    @Test
    public void testDeleteById() {
        Playlist newPlaylist = new Playlist();
        newPlaylist.setName("Test Playlist 2");
        newPlaylist.setCreatedAt(Instant.now());

        playlistRepository.save(newPlaylist);

        Long playlistId = newPlaylist.getId();

        playlistRepository.deleteById(playlistId);

        Optional<PlaylistInfo> result = playlistRepository.findPlaylistById(playlistId);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetPlaylistSongs() {
        Song secondSong = new Song();
        secondSong.setName("Test Song 2");
        secondSong.setArtist("Test Artist 2");
        secondSong.setGenre("Test Genre 2");
        secondSong.setCreatedAt(Instant.now());

        songRepository.save(secondSong);

        playlist.addSong(song);
        playlist.addSong(secondSong);

        Playlist updatedPlaylist = playlistRepository.save(playlist);

        Set<Song> songs = updatedPlaylist.getSongs();
        assertEquals(2, songs.size());
    }

    @Test
    public void testAddSongToPlaylist() {
        playlist.addSong(song);

        playlistRepository.save(playlist);

        assertEquals(1, playlist.getSongs().size());
        assertTrue(playlist.getSongs().contains(song));

        assertEquals(1, playlist.getSongsArray().length);
        assertEquals(playlist.getSongsArray()[0][0], song.getName());
    }

    @Test
    public void testDeleteSongFromPlaylist() {
        Song secondSong = new Song();
        secondSong.setName("Test Song 2");
        secondSong.setArtist("Test Artist 2");
        secondSong.setGenre("Test Genre 2");
        secondSong.setCreatedAt(Instant.now());

        songRepository.save(secondSong);

        playlist.addSong(song);
        playlist.addSong(secondSong);

        playlistRepository.save(playlist);

        Long songId = secondSong.getId();

        playlist.deleteSong(songId);

        playlistRepository.save(playlist);

        assertEquals(1, playlist.getSongs().size());
        assertFalse(playlist.getSongs().contains(secondSong));

        assertEquals(1, playlist.getSongsArray().length);
    }
}
