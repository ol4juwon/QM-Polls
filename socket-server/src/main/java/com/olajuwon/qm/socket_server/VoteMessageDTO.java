package com.olajuwon.qm.socket_server;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoteMessageDTO {
    private String username;
    private String option;
    private String action;
}
