package com.example.database.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class LabelOrderService {
    private final Map<String, List<String>> labelsBySection = new HashMap<>();

    public LabelOrderService() throws IOException {
        loadLabels();
    }

    public List<String> getLabels(String sectionKey) {
        return labelsBySection.getOrDefault(sectionKey, Collections.emptyList());
    }

    private void loadLabels() throws IOException {
        ClassPathResource resource = new ClassPathResource("dod-field-labels.json");
        if (!resource.exists()) {
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream inputStream = resource.getInputStream()) {
            Map<String, LinkedHashMap<String, Object>> root = mapper.readValue(
                    inputStream,
                    new TypeReference<Map<String, LinkedHashMap<String, Object>>>() {}
            );

            for (Map.Entry<String, LinkedHashMap<String, Object>> entry : root.entrySet()) {
                if (entry.getKey() == null || entry.getKey().equals("_meta")) {
                    continue;
                }
                LinkedHashMap<String, Object> section = entry.getValue();
                if (section == null) continue;
                labelsBySection.put(entry.getKey(), new ArrayList<>(section.keySet()));
            }
        }
    }
}
