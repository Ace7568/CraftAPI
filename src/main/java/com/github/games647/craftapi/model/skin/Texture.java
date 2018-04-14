package com.github.games647.craftapi.model.skin;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Decoded data of one of the texture types.
 */
public class Texture {

    private static final String URL_PREFIX = "http://textures.minecraft.net/texture/";

    protected transient Type type;

    private final URL url;
    private final Map<String, Model> metadata;

    protected Texture(Type type, String hash, Model skinModel) {
        try {
            this.url = new URL(URL_PREFIX + hash);
        } catch (MalformedURLException malformedURLEx) {
            throw new IllegalArgumentException(malformedURLEx);
        }

        this.type = type;

        if (skinModel == Model.SLIM) {
            metadata = Collections.singletonMap("model", Model.SLIM);
        } else {
            //this have to be null, because for square models this field shouldn't exist
            metadata = null;
        }
    }

    public Texture(String hash, Model skinModel) {
        //only skins can have models
        this(Type.SKIN, hash, skinModel);
    }

    public Texture(Type type, String hash) {
        this(type, hash, null);
    }

    /**
     * @return {@link Optional#empty()} if not a skin otherwise returns the arm model
     */
    public Optional<Model> getArmModel() {
        //only skins have this data
        if (type != Type.SKIN) {
            return Optional.empty();
        }

        if (metadata != null) {
            //check if there more metadata besides the model
            return Optional.ofNullable(metadata.getOrDefault("model", Model.SQUARE));
        }

        //metadata will be discarded for square models and no other metadata
        return Optional.of(Model.SQUARE);
    }

    /**
     * @return complete url where the client can find the skin
     */
    public URL getURL() {
        return url;
    }

    /**
     * @return url without the Mojang prefix.
     *          Example: http://textures.minecraft.net/texture/HASH to HASH
     */
    public String getHash() {
        String path = url.getPath();

        int lastSeparator = path.lastIndexOf('/');
        if (lastSeparator < 0) {
            return path;
        }

        return path.substring(lastSeparator + 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Texture)) return false;
        Texture that = (Texture) o;
        return Objects.equals(url, that.url) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, metadata);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '{' +
                "url='" + url + '\'' +
                ", metadata=" + metadata +
                '}';
    }

    /**
     * Texture types of skin properties
     */
    public enum Type {

        SKIN,

        CAPE,

        ELYTRA
    }
}
