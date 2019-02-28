package com.binarskugga.impl;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class ServiceFactory {

    private static Map<String, ServiceFactory> cache = new HashMap<>();
    private static final String BASE_URL = "http://127.0.0.1:8080/api/";

    private final Retrofit retrofit;

    private ServiceFactory(String urlAppend) {
        String fullUrl = BASE_URL + urlAppend;
        if(!fullUrl.endsWith("/")) fullUrl += "/";

        this.retrofit = new Retrofit.Builder()
                .baseUrl(fullUrl)
                .addConverterFactory(MoshiConverterFactory.create(MoshiProvider.get()))
                .build();
        cache.putIfAbsent(urlAppend, this);
    }

    public <T> T serve(Class<T> service) {
        return this.retrofit.create(service);
    }

    public static ServiceFactory get(String urlAppend) {
        return cache.containsKey(urlAppend) ? cache.get(urlAppend) : new ServiceFactory(urlAppend);
    }

}
