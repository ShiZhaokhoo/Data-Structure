package Entity;

/**
 *
 * @author Ng Theng Yang
 */

public class GroupBuyMemberEntity {
    private Member member;
    private boolean collected;
    
    public GroupBuyMemberEntity(){
        member = new Member();
        collected = false;
    }
    
    public GroupBuyMemberEntity(Member member){
        this.member = member;
        this.collected = false;
    }
    
    public String getMemberId() {
        return member.getMemberId();
    }

    public String getName() {
        return member.getName();
    }

    public String getIcNo() {
        return member.getIcNo();
    }

    public String getPhoneNo() {
        return member.getPhoneNo();
    }

    public String getGender() {
        return member.getGender();
    }
    
    public void setCollected(Boolean collected){
        this.collected = collected;
    }
    
    public boolean getCollected(){
        return collected;
    }
}
