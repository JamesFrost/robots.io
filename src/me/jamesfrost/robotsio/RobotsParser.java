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
     * Creates a new RobotsParser.
     *
     * @param userAgent UserAgent to parse with
     */
    public RobotsParser(String userAgent) {
        this.userAgent = userAgent;
    }

    /**
     * Checks if a URL is allowed in a sites robots.txt. Checks against the current rules in disallowedPaths.
     *
     * @param url URL to check
     * @return True if allowed
     */
    public boolean isAllowed(URL url) {
        String urlString = url.toString();
        for (String disallowedPath : disallowedPaths) {
            if (urlString.contains(disallowedPath))
                return false;
        }
        return true;
    }

    /**
     * Checks if a URL is allowed in a sites robots.txt. Checks against the current rules in disallowedPaths.
     *
     * @param url URL to check
     * @return True if allowed
     */
    public boolean isAllowed(String url) throws MalformedURLException {
        return isAllowed(new URL(url));
    }

    /**
     * Constructs a URL to the expected location of a websites robots.txt.
     *
     * @param url Website to construct the URL for
     * @return Robots.txt URL
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
     * @return Robots.txt URL
     */
    private URL constructRobotsUrl(URL url) {
        return constructRobotsUrl(url.toString());
    }

    /**
     * Processes a websites robots.txt, and overwrites the disallowedPaths and domain.
     *
     * @param robotsTxtUrl Websites robots.txt to process
     * @throws RobotsDisallowedException
     */
    public void connect(URL robotsTxtUrl) throws RobotsDisallowedException {
        robotsTxtUrl = constructRobotsUrl(robotsTxtUrl);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(robotsTxtUrl.openStream()));

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
     * Processes a websites robots.txt, and overwrites the disallowedPaths and domain.
     *
     * @param url Websites robots.txt to process
     * @throws me.jamesfrost.robotsio.RobotsDisallowedException
     */
    public void connect(String url) throws RobotsDisallowedException, MalformedURLException {
        connect(new URL(url));
    }

    /**
     * Gets the disallowed paths currently cached.
     *
     * @return disallowedPaths
     */
    public ArrayList<String> getDisallowedPaths() {
        return disallowedPaths;
    }

    /**
     * Gets the domain that corresponds to the rules currently cached.
     *
     * @return domain
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Gets the User-Agent string being used to parse.
     *
     * @return userAgent
     */
    public String getUserAgent() {
        return userAgent;
    }
}
