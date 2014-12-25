package me.jamesfrost.robotsio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Gets and processes a websites robots.txt. Provides all functionality to use robots.io.
 * <p/>
 * Data parsed from the file is cached until 'connect()' is called, when it is overwritten.
 * <p/>
 * Created by James Frost on 22/12/2014.
 */
public class RobotsParser {

    //List of disallowed paths parsed from last website
    private ArrayList<String> disallowedPaths;
    //Last domain scraped
    private String domain;
    //UserAgent string to parse with
    private String userAgent;

    /**
     * Creates a new RobotsParser.
     */
    public RobotsParser() {
        disallowedPaths = new ArrayList<String>();
    }

    /**
     * Creates a new RobotsParser to parse with a User-agent string.
     *
     * @param userAgent UserAgent to parse with
     */
    public RobotsParser(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Checks if a URL is allowed in a sites robots.txt.
     * <p/>
     * Searches against the current rules in disallowedPaths to see
     * if the path in the URL is disallowed.
     * <p/>
     *
     * @param url URL to check if allowed
     * @return True if URL is allowed
     */
    public boolean isAllowed(URL url) {
        String urlString = url.getPath();
        for (String disallowedPath : disallowedPaths) {
            if (urlString.contains(disallowedPath))
                return false;
        }
        return true;
    }

    /**
     * Checks if a URL is allowed in a sites robots.txt.
     * <p/>
     * Searches against the current rules in disallowedPaths to see
     * if the path in the URL is disallowed.
     * <p/>
     *
     * @param url URL to check if allowed
     * @return True if URL is allowed
     */
    public boolean isAllowed(String url) throws MalformedURLException {
        return isAllowed(new URL(url));
    }

    /**
     * Constructs a URL to the expected location of a websites robots.txt.
     * <p/>
     * For example: when passed "http://jamesfrost.me" it will construct "http://jamesfrost.me/robots.txt."
     * It can also handle URL's with a path.
     * <p/>
     *
     * @param url Website to construct the robots.txt URL for
     * @return Robots.txt URL for the website passed
     */
    private URL constructRobotsUrl(String url) {
        URL robotsTxtUrl = null;
        try {
            robotsTxtUrl = new URL(url);

            if (!robotsTxtUrl.getFile().equals("/") && !robotsTxtUrl.getFile().equals("")) {
                domain = url.replace(robotsTxtUrl.getFile(), "/");
                robotsTxtUrl = new URL(url.replace(robotsTxtUrl.getFile(), "/robots.txt"));
            } else if (robotsTxtUrl.getFile().equals("")) {
                robotsTxtUrl = new URL(url + "/robots.txt");
                domain = url + "/";
            } else {
                domain = url;
                robotsTxtUrl = new URL(url + "robots.txt");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return robotsTxtUrl;
    }

    /**
     * Constructs a URL to the expected location of a websites robots.txt.
     *
     * @param url Website to construct the URL for
     * @return Robots.txt URL for the websites robots.txt
     */
    private URL constructRobotsUrl(URL url) {
        return constructRobotsUrl(url.toString());
    }

    /**
     * Connects and caches a websites robots.txt.
     * <p/>
     * This method overwrites the currently cached disallowedPaths and domain variables.
     * <p/>
     * The URL passed can contain a path; for example, jamesfrost.me/about.htm.
     * <p/>
     *
     * @param url Website to parse the robots.txt for
     * @throws RobotsDisallowedException Access disallowed
     */
    public void connect(URL url) throws RobotsDisallowedException {
        url = constructRobotsUrl(url);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            RobotsTxtReader robotsTxtReader = new RobotsTxtReader();

            if (userAgent != null)
                disallowedPaths = robotsTxtReader.read(in, userAgent);
            else
                disallowedPaths = robotsTxtReader.read(in);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (RobotsDisallowedException e) {
            throw new RobotsDisallowedException(domain);
        }
    }

    /**
     * Connects and caches a websites robots.txt.
     * <p/>
     * This method overwrites the currently cached disallowedPaths and domain variables.
     * <p/>
     * The URL passed can contain a path; for example, jamesfrost.me/about.htm.
     * <p/>
     *
     * @param url Website to parse the robots.txt for
     * @throws me.jamesfrost.robotsio.RobotsDisallowedException Access disallowed
     */
    public void connect(String url) throws RobotsDisallowedException, MalformedURLException {
        connect(new URL(url));
    }

    /**
     * Gets the disallowed paths currently cached.
     * <p/>
     * These will be normalised so that they never begin with a forward slash. Directories will end with a forward slash,
     * files will not. This is to allow for easy URL building.
     * <p/>
     *
     * @return The disallowed paths currently cached
     */
    public ArrayList<String> getDisallowedPaths() {
        return disallowedPaths;
    }

    /**
     * Gets the domain that is currently cached.
     * <p/>
     * This domain corresponds to the domain for the rules currently cached.
     * <p/>
     *
     * @return The domain currently cached
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Gets the User-Agent string being used to parse.
     *
     * @return The User-agent string being used to parse
     */
    public String getUserAgent() {
        return userAgent;
    }
}
