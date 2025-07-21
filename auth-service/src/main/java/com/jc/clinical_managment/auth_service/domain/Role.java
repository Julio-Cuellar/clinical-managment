package com.jc.clinical_managment.auth_service.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;



public enum Role {

    ADMIN,
    SUPER_ADMIN

}
