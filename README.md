# Modus Technical Test

To complete this technical test follow the instructions to implement the features that are missing from this simple application.

The overall functionality of the application is to display a gallery of popular movies fetched from The Movie DB

## Fetching Data

   - Implement the empty methods in ‘MovieApi.kt’
   - Implement the empty methods in ‘MovieRepository’
   - Use the following endpoint for ‘getPopularMoviesAsync()’:
      https://developers.themoviedb.org/3/movies/get-popular-movies
   You can use the following API Key: bf718d4dd8b23985d9c3edbcfd440a27

## Displaying Data

   - Implement the missing methods in ‘GalleryViewModel’ in order to display a gallery of movies
   - Show a loading view/animation while the data is being loaded
   - Movies should be displayed in two columns
   - The height of each cell should be 1.5x its width
   - Spacing between items and margins to the edges of the screen should be the same
   - Tapping on a movie should show a Snackbar with the name of the movie.
   - Display the following:
       - Title
       - Overview (limited to 3 lines)
       - Poster
   - Highlight movies with a score of over 8 in any way you want (i.e. a star icon, a colored border, etc.)

