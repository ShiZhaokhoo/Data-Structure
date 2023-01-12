package GroupBuy;
import Adt.ListInterface;
import Entity.GroupBuyEntity;
import Adt.ArrayList;
import Entity.ItemGroup;
import Adt.StackInterface;
import Main.*;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ng Theng Yang
 */

public class groupBuy extends javax.swing.JFrame {
    private static final int DEFAULT_TABLE_ROWS = 5; // the table rows fixed 5
    private int jPanelNo = 0; // join = 0,modify = 1,collect = 2,history = 3
    private int currentPageNo = 1; // table page no, default is 1
    private int jTableRowIndex = 0; // table row index, default is 0, will increase when the click next page
    private boolean changedTableValue;//if used search the value will change to true
    private ItemGroup itemGroup;
    private GroupBuyEntity historyGroupBuy;
    private GroupBuyEntity currentGroupBuy;
    private GroupBuyEntity collectGroupBuy;
    private StackInterface<ItemGroup> itemGroups;
    private ListInterface<GroupBuyEntity> currentGroupBuys;
    private ListInterface<GroupBuyEntity> historyGroupBuys;
    private ListInterface<GroupBuyEntity> collectGroupBuys;
    private ListInterface<GroupBuyEntity> searchCollectGroupBuys;
    
    private JLabel[] jtfDisplayPageNo;// add = 0,remove = 1,edit = 2,remove = 3
    private JTable[] jTable;// add = 0,remove = 1,edit = 2,view = 3
    private JTextField[] jText;//remove = 0-3,edit = 4-7,view = 8-11
    private JSpinner[] jSpinner;//remove = 0-1,edit = 2-3,view = 4-5
    private JTextPane[] jTextPane;
    
    CardLayout cardLayout; // switch jplane
    
    public groupBuy() {
        initComponents();
        setLocationRelativeTo(null);
        
        initValues();
        initializeDisplay();
        
        mergeGroupBuy();
        
        setTableValue();
        cardLayout = (CardLayout)(oriPanel.getLayout());
    }
    
    private void mergeGroupBuy(){
        int totalEntriesItemGroup = itemGroups.getNumberOfEntries();
        for(int positionA = 1; positionA <= totalEntriesItemGroup; positionA++){
            int totalEntriesGroupBuy = currentGroupBuys.getNumberOfEntries();
            boolean match = false;
            itemGroup = itemGroups.getEntry(positionA);
            for(int positionB = 1; positionB <= totalEntriesGroupBuy && !match; positionB++){
                currentGroupBuy = currentGroupBuys.getEntry(positionB);
                String itemGroupIdA = itemGroup.getItemGroupId();
                String itemGroupIdB = currentGroupBuy.getItemGroupId();
                if(itemGroupIdA.equals(itemGroupIdB))
                    match = true;
            }
            if(!match){
                String groupBuyId = String.format("G%03d", currentGroupBuy.getNextGroupBuyId());
                String itemGroupId = itemGroup.getItemGroupId();
                String desc = itemGroup.getItemGroupDescription();
                int maxMem = itemGroup.getItemGroupMaxMember();
                double oriPrice = itemGroup.getItemGroupOriginalPrice();
                int discount = itemGroup.getItemGroupDiscountRate();
                double discPrice = itemGroup.getItemGroupFinalPrice();
                currentGroupBuy = new GroupBuyEntity(groupBuyId, itemGroupId, desc, maxMem, oriPrice, discount, discPrice);
                currentGroupBuys.add(currentGroupBuy);
            }
        }
    }
    
    private void initValues(){
        changedTableValue = false;
        itemGroups = mainMenu.itemGroups;
        itemGroup = new ItemGroup();
        historyGroupBuys = mainMenu.historyGroupBuys;
        historyGroupBuy = new GroupBuyEntity();
        currentGroupBuys = mainMenu.currentGroupBuys;
        currentGroupBuy = new GroupBuyEntity();
        collectGroupBuys = new ArrayList();
        searchCollectGroupBuys = new ArrayList();
        collectGroupBuy = new GroupBuyEntity();
        
        jtfDisplayPageNo = new JLabel[4];
        jTable = new JTable[4];
        jText = new JTextField[12];
        jSpinner = new JSpinner[3];
        jTextPane = new JTextPane[3];
        
        JoinDiscount.setText("4");
    }
    
    private void initializeDisplay(){
        //Label Page No
        jtfDisplayPageNo[0] = JoinPage;
        jtfDisplayPageNo[1] = EditPage;
        jtfDisplayPageNo[2] = CollectPage;
        jtfDisplayPageNo[3] = HistoryPage;  
        //Table
        jTable[0] = JoinTable;
        jTable[1] = EditTable;
        jTable[2] = CollectTable;
        jTable[3] = HistoryTable;
        //TextField
        jText[0] = EditId;
        jText[1] = EditOriPrice;
        jText[2] = EditDiscount;
        jText[3] = EditDiscPrice;
        jText[4] = CollectId;
        jText[5] = CollectOriPrice;
        jText[6] = CollectDiscount;
        jText[7] = CollectDiscPrice;
        jText[8] = HistoryId;
        jText[9] = HistoryOriPrice;
        jText[10] = HistoryDiscount;
        jText[11] = HistoryDiscPrice;
        //JRadioButton
        jSpinner[0] = EditMaxMember;
        jSpinner[1] = CollectMaxMember;
        jSpinner[2] = HistoryMaxMember;
        //JTextPane
        jTextPane[0] = EditDesc;
        jTextPane[1] = CollectDesc;
        jTextPane[2] = HistoryDesc;
    }
    
    private void setTableValue(){
        int totalGroup;
        if(jPanelNo == 0 || jPanelNo == 1){
            totalGroup = currentGroupBuys.getNumberOfEntries();
        }
        else if(jPanelNo == 2){
            totalGroup = collectGroupBuys.getNumberOfEntries();
        }
        else{
            totalGroup = historyGroupBuys.getNumberOfEntries();
        }
        int totalPageNo = (int) Math.ceil(((double)totalGroup/DEFAULT_TABLE_ROWS));
        if(currentPageNo > totalPageNo && totalGroup != 0){
            currentPageNo--;
            jTableRowIndex-=5;
        }
        DefaultTableModel model = (DefaultTableModel) jTable[jPanelNo].getModel();
        
        for(int row = 0; row < 5; row++){
            Object[] jTableData = new Object[6];
            jTableData[0] = "-";
            jTableData[1] = "-";
            jTableData[2] = "-";
            jTableData[3] = "-";
            jTableData[4] = "-";
            jTableData[5] = "-";
            if((jPanelNo == 0 || jPanelNo == 1) && getCurrentGroupData(row + jTableRowIndex + 1) != null){
                jTableData = getCurrentGroupData(row + jTableRowIndex + 1);
            }
            else if(jPanelNo == 2 && getCollectGroupBuyData(row + jTableRowIndex + 1) != null){
                jTableData = getCollectGroupBuyData(row + jTableRowIndex + 1);
            }
            else if(jPanelNo == 3 && getHistoryGroupBuyData(row + jTableRowIndex + 1) != null){
                jTableData = getHistoryGroupBuyData(row + jTableRowIndex + 1);
            }
            for(int columnIndex = 0; columnIndex < 6; columnIndex++)
                model.setValueAt(jTableData[columnIndex], row, columnIndex);
        }
        setTablePageValue();
    }
    
