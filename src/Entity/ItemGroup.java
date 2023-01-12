package Entity;

/**
 *
 * @author Yeap Ying Thung
 */

public class ItemGroup {
    private String itemGroupId;
    private String itemGroupDescription;
    private int itemGroupMaxMember;
    private double itemGroupOriginalPrice;
    private int itemGroupDiscountRate;
    private double itemGroupFinalPrice;
    private int itemGroupTotalTransaction = 0;
    private static int nextItemGroupId = 1;
    
    public ItemGroup(){
        this("","",0,0.00,0,0.00);
    }
    
    public ItemGroup(String itemGroupId,String itemGroupDescription, int itemGroupMaxMember,double itemGroupOriginalPrice, int itemGroupDiscountRate, double itemGroupFinalPrice){
        this.itemGroupId = itemGroupId;
        this.itemGroupDescription = itemGroupDescription;
        this.itemGroupMaxMember = itemGroupMaxMember;
        this.itemGroupOriginalPrice = itemGroupOriginalPrice;
        this.itemGroupDiscountRate = itemGroupDiscountRate;
        this.itemGroupFinalPrice = itemGroupFinalPrice;
        if(!itemGroupId.equals(""))
            nextItemGroupId++;
    }

    public String getItemGroupId() {
        return itemGroupId;
    }

    public String getItemGroupDescription() {
        return itemGroupDescription;
    }

    public void setItemGroupDescription(String itemGroupDescription) {
        this.itemGroupDescription = itemGroupDescription;
    }
    
    public int getItemGroupMaxMember() {
        return itemGroupMaxMember;
    }

    public void setItemGroupMaxMember(int itemGroupMaxMember) {
        this.itemGroupMaxMember = itemGroupMaxMember;
    }

    public double getItemGroupOriginalPrice() {
        return itemGroupOriginalPrice;
    }

    public void setItemGroupOriginalPrice(double itemGroupOriginalPrice) {
        this.itemGroupOriginalPrice = itemGroupOriginalPrice;
    }

    public int getItemGroupDiscountRate() {
        return itemGroupDiscountRate;
    }

    public void setItemGroupDiscountRate(int itemGroupDiscountRate) {
        this.itemGroupDiscountRate = itemGroupDiscountRate;
    }

    public double getItemGroupFinalPrice() {
        return itemGroupFinalPrice;
    }

    public void setItemGroupFinalPrice(double itemGroupFinalPrice) {
        this.itemGroupFinalPrice = itemGroupFinalPrice;
    }

    public int getItemGroupTotalTransaction() {
        return itemGroupTotalTransaction;
    }

    public void addItemGroupTotalTransaction() {
        itemGroupTotalTransaction++;
    }
    
    public int getNextItemGroupId(){
        return nextItemGroupId;
    }
    
}
