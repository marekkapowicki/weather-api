package pl.marekk.weather;

import java.io.File;
import java.nio.file.Files;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Resources {
  @SneakyThrows
  public static String loadResource(String resourceName) {
    final ClassLoader classLoader = Resources.class.getClassLoader();
    final File file = new File(classLoader.getResource(resourceName).getFile());
    return Files.readString(file.toPath());
  }
}