    private Object[] getCurrentGroupData(int givenPosition){
        Object[] resultData = null;
        if(currentGroupBuys.getEntry(givenPosition) != null){
            resultData = new Object[6];
            currentGroupBuy = currentGroupBuys.getEntry(givenPosition);
            if(jPanelNo==0)
                resultData[0] = currentGroupBuy.getItemGroupId();
            else
                resultData[0] = currentGroupBuy.getGroupBuyId();
            resultData[1] = currentGroupBuy.getGroupBuyDescription();
            resultData[2] = currentGroupBuy.getGroupBuyMaxMember();
            resultData[3] = String.format("%.2f", currentGroupBuy.getGroupBuyOriginalPrice());
            resultData[4] = currentGroupBuy.getGroupBuyDiscountRate();
            resultData[5] = String.format("%.2f", currentGroupBuy.getGroupBuyFinalPrice()); 
        }
        return resultData;
    }
    
    private Object[] getCollectGroupBuyData(int givenPosition){
        Object[] resultData = null;
        if(collectGroupBuys.getEntry(givenPosition) != null){
            resultData = new Object[6];
            collectGroupBuy = collectGroupBuys.getEntry(givenPosition);
            resultData[0] = collectGroupBuy.getGroupBuyId();
            resultData[1] = collectGroupBuy.getGroupBuyDescription();
            resultData[2] = collectGroupBuy.getGroupBuyMaxMember();
            resultData[3] = String.format("%.2f", collectGroupBuy.getGroupBuyOriginalPrice());
            resultData[4] = collectGroupBuy.getGroupBuyDiscountRate();
            resultData[5] = String.format("%.2f", collectGroupBuy.getGroupBuyFinalPrice()); 
        }
        return resultData;
    }
    
    private Object[] getHistoryGroupBuyData(int givenPosition){
        Object[] resultData = null;
        if(historyGroupBuys.getEntry(givenPosition) != null){
            resultData = new Object[6];
            historyGroupBuy = historyGroupBuys.getEntry(givenPosition);
            resultData[0] = historyGroupBuy.getGroupBuyId();
            resultData[1] = historyGroupBuy.getGroupBuyDescription();
            resultData[2] = historyGroupBuy.getGroupBuyMaxMember();
            resultData[3] = String.format("%.2f", historyGroupBuy.getGroupBuyOriginalPrice());
            resultData[4] = historyGroupBuy.getGroupBuyDiscountRate();
            resultData[5] = String.format("%.2f", historyGroupBuy.getGroupBuyFinalPrice()); 
        }
        return resultData;
    }
    
    private void setTablePageValue(){
        int totalGroup;
        if(jPanelNo == 0 || jPanelNo == 1){
            totalGroup = currentGroupBuys.getNumberOfEntries();
        }
        else if(jPanelNo == 2){
            totalGroup = collectGroupBuys.getNumberOfEntries();
        }
        else{
            totalGroup = historyGroupBuys.getNumberOfEntries();
        }
        int totalPageNo = (int) Math.ceil(((double)totalGroup/DEFAULT_TABLE_ROWS));
        if(totalGroup == 0)
            totalPageNo = 1;
        String pageText = "Page " + currentPageNo + " of " + totalPageNo;
        jtfDisplayPageNo[jPanelNo].setText(pageText);
    }
    
    private void switchjPanel(int jPanelNo){
        this.jPanelNo = jPanelNo;
        currentPageNo = 1;
        jTableRowIndex = 0;
        clearValue();
    }
    
    private void clearValue(){
        if(jPanelNo == 0){
            JoinId.setText("");
            JoinOriPrice.setText("");
            JoinDiscount.setText("4");
            JoinDiscPrice.setText("");
            JoinDesc.setText("");
            JoinMaxMember.setValue(2);
            JoinDiscount.setText("4");
        }
        else{
            for(int index = 0; index < 4; index++){
                if(index + ((jPanelNo - 1) * 4) == 2 ||index + ((jPanelNo - 1) * 4) == 6||index + ((jPanelNo - 1) * 4) == 10)
                    jText[index + ((jPanelNo - 1) * 4)].setText("4");
                else
                    jText[index + ((jPanelNo - 1) * 4)].setText("");
            }
            jTextPane[jPanelNo - 1].setText("");
            jSpinner[jPanelNo - 1].setValue(2);
        }
        currentGroupBuys = mainMenu.currentGroupBuys;
        historyGroupBuys = mainMenu.historyGroupBuys;
        collectGroupBuys = searchCollectGroupBuys;
        changedTableValue = false;
        setTableValue();
    }
    
    public void exitForm() {
        this.setVisible(false);
        new mainMenu().setVisible(true);
    }
    
    private void nextPage(){
        int totalGroup;
        if(jPanelNo == 0 || jPanelNo == 1){
            totalGroup = currentGroupBuys.getNumberOfEntries();
        }
        else if(jPanelNo == 2){
            totalGroup = collectGroupBuys.getNumberOfEntries();
        }
        else{
            totalGroup = historyGroupBuys.getNumberOfEntries();
        }
        int totalPageNo = (int) Math.ceil(((double)totalGroup/DEFAULT_TABLE_ROWS));
        if(currentPageNo < totalPageNo){
            currentPageNo++;
            jTableRowIndex+=5;
            setTableValue();
        }
        else
            JOptionPane.showMessageDialog(this, "This is Last Page Already!");
    }
    
    private void previousPage(){
        if(currentPageNo > 1){
            currentPageNo--;
            jTableRowIndex-=5;
            setTableValue();
        }
        else
            JOptionPane.showMessageDialog(this, "This is First Page Already!");
    }
    
