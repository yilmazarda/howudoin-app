package edu.sabanciuniv.howudoin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    private String id;

    private String senderEmail;       // Sender's email address
    private String content;           // Message content


    @CreatedDate
    private LocalDateTime sentAt;    // Time when the message was sent


    private String receiverEmail;        // Receiver's email (for direct messages)
}
