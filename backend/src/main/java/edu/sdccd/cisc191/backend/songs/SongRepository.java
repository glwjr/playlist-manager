package edu.sdccd.cisc191.backend.songs;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    List<SongInfo> findAllByTitle(String title);
    List<SongInfo> findAllByArtist(String artist);
    List<SongInfo> findAllByOrderByCreatedAtDesc();

    Optional<SongInfo> findSongById(Long id);
}
