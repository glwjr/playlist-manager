package edu.sdccd.cisc191.server.playlist;

import edu.sdccd.cisc191.server.song.Song;
import edu.sdccd.cisc191.server.song.SongRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlaylistServiceTest {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private PlaylistService playlistService;

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
        playlistRepository.deleteAll();
    }

    @Test
    public void testGetAll() {
        Playlist playlist2 = new Playlist();
        playlist2.setName("Test Playlist 2");
        playlist2.setCreatedAt(Instant.now());

        playlistRepository.save(playlist2);

        List<PlaylistInfo> playlists = playlistService.getAllPlaylists();

        assert playlists != null;
        assertEquals(2, playlists.size());
    }

    @Test
    public void testGetPlaylistById() {
        PlaylistInfo foundPlaylist = playlistService.getPlaylistById(playlist.getId());

        assert foundPlaylist != null;
        assertEquals(playlist.getId(), foundPlaylist.getId());
        assertEquals(playlist.getName(), foundPlaylist.getName());
    }

    @Test
    public void testGetPlaylistByName() {
        PlaylistInfo foundPlaylist = playlistService.getPlaylistByName("Test Playlist");

        assert foundPlaylist != null;
        assertEquals(playlist.getId(), foundPlaylist.getId());
    }

    @Test
    public void testCreatePlaylist() {
        Playlist newPlaylist = playlistService.createPlaylist("New Playlist");

        Optional<PlaylistInfo> createdPlaylist = playlistRepository.findPlaylistById(newPlaylist.getId());

        assert createdPlaylist.isPresent();
        assertEquals(newPlaylist.getId(), createdPlaylist.get().getId());
        assertEquals(newPlaylist.getName(), createdPlaylist.get().getName());
    }

    @Test
    public void testUpdatePlaylist() {
        playlistService.updatePlaylist(playlist.getId(), "Updated Playlist Name");

        Optional<PlaylistInfo> updatedPlaylist = playlistRepository.findPlaylistById(playlist.getId());
        assert updatedPlaylist.isPresent();

        assertEquals(playlist.getId(), updatedPlaylist.get().getId());
        assertEquals("Updated Playlist Name", updatedPlaylist.get().getName());
    }

    @Test
    public void testDeletePlaylist() {
        Long playlistId = playlist.getId();

        playlistService.deletePlaylist(playlistId);

        Optional<PlaylistInfo> deletedPlaylist = playlistRepository.findPlaylistById(playlistId);

        assert deletedPlaylist.isEmpty();
    }

    @Test
    public void testGetPlaylistSongs() {
        Song secondSong = new Song();
        secondSong.setName("Test Song 2");
        secondSong.setArtist("Test Artist 2");
        secondSong.setGenre("Test Genre 2");
        secondSong.setCreatedAt(Instant.now());

        songRepository.save(secondSong);

        playlist.getSongs().addAll(List.of(song, secondSong));

        Playlist updatedPlaylist = playlistRepository.save(playlist);

        Set<Song> songs = playlistService.getSongs(updatedPlaylist.getId());

        assert songs != null;
        assertEquals(2, songs.size());
    }

    @Test
    public void testAddSongToPlaylist() {
        playlistService.addSongToPlaylist(playlist.getId(), song.getId());

        Optional<PlaylistInfo> updatedPlaylist = playlistRepository.findPlaylistById(playlist.getId());

        assert updatedPlaylist.isPresent();
        assertEquals(1, updatedPlaylist.get().getSongs().size());
    }

    @Test
    public void testDeleteSongFromPlaylist() {
        playlistService.deleteSongFromPlaylist(playlist.getId(), song.getId());

        Optional<PlaylistInfo> updatedPlaylist = playlistRepository.findPlaylistById(playlist.getId());

        assert updatedPlaylist.isPresent();
        assertEquals(0, updatedPlaylist.get().getSongs().size());
    }

    @Test
    public void testFilterAndSortSongsByGenre() {
        Song secondSong = new Song();
        secondSong.setName("B - Test Song 2");
        secondSong.setArtist("Test Artist 2");
        secondSong.setGenre("Rock");
        secondSong.setCreatedAt(Instant.now());

        Song thirdSong = new Song();
        thirdSong.setName("A - Test Song 3");
        thirdSong.setArtist("Test Artist 3");
        thirdSong.setGenre("Rock");
        thirdSong.setCreatedAt(Instant.now());

        songRepository.saveAll(List.of(secondSong, thirdSong));

        playlist.getSongs().addAll(List.of(song, secondSong, thirdSong));

        playlistRepository.save(playlist);

        List<Song> result = playlistService.filterAndSortSongsByGenre(playlist.getId(), "Rock");

        assertEquals(2, result.size());
        assertEquals(thirdSong.getId(), result.get(0).getId());
        assertEquals(secondSong.getId(), result.get(1).getId());
    }

}
