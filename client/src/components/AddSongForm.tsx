/* eslint-disable @typescript-eslint/no-explicit-any */

"use client";

import React, {useState} from "react";
import {Button, Paper, TextField, Typography} from "@mui/material";
import {useRouter} from "next/navigation";
import Song from "../../../common/Song";

export default function AddSongForm() {
  const router = useRouter();
  const [song, setSong] = useState<Song>({
    name: "",
    artist: "",
    genre: "",
  });
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const {name, value} = e.target;
    setSong((prevSong) => ({
      ...prevSong,
      [name]: value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      await fetch("http://localhost:8080/api/songs", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(song),
      });

      setSong({name: "", artist: "", genre: ""});
      router.push("/songs");
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
        Add a New Song
      </Typography>
      <TextField
        label="Song Name"
        name="name"
        value={song.name}
        onChange={handleChange}
        required
        fullWidth
      />
      <TextField
        label="Artist"
        name="artist"
        value={song.artist}
        onChange={handleChange}
        required
        fullWidth
      />
      <TextField
        label="Genre"
        name="genre"
        value={song.genre}
        onChange={handleChange}
        required
        fullWidth
      />
      <Button type="submit" variant="contained" color="primary" fullWidth>
        Submit
      </Button>
      <Button variant="outlined" fullWidth component="a" href="/songs">
        Cancel
      </Button>
      {error && <Typography color="error.main" sx={{mx: "auto"}}>{error}</Typography>}
    </Paper>
  );
};
