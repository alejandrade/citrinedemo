package com.gigamog.democitrine.domain.model;

import lombok.Getter;

@Getter
/**
 * our database
 */
public enum ConversionsTable {
    minute("min", "time", "s", "60s", "1"),
    hour("h", "time", "s", "3600s", "1"),
    day("d", "time", "s", "86400s", "1"),
    degree2("'", "Plane angle", "rad", "(π / 180) rad", "0.00029088820867"),
    degree("°", "Plane angle", "rad", "(π / 10800) rad", "0.00029088820867"),
    second("\"", "Plane angle", "rad", "(π / 648000) rad", "0.00029088820867"),
    hectare("ha", "area", "m^2", "10000 m^2", "10000"),
    litre("L", "volume", "m^3", "0.001 m^3", "0.001"),
    tonne("t", "mass", "kg", "10^3 kg", "1000");

    private String symbol;
    private String type;
    private String si;
    private String unitConversions;
    private String workingValue;

    ConversionsTable(String symbol, String type, String si, String unitConversions, String workingValue) {
        this.symbol = symbol;
        this.type = type;
        this.si = si;
        this.unitConversions = unitConversions;
        this.workingValue = workingValue;
    }

}
