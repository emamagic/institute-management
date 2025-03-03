package com.emamagic.institutemanagement.entity;

import com.emamagic.institutemanagement.common.BaseEntity;
import com.emamagic.institutemanagement.feature.user.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "users")
public class UserApp extends BaseEntity implements UserDetails {
    private String name;
    /// it automatically creates a unique index on email column
    @Column(nullable = false, unique = true)
    @Email(message = "The email is not valid")
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String hashedPassword;
    private String age;
    @Column(nullable = false)
    private Boolean isVerified;
    @Column(nullable = false)
    private Boolean isApproved;
    @Column(nullable = false)
    private Boolean isProfileCompleted;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName()));
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public String getUsername() {
        return getId().toString();
    }

    @Override
    public boolean isEnabled() {
        return isVerified && isApproved;
    }

    @PrePersist
    public void setDefaultFields() {
        if (isVerified == null) isVerified = false;
        if (isApproved == null) isApproved = false;
        if (isProfileCompleted == null) isProfileCompleted = false;
    }

    @PreUpdate
    public void setIsProfileCompleted() {
        if (name != null
                && !name.isBlank()
                && gender != null
                && hashedPassword != null
                && !hashedPassword.isBlank()
                && email != null
                && !email.isBlank()
                && age != null
                && !age.isBlank()) {
            isProfileCompleted = true;
        }
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (object == null) return false;
        Class<?> oEffectiveClass = object instanceof HibernateProxy ? ((HibernateProxy) object).getHibernateLazyInitializer().getPersistentClass() : object.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserApp userApp = (UserApp) object;
        return getId() != null && Objects.equals(getId(), userApp.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
