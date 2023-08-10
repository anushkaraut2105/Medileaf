package com.megaproject.medileaf;
import java.io.Serializable;

public class Leaf implements Serializable {


    private final String leafName;
    private final String scientificName;
    private final String description;
    private final String usage;
    private final String origin;
    private final String feature;

    public Leaf(String leafName, String scientificName, String description, String usage, String origin, String feature) {
        this.leafName = leafName;
        this.scientificName = scientificName;
        this.description = description;
        this.usage = usage;
        this.origin = origin;
        this.feature = feature;
    }

    public String getLeafName() {
        return leafName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getOrigin() {
        return origin;
    }

    public String getFeature() {
        return feature;
    }

}
