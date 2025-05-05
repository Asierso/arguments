package com.asier.arguments.argumentsbackend.services.discussions.components;

import java.util.Map;

public record BenedictData(Map.Entry<String, Integer> winner, boolean draw){}