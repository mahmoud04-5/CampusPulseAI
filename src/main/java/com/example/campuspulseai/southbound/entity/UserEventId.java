package com.example.campuspulseai.southbound.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEventId implements Serializable {

    private Long userId;
    private Long eventId;
}
