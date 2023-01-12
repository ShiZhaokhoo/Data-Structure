package ItemGroup;

import Adt.StackInterface;
import Entity.ItemGroup;
import Adt.ArrayStack;
import Main.mainMenu;
import Entity.Member;
import java.awt.CardLayout;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Yeap Ying Thung
 */

public class ItemGroupMaintenance extends javax.swing.JFrame {
    private static final int DEFAULT_TABLE_ROWS = 5; // the table rows fixed 5
    private int jPanelNo = 0; // add = 0,remove = 1,edit = 2,remove = 3
    private int currentPageNo = 1; // table page no, default is 1
    private int jTableRowIndex = 0; // table row index, default is 0, will increase when the click next page
    private boolean changedTableValue;//if used search the value will change to true
    private ItemGroup itemGroup;
    private StackInterface<ItemGroup> itemGroups;
    
    private JLabel[] jtfDisplayPageNo;// add = 0,remove = 1,edit = 2,remove = 3
    private JTable[] jTable;// add = 0,remove = 1,edit = 2,view = 3
    private JTextField[] jText;//remove = 0-3,edit = 4-7,view = 8-11
    private JSpinner[] jSpinner;//remove = 0-1,edit = 2-3,view = 4-5
    private JTextPane[] jTextPane;
    CardLayout cardLayout; // switch jplane
    
    public ItemGroupMaintenance() {
        initComponents();
        setLocationRelativeTo(null);
        initValues();
        initializeDisplay();
        
        setTableValue();
        
        cardLayout = (CardLayout)(oriPanel.getLayout());
    }
    
    private void initValues(){
        changedTableValue = false;
        itemGroups = mainMenu.itemGroups;
        itemGroup = new ItemGroup();
        
        jtfDisplayPageNo = new JLabel[4];
        jTable = new JTable[4];
        jText = new JTextField[12];
        jSpinner = new JSpinner[3];
        jTextPane = new JTextPane[3];
        
        
        String itemGroupId = String.format("%c%03d",'I',itemGroup.getNextItemGroupId());
        addIdjTextField.setText(itemGroupId);
        addDiscountjTextField.setText("4");
    }
    
    private void initializeDisplay(){
        //Label Page No
        jtfDisplayPageNo[0] = addPagejLabel;
        jtfDisplayPageNo[1] = removePagejLabel;
        jtfDisplayPageNo[2] = editPagejLabel;
        jtfDisplayPageNo[3] = viewPagejLabel;  
        //Table
        jTable[0] = addjTable;
        jTable[1] = removejTable;
        jTable[2] = editjTable;
        jTable[3] = viewjTable;
        //TextField
        jText[0] = removeIdjTextField;
        jText[1] = removeOriPricejTextField;
        jText[2] = removeDiscountjTextField;
        jText[3] = removeDiscPricejTextField;
        jText[4] = editIdjTextField;
        jText[5] = editOriPricejTextField;
        jText[6] = editDiscountjTextField;
        jText[7] = editDiscPricejTextField;
        jText[8] = viewIdjTextField;
        jText[9] = viewOriPricejTextField;
        jText[10] = viewDiscountjTextField;
        jText[11] = viewDiscPricejTextField;
        //JRadioButton
        jSpinner[0] = removeMaxMemjSpinner;
        jSpinner[1] = editMaxMemjSpinner;
        jSpinner[2] = viewMaxMemjSpinner;
        //JTextPane
        jTextPane[0] = removeDescjTextPane;
        jTextPane[1] = editDescjTextPane;
        jTextPane[2] = viewDescjTextPane;
    }
    
    private void setTableValue(){
        int totalItemGroup = itemGroups.getNumberOfEntries();
        int totalPageNo = (int) Math.ceil(((double)totalItemGroup/DEFAULT_TABLE_ROWS));
        if(currentPageNo > totalPageNo && totalItemGroup != 0){
            currentPageNo--;
            jTableRowIndex-=5;
        }
        DefaultTableModel model = (DefaultTableModel) jTable[jPanelNo].getModel();
        Iterator<ItemGroup> itemGroupIterator = itemGroups.getStackIterator(jTableRowIndex + 1);
        
        for(int row = 0; row < 5; row++){
            Object[] jTableData = new Object[6];
            jTableData[0] = "-";
            jTableData[1] = "-";
            jTableData[2] = "-";
            jTableData[3] = "-";
            jTableData[4] = "-";
            jTableData[5] = "-";
            if(itemGroupIterator.hasNext()){
                itemGroup = itemGroupIterator.next();
                jTableData[0] = itemGroup.getItemGroupId();
                jTableData[1] = itemGroup.getItemGroupDescription();
                jTableData[2] = itemGroup.getItemGroupMaxMember();
                jTableData[3] = String.format("%.2f", itemGroup.getItemGroupOriginalPrice());
                jTableData[4] = itemGroup.getItemGroupDiscountRate();
                jTableData[5] = String.format("%.2f", itemGroup.getItemGroupFinalPrice());
            }
            for(int columnIndex = 0; columnIndex < 6; columnIndex++)
                model.setValueAt(jTableData[columnIndex], row, columnIndex);
        }
        setTablePageValue();
    }
    
    private void setTablePageValue(){
        int totalItemGroup = itemGroups.getNumberOfEntries();
        int totalPageNo = (int) Math.ceil(((double)totalItemGroup/DEFAULT_TABLE_ROWS));
        if(totalItemGroup == 0)
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
            String itemGroupId = String.format("%c%03d",'I',itemGroup.getNextItemGroupId());
            addIdjTextField.setText(itemGroupId);
            addOriPricejTextField.setText("");
            addDiscountjTextField.setText("4");
            addDiscPricejTextField.setText("");
            addDescjTextPane.setText("");
            addMaxMemjSpinner.setValue(2);
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
        addDiscountjTextField.setText("4");
        changedTableValue = false;
        itemGroups = mainMenu.itemGroups;
        setTableValue();
    }
    
