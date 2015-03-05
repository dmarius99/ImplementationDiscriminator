package any.ejbtest;

import com.dms.Discriminator;

import java.util.List;

/**
 * MoviesImplEjb, 02.03.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */
//@Named
@Discriminator(isResultAggregated = true)
public class MoviesImplWrapper implements Movies {

    private Movies movies;

    public void setMovies(Movies movies) {
        this.movies = movies;
    }

    public Movies getMovies() {
        return movies;
    }

    @Override
    public void addMovie(Movie movie) throws Exception {
        movies.addMovie(movie);
    }

    @Override
    public void deleteMovie(Movie movie) throws Exception {
        movies.deleteMovie(movie);
    }

    @Override
    public List<Movie> getAllMovies() throws Exception {
        return movies.getAllMovies();
    }
}