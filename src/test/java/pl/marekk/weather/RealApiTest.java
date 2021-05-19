package pl.marekk.weather;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@EnabledIf(expression = "#{environment.acceptsProfiles('prod')}", loadContext = true)
// use for the integration tests with the real api - prod spring profile
public @interface RealApiTest {}
