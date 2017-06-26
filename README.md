# Udaflux

This is an App done on my way to be certified through the Udacity (www. https://www.udacity.com/) Fast Track.

The app is a movie app, using theMovieDb API and that let you see:

- The last 20 top movies

- The 20 favorites movies 

- Create your own favorites list

# Install

- First your need an API key go to this link https://www.themoviedb.org/account/signup 

  and create an account and generate an key.
  
  

- Then you open gradle.properties files and add this line :

# MyTheMovieDBApiToken="YOUR_API_KEY"

- The last step is to had to app.graddle this line:

# in the buildType section
buildType{
  ...
  buildTypes.each {
        it.buildConfigField 'String', 'THE_MOVIE_DB_API_TOKEN', MyTheMovieDBApiToken
    }
}
