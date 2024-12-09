INSERT INTO SONG (name, artist, genre)
VALUES ('Hotel California', 'Eagles', 'Rock'),
       ('Hey Jude', 'The Beatles', 'Pop'),
       ('Like a Rolling Stone', 'Bob Dylan', 'Rock'),
       ('Smells Like Teen Spirit', 'Nirvana', 'Rock'),
       ('Billie Jean', 'Michael Jackson', 'Pop'),
       ('What a Wonderful World', 'Louis Armstrong', 'Jazz'),
       ('Stairway to Heaven', 'Led Zeppelin', 'Rock'),
       ('Let It Be', 'The Beatles', 'Pop');

INSERT INTO PLAYLIST (name)
VALUES ('Rock Playlist'),
       ('Pop Playlist');

INSERT INTO PLAYLIST_SONG (playlist_id, song_id)
SELECT p.id, s.id
FROM PLAYLIST p
         JOIN SONG s ON
    (p.name = 'Rock Playlist' AND s.genre = 'Rock') OR
    (p.name = 'Pop Playlist' AND s.genre = 'Pop');