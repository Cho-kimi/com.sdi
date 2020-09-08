package com.sdi.mbom.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.JTextComponent;

import com.sdi.common.TCUtils;
import com.sdi.mbom.InvalidMBOMCreationException;
import com.sdi.mbom.MBOM;
import com.sdi.mbom.MBOMChangeEvent;
import com.sdi.mbom.MBOMChangeEventListener;
import com.sdi.mbom.MBOMConstants;
import com.sdi.mbom.MBOMLine;
import com.sdi.mbom.MBOMManager;
import com.sdi.mbom.PreMBOM;
import com.sdi.mbom.PropertyUIProvider;
import com.sdi.mbom.TitledMBOMLine;
import com.teamcenter.rac.aif.AbstractAIFApplication;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentRevisionRule;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.pse.operations.UnpackOperation;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.VerticalLayout;

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
	
	public static String []  DEFAULT_BOMLINE_COLUMN_PROPS = new String[] { 
				MBOMConstants.MBOM_LINE_LEVEL_HEADER, 
			 	MBOMConstants.PROP_BOMLINE_ITEM_ID, 
			 	MBOMConstants.PROP_BOMLINE_OBJECT_NAME, 
			 	MBOMConstants.PROP_BOMLINE_QANTITY , 
			 	MBOMConstants.PROP_BOMLINE_SEQUENCE_NO , 
			 	MBOMConstants.PROP_BOMLINE_MBOM_ADDRESS, 
			 	MBOMConstants.PROP_BOMLINE_REF_DESIGNATOR,
			 	MBOMConstants.MBOM_LINE_STATUS_HEADER
	};
	
	public static String []  DEFAULT_BOMLINE_REF_COPY_PROPNAMES = new String[] { 
			MBOMConstants.PROP_BOMLINE_REF_DESIGNATOR, 
			MBOMConstants.PROP_BOMLINE_MBOM_ADDRESS,
			MBOMConstants.PROP_BOMLINE_SEQUENCE_NO
	};
	
	
	private JPanel contentPane;
	private JButton createButton;
	private JButton cancelButton;
	private PropertyProvideField mBomTopId;
	private PropertyProvideField smdPhantomId;
	private PropertyProvideField autoId;
	private PropertyProvideField manualId;
	
	private JLabel messageFiled;
	private JProgressBar progressBar;
	
	private JLabel  jlblSourceObjInfo;
	
	private JTable resultTable;
	private String[] header;
	private MBOMTableModel tableModel;
	private JScrollPane resultScrollPane;

	private MBOM mbom;

	private boolean isPreMBOM;
	
	protected Window parent;

	private boolean isBusy;
	
	public AddMBomFormDialog(Frame frame)
	{
		super(frame, false);
		this.parent = frame;
		initUI();
	}
	
	private void initUI()
	{
		setTitle("M-BOM 생성");
		
		MBOMManager.getHelper().setMBOMLineCopyPorpNames(DEFAULT_BOMLINE_REF_COPY_PROPNAMES);
		
		
		this.contentPane = ((JPanel)getContentPane());
		this.contentPane.setBackground(new Color(250,250,250));
		this.contentPane.setLayout(new VerticalLayout());
		this.contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		this.contentPane.add("bound.bind.center.center", getHeaderPanel());
		this.contentPane.add("bound.bind.center.center", getDataPanel());
		this.contentPane.add("bound.bind.center.center", getBottomPanel());
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

	private JPanel getBottomPanel()
	{
		JPanel bomtomPanel = new JPanel(new BorderLayout(3,3));
		bomtomPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		
		JPanel messagePanel = new JPanel(new BorderLayout(2,2));
		JPanel buttonPanel = new JPanel(new FlowLayout(2, 10, 0));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		this.messageFiled = new JLabel();
		messageFiled.setOpaque(false);
		messagePanel.add(messageFiled);
		
		this.progressBar = new JProgressBar();
		this.progressBar.setPreferredSize(new Dimension(250, 20) );
		
		JPanel progressPanel = new JPanel(new BorderLayout(2,2));
		progressPanel.add(progressBar);
		
		messagePanel.add(progressPanel, BorderLayout.EAST);
		
		this.createButton = new JButton("생성");
		this.cancelButton = new JButton("닫기");
		
		buttonPanel.setOpaque(false);
		
		this.createButton.addActionListener(this);
		this.cancelButton.addActionListener(this);
		
		buttonPanel.add(this.createButton);
		buttonPanel.add(this.cancelButton);
		
		bomtomPanel.add(messagePanel, BorderLayout.CENTER);
		bomtomPanel.add(buttonPanel, BorderLayout.EAST);
		
		return bomtomPanel;
	}
	
	private JPanel getDataPanel()
	{
		JPanel panel = new JPanel(new BorderLayout(10,10));
		panel.setOpaque(false);
		JPanel dataPanel = new JPanel(new PropertyLayout(5,5));
		dataPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		this.mBomTopId = new PropertyProvideField(15, true);
		this.smdPhantomId = new PropertyProvideField(15, true);
		this.autoId = new PropertyProvideField(15, false);
		this.manualId = new PropertyProvideField(15, false);
		
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
		this.tableModel = new MBOMTableModel(this.header, 0) ;
		
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
		
		
		this.resultTable.setAutoscrolls(true);
		
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
			
			TCSession tcSession = TCUtils.getDefaultSession();
			AbstractAIFOperation operation = new AbstractAIFOperation("Open New MBOM Item") {
				
				public void executeOperation() throws Exception
				{
					createMBOM(tcSession);
				}
			};
			
			operation.addOperationListener(this);
			tcSession.queueOperation(operation);
			
		}
		else if (obj.equals(this.cancelButton)){
			
			if(isBusy()) {
				MessageBox.post(this, "BOM 생성 작업중입니다. 작업 완료후 닫아 주십시요", "안내", MessageBox.INFORMATION);
			}else {
				setVisible(false);
				dispose();
			}
		}
	}
	
	
	private TCComponentItemRevision findTargetItemRevision(TCSession tcSession, MBOMLine mbomLine) 
			throws TCException {
		
		String bomlineStatus = mbomLine.getProperty(MBOMConstants.MBOM_LINE_STATUS_HEADER);
		TCComponentItemRevision targetItemRev = null;
		
		if(bomlineStatus.equals(MBOMConstants.MBOM_LINE_STATUS_NEW)) {
			
			String objectName = mbomLine.getName();
			String itemID = mbomLine.getTargetItemId();
			
			if(itemID == null || itemID.equals(MBOMLine.NEW_ITEM_ID)) {
				
				PropertyUIProvider provider = mbomLine.getPropertyUIProvider(MBOMConstants.PROP_ITEM_ID);
				if(provider == null || provider.getPropertyValue() == null || provider.getPropertyValue().isEmpty() ) {
					
					if(provider != null && provider instanceof JTextComponent) {
						SwingUtilities.invokeLater(new Runnable() {

				            @Override
				            public void run() {
				                final JTextComponent component = (JTextComponent)provider;
				                component.requestFocusInWindow();
				            }
				        });
					}
					throw new NullPointerException(MBOMLine.NEW_ITEM_ID + "값인 Item ID는 필수로 입력하여야 합니다.");
				}else {
					itemID = provider.getPropertyValue();
				}
			}
			
			TCComponentItem newItem = TCUtils.createItem(tcSession, (mbomLine instanceof TitledMBOMLine)? MBOMConstants.TYPE_MBOM_PHANTOM_ITEM : MBOMConstants.TYPE_MBOM_TOP_ITEM , itemID, objectName);
			targetItemRev = newItem.getLatestItemRevision();
			
		}else if(bomlineStatus.equals(MBOMConstants.MBOM_LINE_STATUS_ADD)){
			targetItemRev = mbomLine.getSourceBOMLine().getItemRevision();
		}else if(bomlineStatus.equals(MBOMConstants.MBOM_LINE_STATUS_COMPLETE)){
			targetItemRev = null;
		}else if(bomlineStatus.equals(MBOMConstants.MBOM_LINE_STATUS_PROGRESS)){
			targetItemRev = null;
		}else {
			throw new NullPointerException("BOM에 추가할 Item Revision 정보를 가져올 수 없습니다.");
		}
		
		return targetItemRev;
	}
	
	private TCComponentBOMLine addBOMLine(TCSession tcSession, 
			TCComponentBOMLine parent, TCComponentItemRevision childItemRev, MBOMLine mbomLine, PreMBOM prMBOM, 
			String[] refPropertyNames, Object[] refPropertyValues) 
			throws TCException {
		
		TCComponentBOMLine newBOMLine = null;
		
		if(parent == null) {
			
			TCComponentBOMLine sourceTopLine =  mbomLine.getSourceBOMLine();
			TCComponentBOMWindow sourceBOMWindow = sourceTopLine.getCachedWindow();
			
			TCComponentRevisionRule revisionRule = TCUtils.getRevisionRule(tcSession, sourceBOMWindow );
			TCComponentBOMWindowType bomWindowType = (TCComponentBOMWindowType)TCUtils.getTypeComponent(tcSession, MBOMConstants.TYPE_BOM_WINDOW, sourceBOMWindow); 
			
			TCComponentBOMWindow bomWindow = bomWindowType.create(revisionRule);
			newBOMLine = bomWindow.setWindowTopLine(null, childItemRev, null, null);
			
			prMBOM.setBOMWindow(bomWindow);
		}else {
			newBOMLine = parent.add( null, childItemRev, null, false);
			//속성 복사
			if(refPropertyNames != null && refPropertyValues != null && refPropertyNames.length == refPropertyValues.length) {
				for(int i=0; i < refPropertyNames.length; i++) {
					TCProperty tcProp = newBOMLine.getTCProperty(refPropertyNames[i]);
					if(tcProp != null && refPropertyValues[i] != null) {
						tcProp.setStringValue(String.valueOf(refPropertyValues[i]));
					}
				}
			}
		}
		
		mbomLine.setPermanentBOMLine(newBOMLine);
		return newBOMLine;
	}
	
	
	protected void createMBOM(TCSession tcSession) {

		PreMBOM preMBOM = (PreMBOM)getMBOM();
		
		try {
			validateCreateMBOM(preMBOM);
		}catch(InvalidMBOMCreationException ivex) {
			
			MessageBox.post(this.parent, ivex.getMessage(), "경고-생성 데이터 검증 오류", MessageBox.WARNING);
			return;
		}
		
//		String sTopId = mBomTopId.getText();
//		String sPhantomId = smdPhantomId.getText();
		
		setBusy(true);
		
		try {
			Vector <TCComponentBOMLine> bomLineStepVec = new Vector <TCComponentBOMLine>();
			
			TCComponentBOMWindow bomWindow = null;
			
			this.progressBar.setMaximum(this.tableModel.getRowCount());
			
			for(int i=0; i < this.tableModel.getRowCount(); i++) {

				this.progressBar.setValue(i+1);
				this.setSelectedRow(i, i);
				
				MBOMLineTableRow mbomLineRow = this.tableModel.getRowAt(i);
				MBOMLine mbomLine = mbomLineRow.getMBOMLine();
				int bomLineLevel = mbomLine.getMBOMLineLevel();
				
				TCComponentItemRevision targetItemRev = findTargetItemRevision(tcSession, mbomLine);
				
				if(targetItemRev == null) {
					continue;
				}
				
				String [] refPropertyNames  = mbomLine.getRefPropertyNames();
				Object [] refPropertyValues = mbomLine.getProperties(refPropertyNames).toArray();
				
				//복사할 속성 정보
//				String designator = mbomLine.getProperty(MBOMConstants.PROP_BOMLINE_REF_DESIGNATOR);
//				String mbomAddress = mbomLinegetProperties(MBOMConstants.PROP_BOMLINE_MBOM_ADDRESS);
						
				TCComponentBOMLine parent = null;
				
				
				//TOP이 아닌 경우에는 부모 BOMLine을 찾아온다.
				if(bomLineLevel > 0 && bomLineLevel <= bomLineStepVec.size()) {
					parent = bomLineStepVec.elementAt(bomLineLevel -1 );
				}
				
				TCComponentBOMLine newBOMLine = addBOMLine(tcSession, parent, targetItemRev, mbomLine, preMBOM, refPropertyNames, refPropertyValues);
				
				if(bomWindow == null) {
					bomWindow = preMBOM.getBOMWindow();
				}
				
				if(bomLineLevel < bomLineStepVec.size()) {
					bomLineStepVec.setElementAt(newBOMLine, bomLineLevel);
				}else {
					bomLineStepVec.addElement(newBOMLine);
				}
				
				//Update Event Call
				mbomLineRow.updateMBOMLine();
				this.tableModel.fireTableRowsUpdated(i, i);
			}
			
			if(bomWindow != null) {
				bomWindow.save();
				bomWindow.close();
			}
			
		}catch (TCException e) {
			e.printStackTrace();
		}finally {
			setBusy(false);
		}
	}
	

	private void setBusy(boolean busy) {
		this.isBusy = busy;		
	}
	
	protected boolean isBusy() {
		return this.isBusy ;
	}

	protected void openCreatedItem(TCSession session, final TCComponentItem topItem) {
		session.queueOperation(new AbstractAIFOperation("Open New MBOM Item") {
			public void executeOperation() throws Exception
			{
				AbstractAIFApplication currentApp = AIFUtility.getActiveDesktop().getCurrentApplication();
				
//				if(currentApp instanceof PSEApplicationService) {
//					PSEApplicationService pseApp = (PSEApplicationService)currentApp;
//					BOMPanel bompanel = pseApp.getCurrentBOMPanel();
//					bompanel.setSplit(true);
//					//bompanel.blankSelected();
//					//bompanel.open(topItem, null, null);
//				}
				currentApp.open(topItem);
			}
		});
	}

	protected void validateCreateMBOM(MBOM mbom) throws InvalidMBOMCreationException {

		String checkSourceName = "";
		
		try {

			// 입력창 확인
			String sTopId = mBomTopId.getText();
			String sPhantomId = smdPhantomId.getText();
			
			checkSourceName = "M-BOM TOP ID" ;
			
			if(sTopId == null || sTopId.length() == 0 ) {
				throw new Exception("M-BOM TOP ID 값이 입력되지 않았습니다.");
			}
			
			checkSourceName = "SMD Phantom ID" ;
			if(sPhantomId == null || sPhantomId.length() == 0) {
				throw new Exception("SMD Phantom ID 값이 입력되지 않았습니다.");
			}
			
			checkSourceName = "SAME ID" ;
			if(sTopId.equals(sPhantomId)) {
				throw new Exception("M-BOM TOP ID 값과 SMD Phantom ID 값이 동일합니다.");
			}
			
		}catch(Throwable ex) {
			throw new InvalidMBOMCreationException(checkSourceName, ex.getMessage());
		}
	}


	/**
	 * @return the isPreMBOM
	 */
	public boolean isPreMBOM() {
		return isPreMBOM;
	}

	public void setMBOM(MBOM mbom) {
		this.mbom = mbom;
		
		
		if(mbom != null) {
			loadMBOM(mbom);
		}else {
			clearMBOM(mbom);
		}
		
	}
	
	/**
	 * @return the tableModel
	 */
	public MBOMTableModel getTableModel() {
		return tableModel;
	}

	/**
	 * @param tableModel the tableModel to set
	 */
	public void setTableModel(MBOMTableModel tableModel) {
		this.tableModel = tableModel;
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
	
	
	protected void setSelectedBOMLineInfo(TCComponent sourceComp) {
		
		try {
			String sourceInfo = sourceComp.getProperty(MBOMConstants.PROP_BOMLINE_INDENTIED_TITLE);
			this.jlblSourceObjInfo.setText(sourceInfo);
		}catch(Throwable t) {
			t.printStackTrace();
			this.jlblSourceObjInfo.setText("");
		}
	}
	
	protected void loadMBOM(MBOM mbom) {

		//선택된 TOP E-BOM정보를 업데이트 해준다.
		//최당단 TOP 정보에 선택한 BOMLine의 Object 정보를 업데이트 해준다.
		setSelectedBOMLineInfo(mbom.getSourceComponent());
		
		//tableModel clear;
		clearTable();
		
		//TOP라인 처리
		MBOMLine topLine = mbom.getTopBOMLine();

		//table model에 BOMLine을 recursive하게 내려가면서 Row를 추가한다.
		addTableRow(topLine);
		
		//MBOM변경시에 변경사항을 다이알로그가 이벤트를 처리할 수 있도록 리스너를 연결한다.
		mbom.addMBOMChangeEventListener(this);
		
		if(mbom instanceof PreMBOM) {
			PreMBOM preMBOM = (PreMBOM)mbom;
			List<MBOMLine> phantomMBOMLines = preMBOM.getPhantomMBOMLines();
			//신규 아이템 아이디 입력 필드의 변경시 이벤트를 MBOMLine의 리스너에 연결시켜주어 변경시마다 데이터가 업데이트 되도록 한다.
			setPreMBOMItemIDFileds(topLine, phantomMBOMLines);
			this.isPreMBOM = true;
		}else {
			//update 기존 M-BOM 정보 업데이트
			this.isPreMBOM = false;
		}
	}
	
	/**
	 * 
	 * @param preMBOM
	 */
	protected void setPreMBOMItemIDFileds(MBOMLine topLine, List<MBOMLine> phantomMBOMLines) {

		 //topId
		 addTextFiledChangeListener(this.mBomTopId, topLine, MBOMConstants.PROP_ITEM_ID );
		 
		 //팬텀 라인 처리
		 if(phantomMBOMLines != null) {
			 for(MBOMLine mbomLine : phantomMBOMLines) {

				 TitledMBOMLine phantomLine = (TitledMBOMLine)mbomLine;
				 
				 String title = phantomLine.getTitleIdentity();
				 
				 if(title.equals("SMD")) {
					 addTextFiledChangeListener(this.smdPhantomId, mbomLine, MBOMConstants.PROP_ITEM_ID);
				 }else if(title.equals("AUTO")) {
					 addTextFiledChangeListener(this.autoId, mbomLine, MBOMConstants.PROP_ITEM_ID);
				 }else if(title.equals("MANUAL")) {
					 addTextFiledChangeListener(this.manualId, mbomLine, MBOMConstants.PROP_ITEM_ID);
				 }
			 }
		 }
	}
	
	
	protected void addTextFiledChangeListener(PropertyProvideField textField, MBOMLine bomLine, String propertyName) {
		textField.addActionListener(bomLine.getDataChangeActionListener(textField, propertyName));
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

	private void addTableRow(MBOMLine mbomLine) {
		
		 if(mbomLine != null &&  tableModel != null && tableModel instanceof MBOMTableModel) {
			 
			try {
//				 List<Object> properties = mbomLine.getProperties(DEFAULT_BOMLINE_COLUMN_PROPS);
//				 getTableModel().addRow( properties.toArray() );
				 
				 MBOMLineTableRow tableRow = new MBOMLineTableRow(mbomLine, DEFAULT_BOMLINE_COLUMN_PROPS);
				 getTableModel().addRow(tableRow);
			 
				 if(mbomLine.getChildrenCount() > 0) {
					 for(MBOMLine chilLine : mbomLine.getChildrenList()) {
						 addTableRow(chilLine);
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
		
		clearTable();
		
	}
	
	protected void clearTable() {
		
		//table Data clear
		this.tableModel.clearData();
		
	}

	@Override
	public void endOperation() {
		try {
			TCSession tcSession = TCUtils.getDefaultSession();
			TCComponentItem topItem = this.getMBOM().getTopBOMLine().getPermanentBOMLine().getItem();
			if(topItem != null) {
				this.messageFiled.setText("생성한 M-BOM을 로드합니다.");
				openCreatedItem(tcSession, topItem);
			}else {
				this.messageFiled.setText("생성된 M-BOM을 가져올 수 없습니다.");
				MessageBox.post(this.parent, "생성된 M-BOM을 가져올 수 없습니다.", "생성한 MBOM 로드", MessageBox.WARNING);
			}
		}catch(Throwable e) {
			this.messageFiled.setText("생성된 M-BOM을 가져올 수 없습니다.");
			MessageBox.post(this.parent, e.getMessage(), "생성한 MBOM 로드", MessageBox.ERROR);
		}
	}

	@Override
	public void startOperation(String arg0) {
		showProgress(arg0);
	}

	private void showProgress(String message) {
		this.messageFiled.setText(message);
	}

	public void setTarget(final TCComponentBOMLine topBOMLine) throws TCException {

		if(topBOMLine != null) {
			//Pack이 되어 있으면 BOMLine 속성을 제대로 가져올 수 없으므로 Unpack한다.
			if(isPackaed(topBOMLine)) {
				
				TCSession tcSession = TCUtils.getDefaultSession();
				UnpackOperation unpackOperation = new UnpackOperation(topBOMLine);
				unpackOperation.addOperationListener(new InterfaceAIFOperationListener() {

					@Override
					public void endOperation() {
						loadBOMLine(topBOMLine);
					}

					@Override
					public void startOperation(String arg0) {
						
					}
				});
				
				tcSession.queueOperation(unpackOperation);		
				
			}else {
				loadBOMLine(topBOMLine);
			}
		}
	}
	
	private boolean isPackaed(TCComponentBOMLine parent) throws TCException {
		boolean isPackaed = false;

		AIFComponentContext[] childrenLines = parent.getChildren();
		for(int i=0; i < childrenLines.length; i++ ) {
			TCComponentBOMLine childLine = (TCComponentBOMLine)childrenLines[i].getComponent();
			if(childLine.isPacked()) return true;
		}
		return isPackaed;
	}
	
	private void loadBOMLine(TCComponentBOMLine topBOMLine) {
		
		try {
			
			PreMBOM preMBOM = MBOMManager.getBuilder().buildPreMBOM(topBOMLine);
			setMBOM(preMBOM);
			
		}catch(Throwable e) {
			String message = e.getMessage();
			MessageBox.post(message, "M-BOM Pre Build ERROR ",  MessageBox.ERROR);
		}
	}
	
	public void setSelectedRow(int firstRowIndex, int lastRowIndex)
	{
	    this.resultTable.setRowSelectionInterval(firstRowIndex, lastRowIndex);
	    scrollToSelectedRow(this.resultTable);
	    try
	    {
	        Thread.sleep(100);
	    } catch (InterruptedException exception)
	    {
	        exception.printStackTrace();
	    }
	    scrollToSelectedRow(this.resultTable);
	    this.resultTable.repaint();
	}
	
	private void scrollToSelectedRow(JTable table)
	{
		Rectangle cellRectangle = table.getCellRect(table.getSelectedRow(), 0, true);
//	    JViewport viewport = (JViewport) table.getParent();
//	    Rectangle visibleRectangle = viewport.getVisibleRect();
//	    table.scrollRectToVisible(new Rectangle(cellRectangle.x, cellRectangle.y, (int) visibleRectangle.getWidth(), (int) visibleRectangle.getHeight()));
		table.scrollRectToVisible(cellRectangle);
	}
	

}
