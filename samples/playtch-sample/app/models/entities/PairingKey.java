package models.entities;

import play.data.validation.Constraints.*;

import javax.validation.Valid;

public class PairingKey {

    @Required
    @MinLength(value = 6)
    public String key;

    public PairingKey() {}

    public PairingKey(String key) {
        this.key = key;
    }
    
}