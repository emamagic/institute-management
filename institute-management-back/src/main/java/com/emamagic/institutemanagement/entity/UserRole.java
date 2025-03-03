package com.emamagic.institutemanagement.entity;

import com.emamagic.institutemanagement.feature.user.role.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class UserRole {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleType type;


    public String getName() {
        return type.name();
    }
}
