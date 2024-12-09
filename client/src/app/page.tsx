import { Box, Button, Typography } from "@mui/material";

export default function HomePage() {
  return (
    <Box display="flex" flexDirection="column" alignItems="center">
      <Typography variant="h3" sx={{ mt: 4 }}>
        Playlist Manager
      </Typography>
      <Button
        variant="contained"
        component="a"
        href="/playlists"
        sx={{ mt: 2 }}
      >
        View Playlists
      </Button>
      <Button variant="contained" component="a" href="/songs" sx={{ mt: 2 }}>
        View Songs
      </Button>
    </Box>
  );
}
