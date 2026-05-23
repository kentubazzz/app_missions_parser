package com.service;

import com.exception.InvalidMissionFormatException;
import com.exception.MissionNotFoundException;
import com.factory.ParserFactory;
import com.mapper.MissionMapper;
import com.model.*;
import com.model.dto.*;
import com.model.entity.*;
import com.parser.MissionParser;
import com.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissionService {

    private final MissionRepository missionRepository;
    private final CurseRepository curseRepository;
    private final SorcererRepository sorcererRepository;
    private final TechniqueRepository techniqueRepository;
    private final EconomicAssessmentRepository economicAssessmentRepository;
    private final MissionMapper missionMapper;

    public MissionService(MissionRepository missionRepository,
                          CurseRepository curseRepository,
                          SorcererRepository sorcererRepository,
                          TechniqueRepository techniqueRepository,
                          EconomicAssessmentRepository economicAssessmentRepository,
                          MissionMapper missionMapper) {
        this.missionRepository = missionRepository;
        this.curseRepository = curseRepository;
        this.sorcererRepository = sorcererRepository;
        this.techniqueRepository = techniqueRepository;
        this.economicAssessmentRepository = economicAssessmentRepository;
        this.missionMapper = missionMapper;
    }

    @Transactional
    public MissionResponse saveMission(MultipartFile file) throws InvalidMissionFormatException {
        File tempFile = saveToTempFile(file);

        try {
            MissionParser parser = ParserFactory.getParser(tempFile);
            Mission mission = parser.parse(tempFile);

            // Проверка на дубликат
            if (missionRepository.existsByMissionId(mission.getMissionId())) {
                throw new InvalidMissionFormatException("Миссия с ID " + mission.getMissionId() + " уже существует");
            }

            MissionEntity entity = missionMapper.toEntity(mission);
            MissionEntity savedEntity = missionRepository.save(entity);

            saveRelatedEntities(savedEntity, mission);

            return missionMapper.toResponse(savedEntity);

        } catch (Exception e) {
            throw new InvalidMissionFormatException("Ошибка обработки файла: " + e.getMessage());
        } finally {
            tempFile.delete();
        }
    }

    private File saveToTempFile(MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("mission_", ".tmp");
            file.transferTo(tempFile.toFile());
            return tempFile.toFile();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить временный файл", e);
        }
    }

    private void saveRelatedEntities(MissionEntity mission, Mission sourceMission) {
        // Сохраняем проклятие
        if (sourceMission.getCurse() != null) {
            CurseEntity curseEntity = missionMapper.toCurseEntity(sourceMission.getCurse());
            curseEntity.setMission(mission);
            curseRepository.save(curseEntity);
            mission.setCurse(curseEntity);
        }

        // Сохраняем магов
        for (Sorcerer sorcerer : sourceMission.getSorcerers()) {
            SorcererEntity entity = missionMapper.toSorcererEntity(sorcerer);
            entity.setMission(mission);
            sorcererRepository.save(entity);
            mission.addSorcerer(entity);
        }

        // Сохраняем техники
        for (Technique technique : sourceMission.getTechniques()) {
            TechniqueEntity entity = missionMapper.toTechniqueEntity(technique);
            entity.setMission(mission);
            techniqueRepository.save(entity);
            mission.addTechnique(entity);
        }

        // Сохраняем экономическую оценку
        if (sourceMission.getEconomicAssessment() != null) {
            EconomicAssessmentEntity entity = missionMapper.toEconomicAssessmentEntity(sourceMission.getEconomicAssessment());
            entity.setMission(mission);
            economicAssessmentRepository.save(entity);
            mission.setEconomicAssessment(entity);
        }

        missionRepository.save(mission);
    }

    public List<MissionResponse> getAllMissions() {
        return missionRepository.findAll().stream()
                .map(missionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MissionResponse getMissionById(Long id) throws MissionNotFoundException {
        MissionEntity entity = missionRepository.findById(id)
                .orElseThrow(() -> new MissionNotFoundException("Миссия не найдена с ID: " + id));
        return missionMapper.toResponse(entity);
    }

    public MissionResponse getMissionByMissionId(String missionId) throws MissionNotFoundException {
        MissionEntity entity = missionRepository.findByMissionId(missionId)
                .orElseThrow(() -> new MissionNotFoundException("Миссия не найдена: " + missionId));
        return missionMapper.toResponse(entity);
    }

    @Transactional
    public void deleteMission(Long id) throws MissionNotFoundException {
        if (!missionRepository.existsById(id)) {
            throw new MissionNotFoundException("Миссия не найдена с ID: " + id);
        }
        missionRepository.deleteById(id);
    }
}
