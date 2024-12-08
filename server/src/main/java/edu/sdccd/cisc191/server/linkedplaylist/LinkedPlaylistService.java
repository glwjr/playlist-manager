package edu.sdccd.cisc191.server.linkedplaylist;

import edu.sdccd.cisc191.server.song.Song;
import edu.sdccd.cisc191.server.song.SongNotFoundException;
import edu.sdccd.cisc191.server.song.SongRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkedPlaylistService {

    private final LinkedPlaylist linkedPlaylist;

    private final SongRepository songRepository;

    public LinkedPlaylistService(SongRepository songRepository) {
        this.linkedPlaylist = new LinkedPlaylist();
        this.songRepository = songRepository;
    }

    public Song addSong(Long songId) {
        var song = songRepository.findById(songId)
                .orElseThrow(() -> new SongNotFoundException("Song not found"));

        return linkedPlaylist.addSong(song);
    }

    public void removeCurrentSong() {
        linkedPlaylist.removeCurrentSong();
    }

    public Song playNextSong() {
        return linkedPlaylist.nextSong();
    }

    public Song playPreviousSong() {
        return linkedPlaylist.previousSong();
    }

    public Song getCurrentSong() {
        return linkedPlaylist.getCurrentSong();
    }

    public List<Song> getAllSongs() {
        return linkedPlaylist;
    }

    public Song findSongByName(String name) {
        return linkedPlaylist.findSongByName(name);
    }

    public void sortByName() {
        linkedPlaylist.sortByName();
    }

    public void clearPlaylist() {
        linkedPlaylist.clear();
    }
}
