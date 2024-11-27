package edu.sdccd.cisc191.backend.songs;

import java.time.Instant;

public interface SongInfo {
    Long getId();

    String getName();

    String getArtist();

    String getGenre();

    Instant getCreatedAt();
}
