package edu.sabanciuniv.howudoin.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class FriendRequest {
    private int id;
    private String receiverEmail;
    private String senderEmail;
    private Boolean accepted;
}
