package library.management;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.sql.*;


public class UserFrame extends JFrame {

    public UserFrame(int readerId) {
        setTitle("Hệ thống mượn trả sách - Người dùng");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //TabbedPane
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Tab Borrow
        JPanel borrowPanel = new JPanel(new BorderLayout(10, 10));
    	borrowPanel.setBorder(BorderFactory.createTitledBorder("Mượn sách"));
    	
    	JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
    	
    	JTextField tfBookID = new JTextField();
        JTextField tfLoanNo = new JTextField();
        JTextField tfLoanDate = new JTextField();
        JTextField tfAppointmentDate = new JTextField();
        
    	formPanel.add(new JLabel("Mã sách:"));
    	formPanel.add(tfBookID);
    	formPanel.add(new JLabel("Số lượng:"));
    	formPanel.add(tfLoanNo);
    	/*formPanel.add(new JLabel("Ngày mượn (yyyy-MM-dd):"));
    	formPanel.add(tfLoanDate);*/
    	formPanel.add(new JLabel("Ngày hẹn trả (yyyy-MM-dd):"));
    	formPanel.add(tfAppointmentDate);
    	// Button
    	JButton btnConfirm = new JButton("Xác nhận mượn");
    	formPanel.add(btnConfirm);
    	borrowPanel.add(formPanel, BorderLayout.NORTH);

    	// BookTable
    	DefaultTableModel bookTableModel = new DefaultTableModel();
    	bookTableModel.setColumnIdentifiers(new String[] {
    	    "Mã", "Tên sách", "Số trang", "Ngôn ngữ", "Giá", "SL", "Năm", "Thể loại", "Tác giả", "NXB"
    	});
    	JTable bookTable = new JTable(bookTableModel);
    	JScrollPane scrollPane = new JScrollPane(bookTable);
    	borrowPanel.add(scrollPane, BorderLayout.CENTER);

    	// LoadDB
    	Runnable loadBooks = () -> {
    	    bookTableModel.setRowCount(0);
    	    try (Connection conn = ConnectDB.getConnection()) {
    	        String sql = "SELECT * FROM book";
    	        ResultSet rs = conn.prepareStatement(sql).executeQuery();
    	        while (rs.next()) {
    	            bookTableModel.addRow(new Object[] {
    	                rs.getInt("bookID"),
    	                rs.getString("bookName"),
    	                rs.getString("pageNo"),
    	                rs.getString("language"),
    	                rs.getInt("price"),
    	                rs.getInt("amount"),
    	                rs.getString("publishYear"),
    	                rs.getString("type"),
    	                rs.getString("author"),
    	                rs.getString("publisher")
    	            });
    	        }
    	    } catch (Exception ex) {
    	        ex.printStackTrace();
    	        JOptionPane.showMessageDialog(null, "Lỗi tải sách.");
    	    }
    	};
    	loadBooks.run();

        // ButtonAction
        btnConfirm.addActionListener(e -> {
            try {
                int bookID = Integer.parseInt(tfBookID.getText().trim());
                int loanNo = Integer.parseInt(tfLoanNo.getText().trim());
                java.sql.Date loanDate = new java.sql.Date(System.currentTimeMillis());                
                String appointmentDate = tfAppointmentDate.getText().trim();
                String status = "Chưa trả"; // Mặc định

                try (Connection conn = ConnectDB.getConnection()) {
                    String sql = "INSERT INTO loan (BookID, readerID, loanNo, loanDate, bookReturnAppointmentDate, status) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);

                    stmt.setInt(1, bookID);
                    stmt.setInt(2, readerId);
                    stmt.setInt(3, loanNo);
                    stmt.setDate(4, loanDate);
                    stmt.setDate(5, java.sql.Date.valueOf(appointmentDate));
                    stmt.setString(6, status);

                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        // UpdateSQL
                        String updateSql = "UPDATE book SET amount = amount - ? WHERE BookID = ? AND amount > ?";
                        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                        updateStmt.setInt(1, loanNo);
                        updateStmt.setInt(2, bookID);
                        updateStmt.setInt(3, loanNo);
                        int updated = updateStmt.executeUpdate();
                        if (updated > 0) {
                            JOptionPane.showMessageDialog(null, "Mượn sách thành công!");
                            // Clear TextField
                            tfBookID.setText("");
                            tfLoanNo.setText("");
                            tfLoanDate.setText("");
                            tfAppointmentDate.setText("");
                            loadBooks.run();
                        } else {
                            JOptionPane.showMessageDialog(null, "Mượn sách thành công nhưng không cập nhật được số lượng sách.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Không thể mượn sách.");
                    }
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Lỗi: Mã sách phải là số nguyên.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Ngày phải theo định dạng yyyy-MM-dd.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi hệ thống: " + ex.getMessage());
            }
        });
        
        // Tab Trả sách
        JPanel returnPanel = new JPanel(new BorderLayout(10, 10));
        
        // Panel nhập thông tin trả sách
        JPanel returnForm = new JPanel(new GridLayout(4, 2, 10, 10));
        returnForm.setBorder(BorderFactory.createTitledBorder("Trả sách"));

        JTextField tfLoanID = new JTextField();
        JTextField tfBookID1 = new JTextField();
        JTextField tfReturnDate = new JTextField();
        JButton btnReturn = new JButton("Xác nhận trả");

        returnForm.add(new JLabel("Mã mượn (LoanID):"));
        returnForm.add(tfLoanID);
        returnForm.add(new JLabel("Mã sách (BookID):"));
        returnForm.add(tfBookID1);
        returnForm.add(new JLabel("Ngày trả (yyyy-MM-dd):"));
        returnForm.add(tfReturnDate);
        returnForm.add(new JLabel());
        returnForm.add(btnReturn);

        returnPanel.add(returnForm, BorderLayout.NORTH);

        // Bảng sách đang mượn
        DefaultTableModel loanTableModel = new DefaultTableModel(
        	    new String[] {"Mã mượn", "Mã sách", "Ngày mượn", "Hẹn trả", "Trạng thái"}, 0
        	);
        JTable loanTable = new JTable(loanTableModel);
        JScrollPane loanScroll = new JScrollPane(loanTable);
        returnPanel.add(loanScroll, BorderLayout.CENTER);
        
        //LoadDB        
        Runnable loadLoans = () -> {
            loanTableModel.setRowCount(0);
            try (Connection conn = ConnectDB.getConnection()) {
                String sql = "SELECT loanId, bookId, loanDate, bookReturnAppointmentDate, status FROM loan WHERE status = 'Chưa trả' AND readerID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1,readerId);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {                	
                    loanTableModel.addRow(new Object[] {
                        rs.getInt("loanId"),
                        rs.getInt("bookId"),
                        rs.getString("loanDate"),
                        rs.getString("bookReturnAppointmentDate"),
                        rs.getString("status")
                    });                    
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi tải danh sách sách đang mượn.");
            }
        };
        loadLoans.run();
        
        // Xử lý sự kiện trả sách
        btnReturn.addActionListener(e -> {
            try {
                int loanID = Integer.parseInt(tfLoanID.getText().trim());
                String returnDateStr = tfReturnDate.getText().trim();
                java.sql.Date returnDate = java.sql.Date.valueOf(returnDateStr);

                try (Connection conn = ConnectDB.getConnection()) {
                    // 1. Lấy thông tin bookID và loanNo từ loanID
                    String selectSql = "SELECT BookID, loanNo, STATUS FROM loan WHERE loanID = ?";
                    PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                    selectStmt.setInt(1, loanID);
                    ResultSet rs = selectStmt.executeQuery();

                    if (rs.next()) {
                        String status = rs.getString("STATUS");
                        if (!status.equalsIgnoreCase("Chưa trả")) {
                            JOptionPane.showMessageDialog(null, "Sách này đã được trả trước đó.");
                            return;
                        }

                        int bookId = rs.getInt("bookId");
                        int loanNo = rs.getInt("loanNo"); // loanNo là số lượng sách đã mượn

                        // 2. Cập nhật trạng thái và ngày trả
                        String updateLoanSql = "UPDATE loan SET STATUS = 'Đã trả', bookReturnDate = ? WHERE loanID = ?";
                        PreparedStatement updateLoanStmt = conn.prepareStatement(updateLoanSql);
                        updateLoanStmt.setDate(1, returnDate);
                        updateLoanStmt.setInt(2, loanID);
                        updateLoanStmt.executeUpdate();

                        // 3. Cập nhật số lượng sách (amount = amount + loanNo)
                        String updateBookSql = "UPDATE book SET amount = amount + ? WHERE BookID = ?";
                        PreparedStatement updateBookStmt = conn.prepareStatement(updateBookSql);
                        updateBookStmt.setInt(1, loanNo);
                        updateBookStmt.setInt(2, bookId);
                        updateBookStmt.executeUpdate();

                        JOptionPane.showMessageDialog(null, "Đã trả sách thành công cho LoanID: " + loanID);
                        tfLoanID.setText("");
                        tfBookID1.setText("");
                        tfReturnDate.setText("");
                        loadLoans.run();
                    } else {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy bản ghi mượn với LoanID: " + loanID);
                        tfLoanID.setText("");
                        tfBookID1.setText("");
                        tfReturnDate.setText("");
                    }

                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "LoanID phải là số.");
                tfLoanID.setText("");
                tfBookID1.setText("");
                tfReturnDate.setText("");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Ngày phải theo định dạng yyyy-MM-dd.");
                tfLoanID.setText("");
                tfBookID1.setText("");
                tfReturnDate.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi: " + ex.getMessage());
                tfLoanID.setText("");
                tfBookID1.setText("");
                tfReturnDate.setText("");
            }
        });
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String selectedTitle = tabbedPane.getTitleAt(selectedIndex);
            if (selectedTitle.equals("Mượn sách")) {
                // Gọi hàm load lại bảng sách đang mượn
                loadLoans.run();
            }
        });
        
