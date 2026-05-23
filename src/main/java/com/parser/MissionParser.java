package com.parser;

import com.exception.InvalidMissionFormatException;
import com.model.Mission;

import java.io.File;

public interface MissionParser {
    Mission parse(File file) throws InvalidMissionFormatException;
    boolean supportsFormat(File file);
}