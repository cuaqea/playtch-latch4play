package actions;

import pairingkey.ObtainPairingKey;
import pairingkey.PairingKeyFactory;
import play.mvc.With;

import java.lang.annotation.*;


/**
 * Created by Enrique Ismael Mendoza Robaina on 12/01/15.
 */

@With(LatchPairAction.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
@Documented
public @interface LatchPair {

    /**
     * pairingKey is a reflexive class where you have to declare a method that returns a pairing key value
     */
    public Class<? extends ObtainPairingKey> pairingKey() default PairingKeyFactory.class;

}