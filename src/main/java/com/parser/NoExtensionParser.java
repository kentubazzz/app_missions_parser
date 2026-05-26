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

        if (firstLine == null || firstLine.trim().isEmpty()) {
            throw new InvalidMissionFormatException("Файл пуст");
        }

        firstLine = firstLine.trim();

        // ========== 1. СЕКЦИОННЫЙ ФОРМАТ (начинается с [ и заканчивается ]) ==========
        if (firstLine.startsWith("[") && firstLine.endsWith("]")) {
            return new TxtMissionParser().parse(file);
        }

        // ========== 2. PIPE ФОРМАТ (с разделителем | и событиями) ==========
        if (firstLine.contains("|") && firstLine.contains("MISSION_CREATED")) {
            return new EventMissionParser().parse(file);
        }

        // ========== 3. JSON ФОРМАТ ==========
        if (firstLine.startsWith("{")) {
            return new JsonMissionParser().parse(file);
        }

        // ========== 4. XML ФОРМАТ ==========
        if (firstLine.startsWith("<")) {
            return new XmlMissionParser().parse(file);
        }

        // ========== 5. YAML ФОРМАТ ==========
        if (firstLine.contains(":") && (firstLine.contains("- ") || firstLine.contains("  ") || firstLine.matches("^[a-zA-Z]+:.*"))) {
            return new YamlMissionParser().parse(file);
        }

        // ========== 6. TXT КЛЮЧ-ЗНАЧЕНИЕ ФОРМАТ ==========
        if (firstLine.contains(":")) {
            return new TxtMissionParser().parse(file);
        }

        // ========== 7. НЕИЗВЕСТНЫЙ ФОРМАТ ==========
        throw new InvalidMissionFormatException(
                "Не удалось определить формат файла: " + file.getName() +
                        "\nПервая строка: " + (firstLine.length() > 50 ? firstLine.substring(0, 50) + "..." : firstLine)
        );
    }

    private String readFirstLine(File file) throws InvalidMissionFormatException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String line = reader.readLine();
            return line != null ? line : "";
        } catch (Exception e) {
            throw new InvalidMissionFormatException("Ошибка чтения файла: " + e.getMessage());
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        String name = file.getName().toLowerCase();

        // Файлы без расширения или с расширением .tmp
        if (!name.contains(".") || name.endsWith(".tmp")) {
            return true;
        }

        // Файлы с неизвестным расширением (не зарегистрированным)
        return !name.endsWith(".json") &&
                !name.endsWith(".xml") &&
                !name.endsWith(".txt") &&
                !name.endsWith(".yaml") &&
                !name.endsWith(".yml");
    }
}