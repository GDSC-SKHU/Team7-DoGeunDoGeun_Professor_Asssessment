package com.dugeun.dugeunbackend.api.professor.service;

import com.dugeun.dugeunbackend.api.ability.dto.AbilityListDto;
import com.dugeun.dugeunbackend.api.comment.dto.CommentListDto;
import com.dugeun.dugeunbackend.api.common.dto.RspsTemplate;
import com.dugeun.dugeunbackend.api.common.dto.SingleRspsTemplate;
import com.dugeun.dugeunbackend.api.professor.dto.AddAssessmentDto;
import com.dugeun.dugeunbackend.api.professor.dto.MainPageProfessorDto;
import com.dugeun.dugeunbackend.api.professor.dto.ProfessorDetailDto;
import com.dugeun.dugeunbackend.domain.professor.Major;
import com.dugeun.dugeunbackend.domain.professor.Professor;
import com.dugeun.dugeunbackend.domain.professor.ProfessorService;
import com.dugeun.dugeunbackend.domain.professor.ability.Ability;
import com.dugeun.dugeunbackend.domain.professor.ability.AbilityRepository;
import com.dugeun.dugeunbackend.domain.professor.ability.AbilityService;
import com.dugeun.dugeunbackend.domain.professor.comment.Comment;
import com.dugeun.dugeunbackend.domain.professor.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ApiProfessorService {

    private final ProfessorService professorService;
    private final AbilityService abilityService;
    private final AbilityRepository abilityRepository;
    private final CommentService commentService;

    @Transactional
    public RspsTemplate<MainPageProfessorDto> findAllByMajor(Major major) {
        List<Professor> professors;
        if (major == null){
            professors = professorService.findAll();
        } else {
            professors = professorService.findAllByMajor(major);
        }

        List<MainPageProfessorDto> mainPageProfessorDtos = MainPageProfessorDto.ofList(professors);

        RspsTemplate<MainPageProfessorDto> response = RspsTemplate.<MainPageProfessorDto>builder()
                .status(HttpStatus.OK.value())
                .data(mainPageProfessorDtos)
                .build();

        return response;
    }

    @Transactional
    public SingleRspsTemplate<ProfessorDetailDto> findDetailById(Long id) {
        System.out.println("============??????===========");
        Instant start = Instant.now();

        // id??? ??????, ????????? ???????????? ?????? detailDto ??????
        Professor professor = professorService.findByIdFetchComment(id);

        // ?????? ????????? ????????? Dto??? ?????????.
        AbilityListDto avgAbilityDto = findAvgByProfessorId(id);
        // ?????? ????????? ???????????? Rating??? ????????????.
        Integer sum = avgAbilityDto.getSum(); // rating??? ????????? ?????? ???
        int rating = (int) Math.round(sum / 5.0 / 20.0);
        avgAbilityDto.setRating(rating); // rating??? ????????? ??????

        List<CommentListDto> commentListDtos = CommentListDto.ofList(professor.getComments());

        ProfessorDetailDto professorDetailDto = ProfessorDetailDto.builder()
                .professorName(professor.getProfessorName())
                .major(professor.getMajor())
                .ability(avgAbilityDto)
                .comments(commentListDtos)
                .build();
        Instant end = Instant.now();

        System.out.println("????????????: " + Duration.between(start, end).toMillis() + " ms");
        return SingleRspsTemplate.<ProfessorDetailDto>builder()
                .status(HttpStatus.OK.value())
                .data(professorDetailDto)
                .build();
    }

    @Transactional
    public void addAssessment(Long id, AddAssessmentDto addAssessmentDto) {
        Professor professor = professorService.findById(id);

        Ability ability = addAssessmentDto.toEntity(professor);
        abilityService.save(ability);

        Comment comment = Comment.builder()
                .content(addAssessmentDto.getCommentContent())
                .professor(professor)
                .build();

        commentService.save(comment);
    }

    // DTO??? ??????????????? API??????????????? Repository ??????
    public AbilityListDto findAvgByProfessorId(Long id) {
        return abilityRepository.findAvgByProfessorId(id);
    }
}




















