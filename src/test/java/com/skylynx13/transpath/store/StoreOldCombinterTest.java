package com.skylynx13.transpath.store;

import com.skylynx13.transpath.error.StoreListException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StoreOldCombinterTest {
    private final StoreOldCombiner storeOldCombiner = new StoreOldCombiner(false);

    @Test
    public void buildListTest() {
        try {
            assertNull(storeOldCombiner.buildList("A1111/B2222,B3333,B4444,B5555a"));
        } catch (StoreListException e) {
            assertEquals("Error branch path.", e.getMessage());
        }
        try {
            assertNull(storeOldCombiner.buildList("A1111/B2222,B3333-1111,B4444,B5555"));
        } catch (StoreListException e) {
            assertEquals("Error branch group.", e.getMessage());
        }
        try {
            assertEquals("[/]", storeOldCombiner.buildList("/").toString());
        } catch (StoreListException ignored) {
        }
    }

    @Test
    public void parseListTest() {
        try {
            assertEquals("[" +
                            "A1111/B2222, " +
                            "A3333/B4444, " +
                            "A3333/B4445, " +
                            "A3333/B4446, " +
                            "A3333/B4447, " +
                            "A3333/B4448, " +
                            "A3333/B4449, " +
                            "A3333/B4450, " +
                            "A3333/B4451, " +
                            "A3333/B4452, " +
                            "A3333/B4453, " +
                            "A3333/B4454, " +
                            "A3333/B4455, " +
                            "A3333/B6666, " +
                            "A3333/B6667, " +
                            "A3333/B6668, " +
                            "A3333/B6669, " +
                            "A3333/B6670, " +
                            "A3333/B6671, " +
                            "A3333/B6672, " +
                            "A3333/B6673, " +
                            "A3333/B6674, " +
                            "A3333/B6675, " +
                            "A3333/B6676, " +
                            "A3333/B6677, " +
                            "A3333/B8888]",
                    storeOldCombiner.parseList("A1111/B2222,A3333/B4444-4455,B6666-6677,B8888")
                            .toString());
        } catch (StoreListException ignored) {
        }
    }


}
