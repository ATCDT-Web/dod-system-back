package com.example.database.parser;

import java.util.List;

public class TemplateDefinition {
    private final String id;
    private final String name;
    private final String description;
    private final List<TemplateEntry> entries;

    public TemplateDefinition(String id, String name, String description, List<TemplateEntry> entries) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.entries = List.copyOf(entries);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<TemplateEntry> getEntries() {
        return entries;
    }
}
