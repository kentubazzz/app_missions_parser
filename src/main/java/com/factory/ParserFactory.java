package com.factory;

import com.exception.UnsupportedFormatException;
import com.parser.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ParserFactory {
    private static final Map<String, MissionParser> PARSERS = new HashMap<>();

    private static final NoExtensionParser FALLBACK_PARSER = new NoExtensionParser();

    static {

        // Регистрация парсеров по расширению
        register("json", new JsonMissionParser());
        register("xml", new XmlMissionParser());
        register("txt", new TxtMissionParser());
        register("yaml", new YamlMissionParser());
        register("yml", new YamlMissionParser());

    }

    public static void register(String extension, MissionParser parser) {
        String ext = extension.toLowerCase();
        PARSERS.put(ext, parser);
    }

    public static MissionParser getParser(File file) {
        String extension = getExtension(file);

        // Сначала ищем по расширению



        MissionParser parser = PARSERS.get(extension);




        if (parser != null) {
            return parser;
        }

        // Если не нашли по расширению, пробуем определить по содержимому
        if (FALLBACK_PARSER.supportsFormat(file)) {
            return FALLBACK_PARSER;
        }

        throw new UnsupportedFormatException(
                "Неподдерживаемый формат: " + extension +
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