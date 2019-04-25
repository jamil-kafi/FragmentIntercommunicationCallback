package jk.android.intercominterface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.FIELD})
public @interface ComInterface {

    /**
     * The ID of the container layout to be used when switching fragments
     * @return
     */
    int containerId();

    /**
     *
     * @return
     */
    int enterAnim() default -1;

    /**
     *
     * @return
     */
    int exitAnim() default -1;

    /**
     *
     * @return
     */
    int popEnterAnim() default -1;

    /**
     *
     * @return
     */
    int popExitAnim() default -1;

}
