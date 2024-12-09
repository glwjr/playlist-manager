/* eslint-disable @typescript-eslint/no-explicit-any */

"use client";

import React, {use, useEffect, useState} from "react";
import {Button, FormControl, InputLabel, MenuItem, Paper, Select, SelectChangeEvent, Typography} from "@mui/material";
import {useRouter} from "next/navigation";
import Song from "../../../../../../../common/Song";

export default function AddSongForm({params}: { params: Promise<{ playlistId: number }> }) {
  const {playlistId} = use(params);
  const router = useRouter();
  const [availableSongs, setAvailableSongs] = useState<Song[]>([]);
  const [selectedSongId, setSelectedSongId] = useState<string>("");
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchData() {
      try {
        const songsRes = await fetch("http://localhost:8080/api/songs");
        const allSongs: Song[] = await songsRes.json();
        const playlistRes = await fetch(
          `http://localhost:8080/api/playlists/${playlistId}/songs`
        );
        const playlistSongs: Song[] = await playlistRes.json();
        const playlistSongIds = new Set(playlistSongs.map((song) => song.id));
        const filteredSongs = allSongs.filter((song) => !playlistSongIds.has(song.id));
        setAvailableSongs(filteredSongs);
      } catch (error) {
        setError((error as Error).message);
      }
    }

    fetchData();
  }, [playlistId]);

  const handleSongSelect = (e: SelectChangeEvent) => {
    setSelectedSongId(e.target.value as string);
  };

  const handleAddSong = async () => {
    setError(null);

    try {
      await fetch(`http://localhost:8080/api/playlists/${playlistId}/songs`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({songId: selectedSongId}),
      });

      router.push(`/playlists/${playlistId}`)
    } catch (error: any) {
      setError(error.message || "An unknown error occurred.");
    }
  };

  return (
    <Paper
      sx={{
        maxWidth: 400,
        margin: "auto",
        display: "flex",
        flexDirection: "column",
        gap: 2,
        padding: 2,
      }}
    >
      <Typography variant="h6" textAlign="center">
        Add a Song to the Playlist
      </Typography>
      {availableSongs.length > 0 ? (
        <>
          <FormControl fullWidth>
            <InputLabel id="select-song-label">Select Song</InputLabel>
            <Select
              required
              labelId="select-song-label"
              id="song-select"
              value={selectedSongId}
              label="Select Song"
              onChange={handleSongSelect}
              variant="outlined">
              {availableSongs.map((song) => (
                <MenuItem key={song.id} value={song.id}>
                  {song.name} by {song.artist}
                </MenuItem>
              ))}
            </Select>
          </FormControl>

          <Button
            variant="contained"
            color="primary"
            onClick={handleAddSong}
            disabled={!selectedSongId}
            fullWidth
          >
            Add Song
          </Button>
        </>
      ) : (
        <Typography textAlign="center">No available songs to add.</Typography>
      )}
      <Button variant="outlined" fullWidth component="a" href={`/playlists/${playlistId}`}>
        Back to Playlist
      </Button>
      {error && (
        <Typography color="error.main" sx={{mx: "auto"}}>
          {error}
        </Typography>
      )}
    </Paper>
  );
}
