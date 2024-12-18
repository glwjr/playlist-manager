import Typography from "@mui/material/Typography";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import AddIcon from "@mui/icons-material/Add";
import PlaylistsTable from "@/components/PlaylistsTable";

async function fetchPlaylists() {
  try {
    const data = await fetch("http://localhost:8080/api/playlists");
    return await data.json();
  } catch (error) {
    console.error(error);
    return [];
  }
}

export default async function PlaylistsPage() {
  const playlists = await fetchPlaylists();

  return (
    <Box>
      <Typography variant="h5">Playlists</Typography>
      <Button
        variant="contained"
        component="a"
        href="/playlists/add"
        startIcon={<AddIcon/>}
        sx={{my: 2}}
      >
        Create Playlist
      </Button>
      <PlaylistsTable playlists={playlists}/>
    </Box>
  );
}
