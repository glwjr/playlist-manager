import Song from "../../../../shared/Song";
import Button from "@mui/material/Button";
import AddIcon from '@mui/icons-material/Add';

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
    <div>
      <Button variant="contained" startIcon={<AddIcon />}>Add Song</Button>
      {songs.map((song: Song) => <div key={song.id}>{song.title}</div>)}
    </div>
  );
}