package edu.sdccd.cisc191.server.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sdccd.cisc191.server.song.Song;
import edu.sdccd.cisc191.server.song.SongRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlaylistControllerTest {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MockMvc mockMvc;

    private Playlist playlist;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        playlist = playlistService.createPlaylist("Test Playlist");
    }

    @AfterEach
    public void tearDown() {
        playlistRepository.deleteAll();
    }

    @Test
    public void testGetAllPlaylists() throws Exception {
        playlistService.createPlaylist("Test Playlist 2");

        List<PlaylistInfo> playlists = playlistService.getAllPlaylists();

        mockMvc.perform(get("/api/playlists"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(playlists.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetPlaylistById() throws Exception {
        mockMvc.perform(get("/api/playlists/" + playlist.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(playlist.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(playlist.getName()));
    }

    @Test
    public void testCreatePlaylist() throws Exception {
        PlaylistController.CreatePlaylistPayload payload = new PlaylistController
                .CreatePlaylistPayload("New Playlist");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/playlists")
                        .content(asJsonString(payload))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdatePlaylist() throws Exception {
        PlaylistController.UpdatePlaylistPayload payload = new PlaylistController
                .UpdatePlaylistPayload("Updated Playlist");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/playlists/{id}", playlist.getId())
                        .content(asJsonString(payload))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/playlists/{id}", playlist.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Playlist"));
    }

    @Test
    public void testDeletePlaylist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/playlists/{id}", 1))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/playlists/{id}", playlist.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSongsByPlaylistId() throws Exception {
        Song song = new Song();
        song.setName("Test Name");
        song.setArtist("Test Artist");
        song.setGenre("Test Genre");
        song.setCreatedAt(Instant.now());

        songRepository.save(song);

        playlist.addSong(song);

        playlistRepository.save(playlist);

        mockMvc.perform(get("/api/playlists/{id}/songs", playlist.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(song.getId()));
    }

    @Test
    public void testAddSongToPlaylist() throws Exception {
        Song song = new Song();
        song.setName("Test Name");
        song.setArtist("Test Artist");
        song.setGenre("Test Genre");
        song.setCreatedAt(Instant.now());

        Song savedSong = songRepository.save(song);

        PlaylistController.AddSongToPlaylistPayload payload = new PlaylistController
                .AddSongToPlaylistPayload(savedSong.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/playlists/{id}/songs", playlist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(payload)))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/playlists/{id}/songs", playlist.getId()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(savedSong.getId()));
    }

    @Test
    public void testDeleteSongFromPlaylist() throws Exception {
        Song song = new Song();
        song.setName("Test Song");
        song.setArtist("Test Artist");
        song.setGenre("Test Genre");
        song.setCreatedAt(Instant.now());

        Song savedSong = songRepository.save(song);

        playlist.addSong(savedSong);
        
        playlistRepository.save(playlist);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/playlists/{id}/songs/{songId}", playlist.getId(), savedSong.getId()))
                .andExpect(status().isNoContent());

        Playlist updatedPlaylist = playlistRepository.findById(playlist.getId()).orElseThrow();
        assertFalse(updatedPlaylist.getSongs().contains(savedSong));
    }


}
