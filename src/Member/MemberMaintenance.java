package Member;
import Adt.CircularArrayQueue;
import Adt.QueueInterface;
import Entity.Member;
import Main.mainMenu;
import java.awt.CardLayout;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Khoo Shi Zhao
 */

public class MemberMaintenance extends javax.swing.JFrame {
    private QueueInterface<Member> members; // queue adt
    private static final int DEFAULT_TABLE_ROWS = 5; // the table rows fixed 5
    private int jPanelNo = 0; // add = 0,remove = 1,edit = 2,remove = 3
    private int currentPageNo = 1; // table page no, default is 1
    private int jTableRowIndex = 0; // table row index, default is 0, will increase when the click next page
    private Member member; // when set and get the member
    private boolean changedTableValue;//if used search the value will change to true
    
    //reset label,table,text,and radio variable name
    private JLabel[] jtfDisplayPageNo;// add = 0,remove = 1,edit = 2,remove = 3
    private JTable[] jTable;// add = 0,remove = 1,edit = 2,view = 3
    private JTextField[] jText;//remove = 0-3,edit = 4-7,view = 8-11
    private JRadioButton[] jRadio;//remove = 0-1,edit = 2-3,view = 4-5
    
    CardLayout cardLayout; // switch jplane
    
    public MemberMaintenance() {
        initComponents();
        setLocationRelativeTo(null);
        initValues();
        initializeDisplay();
        
        setTableValue();
        
        cardLayout = (CardLayout)(oriPanel.getLayout()); 
    }
    
    private void initValues(){
        changedTableValue = false;
        members = Main.mainMenu.members;
        member = new Member();
        
        jtfDisplayPageNo = new JLabel[4];
        jTable = new JTable[4];
        jText = new JTextField[12];
        jRadio = new JRadioButton[6];
        
        String memberId = String.format("%c%03d",'M',member.getNextMemberId());
        AddMemberIdjTextField.setText(memberId);
    }
    
    private void initializeDisplay(){
        //Label Page No
        jtfDisplayPageNo[0] = AddPagejLabel;
        jtfDisplayPageNo[1] = RemovePagejLabel;
        jtfDisplayPageNo[2] = EditPagejLabel;
        jtfDisplayPageNo[3] = ViewPagejLabel;  
        //Table
        jTable[0] = AddjTable;
        jTable[1] = RemovejTable;
        jTable[2] = EditjTable;
        jTable[3] = ViewjTable;
        //TextField
        jText[0] = RemoveMemberIdjTextField;
        jText[1] = RemoveNamejTextField;
        jText[2] = RemoveIcjTextField;
        jText[3] = RemovePhonejTextField;
        jText[4] = EditMemberIdjTextField;
        jText[5] = EditNamejTextField;
        jText[6] = EditIcjTextField;
        jText[7] = EditPhonejTextField;
        jText[8] = ViewMemberIdjTextField;
        jText[9] = ViewNamejTextField;
        jText[10] = ViewIcjTextField;
        jText[11] = ViewPhonejTextField;
        //JRadioButton
        jRadio[0] = RemoveMalejRadioButton;
        jRadio[1] = RemoveFemalejRadioButton;
        jRadio[2] = EditMalejRadioButton;
        jRadio[3] = EditFemalejRadioButton;
        jRadio[4] = ViewMalejRadioButton;
        jRadio[5] = ViewFemalejRadioButton;
    }
    
    private void setTableValue(){
        int totalMember = members.getNumberOfEntries();
        int totalPageNo = (int) Math.ceil(((double)totalMember/DEFAULT_TABLE_ROWS));
        if(currentPageNo > totalPageNo && totalMember != 0){
            currentPageNo--;
            jTableRowIndex-=5;
        }
        DefaultTableModel model = (DefaultTableModel) jTable[jPanelNo].getModel();
        Iterator<Member> memberIterator = members.getQueueIterator(jTableRowIndex);
        
        for(int row = 0; row < 5; row++){
            Object[] jTableData = new Object[5];
            jTableData[0] = "-";
            jTableData[1] = "-";
            jTableData[2] = "-";
            jTableData[3] = "-";
            jTableData[4] = "-";

            if(memberIterator.hasNext()){
                member = memberIterator.next();
                jTableData[0] = member.getMemberId();
                jTableData[1] = member.getName();
                jTableData[2] = member.getGender();
                jTableData[3] = member.getIcNo();
                jTableData[4] = member.getPhoneNo();
            }
            for(int columnIndex = 0; columnIndex < 5; columnIndex++)
                model.setValueAt(jTableData[columnIndex], row, columnIndex);
        }
        setTablePageValue();
    }
    
