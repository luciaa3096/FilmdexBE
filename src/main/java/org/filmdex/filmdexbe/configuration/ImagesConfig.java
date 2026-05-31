package org.filmdex.filmdexbe.configuration;

import java.util.List;

public record ImagesConfig(
        String baseUrl,
        String secureBase_url,
        List<String> backdropSizes,
        List<String> logoSizes,
        List<String> posterSizes,
        List<String> profileSizes,
        List<String> stillSizes
) {
}
