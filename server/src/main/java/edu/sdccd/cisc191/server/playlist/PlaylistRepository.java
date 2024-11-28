package edu.sdccd.cisc191.server.playlist;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<PlaylistInfo> findAllByOrderByCreatedAtDesc();

    Optional<PlaylistInfo> findPlaylistById(Long id);
    Optional<PlaylistInfo> findPlaylistByName(String name);
}
