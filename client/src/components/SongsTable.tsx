import * as React from 'react';
import { DataGrid, GridColDef } from '@mui/x-data-grid';
import Song from "../../../common/Song";

const columns: GridColDef[] = [
  { field: 'name', headerName: 'Name', width: 150 },
  { field: 'artist', headerName: 'Artist', width: 150 },
  { field: 'genre', headerName: 'Genre', width: 150 },
];

export default function SongsTable({ songs } : { songs: Song[] }) {
  return (
    <div style={{ height: 400, width: '100%' }}>
      <DataGrid rows={songs} columns={columns} />
    </div>
  );
}
