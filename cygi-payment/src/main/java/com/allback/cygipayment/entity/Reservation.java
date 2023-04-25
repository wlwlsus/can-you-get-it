package com.allback.cygipayment.entity;

import com.allback.cygipayment.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString
@Table(name = "reservation")
public class Reservation extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id", nullable = false, length = 20, columnDefinition = "BIGINT UNSIGNED")
  private long reservationId;

  @Column(name = "concert_id", nullable = false, length = 20)
  private long concertId;

  @Column(name = "stage_id", nullable = false, length = 20)
  private long stageId;

  @Column(name = "user_id", nullable = false, length = 20)
  private long userId;

  @Column(name = "status", nullable = false, length = 150)
  private String status;

  @Column(name = "price", nullable = false)
  private int price;

  @Column(name = "seat", nullable = false, length = 10)
  private String seat;

  public void setReservation(long stageId, long userId, String status, int price) {
    this.stageId = stageId;
    this.userId = userId;
    this.status = status;
    this.price = price;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
