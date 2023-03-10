package com.dugeun.dugeunbackend.domain.professor.lecture;

import com.dugeun.dugeunbackend.domain.common.BaseTimeEntity;
import com.dugeun.dugeunbackend.domain.professor.Professor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    private Professor professor;
}











