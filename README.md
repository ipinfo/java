# Java IPinfo
[![License](http://img.shields.io/:license-apache-blue.svg)](LICENSE)
[![Travis](https://travis-ci.com/ipinfo/java-ipinfo.svg?branch=master&style=flat-square)](https://travis-ci.com/ipinfo/java-ipinfo)

An official java wrapper around ipinfo's API.

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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
public class Main {

    public static void main(String... args) {
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").build();

        Future<ASNResponse> responseFuture = ipInfo.lookupASN("AS7922");

        try {
            System.out.println(responseFuture.get().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RateLimitedException) {
                System.out.println("Rate limited");
            }
        }
    }
}
````

To get the information about an ASN:

````java
import io.ipinfo.IPInfo;
import io.ipinfo.errors.RateLimitedException;
import io.ipinfo.model.IPResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
public class Main {

    public static void main(String... args) {
        IPInfo ipInfo = IPInfo.builder().setToken("YOUR TOKEN").build();

        Future<ASNResponse> responseFuture = ipInfo.lookupASN("AS7922");

        try {
            System.out.println(responseFuture.get().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof RateLimitedException) {
                System.out.println("Rate limited");
            }
        }
    }
}
````

### Errors

- ErrorResponseException: A runtime exception accessible through the ExecutionException of a future. This exception signals that something went wrong when mapping the API response to the wrapper.
- RateLimitedException A runtime exception signalling that you've been rate limited.

