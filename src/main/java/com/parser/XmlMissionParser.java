package com.parser;

import com.exception.InvalidMissionFormatException;
import com.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

public class XmlMissionParser implements MissionParser {
    private final XmlMapper xmlMapper;

    public XmlMissionParser () {
        this.xmlMapper = new XmlMapper();
        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public Mission parse(File file) throws InvalidMissionFormatException {
        try {
            JsonNode root = xmlMapper.readTree(file);
            Mission mission = new Mission();

            // Основные поля
            mission.setMissionId(getString(root, "missionId"));
            mission.setDate(getString(root, "date"));
            mission.setLocation(getString(root, "location"));
            mission.setOutcome(getString(root, "outcome"));

            Long damage = getString(root, "damageCost") == null ? 0L : Long.parseLong(getString(root, "damageCost"));
            if (damage != null) {
                mission.setDamageCost(damage);
            }

            // Проклятие
            JsonNode curseNode = root.get("curse");
            if (curseNode != null && !curseNode.isNull()) {
                String curseName = getString(curseNode, "name");
                String threatLevel = getString(curseNode, "threatLevel");
                if (curseName != null) {
                    mission.setCurse(new Curse(curseName, threatLevel));
                }
            }

            // Маги
            JsonNode sorcerersNode = root.get("sorcerers");
            if (sorcerersNode != null && !sorcerersNode.isNull()) {
                JsonNode sorcererNode = sorcerersNode.get("sorcerer");
                if (sorcererNode != null && !sorcererNode.isNull()) {
                    if (sorcererNode.isArray()) {
                        for (JsonNode s : sorcererNode) {
                            String name = getString(s, "name");
                            String rank = getString(s, "rank");
                            if (name != null) {
                                mission.addSorcerer(new Sorcerer(name, rank));
                            }
                        }
                    } else {
                        String name = getString(sorcererNode, "name");
                        String rank = getString(sorcererNode, "rank");
                        if (name != null) {
                            mission.addSorcerer(new Sorcerer(name, rank));
                        }
                    }
                }
            }

            // Техники
            JsonNode techniquesNode = root.get("techniques");
            if (techniquesNode != null && !techniquesNode.isNull()) {
                JsonNode techniqueNode = techniquesNode.get("technique");
                if (techniqueNode != null && !techniqueNode.isNull()) {
                    if (techniqueNode.isArray()) {
                        for (JsonNode t : techniqueNode) {
                            String name = getString(t, "name");
                            String type = getString(t, "type");
                            String owner = getString(t, "owner");
                            Long techDamage = Long.valueOf(getString(t, "damage"));
                            if (name != null) {
                                mission.addTechnique(new Technique(name, type, owner, techDamage != null ? techDamage : 0L));
                            }
                        }
                    } else {
                        String name = getString(techniqueNode, "name");
                        String type = getString(techniqueNode, "type");
                        String owner = getString(techniqueNode, "owner");
                        Long techDamage = Long.valueOf(getString(techniqueNode, "damage"));
                        if (name != null) {
                            mission.addTechnique(new Technique(name, type, owner, techDamage != null ? techDamage : 0L));
                        }
                    }
                }
            }

            // Economic Assessment
            JsonNode economicNode = root.get("economicAssessment");
            if (economicNode != null && !economicNode.isNull()) {
                EconomicAssessment ea = new EconomicAssessment();
                ea.setTotalDamageCost(getLong(economicNode, "totalDamageCost"));
                ea.setInfrastructureDamage(getLong(economicNode, "infrastructureDamage"));
                ea.setCommercialDamage(getLong(economicNode, "commercialDamage"));
                ea.setTransportDamage(getLong(economicNode, "transportDamage"));
                ea.setRecoveryEstimateDays(getInt(economicNode, "recoveryEstimateDays"));
                ea.setInsuranceCovered(getBoolean(economicNode, "insuranceCovered"));
                mission.setEconomicAssessment(ea);
            }

            // Дополнительные поля
            String comment = getString(root, "comment");
            if (comment != null) {
                mission.addExtension("comment", comment);
            }

            String note = getString(root, "note");
            if (note != null) {
                mission.addExtension("note", note);
            }

            return mission;

        } catch (IOException e) {
            throw new InvalidMissionFormatException("Ошибка парсинга XML: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean supportsFormat(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".xml");
    }

    private String getString(JsonNode node, String field) {
        if (node == null || node.isNull()) return null;
        JsonNode fieldNode = node.get(field);
        return (fieldNode != null && !fieldNode.isNull()) ? fieldNode.asText() : null;
    }

    private Long getLong(JsonNode node, String field) {
        if (node == null || node.isNull()) return null;
        JsonNode fieldNode = node.get(field);
        if (fieldNode != null && !fieldNode.isNull() && fieldNode.isNumber()) {
            return fieldNode.asLong();
        }
        return null;
    }

    private Integer getInt(JsonNode node, String field) {
        if (node == null || node.isNull()) return null;
        JsonNode fieldNode = node.get(field);
        if (fieldNode != null && !fieldNode.isNull() && fieldNode.isNumber()) {
            return fieldNode.asInt();
        }
        return null;
    }

    private Boolean getBoolean(JsonNode node, String field) {
        if (node == null || node.isNull()) return null;
        JsonNode fieldNode = node.get(field);
        if (fieldNode != null && !fieldNode.isNull() && fieldNode.isBoolean()) {
            return fieldNode.asBoolean();
        }
        return null;
    }
}