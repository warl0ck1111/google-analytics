package com.example.ga4demo.googleanalytics4.core.factories;

import com.google.analytics.data.v1beta.Dimension;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Component
@NoArgsConstructor
public class GA4DimensionsFactory {

    private List<Dimension> dimensions = new ArrayList<>();

    public GA4DimensionsFactory(String... dimensionsAlias) {
        this.dimensions = new ArrayList<>();
        buildDimensions(Arrays.asList(dimensionsAlias));
    }

    public GA4DimensionsFactory addDimensions(String... dimensionsAlias) {
        buildDimensions(Arrays.asList(dimensionsAlias));
        return this;
    }

    private void buildDimensions(List<String> dimensionsAlias) {
        dimensionsAlias.forEach(alias -> this.dimensions.add(buildDimension(alias)));
    }

    public Dimension buildDimension(String dimensionName) {
        return Dimension.newBuilder().setName(dimensionName).build();
    }


}
