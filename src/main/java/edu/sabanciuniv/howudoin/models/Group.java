package edu.sabanciuniv.howudoin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class Group {
    @Id
    private String groupId;

    private String name;

    private List<String> users = new ArrayList<>();

    private List<GroupMessage> groupMessages = new ArrayList<>();
}
