package com.user.matching;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "match.service.find-matches")
@Getter
@Setter
public class MatchConfig {

    private int threads;

}
