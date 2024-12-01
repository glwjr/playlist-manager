package edu.sdccd.cisc191.server.song;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {

    List<SongInfo> findAllByName(String name);

    List<SongInfo> findAllByArtist(String artist);

    List<SongInfo> findAllByGenre(String genre);

    List<SongInfo> findAllByOrderByCreatedAtDesc();

    Optional<SongInfo> findSongById(Long id);

    Optional<SongInfo> findSongByName(String name);

}
