package com.dms;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * DiscriminatorInitializer, 24.10.2014
 * <p/>
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */
abstract class DiscriminatorInitializer<DiscriminatorType, InterfaceType> extends MethodsHelper
        implements DiscriminatorInterface<DiscriminatorType, InterfaceType> {

    @Inject
    @Named("beanManager")
    private BeanManager beanManager;

    private boolean active = true;

    DiscriminatorInitializer() {
        super();
        super.setDiscriminatorClass(getTypeArgumentClass(0));
        super.setInterfaceClass(getTypeArgumentClass(1));
        super.init();
    }

    /**
     * Validates this bean instance in run time by checking that the required fields are not null
     */
    @PostConstruct
    protected void validate() {
        LOG.info("Validating @Discriminator usage");
        if (getImplementations() == null || getImplementations().size() == 0) {
            throwError(new IllegalStateException("No implementations specified"));
        }
    }

    /**
     * @param typeArgumentIndex the argument position
     * @return the argument class type
     */
    private Class getTypeArgumentClass(int typeArgumentIndex) throws ClassCastException {
        Type type = getClass().getGenericSuperclass();
        if (!(type instanceof ParameterizedType)) {
            type = ((Class) type).getGenericSuperclass();
        }
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getActualTypeArguments()[typeArgumentIndex];
        } else {
            throwError(new RuntimeException("Wrong proxy configuration or usage for Parametrized types"));
        }
        return null;
    }

    @Override
    public Map<String, InterfaceType> getImplementations() {
        return getBeanManager().getImplementationBeans();
    }

    boolean isResultAggregated() {
        return getBeanManager().isResultAggregated();
    }

    public boolean isActive() {
        return active;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    BeanManager getBeanManager() {
        return beanManager;
    }

    protected void setBeanManager(BeanManager beanManager) {
        this.beanManager = beanManager;
    }
}
