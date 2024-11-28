import Song from "./Song";

export default interface Playlist {
  id: number;
  name: string;
  songs: Song[];
}