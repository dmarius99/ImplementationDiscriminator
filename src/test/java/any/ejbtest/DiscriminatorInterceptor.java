package any.ejbtest;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

/**
 * DiscriminatorInterceptor, 02.03.2015
 *
 * Copyright (c) 2014 Marius Dinu. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */

public class DiscriminatorInterceptor {

    static final Logger LOG = Logger.getAnonymousLogger();

    @AroundInvoke
    public Object discriminate(InvocationContext invocationContext) throws Exception {
        LOG.info("************** DiscriminatorInterceptor **************");
        invocationContext.getMethod();
        invocationContext.getParameters();
        invocationContext.getTarget();
        return invocationContext.proceed();
    }
}
