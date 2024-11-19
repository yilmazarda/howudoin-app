package edu.sabanciuniv.howudoin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessage {
    private Integer id;
    private String content;
    private String senderEmail;
    private long timeSpan;
    private Integer groupId;

}
