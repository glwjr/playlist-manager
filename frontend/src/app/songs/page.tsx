import Button from "@mui/material/Button";
import AddIcon from '@mui/icons-material/Add';

export default async function SongsPage() {
  return (
    <>
      <Button variant="contained" startIcon={<AddIcon />}>Add Song</Button>
    </>
  );
}