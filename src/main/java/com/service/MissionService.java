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

            if (missionRepository.existsByMissionId(mission.getMissionId())) {
                throw new InvalidMissionFormatException("Миссия с ID " + mission.getMissionId() + " уже существует");
            }

            MissionEntity entity = missionMapper.toEntity(mission);
            MissionEntity savedEntity = missionRepository.save(entity);
            saveRelatedEntities(savedEntity, mission);
            MissionEntity updatedEntity = missionRepository.save(savedEntity);

            return missionMapper.toResponse(updatedEntity);

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
        if (sourceMission.getCurse() != null) {
            CurseEntity curseEntity = missionMapper.toCurseEntity(sourceMission.getCurse());
            curseEntity.setMission(mission);
            curseRepository.save(curseEntity);
            mission.setCurse(curseEntity);
        }

        for (Sorcerer sorcerer : sourceMission.getSorcerers()) {
            SorcererEntity entity = missionMapper.toSorcererEntity(sorcerer);
            SorcererEntity existingSorcerer = sorcererRepository
                    .findByNameAndRank(entity.getName(), entity.getRank())
                    .orElse(null);

            if (existingSorcerer != null) {
                mission.addSorcerer(existingSorcerer);
            } else {
                SorcererEntity savedSorcerer = sorcererRepository.save(entity);
                mission.addSorcerer(savedSorcerer);
            }
        }

        for (Technique technique : sourceMission.getTechniques()) {
            TechniqueEntity entity = missionMapper.toTechniqueEntity(technique);
            TechniqueEntity existingTechnique = techniqueRepository
                    .findByNameAndTypeAndOwner(entity.getName(), entity.getType(), entity.getOwner())
                    .orElse(null);

            if (existingTechnique != null) {
                mission.addTechnique(existingTechnique);
            } else {
                TechniqueEntity savedTechnique = techniqueRepository.save(entity);
                mission.addTechnique(savedTechnique);
            }
        }

        if (sourceMission.getEconomicAssessment() != null) {
            EconomicAssessmentEntity entity = missionMapper.toEconomicAssessmentEntity(sourceMission.getEconomicAssessment());
            entity.setMission(mission);
            economicAssessmentRepository.save(entity);
            mission.setEconomicAssessment(entity);
        }
    }

    public List<MissionResponse> getAllMissions() {
        // Загружаем базовые данные миссий
        List<MissionEntity> missions = missionRepository.findAllWithBasicData();

        // Для каждой миссии загружаем колдунов и техники отдельными запросами
        for (MissionEntity mission : missions) {
            MissionEntity missionWithSorcerers = missionRepository.findByIdWithSorcerers(mission.getId())
                    .orElse(mission);
            MissionEntity missionWithTechniques = missionRepository.findByIdWithTechniques(mission.getId())
                    .orElse(mission);

            mission.setSorcerers(missionWithSorcerers.getSorcerers());
            mission.setTechniques(missionWithTechniques.getTechniques());
        }

        return missions.stream()
                .map(missionMapper::toResponse)
                .collect(Collectors.toList());
    }

    public MissionResponse getMissionById(Long id) throws MissionNotFoundException {
        // Загружаем миссию с базовыми данными
        MissionEntity mission = missionRepository.findById(id)
                .orElseThrow(() -> new MissionNotFoundException("Миссия не найдена с ID: " + id));

        // Загружаем колдунов
        MissionEntity missionWithSorcerers = missionRepository.findByIdWithSorcerers(id)
                .orElse(mission);
        mission.setSorcerers(missionWithSorcerers.getSorcerers());

        // Загружаем техники
        MissionEntity missionWithTechniques = missionRepository.findByIdWithTechniques(id)
                .orElse(mission);
        mission.setTechniques(missionWithTechniques.getTechniques());

        return missionMapper.toResponse(mission);
    }

    public MissionResponse getMissionByMissionId(String missionId) throws MissionNotFoundException {
        MissionEntity mission = missionRepository.findByMissionId(missionId)
                .orElseThrow(() -> new MissionNotFoundException("Миссия не найдена: " + missionId));

        MissionEntity missionWithSorcerers = missionRepository.findByMissionIdWithSorcerers(missionId)
                .orElse(mission);
        mission.setSorcerers(missionWithSorcerers.getSorcerers());

        MissionEntity missionWithTechniques = missionRepository.findByMissionIdWithTechniques(missionId)
                .orElse(mission);
        mission.setTechniques(missionWithTechniques.getTechniques());

        return missionMapper.toResponse(mission);
    }

    @Transactional
    public void deleteMission(Long id) throws MissionNotFoundException {
        MissionEntity mission = missionRepository.findById(id)
                .orElseThrow(() -> new MissionNotFoundException("Миссия не найдена с ID: " + id));

        mission.getSorcerers().clear();
        mission.getTechniques().clear();

        missionRepository.delete(mission);
    }
}