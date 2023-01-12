package GroupBuy;

import Adt.CircularArrayQueue;
import Adt.QueueInterface;
import Entity.Member;
import Adt.ListInterface;
import Entity.GroupBuyMemberEntity;
import Entity.GroupBuyEntity;
import Member.*;
import Main.*;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Ng Theng Yang
 */

public class groupBuyJoinMemberGui extends javax.swing.JFrame {
    private static final int DEFAULT_TABLE_ROWS = 5;
    private int jPanelNo = 0;
    private int currentPageNo = 1;
    private int jTableRowIndex = 0;
    private int joinPosition;
    private int replacePosition;
    private QueueInterface<Member> members;
    private Member member;
    private ListInterface<GroupBuyEntity> currentGroupBuys;
    private GroupBuyEntity currentGroupBuy;
    private GroupBuyMemberEntity groupBuyMember;
    private boolean changedTableValue;
    
    public groupBuyJoinMemberGui(int joinPosition, int replacePosition) {
        initComponents();
        setLocationRelativeTo(null);
        initValues(joinPosition, replacePosition);
        setTableValue();
    }
    
    public void initValues(int joinPosition, int replacePosition){
        currentGroupBuy = new GroupBuyEntity();
        currentGroupBuys = mainMenu.currentGroupBuys;
        member = new Member();
        members = mainMenu.members;
        groupBuyMember = new GroupBuyMemberEntity();
        this.joinPosition = joinPosition;
        this.replacePosition = replacePosition;
    }
    
    private void setTableValue(){
        int totalMember = members.getNumberOfEntries();
        int totalPageNo = (int) Math.ceil(((double)totalMember/DEFAULT_TABLE_ROWS));
        if(currentPageNo > totalPageNo && totalMember != 0){
            currentPageNo--;
            jTableRowIndex-=5;
        }
        DefaultTableModel model = (DefaultTableModel) AddTable.getModel();
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
        PagejLabel.setText(pageText);
    }
    
