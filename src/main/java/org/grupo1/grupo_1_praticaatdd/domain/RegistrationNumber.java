package org.grupo1.grupo_1_praticaatdd.domain;

import jakarta.persistence.*;
import org.grupo1.grupo_1_praticaatdd.domain.enums.RegistrationNumberStatus;

@Entity
@Table(name = "RegistrationNumbers")
public class RegistrationNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationNumberStatus status;

    @Column
    private Double finalGrade;

    @Column(nullable = false)
    private boolean bonus;

    //CONSTRUCTORS
    protected RegistrationNumber() {
    }

    public RegistrationNumber(User user, Course course, boolean bonus) {
        this.user = user;
        this.course = course;
        this.bonus = bonus;
        this.status = RegistrationNumberStatus.IN_PROGRESS;
    }

    //GETTERS
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public RegistrationNumberStatus getRegistrationNumberStatus() {
        return status;
    }

    public Double getFinalGrade() {
        return finalGrade;
    }

    public boolean isBonus() {
        return bonus;
    }

    //SETTERS
    public void setUser(User user) {
        this.user = user;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setRegistrationNumberStatus(RegistrationNumberStatus registrationNumberStatus) {
        this.status = registrationNumberStatus;
    }

    public void setFinalGrade(Double finalGrade) {
        this.finalGrade = finalGrade;
    }

    public void setBonus(boolean bonus) {
        this.bonus = bonus;
    }

    public boolean successfullyCompleted() {
        return RegistrationNumberStatus.COMPLETED.equals(this.status)
                && this.finalGrade != null
                && this.finalGrade >= 7.0;
    }
}