        // Các trường thông tin cá nhân
        JTextField tfSurname = new JTextField();
        JTextField tfName = new JTextField();
        JTextField tfIdentityCard = new JTextField();
        JTextField tfPhone = new JTextField();
        JTextField tfCardIssueDate = new JTextField();
        //JComboBox<String> cbJob = new JComboBox<>(new String[] {"Giảng viên", "Học sinh"});
        
        // Tab Thông tin cá nhân
        JPanel profilePanel = new JPanel(new GridLayout(6, 2, 10, 10));
        profilePanel.setBorder(BorderFactory.createTitledBorder("Thông tin cá nhân"));

        profilePanel.add(new JLabel("Họ:"));
        profilePanel.add(tfSurname);
        profilePanel.add(new JLabel("Tên:"));
        profilePanel.add(tfName);
        profilePanel.add(new JLabel("CMND/CCCD:"));
        profilePanel.add(tfIdentityCard);
        profilePanel.add(new JLabel("SĐT:"));
        profilePanel.add(tfPhone);
        profilePanel.add(new JLabel("Ngày cấp thẻ (yyyy-MM-dd):"));
        profilePanel.add(tfCardIssueDate);

        JButton btnUpdateProfile = new JButton("Cập nhật thông tin");
        profilePanel.add(new JLabel()); 
        profilePanel.add(btnUpdateProfile);
        try (Connection conn = ConnectDB.getConnection()) {
            String sql = "SELECT * FROM reader WHERE readerID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, readerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tfSurname.setText(rs.getString("surname"));
                tfName.setText(rs.getString("name"));
                tfIdentityCard.setText(rs.getString("identityCard"));
                tfPhone.setText(rs.getString("phoneNo"));
                tfCardIssueDate.setText(rs.getString("cardIssueDate"));
                //cbJob.setSelectedItem(rs.getString("job"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Không thể tải thông tin người dùng.");
        }
        
        btnUpdateProfile.addActionListener(e -> {
            try (Connection conn = ConnectDB.getConnection()) {
                String sql = "UPDATE reader SET surname = ?, name = ?, identityCard = ?, phoneNO = ?, cardIssueDate = ? WHERE readerID = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);

                stmt.setString(1, tfSurname.getText().trim());
                stmt.setString(2, tfName.getText().trim());
                stmt.setString(3, tfIdentityCard.getText().trim());
                stmt.setString(4, tfPhone.getText().trim());
                stmt.setDate(5, java.sql.Date.valueOf(tfCardIssueDate.getText().trim()));
                stmt.setInt(6, readerId);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "Cập nhật thông tin thành công!");
                } else {
                    JOptionPane.showMessageDialog(null, "Không cập nhật được thông tin.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Lỗi cập nhật: " + ex.getMessage());
            }
        });


        // Thêm các tab vào tabbedPane
        tabbedPane.addTab("Mượn sách", borrowPanel);
        tabbedPane.addTab("Trả sách", returnPanel);
        tabbedPane.addTab("Thông tin cá nhân", profilePanel);

        add(tabbedPane);
        setVisible(true);
    }
    
    public static void main(String[] args) {
    }
}
