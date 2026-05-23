package com.parser;

import com.exception.InvalidMissionFormatException;
import com.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {

            Mission mission = new Mission();

            // Определяем формат файла по первой непустой строке
            String firstLine = readFirstNonEmptyLine(reader);
            if (firstLine == null) {
                throw new InvalidMissionFormatException("Файл пуст");
            }

            // Возвращаемся к началу файла
            reader.close();
            BufferedReader newReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), "UTF-8"));

            // Определяем тип формата
            if (firstLine.startsWith("[") && firstLine.endsWith("]")) {
                // Секционный формат (как Mission A4.txt)
                parseSectionFormat(newReader, mission);
            } else if (firstLine.contains(":")) {
                // Ключ-значение формат (как Mission A.txt)
                parseKeyValueFormat(newReader, mission);
            } else {
                newReader.close();
                throw new InvalidMissionFormatException("Неизвестный формат TXT файла");
            }

            newReader.close();
            return mission;

        } catch (Exception e) {
            throw new InvalidMissionFormatException("Ошибка парсинга TXT файла: " + e.getMessage(), e);
        }
    }

    private String readFirstNonEmptyLine(BufferedReader reader) throws Exception {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                return line;
            }
        }
        return null;
    }


    private void parseSectionFormat(BufferedReader reader, Mission mission) throws Exception {
        String currentSection = null;
        Map<String, String> currentBlock = new HashMap<>();
        Sorcerer currentSorcerer = null;
        Technique currentTechnique = null;
        String line;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            // Проверка на заголовок секции [SECTION]
            if (line.startsWith("[") && line.endsWith("]")) {
                // Сохраняем предыдущий блок, если был
                if (currentSection != null) {
                    saveSectionData(mission, currentSection, currentBlock, currentSorcerer, currentTechnique);
                }

                // Начинаем новую секцию
                currentSection = line.substring(1, line.length() - 1);
                currentBlock.clear();
                currentSorcerer = null;
                currentTechnique = null;
                continue;
            }

            // Парсим строку в формате key=value
            String[] parts = line.split("=", 2);
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (currentSection != null) {
                    switch (currentSection) {
                        case "MISSION":
                            parseMissionField(mission, key, value);
                            break;
                        case "CURSE":
                            parseCurseField(mission, key, value);
                            break;
                        case "SORCERER":
                            if (currentSorcerer == null) {
                                currentSorcerer = new Sorcerer();
                            }
                            parseSorcererField(currentSorcerer, key, value);
                            break;
                        case "TECHNIQUE":
                            if (currentTechnique == null) {
                                currentTechnique = new Technique();
                            }
                            parseTechniqueField(currentTechnique, key, value);
                            break;
                        case "ENVIRONMENT":
                        case "ECONOMIC":
                        case "ECONOMIC_ASSESSMENT":
                            currentBlock.put(key, value);
                            break;
                        default:
                            // Неизвестная секция - сохраняем в расширения
                            currentBlock.put(key, value);
                            break;
                    }
                }
            }
        }

        // Сохраняем последний блок
        if (currentSection != null) {
            saveSectionData(mission, currentSection, currentBlock, currentSorcerer, currentTechnique);
        }
    }

    private void saveSectionData(Mission mission, String section,
                                 Map<String, String> block,
                                 Sorcerer sorcerer, Technique technique) {
        switch (section) {
            case "SORCERER":
                if (sorcerer != null && sorcerer.getName() != null) {
                    mission.addSorcerer(sorcerer);
                }
                break;
            case "TECHNIQUE":
                if (technique != null && technique.getName() != null) {
                    mission.addTechnique(technique);
                }
                break;
            case "ENVIRONMENT":
                mission.addExtension("environment", block);
                break;
            case "ECONOMIC":
            case "ECONOMIC_ASSESSMENT":
                mission.addExtension("economicAssessment", block);
                break;
            default:
                if (!block.isEmpty()) {
                    mission.addExtension(section.toLowerCase(), block);
                }
                break;
        }
    }

    private void parseMissionField(Mission mission, String key, String value) {
        switch (key) {
            case "missionId":
                mission.setMissionId(value);
                break;
            case "date":
                mission.setDate(value);
                break;
            case "location":
                mission.setLocation(value);
                break;
            case "outcome":
                mission.setOutcome(value);
                break;
            case "damageCost":
                try {
                    mission.setDamageCost(Long.parseLong(value));
                } catch (NumberFormatException e) {}
                break;
            default:
                mission.addExtension("mission_" + key, value);
                break;
        }
    }

    private void parseCurseField(Mission mission, String key, String value) {
        if (mission.getCurse() == null) {
            mission.setCurse(new Curse());
        }
        switch (key) {
            case "name":
                mission.getCurse().setName(value);
                break;
            case "threatLevel":
                mission.getCurse().setThreatLevel(value);
                break;
            default:
                mission.addExtension("curse_" + key, value);
                break;
        }
    }

    private void parseSorcererField(Sorcerer sorcerer, String key, String value) {
        switch (key) {
            case "name":
                sorcerer.setName(value);
                break;
            case "rank":
                sorcerer.setRank(value);
                break;
        }
    }

    private void parseTechniqueField(Technique technique, String key, String value) {
        switch (key) {
            case "name":
                technique.setName(value);
                break;
            case "type":
                technique.setType(value);
                break;
            case "owner":
                technique.setOwner(value);
                break;
            case "damage":
                try {
                    technique.setDamage(Long.parseLong(value));
                } catch (NumberFormatException e) {}
                break;
        }
    }


    private void parseKeyValueFormat(BufferedReader reader, Mission mission) throws Exception {
        // Временные хранилища для магов и техник
        Map<Integer, Sorcerer> sorcerersMap = new HashMap<>();
        Map<Integer, Technique> techniquesMap = new HashMap<>();

        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            parseKeyValueLine(line, mission, sorcerersMap, techniquesMap);
        }

        // Добавляем магов в миссию
        for (Sorcerer s : sorcerersMap.values()) {
            if (s.getName() != null) {
                mission.addSorcerer(s);
            }
        }

        // Добавляем техники в миссию
        for (Technique t : techniquesMap.values()) {
            if (t.getName() != null) {
                mission.addTechnique(t);
            }
        }
    }

    private void parseKeyValueLine(String line, Mission mission,
                                   Map<Integer, Sorcerer> sorcerersMap,
                                   Map<Integer, Technique> techniquesMap) {
        String[] parts = line.split(":", 2);
        if (parts.length < 2) return;

        String key = parts[0].trim();
        String value = parts[1].trim();

        // Основные поля
        switch (key) {
            case "missionId":
                mission.setMissionId(value);
                return;
            case "date":
                mission.setDate(value);
                return;
            case "location":
                mission.setLocation(value);
                return;
            case "outcome":
                mission.setOutcome(value);
                return;
            case "damageCost":
                try {
                    mission.setDamageCost(Long.parseLong(value));
                } catch (NumberFormatException e) {}
                return;
            case "note":
                mission.addExtension("note", value);
                return;
            case "comment":
                mission.addExtension("comment", value);
                return;
        }

        // Проклятие
        if (key.startsWith("curse.")) {
            String field = key.substring(6);
            if (mission.getCurse() == null) {
                mission.setCurse(new Curse());
            }
            if ("name".equals(field)) {
                mission.getCurse().setName(value);
            } else if ("threatLevel".equals(field)) {
                mission.getCurse().setThreatLevel(value);
            }
            return;
        }

        // Маги: sorcerer[0].name
        Pattern sorcererPattern = Pattern.compile("sorcerer\\[(\\d+)\\]\\.(\\w+)");
        Matcher sm = sorcererPattern.matcher(key);
        if (sm.matches()) {
            int index = Integer.parseInt(sm.group(1));
            String field = sm.group(2);

            sorcerersMap.putIfAbsent(index, new Sorcerer());
            Sorcerer sorcerer = sorcerersMap.get(index);

            if ("name".equals(field)) {
                sorcerer.setName(value);
            } else if ("rank".equals(field)) {
                sorcerer.setRank(value);
            }
            return;
        }

        // Техники: technique[0].name
        Pattern techniquePattern = Pattern.compile("technique\\[(\\d+)\\]\\.(\\w+)");
        Matcher tm = techniquePattern.matcher(key);
        if (tm.matches()) {
            int index = Integer.parseInt(tm.group(1));
            String field = tm.group(2);

            techniquesMap.putIfAbsent(index, new Technique());
            Technique technique = techniquesMap.get(index);

            switch (field) {
                case "name":
                    technique.setName(value);
                    break;
                case "type":
                    technique.setType(value);
                    break;
                case "owner":
                    technique.setOwner(value);
                    break;
                case "damage":
                    try {
                        technique.setDamage(Long.parseLong(value));
                    } catch (NumberFormatException e) {}
                    break;
            }
            return;
        }

        // Любые другие поля - в расширения
        mission.addExtension(key, value);
    }

    @Override
    public boolean supportsFormat(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".txt");
    }
}