    private void searchGroupBuy(){
        historyGroupBuys = mainMenu.historyGroupBuys;
        collectGroupBuys = searchCollectGroupBuys;
        currentGroupBuys = mainMenu.currentGroupBuys;
        GroupBuyEntity groupBuy = new GroupBuyEntity();
        ListInterface<GroupBuyEntity> searchGroupBuys = new ArrayList();
        ListInterface<GroupBuyEntity> searchResult = new ArrayList();
        String id;
        String desc;
        String oriPrice;
        int maxMember;
        
        if(jPanelNo == 0){
            id = JoinId.getText();
            desc = JoinDesc.getText();
            oriPrice = JoinOriPrice.getText();
            maxMember = (int)JoinMaxMember.getValue();
        }
        else{
            id = jText[(jPanelNo - 1) * 4].getText();
            desc = jTextPane[jPanelNo - 1].getText();
            oriPrice = jText[((jPanelNo - 1) * 4) + 1].getText();
            maxMember = (int)jSpinner[jPanelNo - 1].getValue();
        }
        
        int totalEntries;
        if(jPanelNo == 3)
            totalEntries = historyGroupBuys.getNumberOfEntries();
        else if(jPanelNo == 2)
            totalEntries = collectGroupBuys.getNumberOfEntries();
        else
            totalEntries = currentGroupBuys.getNumberOfEntries();
        boolean enqueued = false;
        int enqueueTimes = 0;
        boolean end = false;
        if(!id.isEmpty()){
            for(int position = 1; position <= totalEntries; position++){
                if(jPanelNo == 3)
                    groupBuy = historyGroupBuys.getEntry(position);
                else if(jPanelNo == 2)
                    groupBuy = collectGroupBuys.getEntry(position);
                else
                    groupBuy = currentGroupBuys.getEntry(position);
                if(id.equals(groupBuy.getItemGroupId()) && jPanelNo == 0){
                    searchResult.add(groupBuy);
                    enqueueTimes++;
                    enqueued = true;
                }
                else if(id.equals(groupBuy.getGroupBuyId())){
                    searchResult.add(groupBuy);
                    enqueueTimes++;
                    enqueued = true;
                }
                else{
                    boolean successful;
                    if(jPanelNo == 0)
                        successful = searchTools(id.toUpperCase(), groupBuy.getItemGroupId());
                    else
                        successful = searchTools(id.toUpperCase(), groupBuy.getGroupBuyId());
                    if(successful){
                        searchResult.add(groupBuy);
                        enqueued = true;
                        enqueueTimes++;
                    }
                }
            }
            if(enqueueTimes == 0){
                end = true;
            }
        }
        if(!desc.isEmpty()&&!end){
            enqueueTimes = 0;
            if(!enqueued){
                if(jPanelNo == 3){
                    searchGroupBuys = historyGroupBuys;
                }
                else if(jPanelNo == 2){
                    searchGroupBuys = collectGroupBuys;
                }
                else{
                    searchGroupBuys = currentGroupBuys;
                }
            }
            else{
                searchGroupBuys = searchResult;
                totalEntries = searchGroupBuys.getNumberOfEntries();
            }
            for(int position = 1; position <= totalEntries; position++){
                boolean checkBoth = false;
                groupBuy = searchGroupBuys.getEntry(position);
                boolean successful = false;
                if(desc.equals(groupBuy.getGroupBuyDescription())){
                    checkBoth = true;
                    enqueueTimes++;
                }
                else{
                    successful = searchTools(desc.toUpperCase(), groupBuy.getGroupBuyDescription().toUpperCase());
                    if(successful){
                        checkBoth = true;
                        enqueueTimes++;
                    }
                }
                if(checkBoth&&!enqueued){
                    searchResult.add(groupBuy);
                }
                else if(!checkBoth&&enqueued){
                    searchResult.remove(position);
                    totalEntries--;
                    position--;
                }
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&searchResult.getNumberOfEntries()==0)
            end = true;
        if(!oriPrice.isEmpty()&&!end){
            enqueueTimes = 0;
            if(!enqueued){
                if(jPanelNo == 3){
                    searchGroupBuys = historyGroupBuys;
                }
                else if(jPanelNo == 2){
                    searchGroupBuys = collectGroupBuys;
                }
                else{
                    searchGroupBuys = currentGroupBuys;
                }
            }
            else{
                searchGroupBuys = searchResult;
                totalEntries = searchGroupBuys.getNumberOfEntries();
            }
            for(int position = 1; position <= totalEntries; position++){
                groupBuy = searchGroupBuys.getEntry(position);
                boolean checkBoth = false;
                if(oriPrice.equals(String.valueOf(groupBuy.getGroupBuyOriginalPrice()))){
                    enqueueTimes++;
                    checkBoth = true;
                }
                else{
                    boolean successful;
                    successful = searchTools(oriPrice, String.valueOf(groupBuy.getGroupBuyOriginalPrice()));
                    if(successful){
                        checkBoth = true;
                        enqueueTimes++;
                    }
                }
                if(checkBoth&&!enqueued)
                    searchResult.add(groupBuy);
                else if(!checkBoth&&enqueued){
                    searchResult.remove(position);
                    totalEntries--;
                    position--;
                }
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&searchResult.getNumberOfEntries()==0)
            end = true;
        if(!end){
            enqueueTimes = 0;
            if(!enqueued){
                if(jPanelNo == 3){
                    searchGroupBuys = historyGroupBuys;
                }
                else if(jPanelNo == 2){
                    searchGroupBuys = collectGroupBuys;
                }
                else{
                    searchGroupBuys = currentGroupBuys;
                }
            }
            else{
                searchGroupBuys = searchResult;
                totalEntries = searchGroupBuys.getNumberOfEntries();
            }
            for(int position = 1; position <= totalEntries; position++){
                groupBuy = searchGroupBuys.getEntry(position);
                boolean checkBoth = false;
                if(maxMember == groupBuy.getGroupBuyMaxMember()){
                    checkBoth = true;
                    enqueueTimes++;
                }
                if(checkBoth&&!enqueued){
                    searchResult.add(groupBuy);
                }
                else if(!checkBoth&&enqueued){
                    searchResult.remove(position);
                    totalEntries--;
                    position--;
                }
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&!end){
            switch (jPanelNo) {
                case 3 -> historyGroupBuys = searchResult;
                case 2 -> collectGroupBuys = searchResult;
                default -> currentGroupBuys = searchResult;
            }
            changedTableValue = true;
        }
        else{
            JOptionPane.showMessageDialog(this, "Member Not Found!Text field has been Reset!");
            clearValue();
        }
        currentPageNo = 1;
        jTableRowIndex = 0;
        setTableValue();
    }
    
    private boolean searchTools(String memberIdA, String memberIdB){
        boolean checkNextA = true;
        for(int charIndexA = 0; charIndexA < memberIdA.length() && checkNextA; charIndexA++){
            boolean checkNextB = true;
            for(int charIndexB = 0; charIndexB< memberIdB.length() && checkNextB; charIndexB++){
                if(memberIdA.charAt(charIndexA) == memberIdB.charAt(charIndexB))
                    checkNextB = false;
            }
            if(checkNextB)
                checkNextA = false;
        }
        return checkNextA;
    }
    
    private void joinGroupBuy(int buttonPosition){
        int joinPosition = jTableRowIndex + buttonPosition;
        boolean successful;
        if(changedTableValue){
            ListInterface<GroupBuyEntity> groupBuyB = mainMenu.currentGroupBuys;
            currentGroupBuy = currentGroupBuys.getEntry(joinPosition);
            joinPosition = groupBuyB.search(currentGroupBuy);
            successful = joinPosition != -1;
        }
        else{
            currentGroupBuy = currentGroupBuys.getEntry(joinPosition);
            successful = currentGroupBuy != null;
        }
        if(successful){
            setVisible(false);
            new groupBuyJoinGui(joinPosition).setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(this, "Unable to join ! Please select the row that has value to perform join!");
    }
    
    private void editGroupBuy(int buttonPosition){
        int editPosition = jTableRowIndex + buttonPosition;
        boolean successful;
        if(changedTableValue){
            ListInterface<GroupBuyEntity> groupBuyB = mainMenu.currentGroupBuys;
            currentGroupBuy = currentGroupBuys.getEntry(editPosition);
            editPosition = groupBuyB.search(currentGroupBuy);
            successful = editPosition != -1;
        }
        else{
            currentGroupBuy = currentGroupBuys.getEntry(editPosition);
            successful = currentGroupBuy != null;
        }
        if(successful){
            setVisible(false);
            new groupBuyModifyGui(editPosition).setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(this, "Unable to edit ! Please select the row that has value to perform modify!");
    }
    
    private void collectGroupBuy(int buttonPosition){
        int collectPosition = jTableRowIndex + buttonPosition;
        boolean successful;
        if(changedTableValue){
            ListInterface<GroupBuyEntity> groupBuyB = mainMenu.currentGroupBuys;
            collectGroupBuy = collectGroupBuys.getEntry(collectPosition);
            collectPosition = groupBuyB.search(currentGroupBuy);
            successful = collectPosition != -1;
        }
        else{
            collectGroupBuy = collectGroupBuys.getEntry(collectPosition);
            collectPosition = currentGroupBuys.search(collectGroupBuy);
            successful = collectGroupBuy != null;
        }
        if(successful){
            setVisible(false);
            new groupBuyCollectGui(collectPosition).setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(this, "Unable to collect ! Please select the row that has value to perform collect!");
    }
    
    private void historyGroupBuy(int buttonPosition){
        int historyPosition = jTableRowIndex + buttonPosition;
        boolean successful;
        if(changedTableValue){
            ListInterface<GroupBuyEntity> groupBuyB = mainMenu.historyGroupBuys;
            historyGroupBuy = historyGroupBuys.getEntry(historyPosition);
            historyPosition = groupBuyB.search(historyGroupBuy);
            successful = historyPosition != -1;
        }
        else{
            historyGroupBuy = historyGroupBuys.getEntry(historyPosition);
            successful = historyGroupBuy != null;
        }
        if(successful){
            setVisible(false);
            new groupBuyHistoryGui(historyPosition).setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(this, "Unable to view ! Please select the row that has value to perform history!");
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        Join = new javax.swing.JButton();
        Collect = new javax.swing.JButton();
        History = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        Edit = new javax.swing.JButton();
        oriPanel = new javax.swing.JPanel();
        joinGroupBuy = new javax.swing.JPanel();
        retrieveSmallPanel = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        JoinId = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        JoinDesc = new javax.swing.JTextPane();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        JoinMaxMember = new javax.swing.JSpinner();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        JoinOriPrice = new javax.swing.JTextField();
        JoinDiscount = new javax.swing.JTextField();
        JoinDiscPrice = new javax.swing.JTextField();
        JoinSearch = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        Join1 = new javax.swing.JButton();
        Join2 = new javax.swing.JButton();
        Join3 = new javax.swing.JButton();
        Join4 = new javax.swing.JButton();
        Join5 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        JoinTable = new javax.swing.JTable();
        jLabel39 = new javax.swing.JLabel();
        JoinPrevious = new javax.swing.JButton();
        JoinPage = new javax.swing.JLabel();
        JoinNext = new javax.swing.JButton();
        JoinClear = new javax.swing.JButton();
        collectItem = new javax.swing.JPanel();
        retrieveSmallPanel1 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        CollectId = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        CollectDesc = new javax.swing.JTextPane();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        CollectMaxMember = new javax.swing.JSpinner();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        CollectOriPrice = new javax.swing.JTextField();
        CollectDiscount = new javax.swing.JTextField();
        CollectDiscPrice = new javax.swing.JTextField();
        CollectSearch = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        Collect1 = new javax.swing.JButton();
        Collect2 = new javax.swing.JButton();
        Collect3 = new javax.swing.JButton();
        Collect4 = new javax.swing.JButton();
        Collect5 = new javax.swing.JButton();
        jScrollPane9 = new javax.swing.JScrollPane();
        CollectTable = new javax.swing.JTable();
        jLabel49 = new javax.swing.JLabel();
        CollectPrevious = new javax.swing.JButton();
        CollectPage = new javax.swing.JLabel();
        CollectNext = new javax.swing.JButton();
        CollectClear = new javax.swing.JButton();
        history = new javax.swing.JPanel();
        retrieveSmallPanel2 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        HistoryId = new javax.swing.JTextField();
        jScrollPane11 = new javax.swing.JScrollPane();
        HistoryDesc = new javax.swing.JTextPane();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        HistoryMaxMember = new javax.swing.JSpinner();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        HistoryOriPrice = new javax.swing.JTextField();
        HistoryDiscount = new javax.swing.JTextField();
        HistoryDiscPrice = new javax.swing.JTextField();
        HistorySearch = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        History1 = new javax.swing.JButton();
        History2 = new javax.swing.JButton();
        History3 = new javax.swing.JButton();
        History4 = new javax.swing.JButton();
        History5 = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        HistoryTable = new javax.swing.JTable();
        jLabel59 = new javax.swing.JLabel();
        HistoryPrevious = new javax.swing.JButton();
        HistoryPage = new javax.swing.JLabel();
        HistoryNext = new javax.swing.JButton();
        HistoryClear = new javax.swing.JButton();
        modifyGroupBuy = new javax.swing.JPanel();
        EditSearch = new javax.swing.JButton();
        EditClear = new javax.swing.JButton();
        editSmallPanel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        EditId = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        EditDesc = new javax.swing.JTextPane();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        EditMaxMember = new javax.swing.JSpinner();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        EditOriPrice = new javax.swing.JTextField();
        EditDiscount = new javax.swing.JTextField();
        EditDiscPrice = new javax.swing.JTextField();
        EditPrevious = new javax.swing.JButton();
        EditPage = new javax.swing.JLabel();
        EditNext = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        Edit1 = new javax.swing.JButton();
        Edit2 = new javax.swing.JButton();
        Edit3 = new javax.swing.JButton();
        Edit4 = new javax.swing.JButton();
        Edit5 = new javax.swing.JButton();
        jLabel61 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        EditTable = new javax.swing.JTable();
        generateGroupReport = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Group Buy Maintenance");
        setName("itemGroupMaintenance"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPane1.setDividerSize(1);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setForeground(new java.awt.Color(255, 51, 204));

        Join.setBackground(new java.awt.Color(0, 0, 0));
        Join.setForeground(new java.awt.Color(255, 255, 255));
        Join.setText("Join Item Group");
        Join.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Join.setName("itemAddButton"); // NOI18N
        Join.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JoinActionPerformed(evt);
            }
        });

        Collect.setBackground(new java.awt.Color(0, 0, 0));
        Collect.setForeground(new java.awt.Color(255, 255, 255));
        Collect.setText("Collect Item");
        Collect.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Collect.setMaximumSize(new java.awt.Dimension(123, 29));
        Collect.setMinimumSize(new java.awt.Dimension(123, 29));
        Collect.setName("itemRemoveButton"); // NOI18N
        Collect.setPreferredSize(new java.awt.Dimension(123, 29));
        Collect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollectActionPerformed(evt);
            }
        });

        History.setBackground(new java.awt.Color(0, 0, 0));
        History.setForeground(new java.awt.Color(255, 255, 255));
        History.setText("History");
        History.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        History.setName("groupEditButton"); // NOI18N
        History.setPreferredSize(new java.awt.Dimension(103, 29));
        History.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoryActionPerformed(evt);
            }
        });

