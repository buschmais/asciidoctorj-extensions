package com.buschmais.asciidoctorj.extensions;

import org.asciidoctor.Asciidoctor;

/**
 * The extension registry.
 */
public class ExtensionRegistry implements org.asciidoctor.extension.spi.ExtensionRegistry {

    @Override
    public void register(Asciidoctor asciidoctor) {
        asciidoctor.javaExtensionRegistry().inlineMacro("url", UrlInlineMacroProcessor.class);
    }
}
