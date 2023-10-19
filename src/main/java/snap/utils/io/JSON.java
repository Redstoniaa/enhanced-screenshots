package snap.utils.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import snap.utils.Translations;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class JSON {
    private static final ClassLoader CLASS_LOADER = Translations.class.getClassLoader();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    
    public static <T> T readFromResource(String path, TypeToken<T> typeToken) throws IOException {
        InputStream stream = getResourceAsStream(path)
                .orElseThrow(() -> new IOException("Resource could not be found or accessed."));
        String content = toString(stream);
        return readFromString(content, typeToken);
    }
    
    private static Optional<InputStream> getResourceAsStream(String path) {
        InputStream inputStream = CLASS_LOADER.getResourceAsStream(path);
        return Optional.ofNullable(inputStream);
    }
    
    public static <T> T readFromString(String input, TypeToken<T> typeToken) {
        return GSON.fromJson(input, typeToken);
    }
    
    private static String toString(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }
    
    public static <T> T readFromPath(Path path, TypeToken<T> typeToken) throws IOException {
        String content = Files.readString(path);
        return readFromString(content, typeToken);
    }
    
    public static void writeToPath(Object object, Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        GSON.toJson(object, writer);
        writer.flush();
        writer.close();
    }
}
