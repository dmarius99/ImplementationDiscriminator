# ImplementationDiscriminator
A java solution for multiple inheritance and multiple implementation injection using annotation in Spring containers.
Manages in run-time multiple implementations based on a interface and a discriminator parameter.
It is a annotation to be used in Java applications.
Is used to specify the value of the Discriminator method parameter for implementations of the given interface.
The Discriminator annotation can only be specified on concrete classes having the same interface.
The Discriminator annotation can specify if the methods that return Collections should be aggregated or not.
The discriminator value should be implemented in a separate bean that implements DiscriminatorImplementation.
Example:

MathOperations mathOperations = (MathOperations)new DiscriminatorConfiguration<Number, MathOperations>()
        .discriminateDefault(Number.class, MathOperations.class,
        IntegerMathOperations.class, LongMathOperations.class);

@Discriminator
public class IntegerMathOperations implements MathOperations<Integer> {

    @Override
    public Integer plus(Integer number1, Integer number2) {
        return number1+number2;
    }
}

@Discriminator
public class LongMathOperations implements MathOperations<Long> {

    @Override
    public Long plus(Long number1, Long number2) {
        return number1+number2;
    }
}

In order to specify which of the implementations to be used the method
getImplementations(String classNameAsMapKey) will be used.
The method getImplementations contains a map of all the implementations found for their common interface.
The key map will be the implementation class name. All the implementations present in the map verify these assumptions:
 - the implementation is defined as a Spring bean
 - the implementation implements the interface defined as a typed parameter
 - the implementation has the @Discriminator annotation
If several implementations have the annotation attribute "isResultAggregated" than the logical operator "OR" will be
used between the boolean values.
