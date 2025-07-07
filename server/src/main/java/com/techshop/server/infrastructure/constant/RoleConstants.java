package com.techshop.server.infrastructure.constant;

import lombok.Getter;

import java.util.List;

public class RoleConstants {

    public static final String ACTOR_ADMIN = "ADMIN";

    public static final String ACTOR_MEMBER = "MEMBER";

    @Getter
    public static List<String> defaultRoles = List.of(
            ACTOR_ADMIN,
            ACTOR_MEMBER
    );

    @Getter
    public static List<String> defaultRolesForStaff = List.of(
            ACTOR_ADMIN
    );

}
