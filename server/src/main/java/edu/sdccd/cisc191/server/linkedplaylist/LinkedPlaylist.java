package edu.sdccd.cisc191.server.linkedplaylist;

import edu.sdccd.cisc191.server.song.Song;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class LinkedPlaylist extends LinkedList<Song> {

    private int currentIndex;

    public LinkedPlaylist() {
        super();
        this.currentIndex = -1;
    }

    public Song addSong(Song song) {
        this.add(song);
        if (this.size() == 1) {
            currentIndex = 0;
        }
        return song;
    }

    public void removeCurrentSong() {
        if (currentIndex >= 0 && currentIndex < this.size()) {
            this.remove(currentIndex);

            if (this.isEmpty()) {
                currentIndex = -1;
            } else if (currentIndex >= this.size()) {
                currentIndex = this.size() - 1;
            }
        }
    }

    public Song nextSong() {
        if (!this.isEmpty()) {
            currentIndex = (currentIndex + 1) % this.size();
            return this.get(currentIndex);
        }
        return null;
    }

    public Song previousSong() {
        if (!this.isEmpty()) {
            currentIndex = (currentIndex - 1 + this.size()) % this.size();
            return this.get(currentIndex);
        }
        return null;
    }

    public Song getCurrentSong() {
        if (!this.isEmpty() && currentIndex >= 0) {
            return this.get(currentIndex);
        }
        return null;
    }

    public Song findSongByName(String name) {
        for (Song song : this) {
            if (song.getName().equalsIgnoreCase(name)) {
                return song;
            }
        }
        return null;
    }

    public void sortByName() {
        List<Song> sortedList = new ArrayList<>(this);
        sortedList.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER));
        this.clear();
        this.addAll(sortedList);
    }
}
