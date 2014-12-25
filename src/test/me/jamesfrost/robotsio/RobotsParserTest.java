package test.me.jamesfrost.robotsio;

import me.jamesfrost.robotsio.RobotsDisallowedException;
import me.jamesfrost.robotsio.RobotsParser;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * RobotsParser Tester.
 * <p/>
 * Currently, this is testing against the robots.txt file hosted at jamesfrost.me.
 * This could be improved by creating mock classes.
 * <p/>
 *
 * @author <James Frost>
 * @version 1.0
 * @since <pre>Dec 24, 2014</pre>
 */
public class RobotsParserTest {

    private ArrayList<String> disallowedPaths;

    @Before
    public void before() throws Exception {
        disallowedPaths = new ArrayList<String>();
        disallowedPaths.add("cgi-bin/");
        disallowedPaths.add("404.htm");
        disallowedPaths.add("CV.htm");
    }

    /**
     * Method: isAllowed(URL url)
     */
    @Test
    public void testIsAllowedUrl() throws Exception {
        RobotsParser robotsParser = new RobotsParser();
        robotsParser.connect("http://jamesfrost.me");

        assertTrue(robotsParser.isAllowed("http://jamesfrost.me/about.htm"));
        assertTrue(robotsParser.isAllowed(new URL("http://jamesfrost.me/about.htm")));

        assertFalse(robotsParser.isAllowed("http://jamesfrost.me/404.htm"));
        assertFalse(robotsParser.isAllowed(new URL("http://jamesfrost.me/404.htm")));
    }

    /**
     * Method: connect(String url)
     */
    @Test
    public void testConnect() throws Exception {
        RobotsParser robotsParser = new RobotsParser();
        robotsParser.connect("http://jamesfrost.me");
        assertArrayEquals(disallowedPaths.toArray(), robotsParser.getDisallowedPaths().toArray());

        robotsParser.connect("http://jamesfrost.me/about.htm");
        assertArrayEquals(disallowedPaths.toArray(), robotsParser.getDisallowedPaths().toArray());

        robotsParser.connect(new URL("http://jamesfrost.me"));
        assertArrayEquals(disallowedPaths.toArray(), robotsParser.getDisallowedPaths().toArray());

        robotsParser.connect(new URL("http://jamesfrost.me/about.htm"));
        assertArrayEquals(disallowedPaths.toArray(), robotsParser.getDisallowedPaths().toArray());

        try {
            robotsParser = new RobotsParser("shitbot");
            robotsParser.connect("http://jamesfrost.me");
            fail();
        } catch (RobotsDisallowedException e) {
            //
        }
    }

    /**
     * Method: constructRobotsUrl(String url)
     */
    @Test
    public void testConstructRobotsUrl() throws Exception {
        RobotsParser robotsParser = new RobotsParser();

        robotsParser.connect("http://jamesfrost.me/about.htm");
        assertEquals("http://jamesfrost.me/", robotsParser.getDomain());

        robotsParser.connect(new URL("http://jamesfrost.me/about.htm"));
        assertEquals("http://jamesfrost.me/", robotsParser.getDomain());

        robotsParser.connect("http://jamesfrost.me");
        assertEquals("http://jamesfrost.me/", robotsParser.getDomain());

        robotsParser.connect(new URL("http://jamesfrost.me"));
        assertEquals("http://jamesfrost.me/", robotsParser.getDomain());

        robotsParser.connect("http://jamesfrost.me/");
        assertEquals("http://jamesfrost.me/", robotsParser.getDomain());

        robotsParser.connect(new URL("http://jamesfrost.me/"));
        assertEquals("http://jamesfrost.me/", robotsParser.getDomain());
    }
}
