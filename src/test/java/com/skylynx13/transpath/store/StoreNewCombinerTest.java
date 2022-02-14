package com.skylynx13.transpath.store;

import com.skylynx13.transpath.error.StoreListException;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StoreNewCombinerTest {

    private final StoreNewCombiner storeNewCombiner = new StoreNewCombiner(false);

    @Test
    public void buildListTest() {
        try {
            assertNull(storeNewCombiner.buildList("A1111/B2222,B3333,B4444,B5555a"));
        } catch (StoreListException e) {
            assertEquals("Error branch path.", e.getMessage());
        }
        try {
            assertNull(storeNewCombiner.buildList("A1111/B2222,B3333-1111,B4444,B5555"));
        } catch (StoreListException e) {
            assertEquals("Error branch group.", e.getMessage());
        }
        try {
            assertEquals("[/]", storeNewCombiner.buildList("/").toString());
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
                    storeNewCombiner.parseList("A1111/B2222,A3333/B4444-4455,B6666-6677,B8888")
                            .toString());
        } catch (StoreListException ignored) {
        }
    }

    @Test
    public void getReservedNewListTest_Normal()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StoreList newList = new StoreList("src/test/resources/NewList_Normal.txt");
        newList.recap();
        System.out.println("newList:");
        System.out.println(newList);
        StoreList dupList = new StoreList();
        StoreList remList = new StoreList();
        Method method = StoreNewCombiner.class.getDeclaredMethod(
                "getReservedNewList", StoreList.class, StoreList.class, StoreList.class);
        method.setAccessible(true);
        StoreList resNewList = (StoreList) method.invoke(storeNewCombiner, newList, dupList, remList);
        System.out.println("resNewList:");
        System.out.println(resNewList);
        assertEquals(6905, newList.getStoreSize());
        assertEquals(12, newList.size());
        assertEquals(0, dupList.size());
        assertEquals(0, remList.size());
        assertEquals(6905, resNewList.getStoreSize());
        assertEquals(12, resNewList.size());
    }

    @Test
    public void getReservedNewListTest_Empty()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StoreList newList = new StoreList();
        newList.recap();
        System.out.println("newList:");
        System.out.println(newList);
        StoreList dupList = new StoreList();
        StoreList remList = new StoreList();
        Method method = StoreNewCombiner.class.getDeclaredMethod(
                "getReservedNewList", StoreList.class, StoreList.class, StoreList.class);
        method.setAccessible(true);
        StoreList resNewList = (StoreList) method.invoke(storeNewCombiner, newList, dupList, remList);
        System.out.println("resNewList:");
        System.out.println(resNewList);
        assertEquals(0, newList.getStoreSize());
        assertEquals(0, newList.size());
        assertEquals(0, dupList.size());
        assertEquals(0, remList.size());
        assertEquals(0, resNewList.getStoreSize());
        assertEquals(0, resNewList.size());
    }

    @Test
    public void getReservedNewListTest_Duplicate()
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        StoreList newList = new StoreList("src/test/resources/NewList_Duplicate.txt");
        newList.recap();
        System.out.println("newList:");
        System.out.println(newList);
        StoreList dupList = new StoreList();
        StoreList remList = new StoreList();
        Method method = StoreNewCombiner.class.getDeclaredMethod(
                "getReservedNewList", StoreList.class, StoreList.class, StoreList.class);
        method.setAccessible(true);
        StoreList resNewList = (StoreList) method.invoke(storeNewCombiner, newList, dupList, remList);
        System.out.println("resNewList:");
        System.out.println(resNewList);
        assertEquals(7353, newList.getStoreSize());
        assertEquals(12, newList.size());
        assertEquals(5, dupList.size());
        assertEquals(3, remList.size());
        assertEquals(5330, resNewList.getStoreSize());
        assertEquals(9, resNewList.size());
    }

}
