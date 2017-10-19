/**
 * Copyright (c) 2015,qxu. 
 * All Rights Reserved.
 * 
 * Project Name:transpath
 * Package Name:com.qxu.transpath.worker
 * File Name:ArrangerTest.java
 * Date:2015-2-17 上午10:13:07
 * 
 */
package com.libra42.transpath.task;

import static org.junit.Assert.*;

import org.junit.Test;

import com.libra42.transpath.task.Arranger;
import com.libra42.transpath.task.CdEntry;
import com.libra42.transpath.utils.FileUtils;

 /**
 * ClassName: ArrangerTest <br/>
 * Description: TODO <br/>
 * Date: 2015-2-17 上午10:13:07 <br/>
 * <br/>
 * 
 * @author qxu@
 * 
 * Change Log:
 * @version yyyy-mm-dd qxu@<br/>
 * 
 */

public class ArrangerTest {
    
    private static final String TEST_RESOURCES_LOC = "src/test/resources/";
    
    private String testResource(String fileName) {
        return (TEST_RESOURCES_LOC + fileName);
    }
    
    @Test
    public void appendToFileTest() {
        Arranger argr = new Arranger();
        int n = argr.readFromFile(testResource("ArrangerTest_raw.txt")).sort().merge().writeToFile(testResource("ArrangerTest_appendToFileTest.txt"));
        assertEquals(31, n);
        assertEquals(true, FileUtils.compareFileBytes(testResource("ArrangerTest_appendToFileTest.txt"), testResource("ArrangerTest_task_000.txt")));
        argr.clear();
        n = argr.readFromFile(testResource("ArrangerTest_raw.txt")).appendToFile(testResource("ArrangerTest_appendToFileTest.txt"));
        assertEquals(35, n);
        assertEquals(true, FileUtils.compareFileBytes(testResource("ArrangerTest_appendToFileTest.txt"), testResource("ArrangerTest_task_004.txt")));
    }

    @Test
    public void readSortMergeWriteTest_raw_task() {
        Arranger argr = new Arranger();
        int n = argr.readFromFile(testResource("ArrangerTest_raw.txt")).sort().merge().writeToFile(testResource("ArrangerTest_task.txt"));
        assertEquals(31, n);
        assertEquals(true, FileUtils.compareFileBytes(testResource("ArrangerTest_task.txt"), testResource("ArrangerTest_task_000.txt")));
    }

    @Test
    public void applyFilterTest_keywords_filter() {
        String[] keys = {".*123.*", ".*4.6.*"};
        CdEntry ent1 = new CdEntry("entry123 c2c");
        ent1.addComment("comm1");
        ent1.addLink("link1");
        CdEntry ent2 = new CdEntry("entry467 b2b");
        ent2.addComment("comm2");
        ent2.addLink("link2");
        ent2.addLink("link123/467:6666");
        CdEntry ent3 = new CdEntry("entry456 b2b");
        ent3.addComment("comm3");
        ent3.addLink("link3");
        CdEntry ent4 = new CdEntry("entry789 b2b");
        ent4.addComment("comm4");
        ent4.addLink("link://console4g6f.html");
        ent4.addLink("link4");
        CdEntry ent5 = new CdEntry("nomatch (yes it is)");
        ent5.addComment("comm5");
        ent5.addLink("link5");
        ent5.addLink("link55");
        Arranger arr1 = new Arranger();
        arr1.addEntry(ent1);
        arr1.addEntry(ent2);
        arr1.addEntry(ent3);
        arr1.addEntry(ent4);
        arr1.addEntry(ent5);
        
        CdEntry ent11 = new CdEntry("entry123 c2c");
        ent11.addComment("comm1");
        ent11.addLink("link1");
        CdEntry ent12 = new CdEntry("entry467 b2b");
        ent12.addComment("comm2");
        ent12.addLink("link2");
        ent12.addLink("link123/467:6666");
        CdEntry ent13 = new CdEntry("entry456 b2b");
        ent13.addComment("comm3");
        ent13.addLink("link3");
        CdEntry ent14 = new CdEntry("entry789 b2b");
        ent14.addComment("comm4");
        ent14.addLink("link://console4g6f.html");
        ent14.addLink("link4");
        Arranger arr11 = new Arranger();
        arr11.addEntry(ent11);
        arr11.addEntry(ent12);
        arr11.addEntry(ent13);
        arr11.addEntry(ent14);
 
        assertEquals(true, arr1.applyFilter(keys).equals(arr11));
    }
    @Test
    public void applyFilterTest_keywords_filter_upper_lower_cases() {
        String[] keys = {"123", "B2B"};
        CdEntry ent1 = new CdEntry("entry123 c2c");
        CdEntry ent2 = new CdEntry("entry467 B2B");
        CdEntry ent3 = new CdEntry("entry456 B2b");
        CdEntry ent4 = new CdEntry("entry789 b2b");
        CdEntry ent5 = new CdEntry("nomatch (yes it is)");
        Arranger arr1 = new Arranger();
        arr1.addEntry(ent1);
        arr1.addEntry(ent2);
        arr1.addEntry(ent3);
        arr1.addEntry(ent4);
        arr1.addEntry(ent5);
        
        CdEntry ent11 = new CdEntry("entry123 c2c");
        CdEntry ent12 = new CdEntry("entry467 B2B");
        CdEntry ent13 = new CdEntry("entry456 B2b");
        CdEntry ent14 = new CdEntry("entry789 b2b");
        Arranger arr11 = new Arranger();
        arr11.addEntry(ent11);
        arr11.addEntry(ent12);
        arr11.addEntry(ent13);
        arr11.addEntry(ent14);
 
        assertEquals(true, arr1.applyFilter(keys).equals(arr11));
    }
    
