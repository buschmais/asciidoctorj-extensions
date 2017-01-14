package com.buschmais.asciidoctorj.extensions;

import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.ast.AbstractBlock;
import org.asciidoctor.extension.InlineMacroProcessor;

/**
 * Inline macro processor which adds links matching patterns.
 */
public class UrlInlineMacroProcessor extends InlineMacroProcessor {

    /**
     * Constructor.
     * 
     * @param macroName
     *            The name of the macro.
     * @param config
     *            The configuration.
     */
    public UrlInlineMacroProcessor(String macroName, Map<String, Object> config) {
        super(macroName, config);
    }

    @Override
    protected Object process(AbstractBlock parent, String target, Map<String, Object> attributes) {
        Map<String, String> urlMappings = getUrlMappings(parent);
        String url = replace(target, urlMappings);
        Map<String, Object> options = new HashMap<>();
        options.put("type", ":link");
        options.put("target", url);
        return createInline(parent, "anchor", target, attributes, options).convert();
    }

    /**
     * Extracts the URL mappings from the document attributes.
     * 
     * @param parent
     *            The parent to extract the 'url-mapping' attribute.
     * @return A map containing patterns as keys and the replacement patterns as
     *         values.
     * @throws IllegalStateException
     *             If no URL mappings are provided.
     */
    private Map<String, String> getUrlMappings(AbstractBlock parent) {
        String urlMappings = (String) parent.getDocument().getAttributes().get("url-mappings");
        if (urlMappings == null) {
            throw new IllegalStateException("URL Inline Macro (" + getName() + "): attribute url-mappings not found.");
        }
        Map<String, String> result = new HashMap<>();
        for (String urlMapping : urlMappings.trim().split(",")) {
            String[] mapping = urlMapping.split("=");
            String pattern = mapping[0].trim();
            String replacement = mapping[1].trim();
            result.put(pattern, replacement);
        }
        return result;
    }

    /**
     * Replaces the target with a URL that matches by one of the given URL
     * mappings.
     * 
     * @param target
     *            The target value to replace.
     * @param urlMappings
     *            The URL mappings.
     * @return The replaced value.
     * @throws IllegalArgumentException
     *             If no matching URL mapping exists.
     */
    private String replace(String target, Map<String, String> urlMappings) {
        for (Map.Entry<String, String> entry : urlMappings.entrySet()) {
            String pattern = entry.getKey();
            String replacement = entry.getValue();
            if (target.matches(pattern)) {
                return target.replaceAll(pattern, replacement);
            }
        }
        throw new IllegalArgumentException("URL Inline Macro (" + getName() + "): cannot find URL mapping for '" + target + "'");
    }

}
