package com.parser;

import com.exception.InvalidMissionFormatException;
import com.model.Mission;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class NoExtensionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        String firstLine = readFirstLine(file);

        // Event формат (с разделителем | и событиями)
        if (firstLine.contains("|") && firstLine.contains("MISSION_CREATED")) {
            return new EventMissionParser().parse(file);
        }
        // JSON
        else if (firstLine.trim().startsWith("{")) {
            return new JsonMissionParser().parse(file);
        }
        // XML
        else if (firstLine.trim().startsWith("<")) {
            return new XmlMissionParser().parse(file);
        }
        // TXT (ключ: значение)
        else if (firstLine.contains(":") && !firstLine.contains("|") && !firstLine.contains("[")) {
            return new TxtMissionParser().parse(file);
        }
        // YAML
        else if (firstLine.contains(":") && (firstLine.contains("- ") || firstLine.contains("  "))) {
            return new YamlMissionParser().parse(file);
        }
        else {
            throw new InvalidMissionFormatException("Не удалось определить формат файла: " + file.getName());
        }
    }

    private String readFirstLine(File file) throws InvalidMissionFormatException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            return reader.readLine();
        } catch (Exception e) {
            throw new InvalidMissionFormatException("Ошибка чтения файла: " + e.getMessage());
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        String name = file.getName();
        return !name.contains(".") ||
                (!name.endsWith(".json") && !name.endsWith(".xml") &&
                        !name.endsWith(".txt") && !name.endsWith(".yaml") && !name.endsWith(".yml"));
    }
}