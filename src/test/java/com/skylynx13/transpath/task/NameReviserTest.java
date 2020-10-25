package com.skylynx13.transpath.task;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NameReviserTest {
    private final NameReviser nameReviser = new NameReviser();
    private static final String TEST_RENAMELIST = "src/test/resources/rename.list";
    private final String[][] replaceTemplates = {
            {"\\+", " "},
            {"%28", "("},
            {"%29", ")"},
            {"%23", "#"},
            {"_2C ", ", "},
            {"_\\.", ")."},
            {"___", ") ("},
            {"__", " ("},
            {"_", " "},
            {"\\(digital\\)", "(Digital)"},
            {"\\(", " ("},
            {"  \\(", " ("},
            {" Vol\\. ", " v0"},
            {" Vol\\.", " v0"},
            {" Vol ", " v0"},
            {" v00", " v0"},
            { "#", "" },
            { " v1 ", " v01 " },
            { " v2 ", " v02 " },
            { " v3 ", " v03 " },
            { " v4 ", " v04 " },
            { " v5 ", " v05 " },
            { " v6 ", " v06 " },
            { " Of ", " of " },
            { " of The", " of the" },
            { " From ", " from " },
            { " And ", " and " },
            { " VS ", " vs " },
            { " Vs ", " vs " },
            { " Vs\\. ", " vs " },
            { " vs\\. ", " vs " },
            { "\\.zip|\\.ZIP", ".cbz" },
            { "\\.rar|\\.RAR", ".cbr" },
            { "\\.CBZ", ".cbz" },
            { "\\.CBR", ".cbr" },
            { "([-+'A-Za-z0-9 ]+) (\\d{2}) ([-A-Za-z0-9 \\(\\)\\.]+)", "$1 0$2 $3" }
    };

    @Before
    public void setup() {

    }

    @Test
    public void testGetReplacedName() {
        assertEquals("", nameReviser.getReplacedName(replaceTemplates, ""));
        assertEquals("Abcde fg Hijklmn 007 (of 05) (2017) (Digital) (opq-rst).cbr", nameReviser.getReplacedName(replaceTemplates, "Abcde fg Hijklmn 07 (of 05) (2017) (digital) (opq-rst).CBR"));
        assertEquals("Abcde fg Hijklmn 007 (of 05) (2017) (Digital) (opq-rst).cbr", nameReviser.getReplacedName(replaceTemplates, "Abcde+fg+Hijklmn+07+(of+05)+(2017)_(digital)_%28opq-rst%29.rar"));
        assertEquals("Abcde and fg from of the vs Hijklmn v06 v02 007 (of 05) (2017) (Digital) (opq-rst).cbz", nameReviser.getReplacedName(replaceTemplates, "Abcde And fg From Of The Vs. Hijklmn v6 Vol.2 #07 (of 05) (2017) (digital) (opq-rst).ZIP"));
    }

    @Test
    public void testGetReplaceNameByReplaceList() {
        String[][] repList = NameReviser.readRenameList(TEST_RENAMELIST);
        assertNotNull(repList);
        assertEquals("", nameReviser.getReplacedName(repList, ""));
        assertEquals("Abcde fg Hijklmn 007 (of 05) (2017) (Digital) (opq-rst).cbr", nameReviser.getReplacedName(repList, "Abcde fg Hijklmn 07 (of 05) (2017) (digital) (opq-rst).CBR"));
        assertEquals("Abcde fg Hijklmn 007 (of 05) (2017) (Digital) (opq-rst).cbr", nameReviser.getReplacedName(repList, "Abcde+fg+Hijklmn+07+(of+05)+(2017)_(digital)_%28opq-rst%29.rar"));
        assertEquals("Abcde and fg from of the vs Hijklmn v06 v02 007 (of 05) (2017) (Digital) (opq-rst).cbz", nameReviser.getReplacedName(repList, "Abcde And fg From Of The Vs. Hijklmn v6 Vol.2 #07 (of 05) (2017) (digital) (opq-rst).ZIP"));
    }
}