    private void nextPage(){
        int totalItemGroups = itemGroups.getNumberOfEntries();
        int totalPageNo = (int) Math.ceil(((double)totalItemGroups/DEFAULT_TABLE_ROWS));
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
    
    private void removeItemGroup(int buttonPosition){
        int removePosition = jTableRowIndex + buttonPosition;
        boolean successful;
        if(changedTableValue){
            StackInterface<ItemGroup> itemGroupB = mainMenu.itemGroups;
            itemGroup = itemGroups.getEntry(removePosition);
            int itemGroupPosition = itemGroupB.search(itemGroup);
            successful = itemGroupB.remove(itemGroupPosition);
            clearValue();
        }
        else{
            successful = itemGroups.remove(removePosition);
        }
        if(successful){
            JOptionPane.showMessageDialog(this, "Remove Successfully");
            setTableValue();
        }  
        else
            JOptionPane.showMessageDialog(this, "Unable to remove! Please select the row that has value to perform remove!");
    }
    
    private void editItemGroup(int buttonPosition){
        int editPosition = buttonPosition + jTableRowIndex;
        boolean successful;
        int itemGroupPosition;
        if(changedTableValue){
            StackInterface<ItemGroup> itemGroupB = mainMenu.itemGroups;
            itemGroup = itemGroups.getEntry(editPosition);
            itemGroupPosition = itemGroupB.search(itemGroup);
            successful = itemGroupPosition != -1;
        }
        else{
            itemGroup = itemGroups.getEntry(editPosition);
            itemGroupPosition = editPosition;
            successful = itemGroup != null;
        }
        if(successful){
            setVisible(false);
            new EditItemGroupGui(itemGroupPosition).setVisible(true);
        }  
        else
            JOptionPane.showMessageDialog(this, "Unable to edit! Please select the row that has value to perform edit!");
    }
    
    private void viewItemGroup(int buttonPosition){
        int viewPosition = jTableRowIndex + buttonPosition;
        boolean successful;
        int itemGroupPosition;
        if(changedTableValue){
            StackInterface<ItemGroup> itemGroupB = mainMenu.itemGroups;
            itemGroup = itemGroups.getEntry(viewPosition);
            itemGroupPosition = itemGroupB.search(itemGroup);
            successful = itemGroupPosition != -1;
        }
        else{
            itemGroup = itemGroups.getEntry(viewPosition);
            itemGroupPosition = viewPosition;
            successful = itemGroup != null;
        }
        if(successful){        
            setVisible(false);
            new ViewItemGroupGui(itemGroupPosition).setVisible(true);
        }  
        else
            JOptionPane.showMessageDialog(this, "Unable to view! Please select the row that has value to perform view!");
    }
    
    private void searchItemGroup(){
        itemGroups = mainMenu.itemGroups;
        StackInterface<ItemGroup> searchItemGroup = new ArrayStack();
        Iterator<ItemGroup> searchItemGroupIterator;
        String id = jText[(jPanelNo - 1) * 4].getText().toUpperCase();
        String oriPrice = jText[((jPanelNo - 1) * 4) + 1].getText();
        String desc = jTextPane[jPanelNo - 1].getText().toUpperCase();
        int maxMember = (int)jSpinner[jPanelNo - 1].getValue();
        
        boolean enqueued = false;
        int enqueueTimes = 0;
        boolean end = false;
        if(!id.isEmpty()){
            searchItemGroupIterator = itemGroups.getStackIterator();
            while(searchItemGroupIterator.hasNext()){
                itemGroup = searchItemGroupIterator.next();
                if(id.equals(itemGroup.getItemGroupId())){
                    searchItemGroup.push(itemGroup);
                    enqueueTimes++;
                    enqueued = true;
                }
                else{
                    boolean successful;
                    successful = searchTools(id, itemGroup.getItemGroupId());
                    if(successful){
                        searchItemGroup.push(itemGroup);
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
            if(enqueued)
                searchItemGroupIterator = searchItemGroup.getStackIterator();
            else
                searchItemGroupIterator = itemGroups.getStackIterator();
            while(searchItemGroupIterator.hasNext()){
                boolean checkBoth = false;
                itemGroup = searchItemGroupIterator.next();
                if(desc.equals(itemGroup.getItemGroupDescription().toUpperCase())){
                    checkBoth = true;
                    enqueueTimes++;
                }
                else{
                    boolean successful;
                    successful = searchTools(desc, itemGroup.getItemGroupDescription().toUpperCase());
                    if(successful){
                        checkBoth = true;
                        enqueueTimes++;
                    }
                }
                if(checkBoth&&!enqueued)
                    searchItemGroup.push(itemGroup);
                else if(!checkBoth&&enqueued)
                    searchItemGroupIterator.remove();
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&searchItemGroup.getNumberOfEntries()==0)
            end = true;
        if(!oriPrice.isEmpty()&&!end){
            enqueueTimes = 0;
            if(enqueued)
                searchItemGroupIterator = searchItemGroup.getStackIterator();
            else
                searchItemGroupIterator = itemGroups.getStackIterator();
            while(searchItemGroupIterator.hasNext()){
                itemGroup = searchItemGroupIterator.next();
                boolean checkBoth = false;
                if(oriPrice.equals(String.valueOf(itemGroup.getItemGroupOriginalPrice()))){
                    enqueueTimes++;
                    checkBoth = true;
                }
                else{
                    boolean successful;
                    successful = searchTools(oriPrice, String.valueOf(itemGroup.getItemGroupOriginalPrice()));
                    if(successful){
                        checkBoth = true;
                        enqueueTimes++;
                    }
                }
                if(checkBoth&&!enqueued)
                    searchItemGroup.push(itemGroup);
                else if(!checkBoth&&enqueued)
                    searchItemGroupIterator.remove();
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&searchItemGroup.getNumberOfEntries()==0)
            end = true;
        if(!end){
            enqueueTimes = 0;
            if(enqueued)
                searchItemGroupIterator = searchItemGroup.getStackIterator();
            else
                searchItemGroupIterator = itemGroups.getStackIterator();
            while(searchItemGroupIterator.hasNext()){
                itemGroup = searchItemGroupIterator.next();
                boolean checkBoth = false;
                if(maxMember == itemGroup.getItemGroupMaxMember()){
                    checkBoth = true;
                    enqueueTimes++;
                }
                if(checkBoth&&!enqueued)
                    searchItemGroup.push(itemGroup);
                else if(!checkBoth&&enqueued)
                    searchItemGroupIterator.remove();
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&!end){
            StackInterface<ItemGroup> searchItemGroupTranspose = new ArrayStack();
            while(!searchItemGroup.isEmpty()){
                searchItemGroupTranspose.push(searchItemGroup.pop());
            }
            itemGroups = searchItemGroupTranspose;
            changedTableValue = true;
            
        }
        else{
            JOptionPane.showMessageDialog(this, "Member Not Found!Text field has been Reset!");
            clearValue();
            itemGroups = mainMenu.itemGroups;
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
    
    private void exitForm(){
        setVisible(false);
        new mainMenu().setVisible(true);
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
        addItemGroupjButton = new javax.swing.JButton();
        removeItemGroupjButton = new javax.swing.JButton();
        editItemGroupjButton = new javax.swing.JButton();
        viewItemGroupjButton = new javax.swing.JButton();
        backItemGroupjButton = new javax.swing.JButton();
        oriPanel = new javax.swing.JPanel();
        addItemGroup = new javax.swing.JPanel();
        addSmallPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        addIdjTextField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        addDescjTextPane = new javax.swing.JTextPane();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        addMaxMemjSpinner = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        addOriPricejTextField = new javax.swing.JTextField();
        addDiscountjTextField = new javax.swing.JTextField();
        addDiscPricejTextField = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        addClearButton = new javax.swing.JButton();
        addGroupButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        addjTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        addPreviousPageButton = new javax.swing.JButton();
        addNextPageButton = new javax.swing.JButton();
        addPagejLabel = new javax.swing.JLabel();
        removeItemGroup = new javax.swing.JPanel();
        removeSmallPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        removeIdjTextField = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        removeDescjTextPane = new javax.swing.JTextPane();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        removeMaxMemjSpinner = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        removeOriPricejTextField = new javax.swing.JTextField();
        removeDiscPricejTextField = new javax.swing.JTextField();
        removeDiscountjTextField = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        removejTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        removePreviousPageButton = new javax.swing.JButton();
        removePagejLabel = new javax.swing.JLabel();
        removeNextPageButton = new javax.swing.JButton();
        removeSearchButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        deleteButton1 = new javax.swing.JButton();
        deleteButton2 = new javax.swing.JButton();
        deleteButton3 = new javax.swing.JButton();
        deleteButton4 = new javax.swing.JButton();
        deleteButton5 = new javax.swing.JButton();
        removeClearButton = new javax.swing.JButton();
        editItemGroup = new javax.swing.JPanel();
        editSearchButton = new javax.swing.JButton();
        editClearButton = new javax.swing.JButton();
        editSmallPanel = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        editIdjTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        editDescjTextPane = new javax.swing.JTextPane();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        editMaxMemjSpinner = new javax.swing.JSpinner();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        editOriPricejTextField = new javax.swing.JTextField();
        editDiscountjTextField = new javax.swing.JTextField();
        editDiscPricejTextField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        editjTable = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        editPreviousPageButton = new javax.swing.JButton();
        editPagejLabel = new javax.swing.JLabel();
        editNextPageButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        editButton1 = new javax.swing.JButton();
        editButton2 = new javax.swing.JButton();
        editButton3 = new javax.swing.JButton();
        editButton4 = new javax.swing.JButton();
        editButton5 = new javax.swing.JButton();
        retrieveItemGroup = new javax.swing.JPanel();
        retrieveSmallPanel = new javax.swing.JPanel();
        jLabel31 = new javax.swing.JLabel();
        viewIdjTextField = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        viewDescjTextPane = new javax.swing.JTextPane();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        viewMaxMemjSpinner = new javax.swing.JSpinner();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        viewOriPricejTextField = new javax.swing.JTextField();
        viewDiscountjTextField = new javax.swing.JTextField();
        viewDiscPricejTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        viewSearchButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        veiwButton1 = new javax.swing.JButton();
        viewButton2 = new javax.swing.JButton();
        viewButton3 = new javax.swing.JButton();
        viewButton4 = new javax.swing.JButton();
        viewButton5 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        viewjTable = new javax.swing.JTable();
        jLabel39 = new javax.swing.JLabel();
        viewPreviousPageButton = new javax.swing.JButton();
        viewPagejLabel = new javax.swing.JLabel();
        viewNextPageButton = new javax.swing.JButton();
        viewClearButton = new javax.swing.JButton();
        generateItemReport = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Item Group Maintenance");
        setName("itemGroupMaintenance"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPane1.setDividerSize(1);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setForeground(new java.awt.Color(255, 51, 204));

        addItemGroupjButton.setBackground(new java.awt.Color(0, 0, 0));
        addItemGroupjButton.setForeground(new java.awt.Color(255, 255, 255));
        addItemGroupjButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        addItemGroupjButton.setLabel("Add item group");
        addItemGroupjButton.setName("itemAddButton"); // NOI18N
        addItemGroupjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemGroupjButtonActionPerformed(evt);
            }
        });

        removeItemGroupjButton.setBackground(new java.awt.Color(0, 0, 0));
        removeItemGroupjButton.setForeground(new java.awt.Color(255, 255, 255));
        removeItemGroupjButton.setText("Remove Item Group");
        removeItemGroupjButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        removeItemGroupjButton.setMaximumSize(new java.awt.Dimension(123, 29));
        removeItemGroupjButton.setMinimumSize(new java.awt.Dimension(123, 29));
        removeItemGroupjButton.setName("itemRemoveButton"); // NOI18N
        removeItemGroupjButton.setPreferredSize(new java.awt.Dimension(123, 29));
        removeItemGroupjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeItemGroupjButtonActionPerformed(evt);
            }
        });

        editItemGroupjButton.setBackground(new java.awt.Color(0, 0, 0));
        editItemGroupjButton.setForeground(new java.awt.Color(255, 255, 255));
        editItemGroupjButton.setText("Edit Item Group");
        editItemGroupjButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        editItemGroupjButton.setName("groupEditButton"); // NOI18N
        editItemGroupjButton.setPreferredSize(new java.awt.Dimension(103, 29));
        editItemGroupjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemGroupjButtonActionPerformed(evt);
            }
        });

        viewItemGroupjButton.setBackground(new java.awt.Color(0, 0, 0));
        viewItemGroupjButton.setForeground(new java.awt.Color(255, 255, 255));
        viewItemGroupjButton.setText("Retrieve Item Group Details");
        viewItemGroupjButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        viewItemGroupjButton.setName("itemRetrieveButton"); // NOI18N
        viewItemGroupjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewItemGroupjButtonActionPerformed(evt);
            }
        });

        backItemGroupjButton.setBackground(new java.awt.Color(0, 0, 0));
        backItemGroupjButton.setForeground(new java.awt.Color(255, 255, 255));
        backItemGroupjButton.setText("Back");
        backItemGroupjButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        backItemGroupjButton.setName("itemRetrieveButton"); // NOI18N
        backItemGroupjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backItemGroupjButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewItemGroupjButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editItemGroupjButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeItemGroupjButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addItemGroupjButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(backItemGroupjButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(addItemGroupjButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeItemGroupjButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editItemGroupjButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewItemGroupjButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 350, Short.MAX_VALUE)
                .addComponent(backItemGroupjButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
        );

        jSplitPane1.setLeftComponent(jPanel3);

        oriPanel.setLayout(new java.awt.CardLayout());

        addItemGroup.setBackground(new java.awt.Color(255, 204, 204));

        addSmallPanel.setBackground(new java.awt.Color(255, 255, 255));
        addSmallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ADD ITEM GROUP ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N
        addSmallPanel.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                addSmallPanelInputMethodTextChanged(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel6.setText("Group ID: ");

        addIdjTextField.setEditable(false);
        addIdjTextField.setText("G001");
        addIdjTextField.setEnabled(false);

        jScrollPane2.setViewportView(addDescjTextPane);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setText("Group Description: ");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel7.setText("Group Maximum Member: ");

        addMaxMemjSpinner.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        addMaxMemjSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                addMaxMemjSpinnerStateChanged(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel8.setText("Item Original Price / Unit: ");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel9.setText("Item Discount Rate: ");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel10.setText("Item Discounted Price / Unit: ");

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel11.setText("RM ");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel4.setText("RM ");

        addOriPricejTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                addOriPricejTextFieldCaretUpdate(evt);
            }
        });

        addDiscountjTextField.setEditable(false);

        addDiscPricejTextField.setEditable(false);

        jLabel40.setText("%");

        javax.swing.GroupLayout addSmallPanelLayout = new javax.swing.GroupLayout(addSmallPanel);
        addSmallPanel.setLayout(addSmallPanelLayout);
        addSmallPanelLayout.setHorizontalGroup(
            addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addSmallPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addMaxMemjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addSmallPanelLayout.createSequentialGroup()
                        .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(addSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addDiscountjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(addDiscPricejTextField))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(addOriPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        addSmallPanelLayout.setVerticalGroup(
            addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addSmallPanelLayout.createSequentialGroup()
                .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addMaxMemjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel4)
                    .addComponent(addOriPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(addDiscountjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40))
                .addGap(10, 10, 10)
                .addGroup(addSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(addDiscPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        addClearButton.setText("Clear");
        addClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addClearButtonActionPerformed(evt);
            }
        });

        addGroupButton.setText("Add");
        addGroupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addGroupButtonActionPerformed(evt);
            }
        });

        addjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Group ID", "Group Description", "Max Member", "Original Price (RM)", "Discount Rate", "Discounted Price (RM)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        addjTable.setRowHeight(30);
        jScrollPane1.setViewportView(addjTable);

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel1.setText("Group List");

        addPreviousPageButton.setText("<");
        addPreviousPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPreviousPageButtonActionPerformed(evt);
            }
        });

        addNextPageButton.setText(">");
        addNextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNextPageButtonActionPerformed(evt);
            }
        });

        addPagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        addPagejLabel.setText("Page 1 of 1");

        javax.swing.GroupLayout addItemGroupLayout = new javax.swing.GroupLayout(addItemGroup);
        addItemGroup.setLayout(addItemGroupLayout);
        addItemGroupLayout.setHorizontalGroup(
            addItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addItemGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(95, Short.MAX_VALUE))
            .addGroup(addItemGroupLayout.createSequentialGroup()
                .addGroup(addItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addItemGroupLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(addItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(addClearButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(addGroupButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(addItemGroupLayout.createSequentialGroup()
                        .addGap(191, 191, 191)
                        .addComponent(addPreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(addPagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(addNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        addItemGroupLayout.setVerticalGroup(
            addItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addItemGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addItemGroupLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(addGroupButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(addClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(addItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addPreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(addNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(addPagejLabel)))
                .addGap(19, 19, 19))
        );

        oriPanel.add(addItemGroup, "addItemGroup");

        removeItemGroup.setBackground(new java.awt.Color(255, 204, 204));

        removeSmallPanel.setBackground(new java.awt.Color(255, 255, 255));
        removeSmallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "REMOVE ITEM GROUP ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel12.setText("Group Code: ");

        jScrollPane6.setViewportView(removeDescjTextPane);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel13.setText("Group Description: ");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel14.setText("Group Maximum Member: ");

        removeMaxMemjSpinner.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        removeMaxMemjSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                removeMaxMemjSpinnerStateChanged(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel15.setText("Item Original Price / Unit: ");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel16.setText("Item Discount Rate: ");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel17.setText("Item Discounted Price / Unit: ");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel18.setText("RM ");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel19.setText("RM ");

        removeOriPricejTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                removeOriPricejTextFieldCaretUpdate(evt);
            }
        });

        removeDiscPricejTextField.setEditable(false);

        removeDiscountjTextField.setEditable(false);

        jLabel30.setText("%");

        javax.swing.GroupLayout removeSmallPanelLayout = new javax.swing.GroupLayout(removeSmallPanel);
        removeSmallPanel.setLayout(removeSmallPanelLayout);
        removeSmallPanelLayout.setHorizontalGroup(
            removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeSmallPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeDiscountjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeMaxMemjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeSmallPanelLayout.createSequentialGroup()
                        .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(removeIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, removeSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel18)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(removeDiscPricejTextField))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, removeSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel19)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(removeOriPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        removeSmallPanelLayout.setVerticalGroup(
            removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeSmallPanelLayout.createSequentialGroup()
                .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removeMaxMemjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel19)
                    .addComponent(removeOriPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(removeDiscountjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel30))
                .addGap(10, 10, 10)
                .addGroup(removeSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(removeDiscPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        removejTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Group Code", "Group Description", "Max Member", "Original Price (RM)", "Discount Rate", "Discounted Price (RM)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        removejTable.setRowHeight(30);
        jScrollPane10.setViewportView(removejTable);

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel3.setText("Group List");

        removePreviousPageButton.setText("<");
        removePreviousPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removePreviousPageButtonActionPerformed(evt);
            }
        });

        removePagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        removePagejLabel.setText("Page 1 of 1");

        removeNextPageButton.setText(">");
        removeNextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeNextPageButtonActionPerformed(evt);
            }
        });

        removeSearchButton.setText("Search");
        removeSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSearchButtonActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        deleteButton1.setBackground(new java.awt.Color(255, 255, 255));
        deleteButton1.setText("Delete");
        deleteButton1.setActionCommand("delete1");
        deleteButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton1ActionPerformed(evt);
            }
        });

        deleteButton2.setBackground(new java.awt.Color(255, 255, 255));
        deleteButton2.setText("Delete");
        deleteButton2.setActionCommand("delete2");
        deleteButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton2ActionPerformed(evt);
            }
        });

        deleteButton3.setBackground(new java.awt.Color(255, 255, 255));
        deleteButton3.setText("Delete");
        deleteButton3.setActionCommand("delete3");
        deleteButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton3ActionPerformed(evt);
            }
        });

        deleteButton4.setBackground(new java.awt.Color(255, 255, 255));
        deleteButton4.setText("Delete");
        deleteButton4.setActionCommand("delete4");
        deleteButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton4ActionPerformed(evt);
            }
        });

        deleteButton5.setBackground(new java.awt.Color(255, 255, 255));
        deleteButton5.setText("Delete");
        deleteButton5.setActionCommand("delete5");
        deleteButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(deleteButton3)
                    .addComponent(deleteButton4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deleteButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(deleteButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(deleteButton5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deleteButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteButton5)
                .addGap(14, 14, 14))
        );

        removeClearButton.setText("Clear");
        removeClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeClearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout removeItemGroupLayout = new javax.swing.GroupLayout(removeItemGroup);
        removeItemGroup.setLayout(removeItemGroupLayout);
        removeItemGroupLayout.setHorizontalGroup(
            removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeItemGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeItemGroupLayout.createSequentialGroup()
                        .addGroup(removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(removeItemGroupLayout.createSequentialGroup()
                                .addGroup(removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(removeItemGroupLayout.createSequentialGroup()
                                        .addGap(179, 179, 179)
                                        .addComponent(removePreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(47, 47, 47)
                                        .addComponent(removePagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(removeNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(removeItemGroupLayout.createSequentialGroup()
                        .addComponent(removeSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(removeSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(removeClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        removeItemGroupLayout.setVerticalGroup(
            removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeItemGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(removeItemGroupLayout.createSequentialGroup()
                        .addGroup(removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(removeItemGroupLayout.createSequentialGroup()
                                .addComponent(removeSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3))
                            .addGroup(removeItemGroupLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(removeSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(removeClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(removePreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(removeItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(removeNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(removePagejLabel)))
                .addContainerGap(132, Short.MAX_VALUE))
        );

        oriPanel.add(removeItemGroup, "removeItemGroup");

        editItemGroup.setBackground(new java.awt.Color(255, 204, 204));

        editSearchButton.setText("Search");
        editSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSearchButtonActionPerformed(evt);
            }
        });

        editClearButton.setText("Clear");
        editClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editClearButtonActionPerformed(evt);
            }
        });

        editSmallPanel.setBackground(new java.awt.Color(255, 255, 255));
        editSmallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "EDIT ITEM GROUP ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel21.setText("Group Code: ");

        jScrollPane3.setViewportView(editDescjTextPane);

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel22.setText("Group Description: ");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel23.setText("Group Maximum Member: ");

        editMaxMemjSpinner.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        editMaxMemjSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                editMaxMemjSpinnerStateChanged(evt);
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

        editOriPricejTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                editOriPricejTextFieldCaretUpdate(evt);
            }
        });

        editDiscountjTextField.setEditable(false);

        editDiscPricejTextField.setEditable(false);

        jLabel20.setText("%");

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
                        .addComponent(editMaxMemjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editSmallPanelLayout.createSequentialGroup()
                        .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(editIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(editSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editDiscountjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel27)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(editDiscPricejTextField))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel28)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(editOriPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        editSmallPanelLayout.setVerticalGroup(
            editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editSmallPanelLayout.createSequentialGroup()
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editMaxMemjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jLabel28)
                    .addComponent(editOriPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(editDiscountjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addGap(10, 10, 10)
                .addGroup(editSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(editDiscPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        editjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Group Code", "Group Description", "Max Member", "Original Price (RM)", "Discount Rate", "Discounted Price (RM)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        editjTable.setRowHeight(30);
        jScrollPane4.setViewportView(editjTable);

        jLabel29.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel29.setText("Group List");

        editPreviousPageButton.setText("<");
        editPreviousPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editPreviousPageButtonActionPerformed(evt);
            }
        });

        editPagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        editPagejLabel.setText("Page 1 of 1");

        editNextPageButton.setText(">");
        editNextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editNextPageButtonActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        editButton1.setBackground(new java.awt.Color(255, 255, 255));
        editButton1.setText("Edit");
        editButton1.setActionCommand("edit1");
        editButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButton1ActionPerformed(evt);
            }
        });

        editButton2.setBackground(new java.awt.Color(255, 255, 255));
        editButton2.setText("Edit");
        editButton2.setActionCommand("edit2");
        editButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButton2ActionPerformed(evt);
            }
        });

        editButton3.setBackground(new java.awt.Color(255, 255, 255));
        editButton3.setText("Edit");
        editButton3.setActionCommand("edit3");
        editButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButton3ActionPerformed(evt);
            }
        });

        editButton4.setBackground(new java.awt.Color(255, 255, 255));
        editButton4.setText("Edit");
        editButton4.setActionCommand("edit4");
        editButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButton4ActionPerformed(evt);
            }
        });

        editButton5.setBackground(new java.awt.Color(255, 255, 255));
        editButton5.setText("Edit");
        editButton5.setActionCommand("edit5");
        editButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButton5ActionPerformed(evt);
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
                            .addComponent(editButton3)
                            .addComponent(editButton4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(editButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(editButton5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(editButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editButton5)
                .addGap(14, 14, 14))
        );

        javax.swing.GroupLayout editItemGroupLayout = new javax.swing.GroupLayout(editItemGroup);
        editItemGroup.setLayout(editItemGroupLayout);
        editItemGroupLayout.setHorizontalGroup(
            editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editItemGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editItemGroupLayout.createSequentialGroup()
                        .addGroup(editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(editItemGroupLayout.createSequentialGroup()
                                .addGroup(editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(editItemGroupLayout.createSequentialGroup()
                                        .addGap(174, 174, 174)
                                        .addComponent(editPreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(47, 47, 47)
                                        .addComponent(editPagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(editNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(30, Short.MAX_VALUE))
                    .addGroup(editItemGroupLayout.createSequentialGroup()
                        .addComponent(editSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(editClearButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(editSearchButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        editItemGroupLayout.setVerticalGroup(
            editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editItemGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editItemGroupLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(editSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(editClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editPreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(editNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(editPagejLabel)))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        oriPanel.add(editItemGroup, "editItemGroup");

        retrieveItemGroup.setBackground(new java.awt.Color(255, 204, 204));

        retrieveSmallPanel.setBackground(new java.awt.Color(255, 255, 255));
        retrieveSmallPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "VIEW ITEM GROUP ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel31.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel31.setText("Group Code: ");

        jScrollPane5.setViewportView(viewDescjTextPane);

        jLabel32.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel32.setText("Group Description: ");

        jLabel33.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel33.setText("Group Maximum Member: ");

        viewMaxMemjSpinner.setModel(new javax.swing.SpinnerNumberModel(2, 2, 5, 1));
        viewMaxMemjSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                viewMaxMemjSpinnerStateChanged(evt);
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

        viewOriPricejTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                viewOriPricejTextFieldCaretUpdate(evt);
            }
        });

        viewDiscountjTextField.setEditable(false);

        viewDiscPricejTextField.setEditable(false);

        jLabel2.setText("%");

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
                        .addComponent(viewMaxMemjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 318, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(viewIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                        .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewDiscountjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel37)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(viewDiscPricejTextField))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, retrieveSmallPanelLayout.createSequentialGroup()
                            .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel38)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(viewOriPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        retrieveSmallPanelLayout.setVerticalGroup(
            retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveSmallPanelLayout.createSequentialGroup()
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewMaxMemjSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(jLabel38)
                    .addComponent(viewOriPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(viewDiscountjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(10, 10, 10)
                .addGroup(retrieveSmallPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(jLabel37)
                    .addComponent(viewDiscPricejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );

        viewSearchButton.setText("Search");
        viewSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewSearchButtonActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));

        veiwButton1.setBackground(new java.awt.Color(255, 255, 255));
        veiwButton1.setText("View");
        veiwButton1.setActionCommand("view1");
        veiwButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                veiwButton1ActionPerformed(evt);
            }
        });

        viewButton2.setBackground(new java.awt.Color(255, 255, 255));
        viewButton2.setText("View");
        viewButton2.setActionCommand("view2");
        viewButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButton2ActionPerformed(evt);
            }
        });

        viewButton3.setBackground(new java.awt.Color(255, 255, 255));
        viewButton3.setText("View");
        viewButton3.setActionCommand("view3");
        viewButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButton3ActionPerformed(evt);
            }
        });

        viewButton4.setBackground(new java.awt.Color(255, 255, 255));
        viewButton4.setText("View");
        viewButton4.setActionCommand("view4");
        viewButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButton4ActionPerformed(evt);
            }
        });

        viewButton5.setBackground(new java.awt.Color(255, 255, 255));
        viewButton5.setText("View");
        viewButton5.setActionCommand("view5");
        viewButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButton5ActionPerformed(evt);
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
                            .addComponent(viewButton3)
                            .addComponent(viewButton4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(veiwButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(viewButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(viewButton5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(veiwButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(viewButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(viewButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(viewButton5)
                .addGap(14, 14, 14))
        );

        viewjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Group Code", "Group Description", "Max Member", "Original Price (RM)", "Discount Rate", "Discounted Price (RM)"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        viewjTable.setRowHeight(30);
        jScrollPane7.setViewportView(viewjTable);

        jLabel39.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel39.setText("Group List");

        viewPreviousPageButton.setText("<");
        viewPreviousPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewPreviousPageButtonActionPerformed(evt);
            }
        });

        viewPagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        viewPagejLabel.setText("Page 1 of 1");

        viewNextPageButton.setText(">");
        viewNextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewNextPageButtonActionPerformed(evt);
            }
        });

        viewClearButton.setText("Clear");
        viewClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewClearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout retrieveItemGroupLayout = new javax.swing.GroupLayout(retrieveItemGroup);
        retrieveItemGroup.setLayout(retrieveItemGroupLayout);
        retrieveItemGroupLayout.setHorizontalGroup(
            retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveItemGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retrieveItemGroupLayout.createSequentialGroup()
                        .addGroup(retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(retrieveItemGroupLayout.createSequentialGroup()
                                .addGroup(retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(retrieveItemGroupLayout.createSequentialGroup()
                                        .addGap(168, 168, 168)
                                        .addComponent(viewPreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(47, 47, 47)
                                        .addComponent(viewPagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(34, 34, 34)
                                        .addComponent(viewNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(24, Short.MAX_VALUE))
                    .addGroup(retrieveItemGroupLayout.createSequentialGroup()
                        .addComponent(retrieveSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(viewSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(viewClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        retrieveItemGroupLayout.setVerticalGroup(
            retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveItemGroupLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(retrieveSmallPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(retrieveItemGroupLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(viewSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(viewClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewPreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(retrieveItemGroupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(viewNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(viewPagejLabel)))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        oriPanel.add(retrieveItemGroup, "retrieveItemGroup");

        javax.swing.GroupLayout generateItemReportLayout = new javax.swing.GroupLayout(generateItemReport);
        generateItemReport.setLayout(generateItemReportLayout);
        generateItemReportLayout.setHorizontalGroup(
            generateItemReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 840, Short.MAX_VALUE)
        );
        generateItemReportLayout.setVerticalGroup(
            generateItemReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        oriPanel.add(generateItemReport, "generateItemReport");

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

    private void addClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_addClearButtonActionPerformed

    private void addGroupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addGroupButtonActionPerformed
        // TODO add your handling code here:
        if(!addOriPricejTextField.getText().isEmpty() && !addDescjTextPane.getText().isEmpty() && (addMaxMemjSpinner.getValue() != null)){
            String id = String.format("%c%03d",'I',itemGroup.getNextItemGroupId());
            String desc = addDescjTextPane.getText();
            int maxMember = (int)addMaxMemjSpinner.getValue();
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(addOriPricejTextField.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
                JOptionPane.showMessageDialog(this, "Only can enter digit or '.' inside Original Price Text Field!");
            }
            if(isDouble){
                int discount = maxMember * 2;
                String discPriceStr = String.format("%.2f", oriPrice - (oriPrice * discount / 100.00));
                double discPrice = Double.parseDouble(discPriceStr);
                itemGroup = new ItemGroup(id, desc, maxMember, oriPrice, discount, discPrice);
                itemGroups.push(itemGroup);
                clearValue();
                id = String.format("%c%03d",'I',itemGroup.getNextItemGroupId());
                addIdjTextField.setText(id);
                JOptionPane.showMessageDialog(this, "Item Group Added Successfully!");
            }  
        }
        else{
            JOptionPane.showMessageDialog(this, "Item Group Added Unsuccessfully! Please fill in all data column!");
        }
    }//GEN-LAST:event_addGroupButtonActionPerformed

    private void addPreviousPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPreviousPageButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_addPreviousPageButtonActionPerformed

    private void addNextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNextPageButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_addNextPageButtonActionPerformed

    private void addItemGroupjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemGroupjButtonActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "addItemGroup");
        switchjPanel(0);
    }//GEN-LAST:event_addItemGroupjButtonActionPerformed

    private void removeItemGroupjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeItemGroupjButtonActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "removeItemGroup");
        switchjPanel(1);
    }//GEN-LAST:event_removeItemGroupjButtonActionPerformed

    private void editItemGroupjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemGroupjButtonActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "editItemGroup");
        switchjPanel(2);
    }//GEN-LAST:event_editItemGroupjButtonActionPerformed

    private void viewItemGroupjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewItemGroupjButtonActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "retrieveItemGroup");
        switchjPanel(3);
    }//GEN-LAST:event_viewItemGroupjButtonActionPerformed

    private void removePreviousPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removePreviousPageButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_removePreviousPageButtonActionPerformed

    private void removeNextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeNextPageButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_removeNextPageButtonActionPerformed

    private void deleteButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton1ActionPerformed
        // TODO add your handling code here:
        removeItemGroup(1);
    }//GEN-LAST:event_deleteButton1ActionPerformed

    private void deleteButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton2ActionPerformed
        // TODO add your handling code here:
        removeItemGroup(2);
    }//GEN-LAST:event_deleteButton2ActionPerformed

    private void deleteButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton3ActionPerformed
        // TODO add your handling code here:
        removeItemGroup(3);
    }//GEN-LAST:event_deleteButton3ActionPerformed

    private void deleteButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton4ActionPerformed
        // TODO add your handling code here:
        removeItemGroup(4);
    }//GEN-LAST:event_deleteButton4ActionPerformed

    private void deleteButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButton5ActionPerformed
        // TODO add your handling code here:
        removeItemGroup(5);
    }//GEN-LAST:event_deleteButton5ActionPerformed

    private void removeSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSearchButtonActionPerformed
        // TODO add your handling code here:
        searchItemGroup();
    }//GEN-LAST:event_removeSearchButtonActionPerformed

    private void editSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSearchButtonActionPerformed
        // TODO add your handling code here:
        searchItemGroup();
    }//GEN-LAST:event_editSearchButtonActionPerformed

    private void editPreviousPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editPreviousPageButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_editPreviousPageButtonActionPerformed

    private void editNextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editNextPageButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_editNextPageButtonActionPerformed

    private void editClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_editClearButtonActionPerformed

    private void editButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButton1ActionPerformed
        // TODO add your handling code here:
        editItemGroup(1);
    }//GEN-LAST:event_editButton1ActionPerformed

    private void editButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButton2ActionPerformed
        // TODO add your handling code here:
        editItemGroup(2);
    }//GEN-LAST:event_editButton2ActionPerformed

    private void editButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButton3ActionPerformed
        // TODO add your handling code here:
        editItemGroup(3);
    }//GEN-LAST:event_editButton3ActionPerformed

    private void editButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButton4ActionPerformed
        // TODO add your handling code here:
        editItemGroup(4);
    }//GEN-LAST:event_editButton4ActionPerformed

    private void editButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButton5ActionPerformed
        // TODO add your handling code here:
        editItemGroup(5);
    }//GEN-LAST:event_editButton5ActionPerformed

    private void veiwButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_veiwButton1ActionPerformed
        // TODO add your handling code here:
        viewItemGroup(1);
    }//GEN-LAST:event_veiwButton1ActionPerformed

    private void viewButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButton2ActionPerformed
        // TODO add your handling code here:
        viewItemGroup(2);
    }//GEN-LAST:event_viewButton2ActionPerformed

    private void viewButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButton3ActionPerformed
        // TODO add your handling code here:
        viewItemGroup(3);
    }//GEN-LAST:event_viewButton3ActionPerformed

    private void viewButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButton4ActionPerformed
        // TODO add your handling code here:
        viewItemGroup(4);
    }//GEN-LAST:event_viewButton4ActionPerformed

    private void viewButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButton5ActionPerformed
        // TODO add your handling code here:
        viewItemGroup(5);
    }//GEN-LAST:event_viewButton5ActionPerformed

    private void viewPreviousPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewPreviousPageButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_viewPreviousPageButtonActionPerformed

    private void viewNextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewNextPageButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_viewNextPageButtonActionPerformed

    private void viewSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewSearchButtonActionPerformed
        // TODO add your handling code here:
        searchItemGroup();
    }//GEN-LAST:event_viewSearchButtonActionPerformed

    private void removeClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_removeClearButtonActionPerformed

    private void viewClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_viewClearButtonActionPerformed

    private void backItemGroupjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backItemGroupjButtonActionPerformed
        // TODO add your handling code here:
        exitForm();
    }//GEN-LAST:event_backItemGroupjButtonActionPerformed

    private void addSmallPanelInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_addSmallPanelInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_addSmallPanelInputMethodTextChanged

    private void addMaxMemjSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_addMaxMemjSpinnerStateChanged
        // TODO add your handling code here:
        int maxMember = (int)addMaxMemjSpinner.getValue();
        int discount = maxMember * 2;
        addDiscountjTextField.setText(String.format("%d",discount));
        if(!addOriPricejTextField.getText().isEmpty()){
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(addOriPricejTextField.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
            }
            if(isDouble)
                addDiscPricejTextField.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_addMaxMemjSpinnerStateChanged

    private void addOriPricejTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_addOriPricejTextFieldCaretUpdate
        // TODO add your handling code here:
        boolean isDouble = true;
        double oriPrice = 0.00;
        try{
            oriPrice = Double.parseDouble(addOriPricejTextField.getText());
        }
        catch (NumberFormatException ex) {
            isDouble = false;
        }
        if(isDouble){
            int discount = Integer.parseInt(addDiscountjTextField.getText());
            addDiscPricejTextField.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_addOriPricejTextFieldCaretUpdate

    private void removeMaxMemjSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_removeMaxMemjSpinnerStateChanged
        // TODO add your handling code here:
        int maxMember = (int)removeMaxMemjSpinner.getValue();
        int discount = maxMember * 2;
        removeDiscountjTextField.setText(String.format("%d",discount));
        if(!removeOriPricejTextField.getText().isEmpty()){
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(removeOriPricejTextField.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
            }
            if(isDouble)
                removeDiscPricejTextField.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_removeMaxMemjSpinnerStateChanged

    private void removeOriPricejTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_removeOriPricejTextFieldCaretUpdate
        // TODO add your handling code here:
        boolean isDouble = true;
        double oriPrice = 0.00;
        try{
            oriPrice = Double.parseDouble(removeOriPricejTextField.getText());
        }
        catch (NumberFormatException ex) {
            isDouble = false;
        }
        if(isDouble){
            int discount = Integer.parseInt(removeDiscountjTextField.getText());
            removeDiscPricejTextField.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_removeOriPricejTextFieldCaretUpdate

    private void editMaxMemjSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_editMaxMemjSpinnerStateChanged
        // TODO add your handling code here:
        int maxMember = (int)editMaxMemjSpinner.getValue();
        int discount = maxMember * 2;
        editDiscountjTextField.setText(String.format("%d",discount));
        if(!editOriPricejTextField.getText().isEmpty()){
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(editOriPricejTextField.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
            }
            if(isDouble)
                editDiscPricejTextField.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_editMaxMemjSpinnerStateChanged

    private void editOriPricejTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_editOriPricejTextFieldCaretUpdate
        // TODO add your handling code here:
        boolean isDouble = true;
        double oriPrice = 0.00;
        try{
            oriPrice = Double.parseDouble(editOriPricejTextField.getText());
        }
        catch (NumberFormatException ex) {
            isDouble = false;
        }
        if(isDouble){
            int discount = Integer.parseInt(editDiscountjTextField.getText());
            editDiscPricejTextField.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_editOriPricejTextFieldCaretUpdate

    private void viewMaxMemjSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_viewMaxMemjSpinnerStateChanged
        // TODO add your handling code here:
        int maxMember = (int)viewMaxMemjSpinner.getValue();
        int discount = maxMember * 2;
        viewDiscountjTextField.setText(String.format("%d",discount));
        if(!viewOriPricejTextField.getText().isEmpty()){
            boolean isDouble = true;
            double oriPrice = 0.00;
            try{
                oriPrice = Double.parseDouble(viewOriPricejTextField.getText());
            }
            catch (NumberFormatException ex) {
                isDouble = false;
            }
            if(isDouble)
                viewDiscPricejTextField.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_viewMaxMemjSpinnerStateChanged

    private void viewOriPricejTextFieldCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_viewOriPricejTextFieldCaretUpdate
        // TODO add your handling code here:
        boolean isDouble = true;
        double oriPrice = 0.00;
        try{
            oriPrice = Double.parseDouble(viewOriPricejTextField.getText());
        }
        catch (NumberFormatException ex) {
            isDouble = false;
        }
        if(isDouble){
            int discount = Integer.parseInt(viewDiscountjTextField.getText());
            viewDiscPricejTextField.setText(String.format("%.2f", oriPrice - (oriPrice * discount / 100.00)));
        }
    }//GEN-LAST:event_viewOriPricejTextFieldCaretUpdate

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        exitForm();
    }//GEN-LAST:event_formWindowClosing

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
//            java.util.logging.Logger.getLogger(ItemGroupMaintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ItemGroupMaintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ItemGroupMaintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ItemGroupMaintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ItemGroupMaintenance().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addClearButton;
    private javax.swing.JTextPane addDescjTextPane;
    private javax.swing.JTextField addDiscPricejTextField;
    private javax.swing.JTextField addDiscountjTextField;
    private javax.swing.JButton addGroupButton;
    private javax.swing.JTextField addIdjTextField;
    private javax.swing.JPanel addItemGroup;
    private javax.swing.JButton addItemGroupjButton;
    private javax.swing.JSpinner addMaxMemjSpinner;
    private javax.swing.JButton addNextPageButton;
    private javax.swing.JTextField addOriPricejTextField;
    private javax.swing.JLabel addPagejLabel;
    private javax.swing.JButton addPreviousPageButton;
    private javax.swing.JPanel addSmallPanel;
    private javax.swing.JTable addjTable;
    private javax.swing.JButton backItemGroupjButton;
    private javax.swing.JButton deleteButton1;
    private javax.swing.JButton deleteButton2;
    private javax.swing.JButton deleteButton3;
    private javax.swing.JButton deleteButton4;
    private javax.swing.JButton deleteButton5;
    private javax.swing.JButton editButton1;
    private javax.swing.JButton editButton2;
    private javax.swing.JButton editButton3;
    private javax.swing.JButton editButton4;
    private javax.swing.JButton editButton5;
    private javax.swing.JButton editClearButton;
    private javax.swing.JTextPane editDescjTextPane;
    private javax.swing.JTextField editDiscPricejTextField;
    private javax.swing.JTextField editDiscountjTextField;
    private javax.swing.JTextField editIdjTextField;
    private javax.swing.JPanel editItemGroup;
    private javax.swing.JButton editItemGroupjButton;
    private javax.swing.JSpinner editMaxMemjSpinner;
    private javax.swing.JButton editNextPageButton;
    private javax.swing.JTextField editOriPricejTextField;
    private javax.swing.JLabel editPagejLabel;
    private javax.swing.JButton editPreviousPageButton;
    private javax.swing.JButton editSearchButton;
    private javax.swing.JPanel editSmallPanel;
    private javax.swing.JTable editjTable;
    private javax.swing.JPanel generateItemReport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel oriPanel;
    private javax.swing.JButton removeClearButton;
    private javax.swing.JTextPane removeDescjTextPane;
    private javax.swing.JTextField removeDiscPricejTextField;
    private javax.swing.JTextField removeDiscountjTextField;
    private javax.swing.JTextField removeIdjTextField;
    private javax.swing.JPanel removeItemGroup;
    private javax.swing.JButton removeItemGroupjButton;
    private javax.swing.JSpinner removeMaxMemjSpinner;
    private javax.swing.JButton removeNextPageButton;
    private javax.swing.JTextField removeOriPricejTextField;
    private javax.swing.JLabel removePagejLabel;
    private javax.swing.JButton removePreviousPageButton;
    private javax.swing.JButton removeSearchButton;
    private javax.swing.JPanel removeSmallPanel;
    private javax.swing.JTable removejTable;
    private javax.swing.JPanel retrieveItemGroup;
    private javax.swing.JPanel retrieveSmallPanel;
    private javax.swing.JButton veiwButton1;
    private javax.swing.JButton viewButton2;
    private javax.swing.JButton viewButton3;
    private javax.swing.JButton viewButton4;
    private javax.swing.JButton viewButton5;
    private javax.swing.JButton viewClearButton;
    private javax.swing.JTextPane viewDescjTextPane;
    private javax.swing.JTextField viewDiscPricejTextField;
    private javax.swing.JTextField viewDiscountjTextField;
    private javax.swing.JTextField viewIdjTextField;
    private javax.swing.JButton viewItemGroupjButton;
    private javax.swing.JSpinner viewMaxMemjSpinner;
    private javax.swing.JButton viewNextPageButton;
    private javax.swing.JTextField viewOriPricejTextField;
    private javax.swing.JLabel viewPagejLabel;
    private javax.swing.JButton viewPreviousPageButton;
    private javax.swing.JButton viewSearchButton;
    private javax.swing.JTable viewjTable;
    // End of variables declaration//GEN-END:variables
}
