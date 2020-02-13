package com.github.stcarolas.gittemplateloader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import lombok.val;

public class GitTemplateSourceTest {

	@Test
	public void testReadingContentFromFile() throws IOException{
		val dirName = "/tmp/gittemplateloader-test/" + UUID.randomUUID();
		val filename = "testfile";

		val dir = new File(dirName);
		dir.mkdirs();
		val file = new File(dirName + "/" + filename);
		file.createNewFile();
		try (val out = new FileWriter(file)) {
			out.write("test string");
		}

		val source = GitTemplateSource.builder().filename(filename).directory(dir).build();
		assertEquals("test string\n", source.content(Charset.defaultCharset()));
	}

}
