package org.grupo1.grupo_1_praticaatdd.domain;

import jakarta.persistence.*;
import org.grupo1.grupo_1_praticaatdd.domain.enums.SignaturePlan;

@Entity
@Table(name = "signatures")
public class Signature {

    private static final int COURSES_TO_BECOME_PREMIUM = 12;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SignaturePlan plan;

    @Column(nullable = false)
    private Integer courseCredits;

    @Column(nullable = false)
    private Integer successFinishedCourses;

    @Column(nullable = false)
    private Integer coins;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    //CONSTRUCTORS
    protected Signature() {
    }

    public Signature(User user) {
        this.plan = SignaturePlan.BASIC;
        this.courseCredits = 0;
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
        return courseCredits;
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
        this.courseCredits = courseCredits;
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

    public void registerCourseCompletion() {
        this.successFinishedCourses++;

        if (shouldBePromotedToPremium()) {
            this.plan = SignaturePlan.PREMIUM;
        }
    }

    private boolean shouldBePromotedToPremium() {
        return this.plan == SignaturePlan.BASIC
                && this.successFinishedCourses >= COURSES_TO_BECOME_PREMIUM;
    }
}
