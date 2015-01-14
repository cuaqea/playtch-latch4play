package actions;

import latchid.LatchIdFactory;
import latchid.ObtainLatchId;
import play.mvc.With;

import java.lang.annotation.*;


/**
 * Created by Enrique Ismael Mendoza Robaina on 12/01/15.
 */

@With(LatchCheckStatusAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface LatchCheckStatus {

    /**
     * value indicates a Latch operation id
     */
    public String value() default " ";

    /**
     * latchId is a reflexive class where you have to declare a method that returns a user accountId returned when he
     * or she paired the account with Latch
     */
    public Class<? extends ObtainLatchId> latchId() default LatchIdFactory.class;

}