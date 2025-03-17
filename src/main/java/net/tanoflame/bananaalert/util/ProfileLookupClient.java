package net.tanoflame.bananaalert.util;

import com.google.gson.*;
import net.tanoflame.bananaalert.BananaAlert;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class ProfileLookupClient {
    private static final long CACHE_TTL = 600000;
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LookupReturn.class, new ProfileLookupDeserializer())
            .create();

    private final String API_URL = "https://api.minecraftservices.com/minecraft/profile/lookup/";
    private final HttpClient client;

    private final List<LookupReturn> cache;

    public ProfileLookupClient() {
        client = HttpClient.newHttpClient();
        cache = new ArrayList<>();
    }

    public UUID getUUIDFromName(String name) throws IOException, InterruptedException {
        if (BananaAlert.isDevEnvironment()) return null;

        Optional<LookupReturn> cachedLookup = cache.stream().filter((lookupReturn -> Objects.equals(lookupReturn.name, name))).findFirst();
        if (cachedLookup.isPresent()) {
            if (cachedLookup.get().retrievalTime < System.currentTimeMillis() - CACHE_TTL) {
                return cachedLookup.get().id;
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "name/" + name))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            LookupReturn lookupReturn = GSON.fromJson(response.body(), LookupReturn.class);
            if (cachedLookup.isPresent()) {
                LookupReturn result = cachedLookup.get();
                if (result.id == lookupReturn.id) {
                    result.name = lookupReturn.name;
                    result.retrievalTime = lookupReturn.retrievalTime;
                }
            } else {
                cache.add(lookupReturn);
            }
            return lookupReturn.id;
        }

        return Util.NIL_UUID;
    }

    public @Nullable String getNameFromUUID(UUID uuid) throws IOException, InterruptedException {
        if (BananaAlert.isDevEnvironment()) return null;

        Optional<LookupReturn> cachedLookup = cache.stream().filter((lookupReturn -> Objects.equals(lookupReturn.id, uuid))).findFirst();
        if (cachedLookup.isPresent()) {
            if (cachedLookup.get().retrievalTime < System.currentTimeMillis() - CACHE_TTL) {
                return cachedLookup.get().name;
            } else {
                cache.remove(cachedLookup.get());
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + uuid))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            LookupReturn lookupReturn = GSON.fromJson(response.body(), LookupReturn.class);
            cache.add(lookupReturn);
            return lookupReturn.name;
        }

        return null;
    }

    private static class ProfileLookupDeserializer implements JsonDeserializer<LookupReturn> {
        @Override
        public LookupReturn deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            UUID id = UUID.fromString(jsonObject.get("id").getAsString()
                    .replaceAll("([a-f0-9]{8})([a-f0-9]{4})(4[a-f0-9]{3})([89aAbB][a-f0-9]{3})([a-f0-9]{12})", "$1-$2-$3-$4-$5"));

            return new LookupReturn(id, jsonObject.get("name").getAsString());
        }
    }

    private static class LookupReturn {
        public UUID id;
        public String name;
        public long retrievalTime;

        public LookupReturn(UUID id, String name) {
            this.id = id;
            this.name = name;
            this.retrievalTime = System.currentTimeMillis();
        }
    }
}
