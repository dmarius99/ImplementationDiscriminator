package com.dms;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * DiscriminatorInitializer, 24.10.2014
 *
 * Created by Marius Dinu (marius.dinu@gmail.com) on 27/09/14.
 */

/**
 *
 * @param <DiscriminatorType> the class type of the parameter used in choosing the implementation
 * @param <InterfaceType> the class type of the interface implemented by the implementation beans
 */
abstract class DiscriminatorInitializer<DiscriminatorType, InterfaceType> extends MethodsHelper
        implements DiscriminatorInterface<DiscriminatorType, InterfaceType> {

    /**
     * Interface BeanManager is implemented for Spring container.
     */
    @Inject
    private BeanManager beanManager;

    /**
     * Default active value for the discriminator usage is true.
     * If the class gets initialized then I suppose it is enabled.
     */
    private boolean active = true;

    DiscriminatorInitializer() {
        super();
        super.setDiscriminatorClass(getTypeArgumentClass(0));
        super.setInterfaceClass(getTypeArgumentClass(1));
        super.init();
    }

    /**
     * Validates this bean instance in run time by checking that the required fields are not null.
     */
    @PostConstruct
    protected void validate() {
        LOG.info("Validating " + ANNOTATION_NAME + " usage");
        if (getImplementations() == null || getImplementations().size() == 0) {
            throwError(new IllegalStateException("No implementations specified"));
        }
    }

    /**
     * @param typeArgumentIndex the argument position
     * @return the argument class type
     */
    private Class getTypeArgumentClass(final int typeArgumentIndex) {
        Type type = getClass().getGenericSuperclass();
        return (Class) ((ParameterizedType) type).getActualTypeArguments()[typeArgumentIndex];
    }

    @Override
    public Map<String, InterfaceType> getImplementations() {
        return getBeanManager().getImplementationBeans();
    }

    protected boolean isResultAggregated() {
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

    protected void setBeanManager(final BeanManager beanManager) {
        this.beanManager = beanManager;
    }
}
