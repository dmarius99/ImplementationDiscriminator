package any.ejbtest;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

/**
 * LegacyMovies, 26.02.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */
@Stateless(name = "MoviesImplLegacy")
//@Discriminated
//@Interceptors(SpringBeanAutowiringInterceptor.class)
//@Named
//@Interceptors(DiscriminatorInterceptor.class)
public class MoviesImplLegacy implements Movies {

    private List<Movie> entityManager = new ArrayList<>();

    public void addMovie(Movie movie) throws Exception {
        entityManager.add(movie);
    }

    public void deleteMovie(Movie movie) throws Exception {
        entityManager.remove(movie);
    }

    public List<Movie> getAllMovies() throws Exception {
        return entityManager;
    }

}
