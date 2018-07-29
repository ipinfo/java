package io.ipinfo;

import okhttp3.OkHttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IPInfoBuilder {
    private ExecutorService service = Executors.newSingleThreadExecutor();
    private OkHttpClient client = new OkHttpClient.Builder().build();
    private String token = "";

    public IPInfoBuilder setService(ExecutorService service) {
        this.service = service;
        return this;
    }

    public IPInfoBuilder setClient(OkHttpClient client) {
        this.client = client;
        return this;
    }

    public IPInfoBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public IPInfo build() {
        return new IPInfo(service, client, token);
    }
}
