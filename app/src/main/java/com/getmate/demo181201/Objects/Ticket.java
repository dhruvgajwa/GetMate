package com.getmate.demo181201.Objects;

public class Ticket {
    String ticketFId=" ";
    String ticketId=" ";
    int noOfTickets=0;
    String createdAt=" ";

    String bName=" ";
    String bFId=" ";
    String bHandle=" ";
    String bProfilePic=" ";
    String bEmail=" ";
    String bPhone=" ";

    String cName=" ";
    String cFId=" ";
    String cHandle=" ";
    String cProfilePic=" ";
    String cEmail=" ";
    String cPhone=" ";

    String eTitle=" ";
    String eAddress=" ";
    String eTimeStamp=" ";

    double pricePerTicket=0.00;
    double convenienceFeesPerTicket=0.00;
    double totalAmountPaid=0.00;

    String paidBy=" ";
    String paidTo=" ";
    String orderId=" ";
    String custId=" ";
    String bANKTXNID=" ";

    public Ticket() {
    }

    public Ticket(String ticketFId,
                  String ticketId,
                  int noOfTickets,
                  String createdAt,
                  String BName,
                  String BFId,
                  String BHandle,
                  String BProfilePic,
                  String BEmail,
                  String BPhone,
                  String CName,
                  String CFId,
                  String CHandle,
                  String CProfilePic,
                  String CEmail,
                  String CPhone,
                  String ETitle,
                  String EAddress,
                  String ETimeStamp,
                  double pricePerTicket,
                  double convenienceFeesPerTicket,
                  double totalAmountPaid,
                  String paidBy,
                  String paidTo,
                  String orderId,
                  String custId,
                  String BANKTXNID) {
        this.ticketFId = ticketFId;
        ticketId = ticketId;
        noOfTickets = noOfTickets;
        createdAt = createdAt;
        this.bName = BName;
        this.bFId = BFId;
        this.bHandle = BHandle;
        this.bProfilePic = BProfilePic;
        this.bEmail = BEmail;
        this.bPhone = BPhone;
        this.cName = CName;
        this.cFId = CFId;
        this.cHandle = CHandle;
        this.cProfilePic = CProfilePic;
        this.cEmail = CEmail;
        this.cPhone = CPhone;
        this.eTitle = ETitle;
        this.eAddress = EAddress;
        this.eTimeStamp = ETimeStamp;
        this.pricePerTicket = pricePerTicket;
        this.convenienceFeesPerTicket = convenienceFeesPerTicket;
        this.totalAmountPaid = totalAmountPaid;
        this.paidBy = paidBy;
        this.paidTo = paidTo;
        this.orderId = orderId;
        this.custId = custId;
        this.bANKTXNID = BANKTXNID;
    }

    public String getTicketFId() {
        return ticketFId;
    }

    public void setTicketFId(String ticketFId) {
        this.ticketFId = ticketFId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public void setNoOfTickets(int noOfTickets) {
        this.noOfTickets = noOfTickets;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbFId() {
        return bFId;
    }

    public void setbFId(String bFId) {
        this.bFId = bFId;
    }

    public String getbHandle() {
        return bHandle;
    }

    public void setbHandle(String bHandle) {
        this.bHandle = bHandle;
    }

    public String getbProfilePic() {
        return bProfilePic;
    }

    public void setbProfilePic(String bProfilePic) {
        this.bProfilePic = bProfilePic;
    }

    public String getbEmail() {
        return bEmail;
    }

    public void setbEmail(String bEmail) {
        this.bEmail = bEmail;
    }

    public String getbPhone() {
        return bPhone;
    }

    public void setbPhone(String bPhone) {
        this.bPhone = bPhone;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getcFId() {
        return cFId;
    }

    public void setcFId(String cFId) {
        this.cFId = cFId;
    }

    public String getcHandle() {
        return cHandle;
    }

    public void setcHandle(String cHandle) {
        this.cHandle = cHandle;
    }

    public String getcProfilePic() {
        return cProfilePic;
    }

    public void setcProfilePic(String cProfilePic) {
        this.cProfilePic = cProfilePic;
    }

    public String getcEmail() {
        return cEmail;
    }

    public void setcEmail(String cEmail) {
        this.cEmail = cEmail;
    }

    public String getcPhone() {
        return cPhone;
    }

    public void setcPhone(String cPhone) {
        this.cPhone = cPhone;
    }

    public String geteTitle() {
        return eTitle;
    }

    public void seteTitle(String eTitle) {
        this.eTitle = eTitle;
    }

    public String geteAddress() {
        return eAddress;
    }

    public void seteAddress(String eAddress) {
        this.eAddress = eAddress;
    }

    public String geteTimeStamp() {
        return eTimeStamp;
    }

    public void seteTimeStamp(String eTimeStamp) {
        this.eTimeStamp = eTimeStamp;
    }

    public double getPricePerTicket() {
        return pricePerTicket;
    }

    public void setPricePerTicket(double pricePerTicket) {
        this.pricePerTicket = pricePerTicket;
    }

    public double getConvenienceFeesPerTicket() {
        return convenienceFeesPerTicket;
    }

    public void setConvenienceFeesPerTicket(double convenienceFeesPerTicket) {
        this.convenienceFeesPerTicket = convenienceFeesPerTicket;
    }

    public double getTotalAmountPaid() {
        return totalAmountPaid;
    }

    public void setTotalAmountPaid(double totalAmountPaid) {
        this.totalAmountPaid = totalAmountPaid;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getbANKTXNID() {
        return bANKTXNID;
    }

    public void setbANKTXNID(String bANKTXNID) {
        this.bANKTXNID = bANKTXNID;
    }
}
