package com.buschmais.asciidoctorj.extensions;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.internal.IOUtils;
import org.junit.Test;

public class UrlInlineMacroProcessorTest {

    private static final File BASE_DIRECTORY = new File("target/test-classes/");

    private static final String DOCUMENT = "urlInlineMacroProcessorTest";

    @Test
    public void render() throws FileNotFoundException {
        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        OptionsBuilder optionsBuilder = OptionsBuilder.options().mkDirs(true).baseDir(BASE_DIRECTORY);
        String urlMappings = "jQA-(\\d*)=https://github.com/buschmais/jqassistant/issues/$1,\n"
                + "XO-(\\d*)=https://github.com/buschmais/extended-objects/issues/$1\n";
        AttributesBuilder attributesBuilder = AttributesBuilder.attributes().attribute("url-mappings", urlMappings);
        optionsBuilder.attributes(attributesBuilder);
        File adocFile = new File(BASE_DIRECTORY, DOCUMENT + ".adoc");
        asciidoctor.renderFile(adocFile, optionsBuilder);
        File htmlFile = new File(BASE_DIRECTORY, DOCUMENT + ".html");
        assertThat("Expecting an HTML file " + htmlFile.getPath(), htmlFile.exists(), equalTo(true));
        String html = IOUtils.readFull(new FileReader(htmlFile));
        assertThat("Expecting a link for 'url:jQA-123[]'.", html,
                containsString("<a href=\"https://github.com/buschmais/jqassistant/issues/123\">jQA-123</a>"));
        assertThat("Expecting a link for 'url:XO-456[]'.", html,
                containsString("<a href=\"https://github.com/buschmais/extended-objects/issues/456\">XO-456</a>"));
    }
}
