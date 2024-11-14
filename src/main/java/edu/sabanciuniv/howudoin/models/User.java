package edu.sabanciuniv.howudoin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {
    private String name;
    private String lastName;

    @Id
    private String email;

    private String password;
    private List<String> friends;
}
