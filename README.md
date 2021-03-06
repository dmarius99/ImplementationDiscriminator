# ImplementationDiscriminator  <img src="https://travis-ci.org/dmarius99/ImplementationDiscriminator.svg?branch=master">
-------

## Description/Definition
-------
A java solution for multiple inheritance or multiple implementations management using discrimination of types.
Manages in run-time multiple implementations based on a interface and a discriminator parameter.
It is a annotation to be used in Java applications.
Is used to specify the value of the Discriminator method parameter for implementations of the given interface.
The Discriminated annotation can only be specified on concrete classes having the same interface.
The Discriminated annotation can specify if the methods that return Collections should be aggregated or not.
The discriminated value should be implemented in a separate bean that implements DiscriminatorImplementation.
Example:

Wanted:

		public class ResultedImplementations
				extends IntegerMathOperations<Integer>, LongMathOperations<Long>
				implements MathOperations<Number> {
		}

but unfortunately multiple inheritance is not available in java. So, I got :)

        MathOperations mathOperations = (MathOperations)DiscriminatorConfiguration.discriminateDefault(
                        Number.class, MathOperations.class,
                        IntegerMathOperations.class, LongMathOperations.class);

Here are 2 MathOperations implementations as example:

        @Discriminated
        public class IntegerMathOperations implements MathOperations<Integer> {
            @Override
            public Integer plus(Integer number1, Integer number2) {
                return number1+number2;
            }
        }

        @Discriminated
        public class LongMathOperations implements MathOperations<Long> {
            @Override
            public Long plus(Long number1, Long number2) {
                return number1+number2;
            }
        }

		public interface MathOperations<T extends Number> {
    		T plus(T number1, T number2);
		}

## Features

* is a particular case of multiple inheritance
* is a default type dispatcher with annotations in runtime
* validates the usage of it (validates the @Discriminated annotation)
* works in java 7/8 applications
* very little overhead (less than 1 ms/call)
* works in synchronous programming and asynchronous programming

## Overriding default mechanism

The default parameter type discrimination can be overridden by overriding the getImplementationForDiscriminator method
from class DiscriminatorImplementation.
In order to specify which of the implementations to be used the method
getImplementations(String classNameAsMapKey) will be used.
The method getImplementations contains a map of all the implementations found for their common interface.
The key map will be the implementation class name. All the implementations present in the map verify these assumptions:
 - the implementation is defined as a Spring bean
 - the implementation implements the interface defined as a typed parameter
 - the implementation has the @Discriminated annotation
If several implementations have the annotation attribute "isResultAggregated" than the logical operator "OR" will be
used between the boolean values.

Second usage might be more intuitive for some: add the com.dms package import in context.xml:

		<context:component-scan base-package="com.dms"/>

Within the code nothing changes as long as you inject the interface and specify the default implementation
using the injected Spring bean:

	    @Inject
	    @Named(value = "integerMathOperations")
    	private MathOperations mathOperations;

		Named
		public class DiscriminatorExampleForNumbers extends DiscriminatorImplementation<Number, MathOperations> {

			@Override
			public MathOperations getImplementationForDiscriminator(Number parameter) {
				if (parameter.longValue() < Integer.MAX_VALUE) {
					return getImplementations().get(IntegerMathOperations.class.getName());
				} else {
					if (parameter.longValue() > Integer.MAX_VALUE) {
						return getImplementations().get(LongMathOperations.class.getName());
					} else {
						return null;
					}
				}
			}

		}
The above interface will be intercepted and will apply the @Discriminated on multiple implementations.

An example of using 3 implementations with default type discrimination:

		MathOperations mathOperations = (MathOperations)DiscriminatorConfiguration.discriminateDefault(
                Number.class, MathOperations.class,
                IntegerMathOperations.class, LongMathOperations.class, AtomicIntegerMathOperations.class);
