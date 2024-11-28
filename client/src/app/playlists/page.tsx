import Playlist from "../../../../common/Playlist";
import Button from "@mui/material/Button";
import AddIcon from '@mui/icons-material/Add';
import Card from "@mui/material/Card";
import Stack from "@mui/material/Stack";
import Typography from "@mui/material/Typography";

async function fetchPlaylists() {
  try {
    const data = await fetch("http://localhost:8080/api/songs");
    return await data.json();
  } catch (error) {
    console.error(error);
    return [];
  }
}

export default async function PlaylistsPage() {
  const playlists = await fetchPlaylists();

  return (
    <div>
      <Card sx={{ display: "flex", flexDirection: "column", alignSelf: "center", width: "100%", p: 4 }}>
        <Button variant="contained" startIcon={<AddIcon />}>Add Playlist</Button>
      </Card>
      {playlists.map((playlist: Playlist) => <div key={playlist.id}>{playlist.name}</div>)}
    </div>
  );
}