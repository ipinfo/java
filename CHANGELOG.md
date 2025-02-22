# 3.0.1

- `IPResponse`: reflect `anycast` response field being renamed to `is_anycast`
- fix and improve tests

# 3.0.0

- Removed loading of country/continent/currency/EU-related data via files. This
  is done fully statically now.
- IPinfo builder no longer supports functions `setContinentFile`,
  `setCountryCurrencyFile`, `setCountryFlagFile`, `setEUCountryFile`,
  `setCountryFile`.
- IPinfo `Context` object no longer supports being initialized via input maps.

# 2.2.2

- Updated guava to vsn 32.1.2

# 2.2.1

- Updated vulnerable dependency; `com.google.guava`

# 2.2.0

- Added `isEU`, `CountryFlag`, `CountryCurrency` and `Continent` fields.
- Checking bogon IP locally.
- Upgraded `okhttp` to `4.10.0`.

# 2.1.0

- Added `Relay` and `Service` fields to `io.ipinfo.api.model.Privacy`.

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
