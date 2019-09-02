package com.github.stcarolas.gittemplateloader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import com.github.jknack.handlebars.io.AbstractTemplateLoader;
import com.github.jknack.handlebars.io.TemplateSource;
import org.eclipse.jgit.api.Git;
import io.vavr.control.Try;
import lombok.Builder;
import lombok.val;
import lombok.extern.log4j.Log4j2;

@Builder
@Log4j2
public class GitTemplateLoader extends AbstractTemplateLoader {
    private final String url;
    private final UrlType urlType;

    @Override
    public TemplateSource sourceAt(String filename) throws IOException {
        val source = getDirectory()
            .map(
                dir -> {
                    return GitTemplateSource.builder()
                        .directory(dir)
                        .filename(filename)
                        .build();
                }
            );
        if (source.isPresent()) {
            return source.get();
        }
        return null;
    }

    public Optional<File> getDirectory() {
        val directory = new File("/tmp/enki/" + UUID.randomUUID().toString());
        directory.mkdirs();
        return Try.of(
            () -> {
                Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(directory)
                    .setTransportConfigCallback(new DefaultTransportConfigCallback())
                    .call();
                return directory;
            }
        )
            .onFailure(error -> log.error("error: {}"))
            .toOptional();
    }
}
