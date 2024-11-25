package edu.sdccd.cisc191.backend.songs;

import java.time.Instant;

public interface SongInfo {
    Long getId();

    String getTitle();

    String getArtist();

    Instant getCreatedAt();
}
