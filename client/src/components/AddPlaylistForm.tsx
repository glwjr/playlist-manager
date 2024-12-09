/* eslint-disable @typescript-eslint/no-explicit-any */

"use client";

import React, {useState} from "react";
import {Button, Paper, TextField, Typography} from "@mui/material";
import {useRouter} from "next/navigation";

export default function AddPlaylistForm() {
  const router = useRouter();
  const [playlist, setPlaylist] = useState({name: ""});
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const {name, value} = e.target;
    setPlaylist((prevPlaylist) => ({
      ...prevPlaylist,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      await fetch("http://localhost:8080/api/playlists", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(playlist),
      });

      setPlaylist({name: ""});
      router.push("/playlists");
    } catch (error: any) {
      setError(error.message || "An unknown error occurred.");
    }
  };

  return (
    <Paper
      component="form"
      onSubmit={handleSubmit}
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
        Add a New Playlist
      </Typography>
      <TextField
        label="Playlist Name"
        name="name"
        value={playlist.name}
        onChange={handleChange}
        required
        fullWidth
      />
      <Button type="submit" variant="contained" color="primary" fullWidth>
        Submit
      </Button>
      <Button variant="outlined" fullWidth component="a" href="/playlists">
        Cancel
      </Button>
      {error && <Typography color="error.main" sx={{mx: "auto"}}>{error}</Typography>}
    </Paper>
  );
};
