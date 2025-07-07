package com.techshop.server.infrastructure.config.database;

import com.techshop.server.entity.Role;
import com.techshop.server.infrastructure.config.database.repository.RoleDefaultRepository;
import com.techshop.server.infrastructure.constant.EntityStatus;
import com.techshop.server.infrastructure.constant.RoleConstants;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultDatabaseData {

    private final RoleDefaultRepository roleDefaultRepository;

    List<String> defaultRoles = RoleConstants.getDefaultRoles();

    @PostConstruct
    public void generateDefaultRoles() {
        defaultRoles.forEach(role -> {
            if (roleDefaultRepository.findByCode(role).isEmpty()) {
                Role roleDefault = new Role();
                roleDefault.setCode(role);
                roleDefault.setName(role);
                roleDefault.setEntityStatus(EntityStatus.NOT_DELETED);
                roleDefaultRepository.save(roleDefault);
            }
        });
    }


}
