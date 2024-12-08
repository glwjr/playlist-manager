package edu.sdccd.cisc191.server.song;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SongControllerTest {

    @Autowired
    private SongService songService;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MockMvc mockMvc;

    private Song song;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        song = songService.createSong("Test Song", "Test Artist", "Test Genre");
    }

    @AfterEach
    public void tearDown() {
        songRepository.deleteAll();
    }

    @Test
    public void testGetAllSongs() throws Exception {
        songService.createSong("Test Song 2", "Test Artist 2", "Test Genre 2");
        songService.createSong("Test Song 3", "Test Artist 3", "Test Genre 3");

        List<SongInfo> baseSongs = songService.getAllSongs();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/songs"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(baseSongs.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
    }

    @Test
    public void testGetSongById() throws Exception {
        List<SongInfo> baseSongs = songService.getAllSongs();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/songs/" + baseSongs.get(0).getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(baseSongs.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(baseSongs.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist").value(baseSongs.get(0).getArtist()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value(baseSongs.get(0).getGenre()));
    }

    @Test
    public void testAddSong() throws Exception {
        SongController.CreateSongPayload payload = new SongController
                .CreateSongPayload("Song Name", "Artist Name", "Genre Name");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/songs")
                        .content(asJsonString(payload))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testUpdateSong() throws Exception {
        SongController.UpdateSongPayload payload = new SongController
                .UpdateSongPayload("Updated Song Name", "Updated Artist Name", "Updated Genre Name");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/songs/{id}", song.getId())
                        .content(asJsonString(payload))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testDeleteSong() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/songs/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
