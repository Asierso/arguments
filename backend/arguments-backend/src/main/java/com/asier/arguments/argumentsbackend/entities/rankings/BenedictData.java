package com.asier.arguments.argumentsbackend.entities.rankings;

import java.util.Map;

public record BenedictData(Map.Entry<String, Integer> winner, boolean draw){}