package io.ipinfo;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.ipinfo.cache.Cache;
import io.ipinfo.cache.SimpleCache;
import okhttp3.OkHttpClient;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IPInfoBuilder {
    private File countryFile = new File(this.getClass().getClassLoader().getResource("en_US.json").getFile());
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private String token = "";
    private Cache cache = new SimpleCache(Duration.ofDays(1));

    public IPInfoBuilder setClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public IPInfoBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public IPInfoBuilder setCountryFile(File file) {
        this.countryFile = file;
        return this;
    }

    public IPInfoBuilder setCache(Cache cache) {
        this.cache = cache;
        return this;
    }

    public IPInfo build() {
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        Gson gson = new Gson();
        Map<String, String> map;

        try {
            map = Collections.unmodifiableMap(gson.fromJson(new FileReader(countryFile), type));
        } catch (Exception e) {
            map = Collections.unmodifiableMap(new HashMap<>());
        }

        return new IPInfo(client, token, map, cache);
    }
}
