package com.ys.hsm.society.config;

import com.ys.hsm.society.enums.StateCode;
import org.springframework.stereotype.Component;

@Component
public class StateCodeResolver {

    public String resolve(String stateName) {

        return java.util.Arrays.stream(StateCode.values())
                .filter(state -> state.getStateName()
                        .equalsIgnoreCase(stateName.trim()))
                .map(StateCode::getCode)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported state: " + stateName));
    }
}
