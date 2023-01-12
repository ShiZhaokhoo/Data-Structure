package Entity;

import Adt.ArrayList;
import Adt.ListInterface;

/**
 *
 * @author Ng Theng Yang
 */

public class GroupBuyEntity {
    private String groupBuyId;
    private String itemGroupId;
    private String groupBuyDescription;
    private int groupBuyMaxMember;
    private double groupBuyOriginalPrice;
    private int groupBuyDiscountRate;
    private double groupBuyFinalPrice;
    private ListInterface<GroupBuyMemberEntity> groupBuyMembers;
    private static int nextGroupBuyId = 1;

    public GroupBuyEntity(){
        this("", "", "", 0, 0.00, 0, 0.00);
    }
    
    public GroupBuyEntity(String groupBuyId, String itemGroupId, String groupBuyDescription, int groupBuyMaxMember, double groupBuyOriginalPrice, int groupBuyDiscount, double groupBuyFinalPrice){
        this.groupBuyId = groupBuyId;
        this.itemGroupId = itemGroupId;
        this.groupBuyDescription = groupBuyDescription;
        this.groupBuyMaxMember = groupBuyMaxMember;
        this.groupBuyOriginalPrice = groupBuyOriginalPrice;
        this.groupBuyDiscountRate = groupBuyDiscount;
        this.groupBuyFinalPrice = groupBuyFinalPrice;
        this.groupBuyMembers = new ArrayList();
        if(!groupBuyId.equals(""))
            nextGroupBuyId+=1;
    }
    
    public String getGroupBuyId() {
        return groupBuyId;
    }

    public String getGroupBuyDescription() {
        return groupBuyDescription;
    }

    public void setGroupBuyDescription(String groupBuyDescription) {
        this.groupBuyDescription = groupBuyDescription;
    }

    public int getGroupBuyMaxMember() {
        return groupBuyMaxMember;
    }

    public void setGroupBuyMaxMember(int groupBuyMaxMember) {
        this.groupBuyMaxMember = groupBuyMaxMember;
    }

    public Double getGroupBuyOriginalPrice() {
        return groupBuyOriginalPrice;
    }

    public void setGroupBuyOriginalPrice(Double groupBuyOriginalPrice) {
        this.groupBuyOriginalPrice = groupBuyOriginalPrice;
    }

    public int getGroupBuyDiscountRate() {
        return groupBuyDiscountRate;
    }

    public void setGroupBuyDiscountRate(int groupBuyDiscountRate) {
        this.groupBuyDiscountRate = groupBuyDiscountRate;
    }

    public Double getGroupBuyFinalPrice() {
        return groupBuyFinalPrice;
    }

    public void setGroupBuyFinalPrice(Double groupBuyFinalPrice) {
        this.groupBuyFinalPrice = groupBuyFinalPrice;
    }

    public GroupBuyMemberEntity[] getMemberEntity() {
        int numberOfMembersData = groupBuyMembers.getNumberOfEntries();
        GroupBuyMemberEntity[] membersData = new GroupBuyMemberEntity[numberOfMembersData];
        if(numberOfMembersData > 0){
            for(int index = 0; index < numberOfMembersData; index++){
                membersData[index] = groupBuyMembers.getEntry(index + 1);
            }
        }
        return membersData;
    }

    public void addGroupBuyMember(GroupBuyMemberEntity memberData) {
        groupBuyMembers.add(memberData);
    }
    
    public void setGroupBuyMemberCollected(int givenPosition, boolean collected){
        GroupBuyMemberEntity memberData = groupBuyMembers.getEntry(givenPosition);
        memberData.setCollected(collected);
        groupBuyMembers.replace(memberData, givenPosition);
    }
    
    public void removeGroupBuyMember(int givenPosition){
        groupBuyMembers.remove(givenPosition);
    }
    
    public void replaceGroupBuyMember(GroupBuyMemberEntity memberEntry, int replacePosition){
        groupBuyMembers.replace(memberEntry, replacePosition);
    }
    
    public int getTotalNumberOfGroupBuyMembers(){
        return groupBuyMembers.getNumberOfEntries();
    }
    
    public String getItemGroupId(){
        return itemGroupId;
    }
    
    public int getNextGroupBuyId(){
        return nextGroupBuyId;
    }
    
    
}
