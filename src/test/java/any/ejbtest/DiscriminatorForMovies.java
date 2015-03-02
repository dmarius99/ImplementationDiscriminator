package any.ejbtest;

import com.dms.DiscriminatorImplementation;

import javax.inject.Named;

/**
 * DiscriminatorForMovies, 26.02.2015
 *
 * Copyright (c) 2014 1&1 Internet AG. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */
@Named
public class DiscriminatorForMovies extends DiscriminatorImplementation<Movie, Movies> {

    @Override
    public Movies getImplementationForDiscriminator(Movie parameter) {
        if (parameter.getYear() < 1995) {
            return getImplementations().get(MoviesImpl.class.getName());
        } else {
            return getImplementations().get(MoviesImplLegacy.class.getName());
        }
    }
}
