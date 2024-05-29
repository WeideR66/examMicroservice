package org.notification.notificator.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.notification.notificator.dto.LogDTO;
import org.notification.notificator.service.LogOut;
import org.slf4j.event.Level;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LogOutService implements LogOut {

    @Override
    public void printLog(LogDTO logInfo) {
        log.atLevel(Level.valueOf(logInfo.getLevel())).log(logInfo.getMessage());
    }

}
