package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ErrorPlaceHolders {

    private ErrorPlaceHolders() {
        throw new UnsupportedOperationException("Only use the static classes constructor");
    }

    public static Map<String, Object> of(Object... kv) {
        if ( kv.length % 2 != 0 ) {
            throw new IllegalArgumentException("ErrorPlaceHolders needs an even number of arguments");
        }
        Map<String, Object> map = new HashMap<>();
        for ( int i = 0; i < kv.length; i += 2 ) {
            map.put(String.valueOf(kv[i]), kv[i+1]);
        }
        return map;
    }

}
