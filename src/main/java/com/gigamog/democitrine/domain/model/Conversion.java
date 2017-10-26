package com.gigamog.democitrine.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Conversion {
    private String name;
    private String symbol;
    private String type;
    private String si;
    private String unitConverstions;
    private String workingValue;
}
