package com.minko.socket.service;

import com.minko.socket.entity.NotificationEmail;

public interface MailService {

    void sendMail(NotificationEmail notificationEmail);
}
