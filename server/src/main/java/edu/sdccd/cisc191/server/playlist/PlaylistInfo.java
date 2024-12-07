package edu.sdccd.cisc191.server.playlist;

import edu.sdccd.cisc191.server.song.Song;

import java.util.Set;

public interface PlaylistInfo {

    Long getId();

    String getName();

    Set<Song> getSongs();

}
