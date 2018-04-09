package com.xiaoyong.download.mmjpg;

import org.junit.Before;
import org.junit.Test;

/**
 * @author : XiaoYong
 * @date : 2018/4/9 8:35
 * Description    :
 */
public class HtmlParserTest {

    private HtmlParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new HtmlParser();
    }

    @Test
    public void testCatchMMJpg() throws Exception {

        parser.catchMMJpg(1307, 1307);

    }
}