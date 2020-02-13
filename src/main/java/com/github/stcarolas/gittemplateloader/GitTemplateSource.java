package com.github.stcarolas.gittemplateloader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

import com.github.jknack.handlebars.io.AbstractTemplateSource;

import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class GitTemplateSource extends AbstractTemplateSource {
	private final String filename;
	private final File directory;

	@Override
	public String content(Charset charset) throws IOException {
		val targetFile = directory.toPath().resolve(filename).toFile();
		log.info("target: {}", targetFile.toString());
		StringBuilder content = new StringBuilder();
		try ( val in =  new Scanner(targetFile)) {
			while(in.hasNextLine()) {
				content.append(in.nextLine()).append("\n");
			}
		}
		return content.toString();
	}

	@Override
	public String filename() {
		return filename;
	}

	@Override
	public long lastModified() {
		return 0;
	}
}
