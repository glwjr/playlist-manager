import React from "react";
import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";

import SongsTable from "@/components/SongsTable";

async function fetchData(playlistId: number) {
  try {
    const data = await fetch(`http://localhost:8080/api/playlists/${playlistId}`);
    return await data.json();
  } catch (error) {
    console.error(error);
    return [];
  }
}

export default async function PlaylistPage({params}: { params: Promise<{ playlistId: number }> }) {
  const id = (await params).playlistId
  const {name, songs} = await fetchData(id);

  return (
    <Box>
      <Typography variant="h5">{name}</Typography>
      <Button variant="contained" sx={{my: 2}} component="a" href={`/playlists/${id}/songs/add`}>
        Add Existing Song to Playlist
      </Button>
      <SongsTable songs={songs}/>
      <Button variant="outlined" sx={{my: 2}} component="a" href="/playlists">
        Return to Playlists
      </Button>
    </Box>
  );
}