    private void setTablePageValue(){
        int totalMember = members.getNumberOfEntries();
        int totalPageNo = (int) Math.ceil(((double)totalMember/DEFAULT_TABLE_ROWS));
        if(totalMember == 0)
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
            ButtonGroup addButtonGroup = new ButtonGroup();
            addButtonGroup.add(AddFemalejRadioButton);
            addButtonGroup.add(AddMalejRadioButton);
            addButtonGroup.clearSelection();
            AddFemalejRadioButton.setSelected(false);
            AddMalejRadioButton.setSelected(false);
            AddIcjTextField.setText("");
            AddNamejTextField.setText("");
            AddPhonejTextField.setText("");
        }
        else{
            for(int index = 0; index < 4; index++)
                jText[index + ((jPanelNo - 1) * 4)].setText("");
            ButtonGroup buttonGroup = new ButtonGroup();
            for(int index = 0; index < 2;index++){
                buttonGroup.add(jRadio[index + ((jPanelNo - 1) * 2)]);
                jRadio[index + ((jPanelNo - 1) * 2)].setSelected(false);    
            }
            buttonGroup.clearSelection();
        }
        changedTableValue = false;
        members = mainMenu.members;
        setTableValue();
    }
    
    private void nextPage(){
        int totalMember = members.getNumberOfEntries();
        int totalPageNo = (int) Math.ceil(((double)totalMember/DEFAULT_TABLE_ROWS));
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
    
    private void removeMember(int buttonPosition){
        int removePosition = jTableRowIndex + buttonPosition;
        boolean successful;
        if(changedTableValue){
            QueueInterface<Member> membersB = mainMenu.members;
            member = members.getEntry(removePosition);
            int memberPosition = membersB.search(member);
            successful = membersB.remove(memberPosition);
            clearValue();
        }
        else{
            successful = members.remove(removePosition);
        }
        if(successful){
            JOptionPane.showMessageDialog(this, "Remove Successfully");
            setTableValue();
        }  
        else
            JOptionPane.showMessageDialog(this, "Unable to remove ! Please select the row that has value to perform remove!");
    }
    
    private void editMember(int buttonPosition){
        int editPosition = buttonPosition + jTableRowIndex;
        boolean successful;
        int memberPosition;
        if(changedTableValue){
            QueueInterface<Member> membersB = mainMenu.members;
            member = members.getEntry(editPosition);
            memberPosition = membersB.search(member);
            successful = memberPosition != -1;
        }
        else{
            member = members.getEntry(editPosition);
            memberPosition = editPosition;
            successful = member != null;
        }
        if(successful){
            setVisible(false);
            new EditMemberGui(memberPosition).setVisible(true);
        }  
        else
            JOptionPane.showMessageDialog(this, "Unable to edit ! Please select the row that has value to perform edit!");
    }
    
    private void viewMember(int buttonPosition){
        int viewPosition = jTableRowIndex + buttonPosition;
        boolean successful;
        int memberPosition;
        if(changedTableValue){
            QueueInterface<Member> membersB = mainMenu.members;
            member = members.getEntry(viewPosition);
            memberPosition = membersB.search(member);
            successful = memberPosition != -1;
        }
        else{
            member = members.getEntry(viewPosition);
            memberPosition = viewPosition;
            successful = member != null;
        }
        if(successful){        
            setVisible(false);
            new ViewMemberGui(memberPosition).setVisible(true);
        }  
        else
            JOptionPane.showMessageDialog(this, "Unable to view ! Please select the row that has value to perform view!");
    }
    
    private void searchMember(){
        members = mainMenu.members;
        QueueInterface<Member> searchMembers = new CircularArrayQueue();
        Iterator<Member> searchMembersIterator;
        boolean selectedGender = (AddMalejRadioButton.isSelected() || AddFemalejRadioButton.isSelected());
        String memberId = jText[(jPanelNo - 1) * 4].getText();
        String name = jText[((jPanelNo - 1) * 4) + 1].getText();
        String icNo = jText[((jPanelNo - 1) * 4) + 2].getText();
        String phoneNo = jText[((jPanelNo - 1) * 4) + 3].getText();
        String gender = (jRadio[((jPanelNo - 1) * 2)].isSelected() ? "Male" : null);
        if(gender == null)
            gender = (jRadio[((jPanelNo - 1) * 2) + 1].isSelected() ? "Female" : null);
        boolean enqueued = false;
        int enqueueTimes = 0;
        boolean end = false;
        if(!memberId.isEmpty()){
            searchMembersIterator = members.getQueueIterator();
            while(searchMembersIterator.hasNext()){
                member = searchMembersIterator.next();
                if(memberId.equals(member.getMemberId())){
                    searchMembers.enqueue(member);
                    enqueueTimes++;
                    enqueued = true;
                }
                else{
                    boolean successful;
                    successful = searchTools(memberId.toUpperCase(), member.getMemberId());
                    if(successful){
                        searchMembers.enqueue(member);
                        enqueued = true;
                        enqueueTimes++;
                    }
                }
            }
            if(enqueueTimes == 0){
                end = true;
            }
        }
        if(!name.isEmpty()&&!end){
            enqueueTimes = 0;
            if(enqueued)
                searchMembersIterator = searchMembers.getQueueIterator();
            else
                searchMembersIterator = members.getQueueIterator();
            while(searchMembersIterator.hasNext()){
                boolean checkBoth = false;
                member = searchMembersIterator.next();
                if(name.equals(member.getName())){
                    checkBoth = true;
                    enqueueTimes++;
                }
                else{
                    boolean successful;
                    successful = searchTools(name.toUpperCase(), member.getName().toUpperCase());
                    if(successful){
                        checkBoth = true;
                        enqueueTimes++;
                    }
                }
                if(checkBoth&&!enqueued)
                    searchMembers.enqueue(member);
                else if(!checkBoth&&enqueued)
                    searchMembersIterator.remove();
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&searchMembers.getNumberOfEntries()==0)
            end = true;
        if(!icNo.isEmpty()&&!end){
            enqueueTimes = 0;
            if(enqueued)
                searchMembersIterator = searchMembers.getQueueIterator();
            else
                searchMembersIterator = members.getQueueIterator();
            while(searchMembersIterator.hasNext()){
                member = searchMembersIterator.next();
                boolean checkBoth = false;
                if(icNo.equals(member.getIcNo())){
                    enqueueTimes++;
                    checkBoth = true;
                }
                else{
                    boolean successful;
                    successful = searchTools(icNo, member.getIcNo());
                    if(successful){
                        checkBoth = true;
                        enqueueTimes++;
                    }
                }
                if(checkBoth&&!enqueued)
                    searchMembers.enqueue(member);
                else if(!checkBoth&&enqueued)
                    searchMembersIterator.remove();
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&searchMembers.getNumberOfEntries()==0)
            end = true;
        if(!phoneNo.isEmpty()&&!end){
            enqueueTimes = 0;
            if(enqueued)
                searchMembersIterator = searchMembers.getQueueIterator();
            else
                searchMembersIterator = members.getQueueIterator();
            while(searchMembersIterator.hasNext()){
                member = searchMembersIterator.next();
                boolean checkBoth = false;
                if(phoneNo.equals(member.getPhoneNo())){
                    checkBoth = true;
                    enqueueTimes++;
                }
                else{
                    boolean successful;
                    successful = searchTools(phoneNo, member.getPhoneNo());
                    if(successful){
                        checkBoth = true;
                        enqueueTimes++;
                    }
                }
                if(checkBoth&&!enqueued)
                    searchMembers.enqueue(member);
                else if(!checkBoth&&enqueued)
                    searchMembersIterator.remove();
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&searchMembers.getNumberOfEntries()==0)
            end = true;
        if(!(gender == null)&&!end){
            enqueueTimes = 0;
            if(enqueued)
                searchMembersIterator = searchMembers.getQueueIterator();
            else
                searchMembersIterator = members.getQueueIterator();
            while(searchMembersIterator.hasNext()){
                member = searchMembersIterator.next();
                boolean checkBoth = false;
                if(gender.equals(member.getGender())){
                    checkBoth = true;
                    enqueueTimes++;
                }
                if(checkBoth&&!enqueued)
                    searchMembers.enqueue(member);
                else if(!checkBoth&&enqueued)
                    searchMembersIterator.remove();
            }
            if(enqueueTimes == 0)
                end = true;
            else
                enqueued = true;
        }
        if(enqueued&&!end){
            members = searchMembers;
            changedTableValue = true;
        }
        else{
            JOptionPane.showMessageDialog(this, "Member Not Found!Text field has been Reset!");
            clearValue();
            members = mainMenu.members;
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

        addGender = new javax.swing.ButtonGroup();
        removeGender = new javax.swing.ButtonGroup();
        editGender = new javax.swing.ButtonGroup();
        viewGender = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel3 = new javax.swing.JPanel();
        AddMenujButton = new javax.swing.JButton();
        RemoveMenujButton = new javax.swing.JButton();
        EditMenujButton = new javax.swing.JButton();
        RetrieveMenujButton = new javax.swing.JButton();
        BackjButton = new javax.swing.JButton();
        oriPanel = new javax.swing.JPanel();
        addMember = new javax.swing.JPanel();
        addMemberPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        AddMemberIdjTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        AddIcjTextField = new javax.swing.JTextField();
        AddPhonejTextField = new javax.swing.JTextField();
        AddNamejTextField = new javax.swing.JTextField();
        AddMalejRadioButton = new javax.swing.JRadioButton();
        AddFemalejRadioButton = new javax.swing.JRadioButton();
        AddClearButton = new javax.swing.JButton();
        addMemberButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        AddjTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        AddPreviousPageButton = new javax.swing.JButton();
        AddNextPageButton = new javax.swing.JButton();
        AddPagejLabel = new javax.swing.JLabel();
        removeMember = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        RemoveSearchButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        RemoveButton1 = new javax.swing.JButton();
        RemoveButton2 = new javax.swing.JButton();
        RemoveButton3 = new javax.swing.JButton();
        RemoveButton4 = new javax.swing.JButton();
        RemoveButton5 = new javax.swing.JButton();
        removeMemberPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        RemoveMemberIdjTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        RemoveIcjTextField = new javax.swing.JTextField();
        RemovePhonejTextField = new javax.swing.JTextField();
        RemoveNamejTextField = new javax.swing.JTextField();
        RemoveMalejRadioButton = new javax.swing.JRadioButton();
        RemoveFemalejRadioButton = new javax.swing.JRadioButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        RemovejTable = new javax.swing.JTable();
        RemovePreviousButton = new javax.swing.JButton();
        RemoveNextButton = new javax.swing.JButton();
        RemovePagejLabel = new javax.swing.JLabel();
        RemoveClearButton = new javax.swing.JButton();
        editMemberDetails = new javax.swing.JPanel();
        EditSearchButton = new javax.swing.JButton();
        EditClearButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        EditjTable = new javax.swing.JTable();
        jLabel29 = new javax.swing.JLabel();
        EditPreviousButton = new javax.swing.JButton();
        EditPagejLabel = new javax.swing.JLabel();
        EditNextButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        EditButton1 = new javax.swing.JButton();
        EditButton2 = new javax.swing.JButton();
        EditButton3 = new javax.swing.JButton();
        EditButton4 = new javax.swing.JButton();
        EditButton5 = new javax.swing.JButton();
        removeMemberPanel1 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        EditMemberIdjTextField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        EditIcjTextField = new javax.swing.JTextField();
        EditPhonejTextField = new javax.swing.JTextField();
        EditNamejTextField = new javax.swing.JTextField();
        EditMalejRadioButton = new javax.swing.JRadioButton();
        EditFemalejRadioButton = new javax.swing.JRadioButton();
        retrieveMemberDetails = new javax.swing.JPanel();
        ViewSearchButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        ViewButton1 = new javax.swing.JButton();
        ViewButton2 = new javax.swing.JButton();
        ViewButton3 = new javax.swing.JButton();
        ViewButton4 = new javax.swing.JButton();
        ViewButton5 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        ViewjTable = new javax.swing.JTable();
        jLabel39 = new javax.swing.JLabel();
        ViewPreviousButton = new javax.swing.JButton();
        ViewPagejLabel = new javax.swing.JLabel();
        ViewNextButton = new javax.swing.JButton();
        removeMemberPanel2 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        ViewMemberIdjTextField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        ViewIcjTextField = new javax.swing.JTextField();
        ViewPhonejTextField = new javax.swing.JTextField();
        ViewNamejTextField = new javax.swing.JTextField();
        ViewMalejRadioButton = new javax.swing.JRadioButton();
        ViewFemalejRadioButton = new javax.swing.JRadioButton();
        ViewClearButton = new javax.swing.JButton();
        generateMemberReport = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Member Maintenance");
        setName("itemGroupMaintenance"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jSplitPane1.setDividerSize(1);

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setForeground(new java.awt.Color(255, 51, 204));

        AddMenujButton.setBackground(new java.awt.Color(0, 0, 0));
        AddMenujButton.setForeground(new java.awt.Color(255, 255, 255));
        AddMenujButton.setText("Add Member");
        AddMenujButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        AddMenujButton.setName("itemAddButton"); // NOI18N
        AddMenujButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddMenujButtonActionPerformed(evt);
            }
        });

        RemoveMenujButton.setBackground(new java.awt.Color(0, 0, 0));
        RemoveMenujButton.setForeground(new java.awt.Color(255, 255, 255));
        RemoveMenujButton.setText("Remove Member");
        RemoveMenujButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RemoveMenujButton.setMaximumSize(new java.awt.Dimension(123, 29));
        RemoveMenujButton.setMinimumSize(new java.awt.Dimension(123, 29));
        RemoveMenujButton.setName("itemRemoveButton"); // NOI18N
        RemoveMenujButton.setPreferredSize(new java.awt.Dimension(123, 29));
        RemoveMenujButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveMenujButtonActionPerformed(evt);
            }
        });

        EditMenujButton.setBackground(new java.awt.Color(0, 0, 0));
        EditMenujButton.setForeground(new java.awt.Color(255, 255, 255));
        EditMenujButton.setText("Edit Member Details");
        EditMenujButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        EditMenujButton.setName("groupEditButton"); // NOI18N
        EditMenujButton.setPreferredSize(new java.awt.Dimension(103, 29));
        EditMenujButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditMenujButtonActionPerformed(evt);
            }
        });

        RetrieveMenujButton.setBackground(new java.awt.Color(0, 0, 0));
        RetrieveMenujButton.setForeground(new java.awt.Color(255, 255, 255));
        RetrieveMenujButton.setText("Retrieve Member Details");
        RetrieveMenujButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        RetrieveMenujButton.setName("itemRetrieveButton"); // NOI18N
        RetrieveMenujButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RetrieveMenujButtonActionPerformed(evt);
            }
        });

        BackjButton.setBackground(new java.awt.Color(0, 0, 0));
        BackjButton.setForeground(new java.awt.Color(255, 255, 255));
        BackjButton.setText("Back");
        BackjButton.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BackjButton.setName("itemRetrieveButton"); // NOI18N
        BackjButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackjButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(RetrieveMenujButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EditMenujButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RemoveMenujButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AddMenujButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(BackjButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(AddMenujButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RemoveMenujButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EditMenujButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RetrieveMenujButton, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 260, Short.MAX_VALUE)
                .addComponent(BackjButton, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(110, 110, 110))
        );

        jSplitPane1.setLeftComponent(jPanel3);

        oriPanel.setLayout(new java.awt.CardLayout());

        addMember.setBackground(new java.awt.Color(255, 204, 204));

        addMemberPanel.setBackground(new java.awt.Color(255, 255, 255));
        addMemberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "ADD MEMBER ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel6.setText("Member ID: ");

        AddMemberIdjTextField.setEditable(false);
        AddMemberIdjTextField.setText("M001");
        AddMemberIdjTextField.setEnabled(false);
        AddMemberIdjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddMemberIdjTextFieldActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel5.setText("Name: ");

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel8.setText("Gender: ");

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel9.setText("No IC: ");

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel10.setText("Phone No: ");

        AddNamejTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddNamejTextFieldActionPerformed(evt);
            }
        });

        AddMalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        addGender.add(AddMalejRadioButton);
        AddMalejRadioButton.setText("Male");
        AddMalejRadioButton.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AddMalejRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddMalejRadioButtonActionPerformed(evt);
            }
        });

        AddFemalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        addGender.add(AddFemalejRadioButton);
        AddFemalejRadioButton.setText("Female");
        AddFemalejRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddFemalejRadioButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addMemberPanelLayout = new javax.swing.GroupLayout(addMemberPanel);
        addMemberPanel.setLayout(addMemberPanelLayout);
        addMemberPanelLayout.setHorizontalGroup(
            addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addMemberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addMemberPanelLayout.createSequentialGroup()
                        .addGroup(addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AddMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AddNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(addMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AddIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AddPhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(AddMalejRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(AddFemalejRadioButton)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        addMemberPanelLayout.setVerticalGroup(
            addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addMemberPanelLayout.createSequentialGroup()
                .addGroup(addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(AddMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(AddMalejRadioButton)
                    .addComponent(AddFemalejRadioButton))
                .addGap(13, 13, 13)
                .addGroup(addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(AddIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(addMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(AddPhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        AddClearButton.setText("Clear");
        AddClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddClearButtonActionPerformed(evt);
            }
        });

        addMemberButton.setText("Add");
        addMemberButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addMemberButtonActionPerformed(evt);
            }
        });

        AddjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MemberId", "Name", "Gender", "IC No.", "Phone No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        AddjTable.setRowHeight(30);
        jScrollPane1.setViewportView(AddjTable);

        jLabel1.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel1.setText("Member List");

        AddPreviousPageButton.setText("<");
        AddPreviousPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddPreviousPageButtonActionPerformed(evt);
            }
        });

        AddNextPageButton.setText(">");
        AddNextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddNextPageButtonActionPerformed(evt);
            }
        });

        AddPagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        AddPagejLabel.setText("Page 1 of 1");

        javax.swing.GroupLayout addMemberLayout = new javax.swing.GroupLayout(addMember);
        addMember.setLayout(addMemberLayout);
        addMemberLayout.setHorizontalGroup(
            addMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addMemberLayout.createSequentialGroup()
                .addGroup(addMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addMemberLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addMemberLayout.createSequentialGroup()
                        .addGap(187, 187, 187)
                        .addComponent(AddPreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(AddPagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(AddNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(455, Short.MAX_VALUE))
            .addGroup(addMemberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addMemberLayout.createSequentialGroup()
                        .addComponent(addMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(addMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(AddClearButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                            .addComponent(addMemberButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        addMemberLayout.setVerticalGroup(
            addMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addMemberLayout.createSequentialGroup()
                .addGroup(addMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addMemberLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(addMemberButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(AddClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addMemberLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(addMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(addMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AddPreviousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(addMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(AddNextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AddPagejLabel)))
                .addContainerGap(121, Short.MAX_VALUE))
        );

        oriPanel.add(addMember, "addMember");

        removeMember.setBackground(new java.awt.Color(255, 204, 204));

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel3.setText("Member List");

        RemoveSearchButton.setText("Search");
        RemoveSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveSearchButtonActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        RemoveButton1.setBackground(new java.awt.Color(255, 255, 255));
        RemoveButton1.setText("Delete");
        RemoveButton1.setActionCommand("delete1");
        RemoveButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveButton1ActionPerformed(evt);
            }
        });

        RemoveButton2.setBackground(new java.awt.Color(255, 255, 255));
        RemoveButton2.setText("Delete");
        RemoveButton2.setActionCommand("delete2");
        RemoveButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveButton2ActionPerformed(evt);
            }
        });

        RemoveButton3.setBackground(new java.awt.Color(255, 255, 255));
        RemoveButton3.setText("Delete");
        RemoveButton3.setActionCommand("delete3");
        RemoveButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveButton3ActionPerformed(evt);
            }
        });

        RemoveButton4.setBackground(new java.awt.Color(255, 255, 255));
        RemoveButton4.setText("Delete");
        RemoveButton4.setActionCommand("delete4");
        RemoveButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveButton4ActionPerformed(evt);
            }
        });

        RemoveButton5.setBackground(new java.awt.Color(255, 255, 255));
        RemoveButton5.setText("Delete");
        RemoveButton5.setActionCommand("delete5");
        RemoveButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RemoveButton3)
                            .addComponent(RemoveButton4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RemoveButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(RemoveButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(RemoveButton5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(RemoveButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(RemoveButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RemoveButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(RemoveButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(RemoveButton5)
                .addGap(14, 14, 14))
        );

        removeMemberPanel.setBackground(new java.awt.Color(255, 255, 255));
        removeMemberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "REMOVE MEMBER ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel12.setText("Member ID: ");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel13.setText("Name: ");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel14.setText("Gender: ");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel15.setText("No IC: ");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel16.setText("Phone No: ");

        RemoveIcjTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveIcjTextFieldActionPerformed(evt);
            }
        });

        RemoveMalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        removeGender.add(RemoveMalejRadioButton);
        RemoveMalejRadioButton.setSelected(true);
        RemoveMalejRadioButton.setText("Male");

        RemoveFemalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        removeGender.add(RemoveFemalejRadioButton);
        RemoveFemalejRadioButton.setText("Female");

        javax.swing.GroupLayout removeMemberPanelLayout = new javax.swing.GroupLayout(removeMemberPanel);
        removeMemberPanel.setLayout(removeMemberPanelLayout);
        removeMemberPanelLayout.setHorizontalGroup(
            removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeMemberPanelLayout.createSequentialGroup()
                        .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RemoveMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RemoveNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(removeMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RemoveIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RemovePhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RemoveMalejRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(RemoveFemalejRadioButton)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        removeMemberPanelLayout.setVerticalGroup(
            removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberPanelLayout.createSequentialGroup()
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(RemoveMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(RemoveNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(RemoveMalejRadioButton)
                    .addComponent(RemoveFemalejRadioButton))
                .addGap(13, 13, 13)
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(RemoveIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(RemovePhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        RemovejTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Member ID", "Name", "Gender", "IC No.", "Phone No"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        RemovejTable.setRowHeight(30);
        jScrollPane2.setViewportView(RemovejTable);

        RemovePreviousButton.setText("<");
        RemovePreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemovePreviousButtonActionPerformed(evt);
            }
        });

        RemoveNextButton.setText(">");
        RemoveNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveNextButtonActionPerformed(evt);
            }
        });

        RemovePagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        RemovePagejLabel.setText("Page 1 of 1");

        RemoveClearButton.setText("Clear");
        RemoveClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveClearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout removeMemberLayout = new javax.swing.GroupLayout(removeMember);
        removeMember.setLayout(removeMemberLayout);
        removeMemberLayout.setHorizontalGroup(
            removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeMemberLayout.createSequentialGroup()
                        .addComponent(removeMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(RemoveSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(RemoveClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(removeMemberLayout.createSequentialGroup()
                        .addGroup(removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(removeMemberLayout.createSequentialGroup()
                                .addGap(177, 177, 177)
                                .addComponent(RemovePreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(RemovePagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(RemoveNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 112, Short.MAX_VALUE))
        );
        removeMemberLayout.setVerticalGroup(
            removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberLayout.createSequentialGroup()
                .addGroup(removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeMemberLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(removeMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(RemoveSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(RemoveClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(RemovePreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(removeMemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(RemoveNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(RemovePagejLabel)))
                .addContainerGap(121, Short.MAX_VALUE))
        );

        oriPanel.add(removeMember, "removeMember");

        editMemberDetails.setBackground(new java.awt.Color(255, 204, 204));

        EditSearchButton.setText("Search");
        EditSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditSearchButtonActionPerformed(evt);
            }
        });

        EditClearButton.setText("Clear");
        EditClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditClearButtonActionPerformed(evt);
            }
        });

        EditjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Member ID", "Name", "Gender", "IC No.", "Phone No."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EditjTable.setRowHeight(30);
        jScrollPane4.setViewportView(EditjTable);

        jLabel29.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel29.setText("Member List");

        EditPreviousButton.setText("<");
        EditPreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditPreviousButtonActionPerformed(evt);
            }
        });

        EditPagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        EditPagejLabel.setText("Page 1 of 1");

        EditNextButton.setText(">");
        EditNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditNextButtonActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(255, 204, 204));

        EditButton1.setBackground(new java.awt.Color(255, 255, 255));
        EditButton1.setText("Edit");
        EditButton1.setActionCommand("edit1");
        EditButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButton1ActionPerformed(evt);
            }
        });

        EditButton2.setBackground(new java.awt.Color(255, 255, 255));
        EditButton2.setText("Edit");
        EditButton2.setActionCommand("edit2");
        EditButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButton2ActionPerformed(evt);
            }
        });

        EditButton3.setBackground(new java.awt.Color(255, 255, 255));
        EditButton3.setText("Edit");
        EditButton3.setActionCommand("edit3");
        EditButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButton3ActionPerformed(evt);
            }
        });

        EditButton4.setBackground(new java.awt.Color(255, 255, 255));
        EditButton4.setText("Edit");
        EditButton4.setActionCommand("edit4");
        EditButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButton4ActionPerformed(evt);
            }
        });

        EditButton5.setBackground(new java.awt.Color(255, 255, 255));
        EditButton5.setText("Edit");
        EditButton5.setActionCommand("edit5");
        EditButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditButton5ActionPerformed(evt);
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
                            .addComponent(EditButton3)
                            .addComponent(EditButton4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(EditButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(EditButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(EditButton5, javax.swing.GroupLayout.Alignment.TRAILING)))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(EditButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(EditButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(EditButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(EditButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(EditButton5)
                .addGap(14, 14, 14))
        );

        removeMemberPanel1.setBackground(new java.awt.Color(255, 255, 255));
        removeMemberPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "EDIT MEMBER ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel17.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel17.setText("Member ID: ");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel18.setText("Name: ");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel19.setText("Gender: ");

        jLabel41.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel41.setText("No IC: ");

        jLabel42.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel42.setText("Phone No: ");

        EditMalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        editGender.add(EditMalejRadioButton);
        EditMalejRadioButton.setSelected(true);
        EditMalejRadioButton.setText("Male");

        EditFemalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        editGender.add(EditFemalejRadioButton);
        EditFemalejRadioButton.setText("Female");

        javax.swing.GroupLayout removeMemberPanel1Layout = new javax.swing.GroupLayout(removeMemberPanel1);
        removeMemberPanel1.setLayout(removeMemberPanel1Layout);
        removeMemberPanel1Layout.setHorizontalGroup(
            removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeMemberPanel1Layout.createSequentialGroup()
                        .addGroup(removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(EditMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EditNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(removeMemberPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditPhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(EditMalejRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(EditFemalejRadioButton)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        removeMemberPanel1Layout.setVerticalGroup(
            removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberPanel1Layout.createSequentialGroup()
                .addGroup(removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EditNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(EditMalejRadioButton)
                    .addComponent(EditFemalejRadioButton))
                .addGap(13, 13, 13)
                .addGroup(removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(EditIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(removeMemberPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(EditPhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout editMemberDetailsLayout = new javax.swing.GroupLayout(editMemberDetails);
        editMemberDetails.setLayout(editMemberDetailsLayout);
        editMemberDetailsLayout.setHorizontalGroup(
            editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editMemberDetailsLayout.createSequentialGroup()
                .addGroup(editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editMemberDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(editMemberDetailsLayout.createSequentialGroup()
                                .addComponent(removeMemberPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addGroup(editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(EditClearButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 96, Short.MAX_VALUE)
                                    .addComponent(EditSearchButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(editMemberDetailsLayout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(editMemberDetailsLayout.createSequentialGroup()
                        .addGap(177, 177, 177)
                        .addComponent(EditPreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47)
                        .addComponent(EditPagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(EditNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 128, Short.MAX_VALUE))
        );
        editMemberDetailsLayout.setVerticalGroup(
            editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editMemberDetailsLayout.createSequentialGroup()
                .addGroup(editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editMemberDetailsLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(EditSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(EditClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(editMemberDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(removeMemberPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(EditPreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(EditNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(EditPagejLabel)))
                .addContainerGap(121, Short.MAX_VALUE))
        );

        oriPanel.add(editMemberDetails, "editMemberDetails");

        retrieveMemberDetails.setBackground(new java.awt.Color(255, 204, 204));

        ViewSearchButton.setText("Search");
        ViewSearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewSearchButtonActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 204, 204));

        ViewButton1.setBackground(new java.awt.Color(255, 255, 255));
        ViewButton1.setText("View");
        ViewButton1.setActionCommand("view1");
        ViewButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewButton1ActionPerformed(evt);
            }
        });

        ViewButton2.setBackground(new java.awt.Color(255, 255, 255));
        ViewButton2.setText("View");
        ViewButton2.setActionCommand("view2");
        ViewButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewButton2ActionPerformed(evt);
            }
        });

        ViewButton3.setBackground(new java.awt.Color(255, 255, 255));
        ViewButton3.setText("View");
        ViewButton3.setActionCommand("view3");
        ViewButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewButton3ActionPerformed(evt);
            }
        });

        ViewButton4.setBackground(new java.awt.Color(255, 255, 255));
        ViewButton4.setText("View");
        ViewButton4.setActionCommand("view4");
        ViewButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewButton4ActionPerformed(evt);
            }
        });

        ViewButton5.setBackground(new java.awt.Color(255, 255, 255));
        ViewButton5.setText("View");
        ViewButton5.setActionCommand("view5");
        ViewButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewButton5ActionPerformed(evt);
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
                            .addComponent(ViewButton3)
                            .addComponent(ViewButton4))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ViewButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ViewButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ViewButton5, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(6, 6, 6))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ViewButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ViewButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ViewButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ViewButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ViewButton5)
                .addGap(14, 14, 14))
        );

        ViewjTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Member ID", "Name", "Gender", "IC No.", "Phone No."
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ViewjTable.setRowHeight(30);
        jScrollPane7.setViewportView(ViewjTable);

        jLabel39.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel39.setText("Member List");

        ViewPreviousButton.setText("<");
        ViewPreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewPreviousButtonActionPerformed(evt);
            }
        });

        ViewPagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        ViewPagejLabel.setText("Page 1 of 1");

        ViewNextButton.setText(">");
        ViewNextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewNextButtonActionPerformed(evt);
            }
        });

        removeMemberPanel2.setBackground(new java.awt.Color(255, 255, 255));
        removeMemberPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "VIEW MEMBER ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel21.setText("Member ID: ");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel22.setText("Name: ");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel23.setText("Gender: ");

        jLabel43.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel43.setText("No IC: ");

        jLabel44.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel44.setText("Phone No: ");

        ViewMalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        viewGender.add(ViewMalejRadioButton);
        ViewMalejRadioButton.setSelected(true);
        ViewMalejRadioButton.setText("Male");

        ViewFemalejRadioButton.setBackground(new java.awt.Color(255, 255, 255));
        viewGender.add(ViewFemalejRadioButton);
        ViewFemalejRadioButton.setText("Female");

        javax.swing.GroupLayout removeMemberPanel2Layout = new javax.swing.GroupLayout(removeMemberPanel2);
        removeMemberPanel2.setLayout(removeMemberPanel2Layout);
        removeMemberPanel2Layout.setHorizontalGroup(
            removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeMemberPanel2Layout.createSequentialGroup()
                        .addGroup(removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ViewMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ViewNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(removeMemberPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ViewIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ViewPhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ViewMalejRadioButton)
                        .addGap(18, 18, 18)
                        .addComponent(ViewFemalejRadioButton)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        removeMemberPanel2Layout.setVerticalGroup(
            removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberPanel2Layout.createSequentialGroup()
                .addGroup(removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ViewMemberIdjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ViewNamejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(ViewMalejRadioButton)
                    .addComponent(ViewFemalejRadioButton))
                .addGap(13, 13, 13)
                .addGroup(removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(ViewIcjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(removeMemberPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(ViewPhonejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        ViewClearButton.setText("Clear");
        ViewClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewClearButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout retrieveMemberDetailsLayout = new javax.swing.GroupLayout(retrieveMemberDetails);
        retrieveMemberDetails.setLayout(retrieveMemberDetailsLayout);
        retrieveMemberDetailsLayout.setHorizontalGroup(
            retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveMemberDetailsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retrieveMemberDetailsLayout.createSequentialGroup()
                        .addComponent(removeMemberPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ViewSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ViewClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(retrieveMemberDetailsLayout.createSequentialGroup()
                        .addGroup(retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(retrieveMemberDetailsLayout.createSequentialGroup()
                                .addGap(182, 182, 182)
                                .addComponent(ViewPreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(47, 47, 47)
                                .addComponent(ViewPagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(ViewNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 116, Short.MAX_VALUE))
        );
        retrieveMemberDetailsLayout.setVerticalGroup(
            retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retrieveMemberDetailsLayout.createSequentialGroup()
                .addGroup(retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(retrieveMemberDetailsLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(ViewSearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(ViewClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(retrieveMemberDetailsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(removeMemberPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ViewPreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(retrieveMemberDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ViewNextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(ViewPagejLabel)))
                .addGap(0, 119, Short.MAX_VALUE))
        );

        oriPanel.add(retrieveMemberDetails, "retrieveMemberDetails");

        generateMemberReport.setBackground(new java.awt.Color(255, 204, 204));

        javax.swing.GroupLayout generateMemberReportLayout = new javax.swing.GroupLayout(generateMemberReport);
        generateMemberReport.setLayout(generateMemberReportLayout);
        generateMemberReportLayout.setHorizontalGroup(
            generateMemberReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 938, Short.MAX_VALUE)
        );
        generateMemberReportLayout.setVerticalGroup(
            generateMemberReportLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        oriPanel.add(generateMemberReport, "generateMemberReport");

        jSplitPane1.setRightComponent(oriPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_AddClearButtonActionPerformed

    private void addMemberButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addMemberButtonActionPerformed
        // TODO add your handling code here:
        boolean selectedGender = (AddMalejRadioButton.isSelected() || AddFemalejRadioButton.isSelected());
        if(!AddNamejTextField.getText().isEmpty() && !AddPhonejTextField.getText().isEmpty() && !AddIcjTextField.getText().isEmpty() && selectedGender){
            String gender = (AddMalejRadioButton.isSelected() ? "Male" : "Female");
            String memberId = String.format("%c%03d",'M',member.getNextMemberId());
            member = new Member(memberId, AddNamejTextField.getText(),AddIcjTextField.getText(),AddPhonejTextField.getText(),gender);
            members.enqueue(member);
            clearValue();
            memberId = String.format("%c%03d",'M',member.getNextMemberId());
            AddMemberIdjTextField.setText(memberId);
            JOptionPane.showMessageDialog(this, "Member Added Successfully!");
        }
        else{
            JOptionPane.showMessageDialog(this, "Please fill in all data!");
        }
    }//GEN-LAST:event_addMemberButtonActionPerformed

    private void AddPreviousPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddPreviousPageButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
        
    }//GEN-LAST:event_AddPreviousPageButtonActionPerformed

    private void AddNextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddNextPageButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_AddNextPageButtonActionPerformed

    private void AddMenujButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddMenujButtonActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "addMember");
        switchjPanel(0);
    }//GEN-LAST:event_AddMenujButtonActionPerformed

    private void RemoveMenujButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveMenujButtonActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "removeMember");
        switchjPanel(1);
    }//GEN-LAST:event_RemoveMenujButtonActionPerformed

    private void EditMenujButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditMenujButtonActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "editMemberDetails");
        switchjPanel(2);
    }//GEN-LAST:event_EditMenujButtonActionPerformed

    private void RetrieveMenujButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RetrieveMenujButtonActionPerformed
        // TODO add your handling code here:
        cardLayout.show(oriPanel, "retrieveMemberDetails");
        switchjPanel(3);
    }//GEN-LAST:event_RetrieveMenujButtonActionPerformed

    private void RemoveButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveButton1ActionPerformed
        // TODO add your handling code here:
        removeMember(1);
    }//GEN-LAST:event_RemoveButton1ActionPerformed

    private void RemoveButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveButton2ActionPerformed
        // TODO add your handling code here:
        removeMember(2);
    }//GEN-LAST:event_RemoveButton2ActionPerformed

    private void RemoveButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveButton3ActionPerformed
        // TODO add your handling code here:
        removeMember(3);
    }//GEN-LAST:event_RemoveButton3ActionPerformed

    private void RemoveButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveButton4ActionPerformed
        // TODO add your handling code here:
        removeMember(4);
    }//GEN-LAST:event_RemoveButton4ActionPerformed

    private void RemoveButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveButton5ActionPerformed
        // TODO add your handling code here:
        removeMember(5);
    }//GEN-LAST:event_RemoveButton5ActionPerformed

    private void EditSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditSearchButtonActionPerformed
        // TODO add your handling code here:
        searchMember();
    }//GEN-LAST:event_EditSearchButtonActionPerformed

    private void EditPreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditPreviousButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_EditPreviousButtonActionPerformed

    private void EditNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditNextButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_EditNextButtonActionPerformed

    private void EditClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_EditClearButtonActionPerformed

    private void EditButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButton1ActionPerformed
        // TODO add your handling code here:
        editMember(1);
    }//GEN-LAST:event_EditButton1ActionPerformed

    private void EditButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButton2ActionPerformed
        // TODO add your handling code here:
        editMember(2);
    }//GEN-LAST:event_EditButton2ActionPerformed

    private void EditButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButton3ActionPerformed
        // TODO add your handling code here:
        editMember(3);
    }//GEN-LAST:event_EditButton3ActionPerformed

    private void EditButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButton4ActionPerformed
        // TODO add your handling code here:
        editMember(4);
    }//GEN-LAST:event_EditButton4ActionPerformed

    private void EditButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditButton5ActionPerformed
        // TODO add your handling code here:
        editMember(5);
    }//GEN-LAST:event_EditButton5ActionPerformed

    private void ViewButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewButton1ActionPerformed
        // TODO add your handling code here:
        viewMember(1);
    }//GEN-LAST:event_ViewButton1ActionPerformed

    private void ViewButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewButton2ActionPerformed
        // TODO add your handling code here:
        viewMember(2);
    }//GEN-LAST:event_ViewButton2ActionPerformed

    private void ViewButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewButton3ActionPerformed
        // TODO add your handling code here:
        viewMember(3);
    }//GEN-LAST:event_ViewButton3ActionPerformed

    private void ViewButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewButton4ActionPerformed
        // TODO add your handling code here:
        viewMember(4);
    }//GEN-LAST:event_ViewButton4ActionPerformed

    private void ViewButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewButton5ActionPerformed
        // TODO add your handling code here:
        viewMember(5);
    }//GEN-LAST:event_ViewButton5ActionPerformed

    private void ViewPreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewPreviousButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_ViewPreviousButtonActionPerformed

    private void ViewNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewNextButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_ViewNextButtonActionPerformed

    private void ViewSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewSearchButtonActionPerformed
        // TODO add your handling code here:
        searchMember();
    }//GEN-LAST:event_ViewSearchButtonActionPerformed

    private void RemovePreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemovePreviousButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_RemovePreviousButtonActionPerformed

    private void RemoveNextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveNextButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_RemoveNextButtonActionPerformed

    private void RemoveSearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveSearchButtonActionPerformed
        // TODO add your handling code here:
        searchMember();
    }//GEN-LAST:event_RemoveSearchButtonActionPerformed

    private void RemoveClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_RemoveClearButtonActionPerformed

    private void ViewClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_ViewClearButtonActionPerformed

    private void BackjButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackjButtonActionPerformed
        // TODO add your handling code here:
        exitForm();
    }//GEN-LAST:event_BackjButtonActionPerformed

    private void AddMemberIdjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddMemberIdjTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddMemberIdjTextFieldActionPerformed

    private void AddNamejTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddNamejTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddNamejTextFieldActionPerformed

    private void RemoveIcjTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveIcjTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_RemoveIcjTextFieldActionPerformed

    private void AddMalejRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddMalejRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddMalejRadioButtonActionPerformed

    private void AddFemalejRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddFemalejRadioButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AddFemalejRadioButtonActionPerformed

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
//            java.util.logging.Logger.getLogger(MemberMaintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(MemberMaintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(MemberMaintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(MemberMaintenance.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new MemberMaintenance().setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddClearButton;
    private javax.swing.JRadioButton AddFemalejRadioButton;
    private javax.swing.JTextField AddIcjTextField;
    private javax.swing.JRadioButton AddMalejRadioButton;
    private javax.swing.JTextField AddMemberIdjTextField;
    private javax.swing.JButton AddMenujButton;
    private javax.swing.JTextField AddNamejTextField;
    private javax.swing.JButton AddNextPageButton;
    private javax.swing.JLabel AddPagejLabel;
    private javax.swing.JTextField AddPhonejTextField;
    private javax.swing.JButton AddPreviousPageButton;
    private javax.swing.JTable AddjTable;
    private javax.swing.JButton BackjButton;
    private javax.swing.JButton EditButton1;
    private javax.swing.JButton EditButton2;
    private javax.swing.JButton EditButton3;
    private javax.swing.JButton EditButton4;
    private javax.swing.JButton EditButton5;
    private javax.swing.JButton EditClearButton;
    private javax.swing.JRadioButton EditFemalejRadioButton;
    private javax.swing.JTextField EditIcjTextField;
    private javax.swing.JRadioButton EditMalejRadioButton;
    private javax.swing.JTextField EditMemberIdjTextField;
    private javax.swing.JButton EditMenujButton;
    private javax.swing.JTextField EditNamejTextField;
    private javax.swing.JButton EditNextButton;
    private javax.swing.JLabel EditPagejLabel;
    private javax.swing.JTextField EditPhonejTextField;
    private javax.swing.JButton EditPreviousButton;
    private javax.swing.JButton EditSearchButton;
    private javax.swing.JTable EditjTable;
    private javax.swing.JButton RemoveButton1;
    private javax.swing.JButton RemoveButton2;
    private javax.swing.JButton RemoveButton3;
    private javax.swing.JButton RemoveButton4;
    private javax.swing.JButton RemoveButton5;
    private javax.swing.JButton RemoveClearButton;
    private javax.swing.JRadioButton RemoveFemalejRadioButton;
    private javax.swing.JTextField RemoveIcjTextField;
    private javax.swing.JRadioButton RemoveMalejRadioButton;
    private javax.swing.JTextField RemoveMemberIdjTextField;
    private javax.swing.JButton RemoveMenujButton;
    private javax.swing.JTextField RemoveNamejTextField;
    private javax.swing.JButton RemoveNextButton;
    private javax.swing.JLabel RemovePagejLabel;
    private javax.swing.JTextField RemovePhonejTextField;
    private javax.swing.JButton RemovePreviousButton;
    private javax.swing.JButton RemoveSearchButton;
    private javax.swing.JTable RemovejTable;
    private javax.swing.JButton RetrieveMenujButton;
    private javax.swing.JButton ViewButton1;
    private javax.swing.JButton ViewButton2;
    private javax.swing.JButton ViewButton3;
    private javax.swing.JButton ViewButton4;
    private javax.swing.JButton ViewButton5;
    private javax.swing.JButton ViewClearButton;
    private javax.swing.JRadioButton ViewFemalejRadioButton;
    private javax.swing.JTextField ViewIcjTextField;
    private javax.swing.JRadioButton ViewMalejRadioButton;
    private javax.swing.JTextField ViewMemberIdjTextField;
    private javax.swing.JTextField ViewNamejTextField;
    private javax.swing.JButton ViewNextButton;
    private javax.swing.JLabel ViewPagejLabel;
    private javax.swing.JTextField ViewPhonejTextField;
    private javax.swing.JButton ViewPreviousButton;
    private javax.swing.JButton ViewSearchButton;
    private javax.swing.JTable ViewjTable;
    private javax.swing.ButtonGroup addGender;
    private javax.swing.JPanel addMember;
    private javax.swing.JButton addMemberButton;
    private javax.swing.JPanel addMemberPanel;
    private javax.swing.ButtonGroup editGender;
    private javax.swing.JPanel editMemberDetails;
    private javax.swing.JPanel generateMemberReport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel oriPanel;
    private javax.swing.ButtonGroup removeGender;
    private javax.swing.JPanel removeMember;
    private javax.swing.JPanel removeMemberPanel;
    private javax.swing.JPanel removeMemberPanel1;
    private javax.swing.JPanel removeMemberPanel2;
    private javax.swing.JPanel retrieveMemberDetails;
    private javax.swing.ButtonGroup viewGender;
    // End of variables declaration//GEN-END:variables
}
