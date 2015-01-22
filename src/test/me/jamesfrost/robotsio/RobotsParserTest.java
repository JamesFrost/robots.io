package test.me.jamesfrost.robotsio;

import me.jamesfrost.robotsio.RobotsDisallowedException;
import me.jamesfrost.robotsio.RobotsParser;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
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

    //Disallowed paths on jamesfrost.me
    private ArrayList<String> disallowedPaths;

    @Before
    public void before() throws Exception {
        disallowedPaths = new ArrayList<String>();
        disallowedPaths.add("cgi-bin/");
        disallowedPaths.add("404.htm");
        disallowedPaths.add("CV.htm");
        disallowedPaths.add("simpleDo/");
        disallowedPaths.add("basic-projects-template.htm");
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

        robotsParser.connect("http://youtube.com/"); //Youtube does not have a robots.txt
        assertTrue(robotsParser.getDisallowedPaths().isEmpty());

        try {
            robotsParser = new RobotsParser("shitbot"); //The 'shitbot' user-agent is disallowed in the robots.txt on jamesfrost.me
            robotsParser.connect("http://jamesfrost.me");
            fail();
        } catch (RobotsDisallowedException e) {
            assertFalse(robotsParser.isAllowed("http://jamesfrost.me"));
        }
    }

    /**
     * Method: filterURLs(ArrayList<URL> unfilteredList)
     */
    @Test
    public void testFilter() throws Exception {
        RobotsParser robotsParser = new RobotsParser();

        ArrayList<URL> input = new ArrayList<URL>();
        input.add(new URL("http://jamesfrost.me/404.htm"));

        ArrayList<URL> expectedOutput = new ArrayList<URL>();

        robotsParser.connect("http://jamesfrost.me/");
        assertEquals(expectedOutput, robotsParser.filterURLs(input));
    }

    /**
     * Method: filterStrings(ArrayList<String> unfilteredList)
     */
    @Test
    public void testFilterString() throws Exception {
        RobotsParser robotsParser = new RobotsParser();

        ArrayList<String> input = new ArrayList<String>();
        input.add("http://jamesfrost.me/404.htm");

        ArrayList<String> expectedOutput = new ArrayList<String>();

        robotsParser.connect("http://jamesfrost.me/");
        assertEquals(expectedOutput, robotsParser.filterStrings(input));
    }

    /**
     * Method:  getDisallowedUrls()
     */
    @Test
    public void testGetDisallowedURLs() throws Exception {
        RobotsParser robotsParser = new RobotsParser();
        robotsParser.connect("http://jamesfrost.me");

        ArrayList<String> expectedOutput = new ArrayList<String>();
        for (String path : disallowedPaths) {
            expectedOutput.add("http://jamesfrost.me/" + path);
        }

        assertEquals(expectedOutput, robotsParser.getDisallowedUrls());
    }

    /**
     * Method: constructRobotsUrl(String url)
     */
    @Test
    public void testConstructRobotsUrl() throws Exception {
        try {
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

            robotsParser.connect("http://jamesfrost.me/about.htm/");
            assertEquals("http://jamesfrost.me/", robotsParser.getDomain());

            robotsParser.connect(new URL("http://jamesfrost.me/about.htm/"));
            assertEquals("http://jamesfrost.me/", robotsParser.getDomain());
        } catch (MalformedURLException e) {
            fail();
        }
    }

    /**
     * Method: getUserAgent()
     */
    @Test
    public void testGetUserAgent() throws Exception {
        //Tests that the userAgent variable is not initialised when no value is passed in the constructor
        RobotsParser robotsParser = new RobotsParser();
        assertNull(robotsParser.getUserAgent());

        robotsParser = new RobotsParser("shitbot");
        assertEquals(robotsParser.getUserAgent(), "shitbot");
    }
}
