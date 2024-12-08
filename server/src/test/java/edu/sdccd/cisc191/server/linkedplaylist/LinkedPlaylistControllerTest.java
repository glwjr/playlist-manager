package edu.sdccd.cisc191.server.linkedplaylist;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LinkedPlaylistControllerTest {

    @Autowired
    private LinkedPlaylistService linkedPlaylistService;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private MockMvc mockMvc;

    private Song song1;
    private Song song2;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setUp() {
        song1 = new Song();
        song1.setName("Song 1");
        song1.setArtist("Artist 1");
        song1.setGenre("Pop");
        song1.setCreatedAt(Instant.now());

        song2 = new Song();
        song2.setName("Song 2");
        song2.setArtist("Artist 2");
        song2.setGenre("Rock");
        song2.setCreatedAt(Instant.now());

        songRepository.saveAll(List.of(song1, song2));
    }

    @AfterEach
    public void tearDown() {
        linkedPlaylistService.clearPlaylist();
        songRepository.deleteAll();
    }

    @Test
    public void testGetAllSongs() throws Exception {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/linked-playlist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(song1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(song2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetCurrentSong() throws Exception {
        linkedPlaylistService.addSong(song1.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/linked-playlist/current"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(song1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(song1.getName()));
    }

    @Test
    public void testGetCurrentNullSong() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/linked-playlist/current"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testAddSong() throws Exception {
        LinkedPlaylistController.AddSongPayload payload = new LinkedPlaylistController
                .AddSongPayload(song1.getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/linked-playlist/add")
                        .content(asJsonString(payload))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        assertEquals(song1.getId(), linkedPlaylistService.getCurrentSong().getId());
    }

    @Test
    public void testRemoveCurrentSong() throws Exception {
        linkedPlaylistService.addSong(song1.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/linked-playlist/remove"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        assertNull(linkedPlaylistService.getCurrentSong());
    }

    @Test
    public void testPlayNextSong() throws Exception {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/linked-playlist/next"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(song2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(song2.getName()));
    }

    @Test
    public void testPlayPreviousSong() throws Exception {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());
        linkedPlaylistService.playNextSong();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/linked-playlist/previous"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(song1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(song1.getName()));
    }

    @Test
    public void testFindSongByName() throws Exception {
        linkedPlaylistService.addSong(song1.getId());
        linkedPlaylistService.addSong(song2.getId());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/linked-playlist/search")
                        .param("name", "Song 2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(song2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(song2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artist").value(song2.getArtist()));
    }

    @Test
    public void testSortByName() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders.put("/api/linked-playlist/sort-by-name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/linked-playlist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(songA.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(songB.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id").value(songC.getId()));
    }
}
