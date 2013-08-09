package spring.reference;

import spring.reference.meta.ExternalDependency;
import spring.reference.meta.JBOSS_AS7;
import spring.reference.meta.POI;
import spring.reference.meta.POITag;

public final class ApplicationPoi {
    private ApplicationPoi() {
    }

    @JBOSS_AS7
    @POI(tags = { POITag.JSON }, value = "Had to add jackson-jaxrs to pom to be able to use json (de)serializers.")
    public static final String JSON_SERIALIZER_DEPENDENCY = "jackson-jaxrs";

    @ExternalDependency
    @JBOSS_AS7
    @POI(tags = { POITag.DATASOURCE },
        value = "JbossAS7 will look for datasources in *-ds.xml files on the classpath. This is not recommended for production environments though.")
    public static final String DATASOURCE = "admin-ds.xml";

    @ExternalDependency
    @JBOSS_AS7
    @POI(
        tags = { POITag.DATASOURCE },
        value = "A quote from a Spring forum: ''Spring's AOP runtime tries to get information on class 'org.jboss.jca.adapters.jdbc.WrapperDataSource' but cannot load it from WAR's module classloader. The object you (I) have obtained from JNDI is JBoss' DataSource with the class loaded from one of JBoss' modules, but this module isn't available to your (our) WAR. My 'hack' exposes the required class to WAR and Spring's AOP infrastructure (actually AspectJ).''")
    public static final String DATASOURCE_FOR_SPRING_AOP = "jboss-deployment-structure";

    @ExternalDependency
    @POI(
        tags = { POITag.TRANSACTION, POITag.BE_CAREFUL },
        value = "With JTA, had to set hibernate property 'hibernate.transaction.manager_lookup_class' in the persistence.xml. Probably there's a way to acquire"
            + " an appropriate JTA transaction manager through Spring configuration as well.")
    public static final String TRANSACTION_MANAGER = "";

}
