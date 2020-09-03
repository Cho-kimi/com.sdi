package com.sdi.mbom.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.sdi.mbom.MBOM;
import com.sdi.mbom.MBOMChangeEvent;
import com.sdi.mbom.MBOMChangeEventListener;
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.PreMBOM;
import com.sdi.mbom.TitledMBOMLine;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentRevisionRule;
import com.teamcenter.rac.kernel.TCComponentRevisionRuleType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.VerticalLayout;
import com.teamcenter.rac.util.iTextField;

/**
 * 
 * @author 
 *
 */
public class AddMBomFormDialog extends AbstractAIFDialog implements ActionListener, InterfaceAIFOperationListener, MBOMChangeEventListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3620775456570162810L;
	
	private JPanel contentPane;
	private JButton createButton;
	private JButton cancelButton;
	private iTextField mBomTopId;
	private iTextField smdPhantomId;
	private iTextField autoId;
	private iTextField manualId;
	
	private JLabel  jlblSourceObjInfo;
	
	private JTable resultTable;
	private String[] header;
	private DialogTableModel tableModel;
	private JScrollPane resultScrollPane;

	private MBOM mbom;

	private boolean isPreMBOM;
	
	private TCComponentBOMWindow bomWindow;
	protected Window parent;
	
	public AddMBomFormDialog(Frame frame)
	{
		super(frame, false);
		this.parent = frame;
		initUI();
	}
	
	private void initUI()
	{
		setTitle("M-BOM 생성");
		
		this.contentPane = ((JPanel)getContentPane());
		this.contentPane.setBackground(new Color(250,250,250));
		this.contentPane.setLayout(new VerticalLayout());
		this.contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		this.contentPane.add("bound.bind.center.center", getHeaderPanel());
		this.contentPane.add("bound.bind.center.center", getDataPanel());
		this.contentPane.add("bound.bind.center.center", getButtonPanel());
	}
	
	private JPanel getHeaderPanel() {
		
		JPanel headerPanel = new JPanel(new BorderLayout(5,5));
		headerPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		//headerPanel.setOpaque(false);
		headerPanel.setBackground(Color.lightGray);
		
		//JPanel leftPanel = new JPanel(new BorderLayout(3,3));
		
		JLabel  jlblTargetID = new JLabel("Source Item : ");
		jlblTargetID.setPreferredSize(new Dimension(120, 25));
		jlblTargetID.setHorizontalAlignment(SwingConstants.RIGHT);
		//leftPanel.add(jlblTargetID);
		
		//JPanel centerPanel = new JPanel(new BorderLayout(3,3));
		
		this.jlblSourceObjInfo = new JLabel(" - ");
		this.jlblSourceObjInfo.setPreferredSize(new Dimension(400, 25));
		this.jlblSourceObjInfo.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		//leftPanel.add(jlblTargetObjInfo);
		
		
		headerPanel.add(this.jlblSourceObjInfo, BorderLayout.CENTER);
		headerPanel.add(jlblTargetID, BorderLayout.WEST);
		
		return headerPanel;
	}

	private JPanel getButtonPanel()
	{
		JPanel buttonPanel = new JPanel(new FlowLayout(2, 10, 0));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
		
		this.createButton = new JButton("생성");
		this.cancelButton = new JButton("닫기");
		
		buttonPanel.setOpaque(false);
		
		this.createButton.addActionListener(this);
		this.cancelButton.addActionListener(this);
		
		buttonPanel.add(this.createButton);
		buttonPanel.add(this.cancelButton);
		
		return buttonPanel;
	}
	
	private JPanel getDataPanel()
	{
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setOpaque(false);
		JPanel dataPanel = new JPanel(new PropertyLayout(5,5));
		dataPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		this.mBomTopId = new iTextField(15, true);
		this.smdPhantomId = new iTextField(15, true);
		this.autoId = new iTextField(15, false);
		this.manualId = new iTextField(15, false);
		
		dataPanel.setOpaque(false);
		JLabel  jlblTopID = new JLabel("M-BOM TOP ID : ");
		JLabel  jlblSMDID = new JLabel("SMD Phantom ID : ");
		JLabel  jlblAutoID = new JLabel("Auto ID : ");
		JLabel  jlblManualID = new JLabel("Manual ID : ");
		
		
		jlblTopID.setPreferredSize(new Dimension(120, 25));
		jlblTopID.setHorizontalAlignment(SwingConstants.RIGHT);
		jlblSMDID.setPreferredSize(new Dimension(120, 25));
		jlblSMDID.setHorizontalAlignment(SwingConstants.RIGHT);
		jlblAutoID.setPreferredSize(new Dimension(120, 25));
		jlblAutoID.setHorizontalAlignment(SwingConstants.RIGHT);
		jlblManualID.setPreferredSize(new Dimension(120, 25));
		jlblManualID.setHorizontalAlignment(SwingConstants.RIGHT);
		
		dataPanel.add("1.1.right.center", jlblTopID);
		dataPanel.add("1.2.right.center", this.mBomTopId);
		dataPanel.add("2.1.right.center", jlblSMDID);
		dataPanel.add("2.2.right.center", this.smdPhantomId);
		dataPanel.add("1.3.right.center", jlblAutoID);
		dataPanel.add("1.4.right.center", this.autoId);
		dataPanel.add("2.3.right.center", jlblManualID);
		dataPanel.add("2.4.right.center", this.manualId);
		
		JPanel tablePanel = new JPanel(new BorderLayout());
		this.resultScrollPane = new JScrollPane();
		
		this.header = new String[] { "Lvl", "부품ID", "부품명", "수량", "찾기번호", "MBOMAddress", "참조지정자", "처리결과" };
		this.tableModel = new DialogTableModel(this.header, 0) ;
		
		this.resultTable = new JTable(this.tableModel);
		
		DefaultTableCellRenderer cellCenterRenderer = new DefaultTableCellRenderer();
		cellCenterRenderer.setHorizontalAlignment(JLabel.CENTER);
		
		DefaultTableCellRenderer cellRightRenderer = new DefaultTableCellRenderer();
		cellRightRenderer.setHorizontalAlignment(JLabel.RIGHT);
		
		
		this.resultTable.getTableHeader().setBackground(Color.lightGray);
		this.resultTable.getTableHeader().setBorder(BorderFactory.createRaisedBevelBorder());
		
		this.resultTable.getColumnModel().getColumn(0).setPreferredWidth(50);
		this.resultTable.getColumnModel().getColumn(1).setPreferredWidth(160);
		this.resultTable.getColumnModel().getColumn(2).setPreferredWidth(200);
		this.resultTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		this.resultTable.getColumnModel().getColumn(4).setPreferredWidth(60);
		
		this.resultTable.getColumnModel().getColumn(5).setPreferredWidth(120);
		this.resultTable.getColumnModel().getColumn(6).setPreferredWidth(200);
		this.resultTable.getColumnModel().getColumn(7).setPreferredWidth(80);
		
		
		this.resultTable.getColumnModel().getColumn(0).setCellRenderer(cellRightRenderer);
		this.resultTable.getColumnModel().getColumn(1).setCellRenderer(cellCenterRenderer);
		//this.resultTable.getColumnModel().getColumn(2).setCellRenderer(cellCenterRenderer); //use default
		this.resultTable.getColumnModel().getColumn(3).setCellRenderer(cellRightRenderer);
		this.resultTable.getColumnModel().getColumn(4).setCellRenderer(cellCenterRenderer);
		this.resultTable.getColumnModel().getColumn(5).setCellRenderer(cellCenterRenderer);
		this.resultTable.getColumnModel().getColumn(6).setCellRenderer(cellCenterRenderer);
		this.resultTable.getColumnModel().getColumn(7).setCellRenderer(cellCenterRenderer);
		
		
		
		
		this.resultTable.setAutoResizeMode(1);
		this.resultScrollPane.getViewport().add(this.resultTable);
		this.resultScrollPane.setPreferredSize(new Dimension(800, 500));
		
		tablePanel.add(resultScrollPane);
		
		panel.add("North", dataPanel);
		panel.add("Center", tablePanel);
		
		return panel;
	}
	
	public void actionPerformed(ActionEvent event)
	{
		Object obj = event.getSource();
		
		if (obj.equals(this.createButton)){
			/*
			 * if(mbom instanceof PreMBOM) { PreMBOM preMbom = (PreMBOM)mbom;
			 * preMbom.getOwningBuilder().buildBOM(preMbom); }
			 */
			String sTopId = mBomTopId.getText();
			String sPhantomId = smdPhantomId.getText();
			if(sTopId == null || sTopId.length() == 0 ) {
				MessageBox.post(this.parent, "M-BOM TOP ID 값이 입력되지 않았습니다.", "경고", 4);
				return;
			}
			if(sPhantomId == null || sPhantomId.length() == 0) {
				MessageBox.post(this.parent, "SMD Phantom ID 값이 입력되지 않았습니다.", "경고", 4);
				return;
			}
			if(sTopId.equals(sPhantomId)) {
				MessageBox.post(this.parent, "M-BOM TOP ID 값과 SMD Phantom ID 값이 동일합니다.", "경고", 4);
				return;
			}
			
			
			TCSession TCSession = (TCSession)AIFUtility.getSessionManager().getDefaultSession();
			
			TCComponentItem topItem = createItem(TCSession, "M2_MBomTop", sTopId, "M-BOM Top");
			TCComponentItem phantomItem = createItem(TCSession, "M2_MBomPhantom", sPhantomId, "SMD Phantom");
			
			TCComponentBOMLine bomTopLine = null;
			TCComponentBOMLine bomPhantomLine = null;
			TCComponentBOMLine bomChildLine = null;
			try {
				TCComponentRevisionRuleType revisionRuleType = (TCComponentRevisionRuleType)TCSession.getTypeComponent("RevisionRule");
				TCComponentRevisionRule revisionRule = revisionRuleType.getDefaultRule();
				TCComponentBOMWindowType bomWindowType = (TCComponentBOMWindowType)TCSession.getTypeComponent("BOMWindow");
				this.bomWindow = bomWindowType.create(revisionRule);
				
				bomTopLine = this.bomWindow.setWindowTopLine(null, topItem.getLatestItemRevision(), null, null);
				
				String itemId = "";
				String mbomAdress = "";
				String designator = "";
				TCComponentItem childItem = null;
				for(int i=0; i<this.tableModel.getRowCount(); i++) {
					itemId = this.tableModel.getValueAt(i, 1).toString();
					mbomAdress = this.tableModel.getValueAt(i, 5).toString();
					designator = this.tableModel.getValueAt(i, 6).toString();
					if(mbomAdress.length() == 0) {
						if(itemId.equals("NEED NEW ITEM ID")) {
							this.tableModel.setValueAt("완료", i, 7);
						}else {
							childItem = findItem(TCSession, "Item", itemId);
							bomChildLine = bomTopLine.add( null, childItem.getLatestItemRevision(), null, false );
							bomChildLine.getTCProperty("bl_ref_designator").setStringValue(designator);
							this.tableModel.setValueAt("완료", i, 7);
						}
					}
				}
				
				bomTopLine.add( null, phantomItem.getLatestItemRevision(), null, false );
				
				this.bomWindow.save();
				
				bomPhantomLine = this.bomWindow.setWindowTopLine(null, phantomItem.getLatestItemRevision(), null, null);
				
				for(int i=0; i<this.tableModel.getRowCount(); i++) { 
					itemId = this.tableModel.getValueAt(i, 1).toString(); 
					mbomAdress = this.tableModel.getValueAt(i, 5).toString(); 
					designator = this.tableModel.getValueAt(i, 6).toString();
					if(mbomAdress != null && mbomAdress.length() > 0) { 
						if(mbomAdress.equals("SMD")) {
							childItem = findItem(TCSession, "Item", itemId);
							bomChildLine = bomPhantomLine.add( null, childItem.getLatestItemRevision(), null, false );
							bomChildLine.getTCProperty("m2_MbomAddress").setStringValue("SMD");
							bomChildLine.getTCProperty("bl_ref_designator").setStringValue(designator);
							this.tableModel.setValueAt("완료", i, 7);
						}
					}
				}
				
				this.bomWindow.save();
				this.bomWindow.close();
			
			}catch (TCException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (obj.equals(this.cancelButton)){
		  setVisible(false);
		  dispose();
		}
	}

	@Override
	public void endOperation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startOperation(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setMBOM(MBOM mbom) {
		this.mbom = mbom;
		
		
		if(mbom != null) {
			loadMBOM(mbom);
		}else {
			clearMBOM(mbom);
		}
		
	}
	
	public void addMBOM(MBOM mbom) {
		
		if(mbom != null) {
			loadMBOM(mbom);
		}else {
			clearMBOM(mbom);
		}
	}
	
	public MBOM getMBOM() {
		return this.mbom;
	}
	
	
	protected void updateSourceInfo(TCComponent sourceComp) {
		
		try {
			//String sourceInfo = sourceComp.getStringProperty("bl_item_item_id") + " / " + sourceComp.getStringProperty("bl_item_object_name");
			String sourceInfo = sourceComp.getProperty("bl_indented_title");
			this.jlblSourceObjInfo.setText(sourceInfo);
		}catch(Throwable t) {
			t.printStackTrace();
			this.jlblSourceObjInfo.setText("");
		}
	}
	
	protected void loadMBOM(MBOM mbom) {

		updateSourceInfo(mbom.getSourceComponent());
		
		//tablemodel reset;
		if(this.tableModel.getRowCount() > 0) {
			this.tableModel = new DialogTableModel(this.header, 0) ;
			this.resultTable.setModel(this.tableModel);
		}
		
		if(mbom instanceof PreMBOM) {
			 this.isPreMBOM = true;
			 
			 PreMBOM preMBOM = (PreMBOM)mbom;
			 
			 MBOMLine topLine = preMBOM.getTopBOMLine();
			 addTableRow(0, this.tableModel, topLine);
			 
			 //topId
			 addTextFiledChangeListener(this.mBomTopId, topLine, "item_id" );
			 
			 for(MBOMLine mbomLine : preMBOM.getPhantomMBOMLines()) {
				 
				 TitledMBOMLine phantomLine = (TitledMBOMLine)mbomLine;
				 String title = phantomLine.getTitleIdentity();
				 if(title.equals("SMD")) {
					 addTextFiledChangeListener(this.smdPhantomId, mbomLine, "item_id" );
				 }else if(title.equals("AUTO")) {
					 addTextFiledChangeListener(this.autoId, mbomLine, "item_id" );
				 }else if(title.equals("MANUAL")) {
					 addTextFiledChangeListener(this.manualId, mbomLine, "item_id" );
				 }
			 }
			 
		}else {
			
			//update 기존 M-BOM 정보 업데이트
			
		}
		mbom.addMBOMChangeEventListener(this);
		
	}
	
	protected void addTextFiledChangeListener(iTextField textField, MBOMLine bomLine, String propertyName) {
		
		textField.addActionListener(bomLine.getDataChangeActionListener(propertyName));
	}
	
	@Override
	public void onMBOMDataChanged(MBOMChangeEvent event) {
		
//		if(event.getEventDatas() != null) {
//			for(MBOMLine mbomline : event.getEventDatas()) {
//				Object row = getTableModelMap(mbomline);
//				//row data update
//			}
//		}else{
//			loadMBOM( this.getMBOM());
//		}
		
		loadMBOM( this.getMBOM());
	}

	private void addTableRow(int level, DialogTableModel tableModel, MBOMLine mbomLine) {
		
		 if(mbomLine != null) {
			 
			try {
				 String []  propNames = new String[] {"bl_item_item_id", "bl_item_object_name", "bl_quantity", "bl_sequence_no" , "m2_MbomAddress" , "bl_ref_designator"};
				
				 List<Object> properties = mbomLine.getProperties(propNames);
				 
//				 String itemId = bomline.getStringProperty("bl_item_item_id");
//				 String itemName = bomline.getStringProperty("bl_item_object_name");
//				 String address  = bomline.getStringProperty("m2_MbomAddress");;
//				 String refDeg = bomline.getStringProperty("bl_ref_designator");
//				 String status = "대기";
				 properties.add(0, String.valueOf(level));
				 properties.add("대기");
			 
				 tableModel.addRow( properties.toArray() );
			 
				 if(mbomLine.getChildrenCount() > 0) {
					 for(MBOMLine chilLine : mbomLine.getChildrenList()) {
						 addTableRow(level+1, tableModel , chilLine);
					 }
				 }
			} catch (Throwable t) {
				t.printStackTrace();
			}
		
		 }
		
	}

	protected void clearMBOM(MBOM mbom) {
		
		//target clear
		this.jlblSourceObjInfo.setText(" - ");
		
		// mbom item clear
		this.mBomTopId.setText("");
		this.smdPhantomId.setText("");
		this.autoId.setText("");
		this.manualId.setText("");
		
		//table clear
		this.tableModel = new DialogTableModel(this.header, 0) ;
		this.resultTable.setModel(tableModel);
		this.resultTable.updateUI();
	}

	public TCComponentItem createItem(TCSession session, String type, String item_id, String item_name)
	{
		TCComponentItem item = null;
		try
		{
		  TCComponentItemType itemType = (TCComponentItemType)session.getTypeComponent(type);
		  item = itemType.create(item_id, "A", type, item_name, "", null);
		
		  session.getUser().getHomeFolder().add("contents", item);
		}
		catch (TCException e)
		{
		  e.printStackTrace();
		}
		
		return item;
	}
	
	public TCComponentItem findItem(TCSession session, String type, String item_id)
	{
		TCComponentItem item = null;
		try
		{
		  TCComponentItemType itemType = (TCComponentItemType)session.getTypeComponent(type);
		  item = itemType.find(item_id);
		}
		catch (TCException e)
		{
		  e.printStackTrace();
		}
		return item;
	}
}
