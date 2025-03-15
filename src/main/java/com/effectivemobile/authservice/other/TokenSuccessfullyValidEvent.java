package com.effectivemobile.authservice.other;

import com.effectivemobile.authservice.entity.OneTimeTokenDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class TokenSuccessfullyValidEvent extends ApplicationEvent {

    private final OneTimeTokenDto oneTimeTokenDto;

    @Autowired
    public TokenSuccessfullyValidEvent(Object source, OneTimeTokenDto oneTimeTokenDto) {
        super(source);
        this.oneTimeTokenDto = oneTimeTokenDto;
    }
}
