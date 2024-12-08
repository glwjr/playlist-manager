package edu.sdccd.cisc191.server.playlist;

import edu.sdccd.cisc191.server.song.Song;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Playlist")
public class Playlist {

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}
    )
    @JoinTable(
            name = "Playlist_Song",
            joinColumns = {@JoinColumn(name = "playlist_id")},
            inverseJoinColumns = {@JoinColumn(name = "song_id")}
    )
    Set<Song> songs = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Transient
    private String[][] songsArray = new String[10][3];

    public Playlist() {
        syncSongsArrayWithSongsSet();
    }

    private void syncSongsArrayWithSongsSet() {
        songsArray = new String[songs.size()][3];
        int i = 0;
        for (Song song : songs) {
            songsArray[i++] = new String[]{song.getName(), song.getArtist(), song.getGenre()};
        }
    }

    public void addSong(Song song) {
        if (songs.add(song)) {
            syncSongsArrayWithSongsSet();
        }
    }

    public void deleteSong(Long songId) {
        boolean removed = songs.removeIf(song -> Objects.equals(song.getId(), songId));

        if (removed) {
            syncSongsArrayWithSongsSet();
        }
    }

    public String[][] getSongsArray() {
        return songsArray.clone();
    }
}
