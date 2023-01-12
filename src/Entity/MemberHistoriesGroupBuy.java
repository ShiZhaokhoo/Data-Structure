package Entity;

/**
 *
 * @author Khoo Shi Zhao
 */

public class MemberHistoriesGroupBuy {
    private GroupBuyEntity groupBuy;
    
    public MemberHistoriesGroupBuy(){
        groupBuy = new GroupBuyEntity();
    }
    
    public MemberHistoriesGroupBuy(GroupBuyEntity groupBuy){
        this.groupBuy = groupBuy;
    }
    
    public String getGroupBuyId() {
        return groupBuy.getGroupBuyId();
    }

    public String getItemGroupId() {
        return groupBuy.getItemGroupId();
    }

    public String getGroupBuyDescription() {
        return groupBuy.getGroupBuyDescription();
    }

    public double getGroupBuyFinalPrice() {
        return groupBuy.getGroupBuyFinalPrice();
    }
    
    public String toString(){
        return groupBuy.getGroupBuyId() + ", " + groupBuy.getItemGroupId() + ", " + groupBuy.getGroupBuyDescription() + ", RM" + groupBuy.getGroupBuyFinalPrice();
    }
}
