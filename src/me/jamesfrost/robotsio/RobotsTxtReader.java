package me.jamesfrost.robotsio;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Contains functionality to parse a robots.txt file.
 * <p/>
 * Created by James Frost on 24/12/2014.
 */
class RobotsTxtReader {

    /**
     * Extracts disallowed paths from a robots.txt.
     * <p/>
     * These will be normalised so that they never begin with a forward slash. Directories will end with a forward slash,
     * files will not. This is to allow for easy URL building.
     * <p/>
     *
     * @param robotsTxt Robots.txt file to read
     * @return Disallowed paths parsed from file
     * @throws RobotsDisallowedException Access disallowed
     * @throws java.io.IOException       404 - File not found
     */
    protected static ArrayList<String> read(BufferedReader robotsTxt) throws RobotsDisallowedException, IOException {
        Boolean userAgentFound = false;
        String line;
        ArrayList<String> disallowedPaths = new ArrayList<String>();

        while ((line = robotsTxt.readLine()) != null) {
            if (line.contains("User-agent: *")) {
                userAgentFound = true;
                continue;
            } else if (line.contains("User-agent:")) {
                userAgentFound = false;
                continue;
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
        return disallowedPaths;
    }

    /**
     * Extracts disallowed paths from a robots.txt with regard to a User-agent string.
     * <p/>
     * These will be normalised so that they never begin with a forward slash. Directories will end with a forward slash,
     * files will not. This is to allow for easy URL building.
     * <p/>
     *
     * @param robotsTxt Robots.txt file to read
     * @param userAgent UserAgent string to parse with
     * @return Disallowed paths parsed from file
     * @throws RobotsDisallowedException Access disallowed
     * @throws java.io.IOException       404 - File not found
     */
    protected static ArrayList<String> read(BufferedReader robotsTxt, String userAgent) throws RobotsDisallowedException, IOException {
        Boolean userAgentFound = false;
        String line;
        ArrayList<String> disallowedPaths = new ArrayList<String>();

        while ((line = robotsTxt.readLine()) != null) {
            if (line.contains("User-agent: *") || line.contains("User-agent: " + userAgent)) {
                userAgentFound = true;
                continue;
            } else if (line.contains("User-agent:")) {
                userAgentFound = false;
                continue;
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
        return disallowedPaths;
    }
}
