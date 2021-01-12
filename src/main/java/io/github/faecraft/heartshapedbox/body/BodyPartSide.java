package io.github.faecraft.heartshapedbox.body;

import java.util.Locale;

interface lowerCaseEnumName {
    String lowerName();
}

public enum BodyPartSide implements lowerCaseEnumName {
    CENTER,
    LEFT,
    RIGHT;
    
    @Override
    public String lowerName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
