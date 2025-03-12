package com.coastee.server.chatroom.dto;

import com.coastee.server.chatroom.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class AddressElement {
    private String location;
    private String details;

    public AddressElement(final Address address) {
        if (address != null) {
            this.location = address.getLocation();
            this.details = address.getDetails();
        }
    }
}
