package training.g2.dto.Request.Voucher;

import java.util.List;

public class AssignVoucherUsersReq {
    private List<String> emails;

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }
}
