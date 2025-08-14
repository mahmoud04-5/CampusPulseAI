package com.example.campuspulseai.southbound.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Table(name = "roles")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    private Long id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
