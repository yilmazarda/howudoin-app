package edu.sabanciuniv.howudoin.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class FriendRequest {
    @Id
    private ObjectId id;

    private String receiverEmail;
    private String senderEmail;
    private Boolean accepted = false;

}