    private void clearValue(){
        ButtonGroup addButtonGroup = new ButtonGroup();
        addButtonGroup.add(FemaleButton);
        addButtonGroup.add(MaleButton);
        addButtonGroup.clearSelection();
        FemaleButton.setSelected(false);
        MaleButton.setSelected(false);
        Ic.setText("");
        Name.setText("");
        Phone.setText("");
        MemberId.setText("");
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
    
    private void searchMember(){
        members = mainMenu.members;
        QueueInterface<Member> searchMembers = new CircularArrayQueue();
        Iterator<Member> searchMembersIterator;
        boolean selectedGender = (MaleButton.isSelected() || FemaleButton.isSelected());
        String memberId = MemberId.getText();
        String name = Name.getText();
        String icNo = Ic.getText();
        String phoneNo = Phone.getText();
        String gender = (MaleButton.isSelected() ? "Male" : null);
        if(gender == null)
            gender = (FemaleButton.isSelected() ? "Female" : null);
        boolean enqueued = false;
        int enqueueTimes = 0;
        boolean end = false;
        if(!memberId.isEmpty()){
            searchMembersIterator = members.getQueueIterator();
            while(searchMembersIterator.hasNext()){
                member = searchMembersIterator.next();
                if(memberId.toUpperCase().equals(member.getMemberId())){
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
                if(name.toUpperCase().equals(member.getName().toUpperCase())){
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
    
    private boolean joinGroupBuy(int buttonPosition){
        int memberPosition = buttonPosition + jTableRowIndex;
        boolean successful;
        if(changedTableValue){
            QueueInterface<Member> membersB = mainMenu.members;
            member = members.getEntry(memberPosition);
            memberPosition = membersB.search(member);
            successful = memberPosition != -1;
        }
        else{
            member = members.getEntry(memberPosition);
            successful = member != null;
        }
        if(successful){  
            members = mainMenu.members;
            member = members.getEntry(memberPosition);
            currentGroupBuy = currentGroupBuys.getEntry(joinPosition);
            groupBuyMember = new GroupBuyMemberEntity(member);
            if(replacePosition == -1){
                currentGroupBuy.addGroupBuyMember(groupBuyMember);
            }
            else{
                currentGroupBuy.replaceGroupBuyMember(groupBuyMember, replacePosition);
            }
            currentGroupBuys.replace(currentGroupBuy, joinPosition);
        }  
        else
            JOptionPane.showMessageDialog(this, "Please select has value row to perform add to group buy process!");
        
        return successful;
    }
    
    public void exitForm() {
        this.setVisible(false);
        if(replacePosition == -1)
            new groupBuyJoinGui(joinPosition).setVisible(true);
        else
            new groupBuyModifyGui(joinPosition).setVisible(true);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Gender = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        Add1 = new javax.swing.JButton();
        Add2 = new javax.swing.JButton();
        Add3 = new javax.swing.JButton();
        Add4 = new javax.swing.JButton();
        Add5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        AddTable = new javax.swing.JTable();
        SearchButton = new javax.swing.JButton();
        ClearButton = new javax.swing.JButton();
        Close = new javax.swing.JButton();
        removeMemberPanel = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        MemberId = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        Ic = new javax.swing.JTextField();
        Phone = new javax.swing.JTextField();
        Name = new javax.swing.JTextField();
        MaleButton = new javax.swing.JRadioButton();
        FemaleButton = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        NextButton = new javax.swing.JButton();
        PreviousButton = new javax.swing.JButton();
        PagejLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Group Buy Maintenance");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 204, 204));

        Add1.setBackground(new java.awt.Color(255, 255, 255));
        Add1.setText("Add to GroupBuy");
        Add1.setActionCommand("delete1");
        Add1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add1ActionPerformed(evt);
            }
        });

        Add2.setBackground(new java.awt.Color(255, 255, 255));
        Add2.setText("Add to GroupBuy");
        Add2.setActionCommand("delete2");
        Add2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add2ActionPerformed(evt);
            }
        });

        Add3.setBackground(new java.awt.Color(255, 255, 255));
        Add3.setText("Add to GroupBuy");
        Add3.setActionCommand("delete3");
        Add3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add3ActionPerformed(evt);
            }
        });

        Add4.setBackground(new java.awt.Color(255, 255, 255));
        Add4.setText("Add to GroupBuy");
        Add4.setActionCommand("delete4");
        Add4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add4ActionPerformed(evt);
            }
        });

        Add5.setBackground(new java.awt.Color(255, 255, 255));
        Add5.setText("Add to GroupBuy");
        Add5.setActionCommand("delete5");
        Add5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Add5ActionPerformed(evt);
            }
        });

        AddTable.setModel(new javax.swing.table.DefaultTableModel(
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
        AddTable.setRowHeight(30);
        jScrollPane2.setViewportView(AddTable);

        SearchButton.setText("Search");
        SearchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SearchButtonActionPerformed(evt);
            }
        });

        ClearButton.setText("Clear");
        ClearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearButtonActionPerformed(evt);
            }
        });

        Close.setText("Back");
        Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseActionPerformed(evt);
            }
        });

        removeMemberPanel.setBackground(new java.awt.Color(255, 255, 255));
        removeMemberPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "MEMBER DETAILS ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Microsoft JhengHei", 1, 18))); // NOI18N

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel22.setText("Member ID: ");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel23.setText("Name: ");

        jLabel24.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel24.setText("Gender: ");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel25.setText("No IC: ");

        jLabel26.setFont(new java.awt.Font("Times New Roman", 0, 12)); // NOI18N
        jLabel26.setText("Phone No: ");

        Ic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IcActionPerformed(evt);
            }
        });

        MaleButton.setBackground(new java.awt.Color(255, 255, 255));
        Gender.add(MaleButton);
        MaleButton.setText("Male");

        FemaleButton.setBackground(new java.awt.Color(255, 255, 255));
        Gender.add(FemaleButton);
        FemaleButton.setText("Female");

        javax.swing.GroupLayout removeMemberPanelLayout = new javax.swing.GroupLayout(removeMemberPanel);
        removeMemberPanel.setLayout(removeMemberPanelLayout);
        removeMemberPanelLayout.setHorizontalGroup(
            removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeMemberPanelLayout.createSequentialGroup()
                        .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(MemberId, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(removeMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Ic, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(removeMemberPanelLayout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(MaleButton)
                        .addGap(18, 18, 18)
                        .addComponent(FemaleButton)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        removeMemberPanelLayout.setVerticalGroup(
            removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeMemberPanelLayout.createSequentialGroup()
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MemberId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(MaleButton)
                    .addComponent(FemaleButton))
                .addGap(13, 13, 13)
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(Ic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(removeMemberPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(Phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 10, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Microsoft JhengHei", 1, 18)); // NOI18N
        jLabel3.setText("Member List");

        NextButton.setText(">");
        NextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NextButtonActionPerformed(evt);
            }
        });

        PreviousButton.setText("<");
        PreviousButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PreviousButtonActionPerformed(evt);
            }
        });

        PagejLabel.setFont(new java.awt.Font("Microsoft JhengHei", 0, 12)); // NOI18N
        PagejLabel.setText("Page 1 of 1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(removeMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Add3)
                            .addComponent(Add4)
                            .addComponent(Add1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Add2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Add5, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 33, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(237, 237, 237)
                .addComponent(PreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(PagejLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Close, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(removeMemberPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(SearchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(ClearButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(Add1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Add2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Add3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Add4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(Add5)
                                .addGap(16, 16, 16)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PreviousButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(NextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(PagejLabel)))
                        .addGap(0, 16, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(Close, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IcActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_IcActionPerformed

    private void SearchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchButtonActionPerformed
        // TODO add your handling code here:
        searchMember();
    }//GEN-LAST:event_SearchButtonActionPerformed

    private void ClearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearButtonActionPerformed
        // TODO add your handling code here:
        clearValue();
    }//GEN-LAST:event_ClearButtonActionPerformed

    private void PreviousButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PreviousButtonActionPerformed
        // TODO add your handling code here:
        previousPage();
    }//GEN-LAST:event_PreviousButtonActionPerformed

    private void NextButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NextButtonActionPerformed
        // TODO add your handling code here:
        nextPage();
    }//GEN-LAST:event_NextButtonActionPerformed

    private void Add1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add1ActionPerformed
        // TODO add your handling code here:
        boolean successful = joinGroupBuy(1);
        if(successful){
            exitForm();
        }
    }//GEN-LAST:event_Add1ActionPerformed

    private void Add2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add2ActionPerformed
        // TODO add your handling code here:
        boolean successful = joinGroupBuy(2);
        if(successful){
            exitForm();
        }
    }//GEN-LAST:event_Add2ActionPerformed

    private void Add3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add3ActionPerformed
        // TODO add your handling code here:
        boolean successful = joinGroupBuy(3);
        if(successful){
            exitForm();
        }
    }//GEN-LAST:event_Add3ActionPerformed

    private void Add4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add4ActionPerformed
        // TODO add your handling code here:
        boolean successful = joinGroupBuy(4);
        if(successful){
            exitForm();
        }
    }//GEN-LAST:event_Add4ActionPerformed

    private void Add5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add5ActionPerformed
        // TODO add your handling code here:
        boolean successful = joinGroupBuy(5);
        if(successful){
            exitForm();
        }
    }//GEN-LAST:event_Add5ActionPerformed

    private void CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseActionPerformed
        // TODO add your handling code here:
        exitForm();
    }//GEN-LAST:event_CloseActionPerformed

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
//            java.util.logging.Logger.getLogger(groupBuyJoinMemberGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(groupBuyJoinMemberGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(groupBuyJoinMemberGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(groupBuyJoinMemberGui.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new groupBuyJoinMemberGui().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add1;
    private javax.swing.JButton Add2;
    private javax.swing.JButton Add3;
    private javax.swing.JButton Add4;
    private javax.swing.JButton Add5;
    private javax.swing.JTable AddTable;
    private javax.swing.JButton ClearButton;
    private javax.swing.JButton Close;
    private javax.swing.JRadioButton FemaleButton;
    private javax.swing.ButtonGroup Gender;
    private javax.swing.JTextField Ic;
    private javax.swing.JRadioButton MaleButton;
    private javax.swing.JTextField MemberId;
    private javax.swing.JTextField Name;
    private javax.swing.JButton NextButton;
    private javax.swing.JLabel PagejLabel;
    private javax.swing.JTextField Phone;
    private javax.swing.JButton PreviousButton;
    private javax.swing.JButton SearchButton;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPanel removeMemberPanel;
    // End of variables declaration//GEN-END:variables
}
