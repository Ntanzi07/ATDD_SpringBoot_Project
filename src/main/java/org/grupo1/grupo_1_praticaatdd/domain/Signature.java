package org.grupo1.grupo_1_praticaatdd.domain;

import jakarta.persistence.*;
import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;

@Entity
@Table(name = "signatures")
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignaturePlan plan;


    @Column(nullable = false)
    private Integer CourseCredits;

    @Column(nullable = false)
    private Integer successFinishedCourses;

    @Column(nullable = false)
    private Integer coins;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    //CONSTRUCTORS
    public Signature(User user) {
        this.plan = SignaturePlan.BASIC;
        CourseCredits = 0;
        this.successFinishedCourses = 0;
        this.coins = 0;
        this.user = user;
    }

    //GETTERS
    public Long getId() {
        return id;
    }

    public SignaturePlan getPlan() {
        return plan;
    }

    public Integer getCourseCredits() {
        return CourseCredits;
    }

    public Integer getSuccessFinishedCourses() {
        return successFinishedCourses;
    }

    public Integer getCoins() {
        return coins;
    }

    public User getUser() {
        return user;
    }

    //SETTERS
    public void setPlan(SignaturePlan plan) {
        this.plan = plan;
    }

    public void setCourseCredits(Integer courseCredits) {
        CourseCredits = courseCredits;
    }

    public void setSuccessFinishedCourses(Integer successFinishedCourses) {
        this.successFinishedCourses = successFinishedCourses;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public void setUser(User user) {
        this.user = user;
    }

    //HELPERS
    public void addingCredits(int quantity) {
        this.CourseCredits += quantity;
    }

    public void usingCredits() {
        if (this.CourseCredits <= 0)
            throw new IllegalStateException("User without credits for a new bonus course");
        this.CourseCredits--;
    }

    public void addingConclusionSuccess() {
        this.successFinishedCourses++;
        addingCredits(3);
        if (this.successFinishedCourses >= 12) {
            this.plan = SignaturePlan.PREMIUM;
        }
    }

    public void addingCoins(int quantity) {
        this.coins += quantity;
    }


}
