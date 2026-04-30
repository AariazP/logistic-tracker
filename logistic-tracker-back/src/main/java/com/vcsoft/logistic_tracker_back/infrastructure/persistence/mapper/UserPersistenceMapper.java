package com.vcsoft.logistic_tracker_back.infrastructure.persistence.mapper;

import com.vcsoft.logistic_tracker_back.domain.model.AppUser;
import com.vcsoft.logistic_tracker_back.domain.model.UserRole;
import com.vcsoft.logistic_tracker_back.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public UserEntity toEntity(AppUser user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }

    public AppUser toDomain(UserEntity entity) {
        return AppUser.reconstitute(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                UserRole.valueOf(entity.getRole()),
                entity.getCreatedAt()
        );
    }
}