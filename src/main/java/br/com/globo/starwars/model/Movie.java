package br.com.globo.starwars.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie implements Serializable {
    @JsonProperty("episode_id")
    private Long id;

    private String title;

    @JsonProperty("opening_crawl")
    private String openingCrawl;

    private String director;

    private String producer;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("characters")
    private List<String> cast;
}