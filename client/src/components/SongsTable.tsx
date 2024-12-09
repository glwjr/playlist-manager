import * as React from 'react';
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import Song from "../../../common/Song";

const columns: GridColDef[] = [
  {field: 'id', headerName: 'ID', flex: 1},
  {field: 'name', headerName: 'Name', flex: 1},
  {field: 'artist', headerName: 'Artist', flex: 1},
  {field: 'genre', headerName: 'Genre', flex: 1},
];

export default function SongsTable({songs}: { songs: Song[] }) {
  return (
    <div style={{height: 400, width: '100%'}}>
      <DataGrid rows={songs} columns={columns}/>
    </div>
  );
}
