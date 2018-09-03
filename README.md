# Java-IPinfo: A java wrapper for the [IPinfo](https://ipinfo.io/) API.

[![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE)
[![Travis](https://travis-ci.com/ipinfo/java-ipinfo.svg?branch=master&style=flat-square)](https://travis-ci.com/ipinfo/java-ipinfo)

Java-IPinfo is a lightweight wrapper for the IPinfo API, which provides up-to-date IP address data.

## Features:

- IP Lookup
- ASN Lookup

## Usage

### Maven
Repository:

```xml
<repositories>
    <repository>
        <id>sonatype-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>
```

Dependency:

```xml
<dependencies>
    <dependency>
        <groupId>io.ipinfo</groupId>
        <artifactId>java-ipinfo</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

### Examples

#### IP Information


````java
import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

public class Main {

    public static void main(String... args) {
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").build();

        try {
            IPResponse response = ipInfo.lookupIP("8.8.8.8");

            // Print out the hostname
            System.out.println(response.getHostname());
        } catch (RateLimitedException ex) {
            // Handle rate limits here.
        }
    }
}
````


#### ASN Information

````java
import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

public class Main {

    public static void main(String... args) {
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").build();

        try {
            ASNResponse response = ipInfo.lookupASN("AS7922");

            // Print out country name
            System.out.println(response.getCountry());
        } catch (RateLimitedException ex) {
            // Handle rate limits here.
        }
    }
}
````

### Errors

- ErrorResponseException: A runtime exception accessible through the ExecutionException of a future. This exception signals that something went wrong when mapping the API response to the wrapper. You probably can't recover from this exception.

- RateLimitedException An exception signalling that you've been rate limited.

### Caching

This library provides a very simple caching system accessible in `SimpleCache`. Simple cache is an in memory caching system that resets every time you restart your code.

If you prefer a different caching methodology, you may use the `Cache` interface and implement your own caching system around your own infrastructure.

The default cache length is 1 day, this can be changed by calling the SimpleCache constructor yourself.


```java
import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

public class Main {

    public static void main(String... args) {
        // 5 Day Cache
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").setCache(new SimpleCache(Duration.ofDays(5))).build();

        try {
            IPResponse response = ipInfo.lookupIP("8.8.8.8");

            // Print out the hostname
            System.out.println(response.getHostname());
        } catch (RateLimitedException ex) {
            // Handle rate limits here.
        }
    }
}
```

### Country Name Lookup

This library provides a system to lookup country names through ISO2 country codes.

By default, this translation exists for English (United States). If you wish to provide a different language mapping, just use the following system in the builder:

```java
import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

public class Main {

    public static void main(String... args) {
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").setCountryFile(new File("path/to/file.json")).build();

        try {
            IPResponse response = ipInfo.lookupIP("8.8.8.8");

            // Print out the country code
            System.out.println(response.getCountryCode());

            // Print out the country name
            System.out.println(response.getCountryName());
        } catch (RateLimitedException ex) {
            // Handle rate limits here.
        }
    }
}
```

This file must follow the same layout as seen [here](https://github.com/ipinfo/java-ipinfo/blob/master/src/main/resources/en_US.json)

More language files can be found [here](https://country.io/data)

### Location Information

This library provides an easy way to get the latitude and longitude of an IP Address:

```java
import io.ipinfo.api.IPInfo;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;

public class Main {

    public static void main(String... args) {
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").setCountryFile(new File("path/to/file.json")).build();

        try {
            IPResponse response = ipInfo.lookupIP("8.8.8.8");

            // Print out the Latitude and Longitude
            System.out.println(response.getLatitude());
            System.out.println(response.getLongitude());

        } catch (RateLimitedException ex) {
            // Handle rate limits here.
        }
    }
}
```

## Extra Information

- This library is thread safe. Feel free to call the different endpoints from different threads.
- This library uses square's http client. Please refer to their documentation to get information on more functionality you can use.

