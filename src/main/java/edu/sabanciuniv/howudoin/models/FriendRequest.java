package edu.sabanciuniv.howudoin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class FriendRequest {
    @Id
    private String id;
    private String receiverEmail;
    private String senderEmail;
    private Boolean accepted;
}
