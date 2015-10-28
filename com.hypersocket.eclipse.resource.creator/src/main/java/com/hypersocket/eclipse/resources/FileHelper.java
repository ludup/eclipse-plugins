package com.hypersocket.eclipse.resources;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.core.runtime.CoreException;

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

	public static void copyResource(URL sourcePath, File target, String resourceName, String packageName,
			String resourceIcon) throws IOException, CoreException {

		String _resource = lowerCaseFirst(resourceName);
		String _resources = _resource + "s";
		String _Resource = resourceName;
		String _Resources = _Resource + "s";
		String resourceDesc = createDescription(resourceName);

		Charset charset = StandardCharsets.UTF_8;

		InputStream in = sourcePath.openStream();
		String content = new String(readAllBytes(in), charset);

		content = content.replaceAll("com.hypersocket.template.task", packageName);
		content = content.replaceAll("com.hypersocket.template", packageName);
		content = content.replaceAll("com.hypersocket.assignable", packageName);
		
		
		content = content.replaceAll("<resource>", _resource);
		content = content.replaceAll("<resources>", _resources);
		content = content.replaceAll("<Resource>", _Resource);
		content = content.replaceAll("<Resources>", _Resources);
		
		content = content.replaceAll("TemplateResource", resourceName + "Resource");
		content = content.replaceAll("TemplateAssignableResource", resourceName + "Resource");
		content = content.replaceAll("TemplateTask", resourceName + "Task");
		
		content = content.replaceAll("<resourceIcon>", resourceIcon);
		content = content.replaceAll("<ResourceDesc>", resourceDesc);
		
		Files.copy(new ByteArrayInputStream(content.getBytes("UTF-8")), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

	}
	
	private static byte[] readAllBytes(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			byte[] buf = new byte[65535];
			int r;
			while((r = in.read(buf)) > -1) {
				out.write(buf, 0, r);
			}
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return out.toByteArray();
		
	}
}
