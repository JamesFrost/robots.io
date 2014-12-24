package me.jamesfrost.robotsio;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads a websites robots.txt.
 * <p/>
 * Created by James Frost on 24/12/2014.
 */
class RobotsTxtReader {

    /**
     * Extracts disallowed paths from a robots.txt.
     *
     * @param robotsTxt Robots.txt file to read
     * @return Disallowed paths
     * @throws RobotsDisallowedException
     */
    protected ArrayList<String> read(BufferedReader robotsTxt) throws RobotsDisallowedException {
        Boolean userAgentFound = false;
        String line;
        ArrayList<String> disallowedPaths = new ArrayList<String>();

        try {
            while ((line = robotsTxt.readLine()) != null) {
                if (line.contains("User-agent: *")) {
                    userAgentFound = true;
                } else if (line.contains("User-agent:")) {
                    userAgentFound = false;
                }

                if (userAgentFound && line.contains("Disallow:") && !line.contains("#")) {
                    line = line.replace("Disallow:", "").trim();
                    if (line.isEmpty()) //Allowed complete access
                        return new ArrayList<String>();

                    if (line.equals("/")) { //No access allowed
                        throw new RobotsDisallowedException();
                    }

                    if (line.substring(0, 1).equals("/"))
                        line = line.substring(1);

                    disallowedPaths.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return disallowedPaths;
    }

    /**
     * Extracts disallowed paths from a robots.txt.
     *
     * @param robotsTxt Robots.txt file to read
     * @param userAgent UserAgent
     * @return Disallowed paths
     * @throws RobotsDisallowedException
     */
    protected ArrayList<String> read(BufferedReader robotsTxt, String userAgent) throws RobotsDisallowedException {
        Boolean userAgentFound = false;
        String line;
        ArrayList<String> disallowedPaths = new ArrayList<String>();

        try {
            while ((line = robotsTxt.readLine()) != null) {
                if (line.contains("User-agent: *") || line.contains("User-agent: " + userAgent)) {
                    userAgentFound = true;
                } else if (line.contains("User-agent:")) {
                    userAgentFound = false;
                }

                if (userAgentFound && line.contains("Disallow:") && !line.contains("#")) {
                    line = line.replace("Disallow:", "").trim();
                    if (line.isEmpty()) //Allowed complete access
                        return new ArrayList<String>();

                    if (line.equals("/")) { //No access allowed
                        throw new RobotsDisallowedException();
                    }

                    if (line.substring(0, 1).equals("/"))
                        line = line.substring(1);

                    disallowedPaths.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return disallowedPaths;
    }

}
