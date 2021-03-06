package com.github.eduardtsoy.heroability.incoming;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.eduardtsoy.heroability.incoming.part.HeroIn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeroesIn {

    // Hypermedia navigation
    private String next;

    // Data
    private List<HeroIn> data = new ArrayList<>();
}
