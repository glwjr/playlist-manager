package edu.sdccd.cisc191.server.playlist;

import java.time.Instant;

public interface PlaylistInfo {

    Long getId();

    String getName();

    String[][] getSongs();

    Instant getCreatedAt();

}
