package com.megaproject.medileaf;

import android.util.JsonReader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DataHelper {


    public Leaf leaf;

    public DataHelper(JsonReader reader, String leafName) throws IOException {

        try {
            List<Leaf> leaves = getLeaf(reader);

            for (Leaf leaf: leaves) {
                if (leaf.getLeafName().equals(leafName)) {
                    this.leaf = leaf;
                    break;
                }
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } finally {
            reader.close();
        }
    }

    private List<Leaf> getLeaf(JsonReader reader) throws IOException, URISyntaxException {

        List<Leaf> leaves = new ArrayList<>();


        reader.beginArray();
        while (reader.hasNext()) {
            leaves.add(readLeaf(reader));
        }
        reader.endArray();
        return leaves;
    }

    private Leaf readLeaf(JsonReader reader) throws IOException {
        String leafName = null, scientificName = null, description = null, usage = null;
        String origin = null, feature = null;

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();

            switch (name) {
                case "leafname":
                    leafName = reader.nextString();
                    break;
                case "sciname":
                    scientificName = reader.nextString();
                    break;
                case "description":
                    description = reader.nextString();
                    break;
                case "usage":
                    usage = reader.nextString();
                    break;
                case "origin":
                    origin = reader.nextString();
                    break;
                case "feature":
                    feature = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return new Leaf(leafName, scientificName, description, usage, origin, feature);
    }

}
