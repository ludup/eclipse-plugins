package com.hypersocket.eclipse.resources;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;

public class FileHelper {

	public static String lowerCaseFirst(String value) {

		char[] array = value.toCharArray();
		array[0] = Character.toLowerCase(array[0]);
		return new String(array);
	}

	public static String createDescription(String value) {

		StringBuffer buf = new StringBuffer();

		buf.append(Character.toUpperCase(value.charAt(0)));
		
		char[] array = value.toCharArray();

		for (int i = 1; i < array.length; i++) {
			if (Character.isUpperCase(array[i])) {
				buf.append(" ");
			}
			buf.append(array[i]);
		}

		// Result.
		return buf.toString();
	}

	public static void copyTree(File sourcePath, File targetFile, final String resourceName, final String packageName,
			final String resourceIcon) throws IOException {

		final String _resource = lowerCaseFirst(resourceName);
		final String _resources = _resource + "s";
		final String _Resource = resourceName;
		final String _Resources = _Resource + "s";
		final String resourceDesc = createDescription(resourceName);

		final Path source = sourcePath.toPath();
		final Path target = targetFile.toPath();

		Files.walkFileTree(source, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,
				new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
						Path targetdir = target.resolve(source.relativize(dir));
						try {
							Files.copy(dir, targetdir);
						} catch (FileAlreadyExistsException e) {
							if (!Files.isDirectory(targetdir))
								throw e;
						}
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						String filename = source.relativize(file).toString();

						filename = filename.replace("__resource__", _resource);
						filename = filename.replace("__resources__", _resources);
						filename = filename.replace("__Resource__", _Resource);
						filename = filename.replace("__Resources__", _Resources);
						filename = filename.replace("TemplateResource", resourceName + "Resource");
						filename = filename.replace("TemplateAssignableResource", resourceName + "Resource");

						Charset charset = StandardCharsets.UTF_8;

						String content = new String(Files.readAllBytes(file), charset);

						content = content.replaceAll("com.hypersocket.resource", packageName);
						content = content.replaceAll("com.hypersocket.assignable", packageName);
						
						content = content.replaceAll("<resource>", _resource);
						content = content.replaceAll("<resources>", _resources);
						content = content.replaceAll("<Resource>", _Resource);
						content = content.replaceAll("<Resources>", _Resources);
						
						content = content.replaceAll("TemplateResource", resourceName + "Resource");
						content = content.replaceAll("TemplateAssignableResource", resourceName + "Resource");
						
						content = content.replaceAll("<resourceIcon>", resourceIcon);
						content = content.replaceAll("<ResourceDesc>", resourceDesc);
						
						Files.write(target.resolve(filename), content.getBytes(charset));

						return FileVisitResult.CONTINUE;
					}
				});
	}
}