    @Test
    public void equalsTest_equals_entries() {
        Arranger firstArranger = new Arranger();
        Arranger secondArranger = new Arranger();
        firstArranger.addEntry("abc");
        secondArranger.addEntry("abc");
        assertEquals(true, firstArranger.equals(secondArranger));
        firstArranger.addEntry("def");
        secondArranger.addEntry("def");
        assertEquals(true, firstArranger.equals(secondArranger));
    }
    @Test
    public void equalsTest_not_equals_entry() {
        Arranger firstArranger = new Arranger();
        Arranger secondArranger = new Arranger();
        firstArranger.addEntry("abc");
        secondArranger.addEntry("def");
        assertEquals(false, firstArranger.equals(secondArranger));
    }
    @Test
    public void equalsTest_not_equals_entries() {
        Arranger firstArranger = new Arranger();
        Arranger secondArranger = new Arranger();
        firstArranger.addEntry("abc");
        secondArranger.addEntry("abc");
        assertEquals(true, firstArranger.equals(secondArranger));
        firstArranger.addEntry("def");
        secondArranger.addEntry("deg");
        assertEquals(false, firstArranger.equals(secondArranger));
    }
    @Test
    public void equalsTest_equals_comments_links() {
        Arranger firstArranger = new Arranger();
        Arranger secondArranger = new Arranger();
        CdEntry ent = new CdEntry("abc");
        ent.addComment("comm1");
        ent.addComment("comm2");
        ent.addLink("link1");
        ent.addLink("link2");
        firstArranger.addEntry(ent);
        CdEntry ent1 = new CdEntry("abc");
        ent1.addComment("comm1");
        ent1.addComment("comm2");
        ent1.addLink("link1");
        ent1.addLink("link2");
        secondArranger.addEntry(ent1);
        assertEquals(true, firstArranger.equals(secondArranger));
    }

    @Test
    public void equalsTest_not_equals_comments_links() {
        Arranger firstArranger = new Arranger();
        CdEntry ent = new CdEntry("abc");
        ent.addComment("comm1");
        ent.addComment("comm2");
        ent.addLink("link1");
        ent.addLink("link2");
        firstArranger.addEntry(ent);
        CdEntry ent1 = new CdEntry("abc");
        ent1.addComment("comm1");
        ent1.addComment("comm2");
        ent1.addLink("link2");
        ent1.addLink("link1");
        Arranger secondArranger = new Arranger();
        secondArranger.addEntry(ent1);
        assertEquals(false, firstArranger.equals(secondArranger));
        CdEntry ent2 = new CdEntry("abc");
        ent2.addComment("comm2");
        ent2.addComment("comm1");
        ent2.addLink("link1");
        ent2.addLink("link2");
        Arranger thirdArranger = new Arranger();
        thirdArranger.addEntry(ent2);
        assertEquals(false, firstArranger.equals(thirdArranger));
    }

}
