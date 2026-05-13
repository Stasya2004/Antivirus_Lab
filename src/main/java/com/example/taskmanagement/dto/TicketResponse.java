package com.example.taskmanagement.dto;

/**
 * DTO для передачи подписанного тикета клиенту.
 * Содержит сам тикет и его цифровую подпись (ЭЦП).
 */
public class TicketResponse {
    private Ticket ticket;
    private String signature;

    // Конструктор по умолчанию (необходим для десериализации JSON)
    public TicketResponse() {}

    // Конструктор с параметрами
    public TicketResponse(Ticket ticket, String signature) {
        this.ticket = ticket;
        this.signature = signature;
    }

    // Геттеры и сеттеры
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}