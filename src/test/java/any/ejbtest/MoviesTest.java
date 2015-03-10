/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package any.ejbtest;

import com.dms.DiscriminatorConfiguration;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class MoviesTest {

    public static final Movie MOVIE1 = new Movie("Quentin Tarantino", "Reservoir Dogs", 1992);
    public static final Movie MOVIE2 = new Movie("Joel Coen", "Fargo", 1996);
    public static final Movie MOVIE3 = new Movie("Joel Coen", "The Big Lebowski", 1998);

    private Movies moviesLocal;
    private Movies moviesLegacyLocal;
    private Context context;

    @Before
    public void initContexts() throws Exception {
        initEjbContext();
        initDataForEjbContext();
    }

    private void initEjbContext() throws Exception {
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.core.LocalInitialContextFactory");
        p.put("openejb.deployments.classpath.ear", "true");
        context = new InitialContext(p);

        moviesLocal = (Movies) context.lookup("MoviesImplLocal");
        moviesLegacyLocal = (Movies) context.lookup("MoviesImplLegacyLocal");
    }

    private void initDataForEjbContext() throws Exception {
        moviesLocal.addMovie(MOVIE1);
        moviesLocal.addMovie(MOVIE2);
        moviesLegacyLocal.addMovie(MOVIE3);

        assertEquals("moviesLocal.getAllMovies()", 2, moviesLocal.getAllMovies().size());
        assertEquals("moviesLegacyLocal.getAllMovies()", 1, moviesLegacyLocal.getAllMovies().size());
    }

    @Test
    @Ignore
    public void testInEjbContext() throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/ejb-context.xml");
        applicationContext.refresh();

        Movies moviesDefault = (Movies) applicationContext.getBean("moviesImpl");
        Movies moviesLegacy = (Movies) applicationContext.getBean("moviesImplLegacy");

        System.out.println("Default impl: ");
        for (Movie movie:moviesDefault.getAllMovies()) {
            System.out.println(movie);
        }
        assertEquals("moviesDefault : ", 2, moviesDefault.getAllMovies().size());

        System.out.println("legacy impl: ");
        for (Movie movie:moviesLegacy.getAllMovies()) {
            System.out.println(movie);
        }
        assertEquals("moviesLegacy : ", 1, moviesLegacy.getAllMovies().size());

        // start discriminator
        Movies moviesDiscriminatedInSpring = (Movies) DiscriminatorConfiguration.discriminateUsing(
                DiscriminatorForMovies.class,
                MoviesImplWrapper.class, MoviesImplLegacyWrapper.class);


        //get any spring bean annotated with @Discriminated or default bean
        Movies moviesBean = moviesDiscriminatedInSpring;
        //(Movies) applicationContext.getBean("moviesImplWrapper");

        for (Movie movie:moviesBean.getAllMovies()) {
            System.out.println(movie);
        }
        assertEquals("moviesImplWrapper : ", 3, moviesBean.getAllMovies().size());
    }
}
