package com.havryliv.auction.service;

public interface MailService {

    void sendMail(String content, String mailBody, String emailTo);

}
