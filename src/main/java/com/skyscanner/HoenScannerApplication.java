package com.skyscanner;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HoenScannerApplication extends Application<HoenScannerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public String getName() {
        return "hoen-scanner";
    }

    @Override
    public void initialize(final Bootstrap<HoenScannerConfiguration> bootstrap) {
        // Application initialization code can go here
    }

    @Override
    public void run(final HoenScannerConfiguration configuration, final Environment environment) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        // Load and deserialize rental cars data
        List<SearchResult> carResults = Arrays.asList(
            mapper.readValue(
                getClass().getClassLoader().getResource("rental_cars.json"),
                SearchResult[].class
            )
        );

        // Load and deserialize hotels data
        List<SearchResult> hotelResults = Arrays.asList(
            mapper.readValue(
                getClass().getClassLoader().getResource("hotels.json"),
                SearchResult[].class
            )
        );

        // Combine both lists into a unified list
        List<SearchResult> searchResults = new ArrayList<>();
        searchResults.addAll(carResults);
        searchResults.addAll(hotelResults);

        // Instantiate and register the API endpoint resource with Jersey
        final SearchResource resource = new SearchResource(searchResults);
        environment.jersey().register(resource);
    }
}
