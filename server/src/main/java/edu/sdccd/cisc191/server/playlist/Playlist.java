package edu.sdccd.cisc191.server.playlist;

import edu.sdccd.cisc191.server.song.Song;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Playlist")
public class Playlist {

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

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "Playlist_Song",
            joinColumns = { @JoinColumn(name = "playlist_id") },
            inverseJoinColumns = { @JoinColumn(name = "song_id") }
    )
    Set<Song> songs = new HashSet<>();

    @Transient
    private String[][] songsArray = new String[10][3];

    public int[] findIndexOfSong(String value) {
        for (int row = 0; row < songsArray.length; row++) {
            for (int col = 0; col < songsArray[row].length; col++) {
                if (songsArray[row][col] != null && songsArray[row][col].equals(value)) {
                    return new int[]{row, col};
                }
            }
        }
        return null;
    }

    public void addSongToArray(int row, String name, String artist, String genre) {
        if (row >= songsArray.length) {
            expandSongsArray();
        }
        songsArray[row][0] = name;
        songsArray[row][1] = artist;
        songsArray[row][2] = genre;
    }

    public void expandSongsArray() {
        String[][] newSongsArray = new String[songsArray.length + 1][3];
        System.arraycopy(songsArray, 0, newSongsArray, 0, songsArray.length);
        this.songsArray = newSongsArray;
    }

    public void deleteSongAtIndex(int row, int col) {
        if (row >= 0 && row < songsArray.length && col >= 0 && col < songsArray[row].length) {
            songsArray[row][col] = null;
        }
    }

    public void deleteRowAtIndex(int row) {
        if (row >= 0 && row < songsArray.length) {
            for (int col = 0; col < songsArray[row].length; col++) {
                songsArray[row][col] = null;
            }
        }
    }

    public String[][] printAllSongs() {
        int validRowCount = 0;
        for (String[] row : songsArray) {
            if (row != null && row[0] != null) {
                validRowCount++;
            }
        }

        String[][] validRows = new String[validRowCount][songsArray[0].length];
        int index = 0;

        for (String[] row : songsArray) {
            if (row != null && row[0] != null) {
                validRows[index++] = row;
            }
        }

        return validRows;
    }

    public void persistSongFromArray(int row) {
        if (row < songsArray.length && songsArray[row][0] != null) {
            Song song = new Song();
            song.setName(songsArray[row][0]);
            song.setArtist(songsArray[row][1]);
            song.setGenre(songsArray[row][2]);
            songs.add(song);
        }
    }

}
