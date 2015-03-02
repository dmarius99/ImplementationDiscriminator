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

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

//START SNIPPET: code
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"/ejb-context.xml"})
public class MoviesTest {

    //@EJB
    private Movies moviesLocal;
    private Movies moviesLegacyLocal;

//    @Inject
//    @Named("moviesImpl")
    Movies moviesDiscriminated;

    private Context context;

    @Before
    public void initContexts() throws Exception {
        initEjbContext();
        //initSpringContext();
        initDataForEjbContext();
    }

    private void initDataForEjbContext() throws Exception {
        moviesLocal.addMovie(new Movie("Quentin Tarantino", "Reservoir Dogs", 1992));
        moviesLocal.addMovie(new Movie("Joel Coen", "Fargo", 1996));
        moviesLegacyLocal.addMovie(new Movie("Joel Coen", "The Big Lebowski", 1998));
//
//        List<Movie> list = moviesLocal.getMovies();
//        assertEquals("List.size()", 3, list.size());

//        for (Movie movie : list) {
//            movies.deleteMovie(movie);
//        }
//        Iterator<Movie> iterator = list.iterator();
//        while(iterator.hasNext()) {
//            System.out.println(iterator.next());
//            iterator.remove();
//        }
        assertEquals("Movies.getMovies()", 2, moviesLocal.getMovies().size());
        assertEquals("Movies.getMovies()", 1, moviesLegacyLocal.getMovies().size());
    }

    private void initEjbContext() throws Exception {
        Properties p = new Properties();
        p.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.openejb.core.LocalInitialContextFactory");

        p.put("openejb.deployments.classpath.ear", "true");

//        p.put("movieDatabase", "new://Resource?type=DataSource");
//        p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
//        p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
//
//        p.put("movieDatabaseUnmanaged", "new://Resource?type=DataSource");
//        p.put("movieDatabaseUnmanaged.JdbcDriver", "org.hsqldb.jdbcDriver");
//        p.put("movieDatabaseUnmanaged.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
//        p.put("movieDatabaseUnmanaged.JtaManaged", "false");

        context = new InitialContext(p);

        moviesLocal = (Movies) context.lookup("MoviesImplLocal");
        moviesLegacyLocal = (Movies) context.lookup("MoviesImplLegacyLocal");
    }

//    private void initSpringContext() {
//        //ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/ejb-context.xml");
//        moviesDiscriminated = (Movies) DiscriminatorConfiguration.activateDiscriminator(
//                DiscriminatorForMovies.class,
//                MoviesImpl.class, MoviesImplLegacy.class);
//
//    }

    @Test
    public void testInEjbContext() throws Exception {
//        moviesLocal.addMovie(new Movie("Hannibal", "Hannibal", 1998));
//        assertEquals("List.size()", 1, moviesLocal.getMovies().size());
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/ejb-context.xml");
        applicationContext.refresh();

        Movies moviesDiscriminated = (Movies) applicationContext.getBean("moviesImpl");
        Movies moviesDefault = (Movies) applicationContext.getBean("moviesImpl");
        Movies moviesLegacy = (Movies) applicationContext.getBean("moviesImplLegacy");

//        Movies moviesDiscriminated = (Movies) DiscriminatorConfiguration.activateDiscriminator(
//                DiscriminatorForMovies.class,
//                MoviesImpl.class, MoviesImplLegacy.class);

        System.out.println("Default impl: ");
        for (Movie movie:moviesDefault.getMovies()) {
            System.out.println(movie);
        }
        System.out.println("legacy impl: ");
        for (Movie movie:moviesLegacy.getMovies()) {
            System.out.println(movie);
        }
        assertEquals("Movies.getMovies()", 3, moviesDiscriminated.getMovies().size());
    }
}
//END SNIPPET: code