        Back.setBackground(new java.awt.Color(0, 0, 0));
        Back.setForeground(new java.awt.Color(255, 255, 255));
        Back.setText("Back");
        Back.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Back.setName("itemRetrieveButton"); // NOI18N
        Back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackActionPerformed(evt);
            }
        });

        Edit.setBackground(new java.awt.Color(0, 0, 0));
        Edit.setForeground(new java.awt.Color(255, 255, 255));
        Edit.setText("Modify Group Buy");
        Edit.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        Edit.setMaximumSize(new java.awt.Dimension(123, 29));
        Edit.setMinimumSize(new java.awt.Dimension(123, 29));
        Edit.setName("itemRemoveButton"); // NOI18N
        Edit.setPreferredSize(new java.awt.Dimension(123, 29));
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(History, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Collect, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                    .addComponent(Join, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Back, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Edit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(Join, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Collect, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(History, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 349, Short.MAX_VALUE)
                .addComponent(Back, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
        );

        Edit.getAccessibleContext().setAccessibleName("modifyGroupBuyButton");

        jSplitPane1.setLeftComponent(jPanel3);

        oriPanel.setLayout(new java.awt.CardLayout());

        joinGroupBuy.setBackground(new java.awt.Color(255, 204, 204));

        retrieveSmallPanel.setBackground(new java.awt.Color(255, 255, 255));
        retrieveSmallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "JOIN GROUP BUY", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel31.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel31.setText("Item Group ID: ");

        jScrollPane5.setViewportView(JoinDesc);

        jLabel32.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel32.setText("Group Description: ");

        jLabel33.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel33.setText("Group Maximum Member: ");

        JoinMaxMember.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        JoinMaxMember.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                JoinMaxMemberStateChanged(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel34.setText("Item Original Price / Unit: ");

        jLabel35.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel35.setText("Item Discount Rate: ");

        jLabel36.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel36.setText("Item Discounted Price / Unit: ");

        jLabel37.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel37.setText("RM ");

        jLabel38.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel38.setText("RM ");

        JoinOriPrice.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                JoinOriPriceCaretUpdate(evt);
            }
        });

        JoinDiscount.setEditable(false);

        JoinDiscPrice.setEditable(false);

        javax.swing.GroupLayout retrieveSmallPanelLayout = new javax.swing.GroupLayout(retrieveSmallPanel);
        retrieveSmallPanel.setLayout(retrieveSmallPanelLayout);
        retrieveSmallPanelLayout.setHorizontalGroup(
            retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JoinMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(JoinId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JoinDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel37)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(JoinDiscPrice))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel38)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(JoinOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        retrieveSmallPanelLayout.setVerticalGroup(
            retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JoinId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(JoinMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel38)
                    .addComponent(JoinOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(JoinDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37)
                    .addComponent(JoinDiscPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        JoinSearch.setText("Search");
        JoinSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JoinSearchActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));

        Join1.setBackground(new java.awt.Color(255, 255, 255));
        Join1.setText("Join");
        Join1.setActionCommand("view1");
        Join1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Join1ActionPerformed(evt);
            }
        });

        Join2.setBackground(new java.awt.Color(255, 255, 255));
        Join2.setText("Join");
        Join2.setActionCommand("view2");
        Join2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Join2ActionPerformed(evt);
            }
        });

        Join3.setBackground(new java.awt.Color(255, 255, 255));
        Join3.setText("Join");
        Join3.setActionCommand("view3");
        Join3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Join3ActionPerformed(evt);
            }
        });

        Join4.setBackground(new java.awt.Color(255, 255, 255));
        Join4.setText("Join");
        Join4.setActionCommand("view4");
        Join4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Join4ActionPerformed(evt);
            }
        });

        Join5.setBackground(new java.awt.Color(255, 255, 255));
        Join5.setText("Join");
        Join5.setActionCommand("view5");
        Join5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Join5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Join3)
                            .addComponent(Join4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Join1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Join2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Join5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Join1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Join2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Join3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Join4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Join5)
                .addGap(14, 14, 14))
        );

        JoinTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Item Group ID", "Group Description", "Max Member", "Original Price", "Discount Rate", "Discounted Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        JoinTable.setRowHeight(30);
        jScrollPane7.setViewportView(JoinTable);

        jLabel39.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel39.setText("Item Group List");

        JoinPrevious.setText("<");
        JoinPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JoinPreviousActionPerformed(evt);
            }
        });

        JoinPage.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        JoinPage.setText("Page 1 of 1");

        JoinNext.setText(">");
        JoinNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JoinNextActionPerformed(evt);
            }
        });

        JoinClear.setText("Clear");
        JoinClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JoinClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout joinGroupBuyLayout = new javax.swing.GroupLayout(joinGroupBuy);
        joinGroupBuy.setLayout(joinGroupBuyLayout);
        joinGroupBuyLayout.setHorizontalGroup(
            joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(joinGroupBuyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(joinGroupBuyLayout.createSequentialGroup()
                        .addGroup(joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(joinGroupBuyLayout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(JoinPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(JoinPage, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(JoinNext, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(80, Short.MAX_VALUE))
                    .addGroup(joinGroupBuyLayout.createSequentialGroup()
                        .addGroup(joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(joinGroupBuyLayout.createSequentialGroup()
                                .addComponent(retrieveSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(JoinSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(JoinClear, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        joinGroupBuyLayout.setVerticalGroup(
            joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(joinGroupBuyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(retrieveSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(joinGroupBuyLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(JoinSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(JoinClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(JoinPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(joinGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(JoinNext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(JoinPage)))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        oriPanel.add(joinGroupBuy, "joinGroupBuy");

        collectItem.setBackground(new java.awt.Color(255, 204, 204));

        retrieveSmallPanel1.setBackground(new java.awt.Color(255, 255, 255));
        retrieveSmallPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "COLLECT ITEM", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel41.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel41.setText("Group Code: ");

        jScrollPane8.setViewportView(CollectDesc);

        jLabel42.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel42.setText("Group Description: ");

        jLabel43.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel43.setText("Group Maximum Member: ");

        CollectMaxMember.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        CollectMaxMember.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                CollectMaxMemberStateChanged(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel44.setText("Item Original Price / Unit: ");

        jLabel45.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel45.setText("Item Discount Rate: ");

        jLabel46.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel46.setText("Item Discounted Price / Unit: ");

        jLabel47.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel47.setText("RM ");

        jLabel48.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel48.setText("RM ");

        CollectOriPrice.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                CollectOriPriceCaretUpdate(evt);
            }
        });

        CollectDiscount.setEditable(false);

        CollectDiscPrice.setEditable(false);

        javax.swing.GroupLayout retrieveSmallPanel1Layout = new javax.swing.GroupLayout(retrieveSmallPanel1);
        retrieveSmallPanel1.setLayout(retrieveSmallPanel1Layout);
        retrieveSmallPanel1Layout.setHorizontalGroup(
            retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retrieveSmallPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CollectMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanel1Layout.createSequentialGroup()
                        .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CollectId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveSmallPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CollectDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel47)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(CollectDiscPrice))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel48)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(CollectOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        retrieveSmallPanel1Layout.setVerticalGroup(
            retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanel1Layout.createSequentialGroup()
                .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CollectId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CollectMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(jLabel48)
                    .addComponent(CollectOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(CollectDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(jLabel47)
                    .addComponent(CollectDiscPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        CollectSearch.setText("Search");
        CollectSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollectSearchActionPerformed(evt);
            }
        });

        jPanel5.setBackground(new java.awt.Color(255, 204, 204));

        Collect1.setBackground(new java.awt.Color(255, 255, 255));
        Collect1.setText("View details");
        Collect1.setActionCommand("view1");
        Collect1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Collect1ActionPerformed(evt);
            }
        });

        Collect2.setBackground(new java.awt.Color(255, 255, 255));
        Collect2.setText("View details");
        Collect2.setActionCommand("view2");
        Collect2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Collect2ActionPerformed(evt);
            }
        });

        Collect3.setBackground(new java.awt.Color(255, 255, 255));
        Collect3.setText("View details");
        Collect3.setActionCommand("view3");
        Collect3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Collect3ActionPerformed(evt);
            }
        });

        Collect4.setBackground(new java.awt.Color(255, 255, 255));
        Collect4.setText("View details");
        Collect4.setActionCommand("view4");
        Collect4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Collect4ActionPerformed(evt);
            }
        });

        Collect5.setBackground(new java.awt.Color(255, 255, 255));
        Collect5.setText("View details");
        Collect5.setActionCommand("view5");
        Collect5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Collect5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Collect3)
                            .addComponent(Collect4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Collect1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Collect2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Collect5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Collect1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Collect2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Collect3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Collect4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Collect5)
                .addGap(14, 14, 14))
        );

        CollectTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Group Code", "Group Description", "Max Member", "Original Price", "Discount Rate", "Discounted Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CollectTable.setRowHeight(30);
        jScrollPane9.setViewportView(CollectTable);

        jLabel49.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel49.setText("Group Buy List");

        CollectPrevious.setText("<");
        CollectPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollectPreviousActionPerformed(evt);
            }
        });

        CollectPage.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        CollectPage.setText("Page 1 of 1");

        CollectNext.setText(">");
        CollectNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollectNextActionPerformed(evt);
            }
        });

        CollectClear.setText("Clear");
        CollectClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CollectClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout collectItemLayout = new javax.swing.GroupLayout(collectItem);
        collectItem.setLayout(collectItemLayout);
        collectItemLayout.setHorizontalGroup(
            collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(collectItemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(collectItemLayout.createSequentialGroup()
                        .addGroup(collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(collectItemLayout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(CollectPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(CollectPage, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(CollectNext, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(48, Short.MAX_VALUE))
                    .addGroup(collectItemLayout.createSequentialGroup()
                        .addGroup(collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(collectItemLayout.createSequentialGroup()
                                .addComponent(retrieveSmallPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CollectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(CollectClear, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        collectItemLayout.setVerticalGroup(
            collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(collectItemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(retrieveSmallPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(collectItemLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(CollectSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(CollectClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel49)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CollectPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(collectItemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(CollectNext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(CollectPage)))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        oriPanel.add(collectItem, "collectItem");

        history.setBackground(new java.awt.Color(255, 204, 204));

        retrieveSmallPanel2.setBackground(new java.awt.Color(255, 255, 255));
        retrieveSmallPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HISTORY", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel51.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel51.setText("Group Buy Code: ");

        jScrollPane11.setViewportView(HistoryDesc);

        jLabel52.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel52.setText("Item Group Description");

        jLabel53.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel53.setText("Group Maximum Member: ");

        HistoryMaxMember.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        HistoryMaxMember.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                HistoryMaxMemberStateChanged(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel54.setText("Item Original Price / Unit: ");

        jLabel55.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel55.setText("Item Discount Rate: ");

        jLabel56.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel56.setText("Item Discounted Price / Unit: ");

        jLabel57.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel57.setText("RM ");

        jLabel58.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel58.setText("RM ");

        HistoryOriPrice.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                HistoryOriPriceCaretUpdate(evt);
            }
        });

        javax.swing.GroupLayout retrieveSmallPanel2Layout = new javax.swing.GroupLayout(retrieveSmallPanel2);
        retrieveSmallPanel2.setLayout(retrieveSmallPanel2Layout);
        retrieveSmallPanel2Layout.setHorizontalGroup(
            retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(HistoryMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                        .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel52, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(HistoryId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(HistoryDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel57)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(HistoryDiscPrice))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel58)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(HistoryOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        retrieveSmallPanel2Layout.setVerticalGroup(
            retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanel2Layout.createSequentialGroup()
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HistoryId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HistoryMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(jLabel58)
                    .addComponent(HistoryOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel55)
                    .addComponent(HistoryDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(jLabel57)
                    .addComponent(HistoryDiscPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        HistorySearch.setText("Search");
        HistorySearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistorySearchActionPerformed(evt);
            }
        });

        jPanel6.setBackground(new java.awt.Color(255, 204, 204));

        History1.setBackground(new java.awt.Color(255, 255, 255));
        History1.setText("View details");
        History1.setActionCommand("view1");
        History1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                History1ActionPerformed(evt);
            }
        });

        History2.setBackground(new java.awt.Color(255, 255, 255));
        History2.setText("View details");
        History2.setActionCommand("view2");
        History2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                History2ActionPerformed(evt);
            }
        });

        History3.setBackground(new java.awt.Color(255, 255, 255));
        History3.setText("View details");
        History3.setActionCommand("view3");
        History3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                History3ActionPerformed(evt);
            }
        });

        History4.setBackground(new java.awt.Color(255, 255, 255));
        History4.setText("View details");
        History4.setActionCommand("view4");
        History4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                History4ActionPerformed(evt);
            }
        });

        History5.setBackground(new java.awt.Color(255, 255, 255));
        History5.setText("View details");
        History5.setActionCommand("view5");
        History5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                History5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(History3)
                            .addComponent(History4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(History1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(History2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(History5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(History1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(History2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(History3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(History4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(History5)
                .addGap(14, 14, 14))
        );

        HistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Group Buy Code", "Group Description", "Max Member", "Original Price", "Discount Rate", "Discounted Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        HistoryTable.setRowHeight(30);
        jScrollPane12.setViewportView(HistoryTable);

        jLabel59.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel59.setText("Group Buy Histories List");

        HistoryPrevious.setText("<");
        HistoryPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoryPreviousActionPerformed(evt);
            }
        });

        HistoryPage.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        HistoryPage.setText("Page 1 of 1");

        HistoryNext.setText(">");
        HistoryNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoryNextActionPerformed(evt);
            }
        });

        HistoryClear.setText("Clear");
        HistoryClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HistoryClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout historyLayout = new javax.swing.GroupLayout(history);
        history.setLayout(historyLayout);
        historyLayout.setHorizontalGroup(
            historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(historyLayout.createSequentialGroup()
                        .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(historyLayout.createSequentialGroup()
                                .addGap(168, 168, 168)
                                .addComponent(HistoryPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(HistoryPage, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(HistoryNext, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(48, Short.MAX_VALUE))
                    .addGroup(historyLayout.createSequentialGroup()
                        .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(historyLayout.createSequentialGroup()
                                .addComponent(retrieveSmallPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(HistorySearch, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(HistoryClear, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        historyLayout.setVerticalGroup(
            historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(retrieveSmallPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(historyLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(HistorySearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(HistoryClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel59)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(HistoryPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(historyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(HistoryNext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(HistoryPage)))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        oriPanel.add(history, "history");

        modifyGroupBuy.setBackground(new java.awt.Color(255, 204, 204));

        EditSearch.setText("Search");
        EditSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditSearchActionPerformed(evt);
            }
        });

        EditClear.setText("Clear");
        EditClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditClearActionPerformed(evt);
            }
        });

        editSmallPanel.setBackground(new java.awt.Color(255, 255, 255));
        editSmallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MODIFY GROUP BUY", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel21.setText("Group Code: ");

        jScrollPane3.setViewportView(EditDesc);

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel22.setText("Group Description: ");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel23.setText("Group Maximum Member: ");

        EditMaxMember.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        EditMaxMember.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                EditMaxMemberStateChanged(evt);
            }
        });

        jLabel24.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel24.setText("Item Original Price / Unit: ");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel25.setText("Item Discount Rate: ");

        jLabel26.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel26.setText("Item Discounted Price / Unit: ");

        jLabel27.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel27.setText("RM ");

        jLabel28.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel28.setText("RM ");

        EditOriPrice.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                EditOriPriceCaretUpdate(evt);
            }
        });

        EditDiscount.setEditable(false);

        EditDiscPrice.setEditable(false);

        javax.swing.GroupLayout editSmallPanelLayout = new javax.swing.GroupLayout(editSmallPanel);
        editSmallPanel.setLayout(editSmallPanelLayout);
        editSmallPanelLayout.setHorizontalGroup(
            editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editSmallPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editSmallPanelLayout.createSequentialGroup()
                        .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EditId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(editSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel27)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(EditDiscPrice))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel28)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(EditOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        editSmallPanelLayout.setVerticalGroup(
            editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editSmallPanelLayout.createSequentialGroup()
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditMaxMember, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel28)
                    .addComponent(EditOriPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(EditDiscount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(EditDiscPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        EditPrevious.setText("<");
        EditPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditPreviousActionPerformed(evt);
            }
        });

        EditPage.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        EditPage.setText("Page 1 of 1");

        EditNext.setText(">");
        EditNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditNextActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        Edit1.setBackground(new java.awt.Color(255, 255, 255));
        Edit1.setText("Modify");
        Edit1.setActionCommand("edit1");
        Edit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Edit1ActionPerformed(evt);
            }
        });

        Edit2.setBackground(new java.awt.Color(255, 255, 255));
        Edit2.setText("Modify");
        Edit2.setActionCommand("edit2");
        Edit2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Edit2ActionPerformed(evt);
            }
        });

        Edit3.setBackground(new java.awt.Color(255, 255, 255));
        Edit3.setText("Modify");
        Edit3.setActionCommand("edit3");
        Edit3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Edit3ActionPerformed(evt);
            }
        });

        Edit4.setBackground(new java.awt.Color(255, 255, 255));
        Edit4.setText("Modify");
        Edit4.setActionCommand("edit4");
        Edit4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Edit4ActionPerformed(evt);
            }
        });

        Edit5.setBackground(new java.awt.Color(255, 255, 255));
        Edit5.setText("Modify");
        Edit5.setActionCommand("edit5");
        Edit5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Edit5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Edit3)
                            .addComponent(Edit4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Edit1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Edit2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Edit5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Edit1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Edit2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Edit3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Edit4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Edit5)
                .addGap(14, 14, 14))
        );

        jLabel61.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel61.setText("Group Buy List");

        EditTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Group Buy Code", "Group Description", "Max Member", "Original Price", "Discount Rate", "Discounted Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EditTable.setRowHeight(30);
        jScrollPane13.setViewportView(EditTable);

        javax.swing.GroupLayout modifyGroupBuyLayout = new javax.swing.GroupLayout(modifyGroupBuy);
        modifyGroupBuy.setLayout(modifyGroupBuyLayout);
        modifyGroupBuyLayout.setHorizontalGroup(
            modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                        .addGroup(modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, modifyGroupBuyLayout.createSequentialGroup()
                                .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(589, 589, 589))
                            .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                                .addGroup(modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                                        .addGap(174, 174, 174)
                                        .addComponent(EditPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(47, 47, 47)
                                        .addComponent(EditPage, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(EditNext, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(68, Short.MAX_VALUE))
                    .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                        .addComponent(editSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(EditClear, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(EditSearch, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        modifyGroupBuyLayout.setVerticalGroup(
            modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(EditSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(EditClear, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(modifyGroupBuyLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel61)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(26, 26, 26)
                .addGroup(modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EditPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(modifyGroupBuyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(EditNext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(EditPage)))
                .addContainerGap(122, Short.MAX_VALUE))
        );

        oriPanel.add(modifyGroupBuy, "modifyGroupBuy");

        javax.swing.GroupLayout generateGroupReportLayout = new javax.swing.GroupLayout(generateGroupReport);
        generateGroupReport.setLayout(generateGroupReportLayout);
        generateGroupReportLayout.setHorizontalGroup(
            generateGroupReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 892, Short.MAX_VALUE)
        );
        generateGroupReportLayout.setVerticalGroup(
            generateGroupReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        oriPanel.add(generateGroupReport, "generateGroupReport");

        jSplitPane1.setRightComponent(oriPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void JoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JoinActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "joinGroupBuy");
        switchjPanel(0);
    }//GEN-LAST:event_JoinActionPerformed

    private void CollectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollectActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "collectItem");
        collectGroupBuys = new ArrayList();
        int totalEntriesOfGroupBuy = currentGroupBuys.getNumberOfEntries();
        for(int index = 0; index < totalEntriesOfGroupBuy; index++){
            currentGroupBuy = currentGroupBuys.getEntry(index + 1);
            int totalEntriesOfMember = currentGroupBuy.getTotalNumberOfGroupBuyMembers();
            int maxMember = currentGroupBuy.getGroupBuyMaxMember();
            if(totalEntriesOfMember == maxMember){
                collectGroupBuys.add(currentGroupBuy);
            }
            searchCollectGroupBuys = collectGroupBuys;
        }
        switchjPanel(2);
    }//GEN-LAST:event_CollectActionPerformed

    private void HistoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistoryActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "history");
        switchjPanel(3);
    }//GEN-LAST:event_HistoryActionPerformed

    private void EditSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditSearchActionPerformed
        // TODO add your handling code here:
        searchGroupBuy();
    }//GEN-LAST:event_EditSearchActionPerformed

    private void EditPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditPreviousActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_EditPreviousActionPerformed

    private void EditNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditNextActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_EditNextActionPerformed

    private void EditClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditClearActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_EditClearActionPerformed

    private void Edit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit1ActionPerformed
        // TODO add your handling code here:
        editGroupBuy(1);
    }//GEN-LAST:event_Edit1ActionPerformed

    private void Edit2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit2ActionPerformed
        // TODO add your handling code here:
        editGroupBuy(2);
    }//GEN-LAST:event_Edit2ActionPerformed

    private void Edit3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit3ActionPerformed
        // TODO add your handling code here:
        editGroupBuy(3);
    }//GEN-LAST:event_Edit3ActionPerformed

    private void Edit4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit4ActionPerformed
        // TODO add your handling code here:
        editGroupBuy(4);
    }//GEN-LAST:event_Edit4ActionPerformed

    private void Edit5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit5ActionPerformed
        // TODO add your handling code here:
        editGroupBuy(5);
    }//GEN-LAST:event_Edit5ActionPerformed

    private void Join1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Join1ActionPerformed
        // TODO add your handling code here:
        joinGroupBuy(1);
    }//GEN-LAST:event_Join1ActionPerformed

    private void Join2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Join2ActionPerformed
        // TODO add your handling code here:
        joinGroupBuy(2);
    }//GEN-LAST:event_Join2ActionPerformed

    private void Join3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Join3ActionPerformed
        // TODO add your handling code here:
        joinGroupBuy(3);
    }//GEN-LAST:event_Join3ActionPerformed

    private void Join4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Join4ActionPerformed
        // TODO add your handling code here:
        joinGroupBuy(4);
    }//GEN-LAST:event_Join4ActionPerformed

    private void Join5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Join5ActionPerformed
        // TODO add your handling code here:
        joinGroupBuy(5);
    }//GEN-LAST:event_Join5ActionPerformed

    private void JoinPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JoinPreviousActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_JoinPreviousActionPerformed

    private void JoinNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JoinNextActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_JoinNextActionPerformed

    private void JoinSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JoinSearchActionPerformed
        // TODO add your handling code here:
        searchGroupBuy();
    }//GEN-LAST:event_JoinSearchActionPerformed

    private void JoinClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JoinClearActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_JoinClearActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
        exitForm();
    }//GEN-LAST:event_BackActionPerformed

    private void CollectSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollectSearchActionPerformed
        // TODO add your handling code here:
        searchGroupBuy();
    }//GEN-LAST:event_CollectSearchActionPerformed

    private void Collect1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Collect1ActionPerformed
        collectGroupBuy(1);
    }//GEN-LAST:event_Collect1ActionPerformed

    private void Collect2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Collect2ActionPerformed
        collectGroupBuy(2);
    }//GEN-LAST:event_Collect2ActionPerformed

    private void Collect3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Collect3ActionPerformed
        collectGroupBuy(3);
    }//GEN-LAST:event_Collect3ActionPerformed

    private void Collect4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Collect4ActionPerformed
        collectGroupBuy(4);
    }//GEN-LAST:event_Collect4ActionPerformed

    private void Collect5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Collect5ActionPerformed
        collectGroupBuy(5);
    }//GEN-LAST:event_Collect5ActionPerformed

    private void CollectPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollectPreviousActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_CollectPreviousActionPerformed

    private void CollectNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollectNextActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_CollectNextActionPerformed

    private void CollectClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CollectClearActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_CollectClearActionPerformed

    private void HistorySearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistorySearchActionPerformed
        // TODO add your handling code here:
        searchGroupBuy();
    }//GEN-LAST:event_HistorySearchActionPerformed

    private void History1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_History1ActionPerformed
        historyGroupBuy(1);
    }//GEN-LAST:event_History1ActionPerformed

    private void History2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_History2ActionPerformed
        historyGroupBuy(2);
    }//GEN-LAST:event_History2ActionPerformed

    private void History3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_History3ActionPerformed
        historyGroupBuy(3);
    }//GEN-LAST:event_History3ActionPerformed

    private void History4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_History4ActionPerformed
        historyGroupBuy(4);
    }//GEN-LAST:event_History4ActionPerformed

    private void History5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_History5ActionPerformed
        historyGroupBuy(5);
    }//GEN-LAST:event_History5ActionPerformed

    private void HistoryPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistoryPreviousActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_HistoryPreviousActionPerformed

    private void HistoryNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistoryNextActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_HistoryNextActionPerformed

    private void HistoryClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HistoryClearActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_HistoryClearActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        cardLayout.show(oriPanel, "modifyGroupBuy");
        switchjPanel(1);
    }//GEN-LAST:event_EditActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exitForm();
    }//GEN-LAST:event_formWindowClosing

    private void JoinMaxMemberStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_JoinMaxMemberStateChanged
        // TODO add your handling code here:
        int maxMember = (int)JoinMaxMember.getValue();
        int discount = maxMember * 2;
        JoinDiscount.setText(String.format("%d",discount));
        if(!JoinOriPrice.getText().isEmpty()){
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(JoinOriPrice.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
            }
            if(isDouble)
                JoinDiscPrice.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_JoinMaxMemberStateChanged

    private void CollectMaxMemberStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_CollectMaxMemberStateChanged
        // TODO add your handling code here:
        int maxMember = (int)CollectMaxMember.getValue();
        int discount = maxMember * 2;
        CollectDiscount.setText(String.format("%d",discount));
        if(!CollectOriPrice.getText().isEmpty()){
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(CollectOriPrice.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
            }
            if(isDouble)
                CollectDiscPrice.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_CollectMaxMemberStateChanged

    private void JoinOriPriceCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_JoinOriPriceCaretUpdate
        // TODO add your handling code here:
        boolean isDouble = true;
        double oriPrice = 0.00;
        try{
            oriPrice = Double.parseDouble(JoinOriPrice.getText());
        }
        catch (NumberFormatException ex) {
            isDouble = false;
        }
        if(isDouble){
            int discount = Integer.parseInt(JoinDiscount.getText());
            JoinDiscPrice.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_JoinOriPriceCaretUpdate

    private void CollectOriPriceCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_CollectOriPriceCaretUpdate
        // TODO add your handling code here:
        boolean isDouble = true;
        double oriPrice = 0.00;
        try{
            oriPrice = Double.parseDouble(CollectOriPrice.getText());
        }
        catch (NumberFormatException ex) {
            isDouble = false;
        }
        if(isDouble){
            int discount = Integer.parseInt(CollectDiscount.getText());
            CollectDiscPrice.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_CollectOriPriceCaretUpdate

    private void HistoryMaxMemberStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_HistoryMaxMemberStateChanged
        // TODO add your handling code here:
        int maxMember = (int)HistoryMaxMember.getValue();
        int discount = maxMember * 2;
        HistoryDiscount.setText(String.format("%d",discount));
        if(!HistoryOriPrice.getText().isEmpty()){
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(HistoryOriPrice.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
            }
            if(isDouble)
                HistoryDiscPrice.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_HistoryMaxMemberStateChanged

    private void HistoryOriPriceCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_HistoryOriPriceCaretUpdate
        // TODO add your handling code here:
        boolean isDouble = true;
        double oriPrice = 0.00;
        try{
            oriPrice = Double.parseDouble(HistoryOriPrice.getText());
        }
        catch (NumberFormatException ex) {
            isDouble = false;
        }
        if(isDouble){
            int discount = Integer.parseInt(HistoryDiscount.getText());
            HistoryDiscPrice.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_HistoryOriPriceCaretUpdate

    private void EditMaxMemberStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_EditMaxMemberStateChanged
        // TODO add your handling code here:
        int maxMember = (int)EditMaxMember.getValue();
        int discount = maxMember * 2;
        EditDiscount.setText(String.format("%d",discount));
        if(!EditOriPrice.getText().isEmpty()){
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(EditOriPrice.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
            }
            if(isDouble)
                EditDiscPrice.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_EditMaxMemberStateChanged

    private void EditOriPriceCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_EditOriPriceCaretUpdate
        // TODO add your handling code here:
        boolean isDouble = true;
        double oriPrice = 0.00;
        try{
            oriPrice = Double.parseDouble(EditOriPrice.getText());
        }
        catch (NumberFormatException ex) {
            isDouble = false;
        }
        if(isDouble){
            int discount = Integer.parseInt(EditDiscount.getText());
            EditDiscPrice.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_EditOriPriceCaretUpdate

    
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(groupBuy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(groupBuy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(groupBuy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(groupBuy.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new groupBuy().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton Collect;
    private javax.swing.JButton Collect1;
    private javax.swing.JButton Collect2;
    private javax.swing.JButton Collect3;
    private javax.swing.JButton Collect4;
    private javax.swing.JButton Collect5;
    private javax.swing.JButton CollectClear;
    private javax.swing.JTextPane CollectDesc;
    private javax.swing.JTextField CollectDiscPrice;
    private javax.swing.JTextField CollectDiscount;
    private javax.swing.JTextField CollectId;
    private javax.swing.JSpinner CollectMaxMember;
    private javax.swing.JButton CollectNext;
    private javax.swing.JTextField CollectOriPrice;
    private javax.swing.JLabel CollectPage;
    private javax.swing.JButton CollectPrevious;
    private javax.swing.JButton CollectSearch;
    private javax.swing.JTable CollectTable;
    private javax.swing.JButton Edit;
    private javax.swing.JButton Edit1;
    private javax.swing.JButton Edit2;
    private javax.swing.JButton Edit3;
    private javax.swing.JButton Edit4;
    private javax.swing.JButton Edit5;
    private javax.swing.JButton EditClear;
    private javax.swing.JTextPane EditDesc;
    private javax.swing.JTextField EditDiscPrice;
    private javax.swing.JTextField EditDiscount;
    private javax.swing.JTextField EditId;
    private javax.swing.JSpinner EditMaxMember;
    private javax.swing.JButton EditNext;
    private javax.swing.JTextField EditOriPrice;
    private javax.swing.JLabel EditPage;
    private javax.swing.JButton EditPrevious;
    private javax.swing.JButton EditSearch;
    private javax.swing.JTable EditTable;
    private javax.swing.JButton History;
    private javax.swing.JButton History1;
    private javax.swing.JButton History2;
    private javax.swing.JButton History3;
    private javax.swing.JButton History4;
    private javax.swing.JButton History5;
    private javax.swing.JButton HistoryClear;
    private javax.swing.JTextPane HistoryDesc;
    private javax.swing.JTextField HistoryDiscPrice;
    private javax.swing.JTextField HistoryDiscount;
    private javax.swing.JTextField HistoryId;
    private javax.swing.JSpinner HistoryMaxMember;
    private javax.swing.JButton HistoryNext;
    private javax.swing.JTextField HistoryOriPrice;
    private javax.swing.JLabel HistoryPage;
    private javax.swing.JButton HistoryPrevious;
    private javax.swing.JButton HistorySearch;
    private javax.swing.JTable HistoryTable;
    private javax.swing.JButton Join;
    private javax.swing.JButton Join1;
    private javax.swing.JButton Join2;
    private javax.swing.JButton Join3;
    private javax.swing.JButton Join4;
    private javax.swing.JButton Join5;
    private javax.swing.JButton JoinClear;
    private javax.swing.JTextPane JoinDesc;
    private javax.swing.JTextField JoinDiscPrice;
    private javax.swing.JTextField JoinDiscount;
    private javax.swing.JTextField JoinId;
    private javax.swing.JSpinner JoinMaxMember;
    private javax.swing.JButton JoinNext;
    private javax.swing.JTextField JoinOriPrice;
    private javax.swing.JLabel JoinPage;
    private javax.swing.JButton JoinPrevious;
    private javax.swing.JButton JoinSearch;
    private javax.swing.JTable JoinTable;
    private javax.swing.JPanel collectItem;
    private javax.swing.JPanel editSmallPanel;
    private javax.swing.JPanel generateGroupReport;
    private javax.swing.JPanel history;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel joinGroupBuy;
    private javax.swing.JPanel modifyGroupBuy;
    private javax.swing.JPanel oriPanel;
    private javax.swing.JPanel retrieveSmallPanel;
    private javax.swing.JPanel retrieveSmallPanel1;
    private javax.swing.JPanel retrieveSmallPanel2;
    // End of variables declaration//GEN-END:variables
}
