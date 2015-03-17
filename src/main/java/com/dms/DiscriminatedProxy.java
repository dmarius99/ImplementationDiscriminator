package com.dms;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * DiscriminatedProxy, 11.03.2015
 *
 * Copyright (c) 2014 Marius Dinu. All rights reserved.
 *
 * @author mdinu
 * @version $Id$
 */

public class DiscriminatedProxy {

    public static void main(String[] args) {
        Class<?> interfaceClass = DiscriminatorInterface.class;
        DiscriminatorInterface discriminatorInterface = (DiscriminatorInterface)DiscriminatedProxy
                .createDiscriminatedProxy(interfaceClass);
        System.out.println("Proxy: " + discriminatorInterface);
    }

    protected static final <T> T createDiscriminatedProxy(Class<T> interfaceClass) {
        InvocationHandler handler = new MyInvocationHandler();
        Class proxyClass = Proxy.getProxyClass(
                interfaceClass.getClassLoader(), new Class[]{interfaceClass});
        Object object = null;
        try {
            object = proxyClass.
                    getConstructor(new Class[] { InvocationHandler.class }).
                    newInstance(new Object[] { handler });
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return (T)object;
    }

    static class MyInvocationHandler implements InvocationHandler {
        @Override
        public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
            //return method.invoke(o, objects);
            return "123";
        }
    }

}
