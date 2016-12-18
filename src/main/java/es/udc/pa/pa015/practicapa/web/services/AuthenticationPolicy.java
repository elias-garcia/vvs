package es.udc.pa.pa015.practicapa.web.services;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Authentication policy interface.
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthenticationPolicy {
  /**
   * This method value the authentication.
   * @return Authentication Policy type
   */
  AuthenticationPolicyType value() default AuthenticationPolicyType.ALL_USERS;
}
