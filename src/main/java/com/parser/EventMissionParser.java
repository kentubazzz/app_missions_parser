package com.parser;


import com.exception.InvalidMissionFormatException;
import com.model.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EventMissionParser implements MissionParser {

    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {

            Mission mission = new Mission();
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                lineCount++;

                String[] parts = line.split("\\|");
                if (parts.length < 2) {
                    continue;
                }

                String eventType = parts[0].trim();

                switch (eventType) {
                    case "MISSION_CREATED":
                        if (parts.length >= 4) {
                            mission.setMissionId(parts[1].trim());
                            mission.setDate(parts[2].trim());
                            mission.setLocation(parts[3].trim());
                        }
                        break;

                    case "CURSE_DETECTED":
                        if (parts.length >= 3) {
                            mission.setCurse(new Curse(parts[1].trim(), parts[2].trim()));
                        }
                        break;

                    case "SORCERER_ASSIGNED":
                        if (parts.length >= 3) {
                            mission.addSorcerer(new Sorcerer(parts[1].trim(), parts[2].trim()));
                        }
                        break;

                    case "TECHNIQUE_USED":
                        if (parts.length >= 5) {
                            long damage = 0;
                            try {
                                damage = Long.parseLong(parts[4].trim());
                            } catch (NumberFormatException e) {
                            }
                            mission.addTechnique(new Technique(parts[1].trim(), parts[2].trim(),
                                    parts[3].trim(), damage));
                        }
                        break;

                    case "MISSION_RESULT":
                        if (parts.length >= 2) {
                            mission.setOutcome(parts[1].trim());
                            for (int i = 2; i < parts.length; i++) {
                                String part = parts[i].trim();
                                if (part.startsWith("damageCost=")) {
                                    try {
                                        int cost = Integer.parseInt(part.substring(11));
                                        mission.setDamageCost(cost);
                                    } catch (NumberFormatException e) {
                                    }
                                    break;
                                }
                            }
                        }
                        break;

                    case "TIMELINE_EVENT":
                        if (parts.length >= 4) {
                            Map<String, String> event = new HashMap<>();
                            event.put("timestamp", parts[1].trim());
                            event.put("type", parts[2].trim());
                            event.put("description", parts[3].trim());
                            mission.addExtension("timeline_event", event);
                        }
                        break;

                    case "ENEMY_ACTION":
                        if (parts.length >= 3) {
                            Map<String, String> action = new HashMap<>();
                            action.put("type", parts[1].trim());
                            action.put("description", parts[2].trim());
                            mission.addExtension("enemy_action", action);
                        }
                        break;

                    case "CIVILIAN_IMPACT":
                        if (parts.length >= 2) {
                            Map<String, String> impact = new HashMap<>();
                            for (int i = 1; i < parts.length; i++) {
                                String[] kv = parts[i].trim().split("=");
                                if (kv.length == 2) {
                                    impact.put(kv[0], kv[1]);
                                }
                            }
                            mission.addExtension("civilian_impact", impact);
                        }
                        break;

                    default:
                        break;
                }
            }

            return mission;

        } catch (Exception e) {
            throw new InvalidMissionFormatException("Ошибка парсинга Event файла: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String firstLine = reader.readLine();
            boolean result = firstLine != null && firstLine.contains("|") && firstLine.contains("MISSION_CREATED");
            return result;
        } catch (Exception e) {
            return false;
        }
    }
}