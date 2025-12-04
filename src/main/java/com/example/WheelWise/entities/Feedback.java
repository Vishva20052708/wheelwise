package com.example.WheelWise.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "FEEDBACK")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "RATING", nullable = false)
    private Integer rating; // Rating as stars (1-5)

    @Column(name = "EXPERIENCE", columnDefinition = "TEXT")
    private String experience;

    @Column(name = "BOOKING_EASY", nullable = false)
    private Boolean bookingEasy;

    @Column(name = "CAR_CONDITION", nullable = false)
    private Boolean carCondition;

    @Column(name = "SUPPORT_HELPFUL", nullable = false)
    private Boolean supportHelpful;

    @Column(name = "RECOMMEND", nullable = false)
    private Boolean recommend;
    @Column(name = "TIMELINESS")
    private String timeliness; // Options: Excellent, Good, Poor, Could be better

    @Column(name = "COMFORT")
    private String comfort; // Options: Excellent, Good, Poor, Could be better

   
    public String getTimeliness() {
		return timeliness;
	}

	public void setTimeliness(String timeliness) {
		this.timeliness = timeliness;
	}

	public String getComfort() {
		return comfort;
	}

	public void setComfort(String comfort) {
		this.comfort = comfort;
	}

	@ManyToOne
    @JoinColumn(name = "BOOKING_ID", nullable = false)
    private Booking booking;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Boolean getBookingEasy() {
        return bookingEasy;
    }

    public void setBookingEasy(Boolean bookingEasy) {
        this.bookingEasy = bookingEasy;
    }

    public Boolean getCarCondition() {
        return carCondition;
    }

    public void setCarCondition(Boolean carCondition) {
        this.carCondition = carCondition;
    }

    public Boolean getSupportHelpful() {
        return supportHelpful;
    }

    public void setSupportHelpful(Boolean supportHelpful) {
        this.supportHelpful = supportHelpful;
    }

    public Boolean getRecommend() {
        return recommend;
    }

    public void setRecommend(Boolean recommend) {
        this.recommend = recommend;
    }

    

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
