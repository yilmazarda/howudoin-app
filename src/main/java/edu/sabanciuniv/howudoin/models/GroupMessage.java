package edu.sabanciuniv.howudoin.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessage {
    @Id
    private String id;

    private String content;
    private String senderEmail;

    private LocalDateTime sentAt;

    private String groupId;
}
