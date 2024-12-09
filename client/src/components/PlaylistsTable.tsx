"use client";

import * as React from 'react';
import Box from '@mui/material/Box';
import Link from '@mui/material/Link';
import Button from "@mui/material/Button";
import Collapse from '@mui/material/Collapse';
import IconButton from '@mui/material/IconButton';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import KeyboardArrowUpIcon from '@mui/icons-material/KeyboardArrowUp';
import Playlist from "../../../common/Playlist"

function Row(props: { row: Playlist }) {
  const {row} = props;
  const [open, setOpen] = React.useState(false);

  return (
    <React.Fragment>
      <TableRow sx={{'& > *': {borderBottom: 'unset'}}}>
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="small"
            onClick={() => setOpen(!open)}
          >
            {open ? <KeyboardArrowUpIcon/> : <KeyboardArrowDownIcon/>}
          </IconButton>
        </TableCell>
        <TableCell component="th" scope="row">
          <Link href={`/playlists/${row.id}`}>{row.name}</Link>
        </TableCell>
        <TableCell align="right">{row.songs?.length ?? "N/A"}</TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{paddingBottom: 0, paddingTop: 0}} colSpan={6}>
          <Collapse in={open} timeout="auto" unmountOnExit>
            <Box sx={{margin: 1}}>
              <Typography variant="h6" gutterBottom component="div">
                {'Songs in ' + row.name}
              </Typography>
              <Table size="small" aria-label="purchases">
                <TableHead>
                  <TableRow>
                    <TableCell>Name</TableCell>
                    <TableCell>Artist</TableCell>
                    <TableCell align="right">Genre</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {row.songs.map((song) => (
                    <TableRow key={song.id}>
                      <TableCell component="th" scope="row">
                        {song.name}
                      </TableCell>
                      <TableCell>{song.artist}</TableCell>
                      <TableCell align="right">{song.genre}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
              <Button variant="outlined" sx={{m: 2}} component="a" href={`/playlists/${row.id}/songs/add`}>
                Add Existing Song to Playlist
              </Button>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </React.Fragment>
  );
}

export default function PlaylistsTable({playlists}: { playlists: Playlist[] }) {
  return (
    <TableContainer component={Paper}>
      <Table aria-label="collapsible table">
        <TableHead>
          <TableRow>
            <TableCell/>
            <TableCell>Playlist Name</TableCell>
            <TableCell align="right">Number of Songs</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {playlists.map((playlist) => (
            <Row key={playlist.name} row={playlist}/>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
