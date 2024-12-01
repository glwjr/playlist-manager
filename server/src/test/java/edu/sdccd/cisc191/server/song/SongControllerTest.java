package edu.sdccd.cisc191.server.song;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SongControllerTest {

    @Autowired
    private SongService songService;

    @Autowired
    private MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGetAllSongs() throws Exception {
        List<SongInfo> baseSongs = songService.getAllSongs();

        mockMvc.perform(get("/api/songs"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(baseSongs.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(10));
    }

    @Test
    public void testGetSongById() throws Exception {
        List<SongInfo> baseSongs = songService.getAllSongs();

        mockMvc.perform(get("/api/songs/" + baseSongs.get(0).getId()))
                .andExpect(status().isOk())
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
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateSong() throws Exception {
        SongController.UpdateSongPayload payload = new SongController
                .UpdateSongPayload("Updated Song Name", "Updated Artist Name", "Updated Genre Name");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/songs/{id}", 1)
                        .content(asJsonString(payload))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteSong() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/songs/{id}", 1))
                .andExpect(status().isNoContent());
    }
}