# Java IPinfo
[![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE)
[![Travis](https://travis-ci.com/ipinfo/java-ipinfo.svg?branch=master&style=flat-square)](https://travis-ci.com/ipinfo/java-ipinfo)

An official java wrapper around IPinfo's API.

## Features:
- IP Lookup
- ASN Lookup

## Using it

### Maven

```xml
<dependency>
    <groupId>io.ipinfo</groupId>
    <artifactId>java-ipinfo</artifactId>
    <version>1.0</version>
    <scope>compile</scope>
</dependency>
```

### Examples

To get information about an IP:

````java
import io.ipinfo.IPInfo;
import io.ipinfo.errors.RateLimitedException;
import io.ipinfo.model.IPResponse;

public class Main {

    public static void main(String... args) {
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").build();

        try {
            IPResponse response = ipInfo.lookupIP("8.8.8.8");
        } catch (RateLimitedException ex) {
            // Handle rate limits here.
        }
    }
}
````

To get the information about an ASN:

````java
import io.ipinfo.IPInfo;
import io.ipinfo.errors.RateLimitedException;
import io.ipinfo.model.IPResponse;

public class Main {

    public static void main(String... args) {
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").build();

        try {
            ASNResponse response = ipInfo.lookupASN("AS7922");
        } catch (RateLimitedException ex) {
            // Handle rate limits here.
        }
    }
}
````

### Errors

- ErrorResponseException: A runtime exception accessible through the ExecutionException of a future. This exception signals that something went wrong when mapping the API response to the wrapper. You probably can't recover from this exception.

- RateLimitedException A runtime exception signalling that you've been rate limited.

## Extra Information

- This library is thread safe. Feel free to call the different endpoints from different threads.
- This library uses square's http client. Please refer to their documentation to get information on more functionality you can use. 

