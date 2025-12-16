package com.example.PureLift.controller;

import com.example.PureLift.entity.ExerciseTemplate;
import com.example.PureLift.entity.Muscle;
import com.example.PureLift.entity.ExerciseTemplateMuscle;
import com.example.PureLift.dto.ExerciseTemplateLiteDTO;
import com.example.PureLift.dto.MuscleLiteDTO;
import com.example.PureLift.dto.ExerciseTemplateMuscleDTO;
import com.example.PureLift.repository.ExerciseTemplateRepository;
import com.example.PureLift.repository.MuscleRepository;
import com.example.PureLift.repository.ExerciseTemplateMuscleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/catalog")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CatalogController {

    private final ExerciseTemplateRepository exerciseTemplateRepository;
    private final MuscleRepository muscleRepository;
    private final ExerciseTemplateMuscleRepository exerciseTemplateMuscleRepository;

    public CatalogController(ExerciseTemplateRepository exerciseTemplateRepository,
                             MuscleRepository muscleRepository,
                             ExerciseTemplateMuscleRepository exerciseTemplateMuscleRepository) {
        this.exerciseTemplateRepository = exerciseTemplateRepository;
        this.muscleRepository = muscleRepository;
        this.exerciseTemplateMuscleRepository = exerciseTemplateMuscleRepository;
    }

    @GetMapping("/exercise-templates")
    public ResponseEntity<List<ExerciseTemplateLiteDTO>> getExerciseTemplates() {
        List<ExerciseTemplateLiteDTO> result = exerciseTemplateRepository.findAll().stream()
                .map(et -> {
                    ExerciseTemplateLiteDTO dto = new ExerciseTemplateLiteDTO();
                    dto.setId(et.getId());
                    dto.setName(et.getName());
                    dto.setDescription(et.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/muscles")
    public ResponseEntity<List<MuscleLiteDTO>> getMuscles() {
        List<MuscleLiteDTO> result = muscleRepository.findAll().stream()
                .map(m -> {
                    MuscleLiteDTO dto = new MuscleLiteDTO();
                    dto.setId(m.getId());
                    dto.setName(m.getName());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exercise-muscles")
    public ResponseEntity<List<ExerciseTemplateMuscleDTO>> getExerciseTemplateMuscles() {
        List<ExerciseTemplateMuscleDTO> result = exerciseTemplateMuscleRepository.findAll().stream()
                .map(link -> {
                    ExerciseTemplateMuscleDTO dto = new ExerciseTemplateMuscleDTO();
                    dto.setId(link.getId());
                    dto.setIntensity(link.getIntensity());
                    if (link.getExerciseTemplate() != null) {
                        dto.setExerciseTemplateId(link.getExerciseTemplate().getId());
                        dto.setExerciseTemplateName(link.getExerciseTemplate().getName());
                    }
                    if (link.getMuscle() != null) {
                        dto.setMuscleId(link.getMuscle().getId());
                        dto.setMuscleName(link.getMuscle().getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exercise/{exerciseId}/muscles")
    public ResponseEntity<List<ExerciseTemplateMuscleDTO>> getMusclesForExercise(@PathVariable Long exerciseId) {
        ExerciseTemplate exercise = exerciseTemplateRepository.findById(exerciseId)
                .orElse(null);

        if (exercise == null) {
            return ResponseEntity.notFound().build();
        }

        List<ExerciseTemplateMuscleDTO> result = exercise.getTargetedMuscles().stream()
                .map(link -> {
                    ExerciseTemplateMuscleDTO dto = new ExerciseTemplateMuscleDTO();
                    dto.setId(link.getId());
                    dto.setIntensity(link.getIntensity());
                    if (link.getExerciseTemplate() != null) {
                        dto.setExerciseTemplateId(link.getExerciseTemplate().getId());
                        dto.setExerciseTemplateName(link.getExerciseTemplate().getName());
                    }
                    if (link.getMuscle() != null) {
                        dto.setMuscleId(link.getMuscle().getId());
                        dto.setMuscleName(link.getMuscle().getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
