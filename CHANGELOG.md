# 2.0.0

Breaking changes:

- `IPInfo` is renamed to `IPinfo`.
- `IPInfoBuilder` is moved into `IPinfo.Builder`.
- `ASNResponse.numIps` is now an `Integer` instead of a `String`.
- The cache implementation now only uses `get` and `set`, which accept
  arbitrary strings, which may not necessarily be IP or ASN strings like
  "1.2.3.4" and "AS123".

Additions:

- `getBatch`, `getBatchIps` and `getBatchAsns` allow you to do lookups of
  multiple entities at once. There is no limit to the size of inputs on these
  library calls as long as your token has quota.
- `getMap` will give you a map URL for https://ipinfo.io/tools/map given a list
  of IPs.
- Many new pieces of data have been added that were previously missing. The new
  dataset reflects all the new data available via raw API calls.
- The keys given to cache functions will now be versioned. `IPinfo.cacheKey`
  must be used to derive the correct key if doing manual lookups.
