package com.factory;

import com.exception.UnsupportedFormatException;
import com.parser.MissionParser;
import com.parser.*;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ParserFactory {
    private static final Map<String, MissionParser> PARSERS = new HashMap<>();

    static {
        // Регистрация парсеров по расширению
        register("json", new JsonMissionParser());
        register("xml", new XmlMissionParser());
        register("txt", new TxtMissionParser());
        register("yaml", new YamlMissionParser());
        register("yml", new YamlMissionParser());
    }

    public static void register(String extension, MissionParser parser) {
        PARSERS.put(extension.toLowerCase(), parser);
    }

    public static MissionParser getParser(File file) {
        String extension = getExtension(file);

        // 1. Пробуем найти парсер по расширению
        MissionParser parser = PARSERS.get(extension);
        if (parser != null) {
            return parser;
        }

        // 2. Для .tmp файлов или файлов без расширения - используем NoExtensionParser
        if (extension.isEmpty() || extension.equals("tmp")) {
            NoExtensionParser fallback = new NoExtensionParser();
            if (fallback.supportsFormat(file)) {
                return fallback;
            }
        }

        // 3. Не удалось определить парсер
        throw new UnsupportedFormatException(
                "Неподдерживаемый формат: " + (extension.isEmpty() ? "без расширения" : extension) +
                        ". Доступные: " + PARSERS.keySet()
        );
    }

    private static String getExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf('.');
        if (lastDot > 0) {
            return name.substring(lastDot + 1).toLowerCase();
        }
        return ""; // Файл без расширения
    }
}