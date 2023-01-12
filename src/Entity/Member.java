package Entity;

import Adt.CircularArrayQueue;
import Adt.QueueInterface;
import java.util.Iterator;

/**
 *
 * @author Khoo Shi Zhao
 */

public class Member {
    private String memberId;
    private String name;
    private String icNo;
    private String phoneNo;
    private String gender;
    private static int nextMemberId = 1;
    private QueueInterface<MemberHistoriesGroupBuy> historiesGroupBuy;
    
    public Member(){
        this("","","","","");
    }
    
    public Member(String memberId, String name, String icNo, String phoneNo, String gender){
        this.memberId = memberId;
        this.name = name;
        this.icNo = icNo;
        this.phoneNo = phoneNo;
        this.gender = gender;
        this.historiesGroupBuy = new CircularArrayQueue();
        if(!memberId.equals(""))
            nextMemberId += 1;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcNo() {
        return icNo;
    }

    public void setICNo(String icNo) {
        this.icNo = icNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static int getNextMemberId() {
        return nextMemberId;
    }
    
    public void enqueueHistoriesGroupBuy(MemberHistoriesGroupBuy historyGroupBuy){
        historiesGroupBuy.enqueue(historyGroupBuy);
    }
    
    public MemberHistoriesGroupBuy[] getHistoriesGroupBuy(){
        int numberOfHistoriesData = historiesGroupBuy.getNumberOfEntries();
        MemberHistoriesGroupBuy[] historiesData = new MemberHistoriesGroupBuy[numberOfHistoriesData];
        if(numberOfHistoriesData > 0){
            Iterator<MemberHistoriesGroupBuy> historiesIterator = historiesGroupBuy.getQueueIterator();
            for(int index = 0; index < numberOfHistoriesData; index++){
                if(historiesIterator.hasNext()){
                    historiesData[index] = historiesIterator.next();
                }
            }
        }
        return historiesData;
    }
    
    public int getCountOfHistoriesGroupBuy(){
        return historiesGroupBuy.getNumberOfEntries();
    }
}
