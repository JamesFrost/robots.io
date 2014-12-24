robots.io
=========
Robots.io is a Java library designed to make parsing a websites 'robots.txt' file easy.

## How to use
The <a href = "https://github.com/JamesFrost/robots.io/blob/master/src/me/jamesfrost/robotsio/RobotsParser.java">RobotsParser</a> class provides all the functionality to use robots.io.

## Examples

### Connecting
To parse the robots.txt for Google with the User-Agent string "test":
```java
RobotsParser robotsParser = new RobotsParser("test");
robotsParser.connect("http://google.com");
```
Alternatively, to parse with no User-Agent, simply leave the constructor blank.

### Querying
To check if a URL is allowed:
```java
robotsParser.isAllowed("http://google.com/test"); //Returns true if allowed
```

Or, to get all the rules parsed from the file:
```java
robotsParser.getDisallowedPaths(); //This will return an ArrayList of Strings
```

The results parsed are cached in the robotsParser object until the ```connect()``` method is called again, overwriting the previously parsed data

### Politeness
In the event that all access is denied, a ```RobotsDisallowedException``` will be thrown.

## URL Normalisation
Domains passed to RobotsParser are normalised to always end in a forward slash.
Disallowed Paths returned will never begin with a forward slash.
This is so that URL's can easily be constructed. For example:
```java
robotsParser.getDomain() + robotsParser.getDisallowedPaths().get(0); // http://google.com/example.htm
```
