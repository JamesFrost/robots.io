package me.jamesfrost.robotsio;

/**
 * Thrown when a websites robots.txt disallows access.
 * <p/>
 * Created by James Frost on 22/12/2014.
 */
public class RobotsDisallowedException extends Exception {

    /**
     * Creates a new RobotsDisallowedException.
     *
     * @param website Website that has disallowed access
     */
    public RobotsDisallowedException(String website) {
        super(website + " does not allow robots.");
    }

    /**
     * Creates a new RobotsDisallowedException.
     */
    public RobotsDisallowedException() {
    }
}
