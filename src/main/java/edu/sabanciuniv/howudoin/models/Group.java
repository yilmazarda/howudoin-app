package edu.sabanciuniv.howudoin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Group {
    @Id
    private int groupId;

    private List<String> users;

    //to be added
    // private List<Message> messages;
}
