package org.pojo;

public class Transaction {
    private int senderId;
    private int receiverId;
    private int amount;
    private String senderName;
    private String receiverName;


    public Transaction(int senderId, int receiverId, int amount) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public Transaction(){}

    public int getAmount() {
        return amount;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public int getSenderId() {
        return senderId;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String toString() {
        return String.format("senderId: %d, receiverId: %d, amount: %d", senderId, receiverId, amount);
    }
}
