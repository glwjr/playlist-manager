# Playlist Manager

Playlist Manager is a full-stack application for managing and organizing playlists. Built with a Next.js frontend and a
Java-based backend, this project was developed as part of Architect Assignment 2 for **CISC 191**.

## Features

- **Frontend**: Developed using Next.js with TypeScript and styled with Material UI
- **Backend**: Java-based backend using the Spring Framework for managing playlists and songs
- **Database**: PostgreSQL integration for persistent data storage
- **Shared Utilities**: Common TypeScript models/interfaces for consistency between client and server

---

## Getting Started

### Prerequisites

- **Node.js** (for the frontend)
- **npm** (comes with Node.js)
- **Java JDK 17+** (for the backend)
- **Maven** (for dependency management)
- **PostgreSQL** (for the database)
- **Git** (for cloning the repository)

### Setup

#### 1. Clone the Repository

```bash
git clone https://github.com/glwjr/playlist-manager.git
cd playlist-manager
```

#### 2. Create a Database

Create a PostgreSQL database named `playlist-manager-db`. If you use a different database name, update the
`spring.datasource.url` in the `server/src/main/resources/application.properties` file.

#### 3. Start the Backend

Navigate to the `server` directory and start the backend:

```bash
cd server
./mvnw spring-boot:run
```

The backend will run on the following ports:

- http://localhost:8080
- http://localhost:8081
- http://localhost:8082

#### 4. Start the Frontend

Navigate to the `client` directory and start the frontend:

```bash
cd ../client
npm install
npm run dev
```

The frontend will run on http://localhost:3000.

---

## Project Structure

- `client/`: Contains the Next.js frontend application
- `server/`: Contains the Spring Boot backend application
- `common/`: TypeScript models shared between frontend and backend

---

## Project Requirements

- **Module 2**: Interactivity with a 2-dimensional array
    - In the `Playlist` class, there is a transient `songsArray` that syncs with the `songs` HashSet as songs are added
      and removed to the playlist. The array starts off with 10 rows and 3 columns, but will expand and shrink as it
      syncs with the
      HashSet:
        ```
        @Transient
        private String[][] songsArray = new String[10][3];
      
        public Playlist() {
            syncSongsArrayWithSongsSet();
        }
      
        private void syncSongsArrayWithSongsSet() {
            songsArray = new String[songs.size()][3];
            int i = 0;
            for (Song song : songs) {
                songsArray[i++] = new String[]{song.getName(), song.getArtist(), song.getGenre()};
            }
        }
        ```
- **Module 8**: Linked Data Structures and Recursion
    - There is a `LinkedPlaylist` class that extends the `LinkedList` class. Songs can be added to or removed from the
      LinkedPlaylist. It also supports advancing to the next or previous song.
- **Module 9**: Searching and Sorting
    - In the `LinkedPlaylist`, one method allows for searching for a song by name, and another allows for sorting the
      songs by name:

      ```
      public Song findSongByName(String name) {
          for (Song song : this) {
              if (song.getName().equalsIgnoreCase(name)) {
                  return song;
              }
          }
          return null;
      }
  
      public void sortByName() {
          List<Song> sortedList = new ArrayList<>(this);
          sortedList.sort(Comparator.comparing(Song::getName, String.CASE_INSENSITIVE_ORDER));
          this.clear();
          this.addAll(sortedList);
      }
      ```

- **Module 10**: Databases
    - Song and Playlist Spring JPA Entities are hosted by repositories with a PostgreSQL database.
- **Module 11**: Concurrency
    - Spring Boot uses an embedded Tomcat server by default. I created a `TomcatConfiguration` class and added multiple
      connectors (ports) to the same Tomcat server so that all ports (8080, 8081, and 8082) share the same application
      context.
- **Module 12**: Stream API and Lambdas
    - In the `PlaylistService` class, there is a method that filters the given playlist's songs by genre, and then sorts
      the songs
      by name:
      ```
      public List<Song> filterAndSortSongsByGenre(Long playlistId, String genre) {
          Playlist playlist = playlistRepository.findById(playlistId)
                  .orElseThrow(() -> new PlaylistNotFoundException("Playlist not found"));
  
          return playlist.getSongs().stream()
                  .filter(song -> song.getGenre().equalsIgnoreCase(genre))
                  .sorted((song1, song2) -> song1.getName().compareToIgnoreCase(song2.getName()))
                  .collect(Collectors.toList());
      }
      ```
