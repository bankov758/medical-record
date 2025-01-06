package org.example.medicalrecord.web.view.model;

import lombok.*;

import java.util.Set;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class UserViewModel {

    private long id;

    private String firstName;

    private String lastName;

    private String username;

    private Set<String> authorities;

}
