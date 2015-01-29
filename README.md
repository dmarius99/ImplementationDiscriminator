# ImplementationDiscriminator
A java solution for multiple inheritance using annotation in Spring containers. Manages in run-time multiple implementations based on a interface and a discriminator parameter.It is a annotation to be used in Spring applications. Is used to specify the value of the Discriminator method parameter for implementations of the given interface. The Discriminator annotation can only be specified on concrete classes having the same interface. The Discriminator annotation must have a default implementation and specify if the methods that return Collections should be aggregated or not.The discriminator value should be implemented in a separate bean that implements DiscriminatorImplementation.