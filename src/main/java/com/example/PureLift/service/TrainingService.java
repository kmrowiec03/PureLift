package com.example.PureLift.service;


import com.example.PureLift.dto.*;
import com.example.PureLift.entity.*;
import com.example.PureLift.exception.TrainingPlanNotFoundException;
import com.example.PureLift.repository.ExerciseRepository;
import com.example.PureLift.repository.ExerciseTemplateRepository;
import com.example.PureLift.repository.TrainingDayRepository;
import com.example.PureLift.repository.TrainingPlanRepository;
import com.example.PureLift.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    private TrainingPlanRepository trainingPlanRepository;
    private TrainingDayRepository trainingDayRepository;
    private ExerciseTemplateRepository exerciseTemplateRepository;
    private UserRepository userRepository;

    public TrainingService(TrainingPlanRepository trainingPlanRepository,
                           TrainingDayRepository trainingDayRepository,
                           ExerciseTemplateRepository exerciseTemplateRepository,
                           UserRepository userRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.trainingDayRepository = trainingDayRepository;
        this.exerciseTemplateRepository = exerciseTemplateRepository;
        this.userRepository = userRepository;
    }
    public TrainingPlan getTrainingPlanById(Long id) {
        return trainingPlanRepository.findById(id).orElse(null);
    }

    public TrainingDay getTrainingDayById(Long planId, Long dayId) {
        return trainingPlanRepository.findById(planId)
                .map(plan -> plan.getTrainingDays().stream()
                        .filter(day -> day.getId().equals(dayId)).findFirst().orElse(null)).orElse(null);
    }

    public List<TrainingPlan> getTrainingPlansByUserName(String userName) {
        return trainingPlanRepository.findByUser_Username(userName);
    }
    public List<TrainingPlan> getTrainingPlansByUserEmail(String email) {
        return trainingPlanRepository.findByUser_Email(email);
    }


    public TrainingPlanDTO convertToDTO(TrainingPlan trainingPlan) {
        TrainingPlanDTO dto = new TrainingPlanDTO();
        dto.setId(trainingPlan.getId());
        dto.setTitle(trainingPlan.getTitle());
        dto.setTrainingDays(
                trainingPlan.getTrainingDays().stream()
                        .map(this::convertDayToDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }

    private TrainingDayDTO convertDayToDTO(TrainingDay trainingDay) {
        TrainingDayDTO dto = new TrainingDayDTO();
        dto.setId(trainingDay.getId());
        dto.setDayNumber(trainingDay.getDayNumber());
        dto.setWeekNumber(trainingDay.getWeekNumber());
        dto.setExercises(
                trainingDay.getExercises().stream()
                        .map(this::convertExerciseToDTO)
                        .collect(Collectors.toList())
        );
        return dto;
    }
    private ExerciseDTO convertExerciseToDTO(Exercise exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setId(exercise.getId());
        dto.setSets(exercise.getSets());
        dto.setReps(exercise.getReps());
        dto.setWeight(exercise.getWeight());

        dto.setExerciseName(exercise.getExerciseTemplate().getName());
        dto.setDescription(exercise.getExerciseTemplate().getDescription());

        List<MuscleDTO> muscleDTOs = exercise.getExerciseTemplate().getTargetedMuscles().stream()
                .map(etm -> {
                    MuscleDTO muscleDTO = new MuscleDTO();
                    muscleDTO.setName(etm.getMuscle().getName());
                    muscleDTO.setIntensity(etm.getIntensity());
                    return muscleDTO;
                }).collect(Collectors.toList());

        dto.setMusclesTargeted(muscleDTOs);
        dto.setCompletedDate(exercise.getCompletedDate());

        return dto;
    }


    public TrainingPlan generatePlan(User user, TrainingPlanRequest request) {
        List<ExerciseTemplate> allTemplates = exerciseTemplateRepository.findAll();
        if (allTemplates.isEmpty()) {
            throw new IllegalArgumentException("Brak szablonów ćwiczeń w bazie danych.");
        }

        // Domyślne wartości
        String planType = request.getPlanType() != null ? request.getPlanType() : "FULL_BODY";
        int numberOfWeeks = (request.getNumberOfWeeks() != null && request.getNumberOfWeeks() > 0) ? request.getNumberOfWeeks() : 1;
        
        // Ustaw domyślną wartość w request, żeby była dostępna w metodach generujących
        if (request.getNumberOfWeeks() == null || request.getNumberOfWeeks() <= 0) {
            request.setNumberOfWeeks(1);
        }

        TrainingPlan plan = new TrainingPlan();
        plan.setTitle(request.getTitle());
        plan.setUser(user);

        List<TrainingDay> days = new ArrayList<>();

        if ("UPPER_LOWER".equals(planType)) {
            // Plan typu Upper/Lower Split
            generateUpperLowerPlan(days, plan, allTemplates, request);
        } else {
            // Plan typu Full Body (domyślny)
            generateFullBodyPlan(days, plan, allTemplates, request);
        }

        plan.setTrainingDays(days);
        return trainingPlanRepository.save(plan);
    }

    private void generateFullBodyPlan(List<TrainingDay> days, TrainingPlan plan,
                                    List<ExerciseTemplate> allTemplates, TrainingPlanRequest request) {
        int totalDays = request.getNumberOfDays() * request.getNumberOfWeeks();

        // Przygotuj ćwiczenia dla każdego dnia w tygodniu (żeby były identyczne w każdym tygodniu)
        List<List<ExerciseTemplate>> exercisesPerDayInWeek = new ArrayList<>();
        for (int d = 1; d <= request.getNumberOfDays(); d++) {
            List<ExerciseTemplate> shuffledTemplates = new ArrayList<>(allTemplates);
            Collections.shuffle(shuffledTemplates);
            int count = Math.min(request.getExercisesPerDay(), shuffledTemplates.size());
            List<ExerciseTemplate> dayExercises = new ArrayList<>(shuffledTemplates.subList(0, count));
            exercisesPerDayInWeek.add(dayExercises);
        }

        for (int i = 1; i <= totalDays; i++) {
            int weekNumber = (i - 1) / request.getNumberOfDays() + 1;
            int dayInWeek = ((i - 1) % request.getNumberOfDays()) + 1;

            TrainingDay day = new TrainingDay();
            day.setDayNumber(dayInWeek);  // Zamiast i, użyj dayInWeek, żeby numeracja dni zaczynała się od 1 w każdym tygodniu
            day.setWeekNumber(weekNumber);
            day.setTrainingPlan(plan);

            // Użyj przygotowanych ćwiczeń dla tego dnia w tygodniu
            List<ExerciseTemplate> dayExercises = exercisesPerDayInWeek.get(dayInWeek - 1);

            List<Exercise> exercisesForDay = new ArrayList<>();

            for (int j = 0; j < request.getExercisesPerDay(); j++) {
                // Jeśli skończyły się ćwiczenia, powtórz od początku
                ExerciseTemplate template = dayExercises.get(j % dayExercises.size());

                Exercise exercise = new Exercise();
                exercise.setTrainingDay(day);
                exercise.setExerciseTemplate(template);
                exercise.setSets(3);
                exercise.setReps(10);
                // weight pozostaje null - użytkownik wpisze później

                exercisesForDay.add(exercise);
            }

            day.setExercises(exercisesForDay);
            days.add(day);
        }
    }

    private void generateUpperLowerPlan(List<TrainingDay> days, TrainingPlan plan,
                                      List<ExerciseTemplate> allTemplates, TrainingPlanRequest request) {
        // Podziel ćwiczenia na upper i lower body
        List<ExerciseTemplate> upperBodyExercises = new ArrayList<>();
        List<ExerciseTemplate> lowerBodyExercises = new ArrayList<>();

        for (ExerciseTemplate template : allTemplates) {
            boolean isUpperBody = isUpperBodyExercise(template);
            if (isUpperBody) {
                upperBodyExercises.add(template);
            } else {
                lowerBodyExercises.add(template);
            }
        }

        // Jeśli nie ma ćwiczeń dla którejś grupy, użyj wszystkich
        if (upperBodyExercises.isEmpty()) upperBodyExercises = new ArrayList<>(allTemplates);
        if (lowerBodyExercises.isEmpty()) lowerBodyExercises = new ArrayList<>(allTemplates);

        // Przygotuj ćwiczenia dla każdego dnia w tygodniu
        List<List<ExerciseTemplate>> exercisesPerDayInWeek = new ArrayList<>();
        for (int d = 1; d <= request.getNumberOfDays(); d++) {
            // Parzyste dni - Upper Body, nieparzyste - Lower Body
            List<ExerciseTemplate> dayExercises = (d % 2 == 0) ? upperBodyExercises : lowerBodyExercises;
            List<ExerciseTemplate> shuffledTemplates = new ArrayList<>(dayExercises);
            Collections.shuffle(shuffledTemplates);
            int count = Math.min(request.getExercisesPerDay(), shuffledTemplates.size());
            List<ExerciseTemplate> selectedExercises = new ArrayList<>(shuffledTemplates.subList(0, count));
            exercisesPerDayInWeek.add(selectedExercises);
        }

        int totalDays = request.getNumberOfDays() * request.getNumberOfWeeks();

        for (int i = 1; i <= totalDays; i++) {
            int weekNumber = (i - 1) / request.getNumberOfDays() + 1;
            int dayInWeek = ((i - 1) % request.getNumberOfDays()) + 1;

            TrainingDay day = new TrainingDay();
            day.setDayNumber(dayInWeek);  // Numeracja dni od 1 w każdym tygodniu
            day.setWeekNumber(weekNumber);
            day.setTrainingPlan(plan);

            // Użyj przygotowanych ćwiczeń dla tego dnia w tygodniu
            List<ExerciseTemplate> dayExercises = exercisesPerDayInWeek.get(dayInWeek - 1);

            List<Exercise> exercisesForDay = new ArrayList<>();

            for (int j = 0; j < request.getExercisesPerDay(); j++) {
                ExerciseTemplate template = dayExercises.get(j % dayExercises.size());

                Exercise exercise = new Exercise();
                exercise.setTrainingDay(day);
                exercise.setExerciseTemplate(template);
                exercise.setSets(3);
                exercise.setReps(10);
                // weight pozostaje null - użytkownik wpisze później

                exercisesForDay.add(exercise);
            }

            day.setExercises(exercisesForDay);
            days.add(day);
        }
    }

    private boolean isUpperBodyExercise(ExerciseTemplate template) {
        String name = template.getName().toLowerCase();

        String[] upperBodyKeywords = {
            "chest", "pec", "shoulder", "deltoid", "back", "lat", "trap", "rhomboid",
            "bicep", "tricep", "arm", "forearm", "wrist", "abs", "abdominal", "core",
            "pull", "push", "press", "row", "curl", "extension"
        };

        for (String keyword : upperBodyKeywords) {
            if (name.contains(keyword)) {
                return true;
            }
        }

        return false;
    }

    public void deleteTrainingPlan(Long planId, String userEmail) {
        TrainingPlan plan = trainingPlanRepository.findById(planId).orElse(null);
        if (plan == null) {
            throw new TrainingPlanNotFoundException(planId);
        }

        // Sprawdź czy plan należy do zalogowanego użytkownika
        if (!plan.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("Nie masz uprawnień do usunięcia tego planu.");
        }

        trainingPlanRepository.delete(plan);
    }

    public TrainingPlan createManualPlan(User user, ManualTrainingPlanRequest request) {
        TrainingPlan plan = new TrainingPlan();
        plan.setTitle(request.getTitle());
        plan.setUser(user);

        List<TrainingDay> days = new ArrayList<>();

        for (ManualTrainingDayRequest dayRequest : request.getDays()) {
            TrainingDay day = new TrainingDay();
            day.setDayNumber(dayRequest.getDayNumber());
            day.setWeekNumber(1); // ręczne plany domyślnie 1 tydzień
            day.setTrainingPlan(plan);

            List<Exercise> exercises = new ArrayList<>();
            for (ManualExerciseRequest exerciseRequest : dayRequest.getExercises()) {
                try {
                    // Znajdź template na podstawie templateId
                    Long templateId = Long.parseLong(exerciseRequest.getTemplateId());
                    ExerciseTemplate template = exerciseTemplateRepository.findById(templateId)
                            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono szablonu ćwiczenia: " + templateId));

                    Exercise exercise = new Exercise();
                    exercise.setTrainingDay(day);
                    exercise.setExerciseTemplate(template);
                    exercise.setSets(exerciseRequest.getSeries());
                    exercise.setReps(exerciseRequest.getReps());
                    // weight pozostaje null - użytkownik wpisze później

                    exercises.add(exercise);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Nieprawidłowy templateId: " + exerciseRequest.getTemplateId());
                }
            }
            day.setExercises(exercises);
            days.add(day);
        }

        plan.setTrainingDays(days);
        return trainingPlanRepository.save(plan);
    }

    public TrainingPlan assignPlanToUser(User coach, ManualTrainingPlanRequest request) {
        // Sprawdź czy coach ma rolę COACH
        boolean hasCoachRole = coach.getAppuserRole().name().equals("ROLE_COACH");
        if (!hasCoachRole) {
            throw new SecurityException("Tylko użytkownicy z rolą COACH mogą przypisywać plany innym użytkownikom");
        }

        // Sprawdź czy podano email użytkownika docelowego
        if (request.getTargetUserEmail() == null || request.getTargetUserEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email użytkownika docelowego jest wymagany");
        }

        // Znajdź użytkownika docelowego
        User targetUser = userRepository.findByEmail(request.getTargetUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika: " + request.getTargetUserEmail()));

        // Utwórz plan dla użytkownika docelowego
        TrainingPlan plan = new TrainingPlan();
        plan.setTitle(request.getTitle());
        plan.setUser(targetUser); // Przypisz plan do użytkownika docelowego

        List<TrainingDay> days = new ArrayList<>();

        for (ManualTrainingDayRequest dayRequest : request.getDays()) {
            TrainingDay day = new TrainingDay();
            day.setDayNumber(dayRequest.getDayNumber());
            day.setWeekNumber(1);
            day.setTrainingPlan(plan);

            List<Exercise> exercises = new ArrayList<>();
            for (ManualExerciseRequest exerciseRequest : dayRequest.getExercises()) {
                try {
                    Long templateId = Long.parseLong(exerciseRequest.getTemplateId());
                    ExerciseTemplate template = exerciseTemplateRepository.findById(templateId)
                            .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono szablonu ćwiczenia: " + templateId));

                    Exercise exercise = new Exercise();
                    exercise.setTrainingDay(day);
                    exercise.setExerciseTemplate(template);
                    exercise.setSets(exerciseRequest.getSeries());
                    exercise.setReps(exerciseRequest.getReps());

                    exercises.add(exercise);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Nieprawidłowy templateId: " + exerciseRequest.getTemplateId());
                }
            }
            day.setExercises(exercises);
            days.add(day);
        }

        plan.setTrainingDays(days);
        return trainingPlanRepository.save(plan);
    }

}
