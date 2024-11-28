import {Box, Button, Grid2, Typography} from "@mui/material"
import AddIcon from '@mui/icons-material/Add';
import SongsTable from "@/components/SongsTable";

async function fetchSongs() {
  try {
    const data = await fetch("http://localhost:8080/api/songs");
    return await data.json();
  } catch (error) {
    console.error(error);
    return [];
  }
}

export default async function SongsPage() {
  const songs = await fetchSongs();

  return (
    <Box>
        <Typography variant="h5">Song Catalog</Typography>
        <Button variant="contained" component="a" href="/songs/add" startIcon={<AddIcon />} sx={{ my: 2 }}>Add Song</Button>
        <SongsTable songs={songs} />
    </Box>
  );
}