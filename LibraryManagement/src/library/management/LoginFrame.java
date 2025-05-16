package library.management;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    // Components
    private Container cont;
    private JPanel pnlLogin, pnlRegister, pnlCards;
    private JLabel lblTitle, lblLoginUsername, lblLoginPassword, lblConfirmPassword, 
                  lblRegName, lblRegSurname, lblRegIdentity, lblRegPhone, lblRegPosition, lblRegUsername, lblRegPassword;
    private JTextField tfLoginUsername, tfRegName, tfRegSurname, tfRegIdentity, tfRegPhone, tfRegUsername;
    private JPasswordField pfLoginPassword, pfRegPassword, pfConfirmPassword;
    private JButton btnLogin, btnToRegister, btnRegister, btnToLogin;
    private JRadioButton rdoStudent, rdoLecturer;
    private ButtonGroup grPosition;
    private CardLayout cardLayout;
    
    private Connection conn = null;
    
    public LoginFrame() {
        // Basic frame setup
        setTitle("Library Management System");
        setBounds(100, 100, 500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cont = getContentPane();
        cont.setLayout(new BorderLayout());
        
        // Create card layout for switching between login and register
        cardLayout = new CardLayout();
        pnlCards = new JPanel(cardLayout);
        
        // Setup Login Panel
        setupLoginPanel();
        
        // Setup Register Panel
        setupRegisterPanel();
        
        // Add panels to card layout
        pnlCards.add(pnlLogin, "login");
        pnlCards.add(pnlRegister, "register");
        
        // Show login panel by default
        cardLayout.show(pnlCards, "login");
        
        cont.add(pnlCards, BorderLayout.CENTER);
        setVisible(true);
    }
    
    private void setupLoginPanel() {
        pnlLogin = new JPanel();
        pnlLogin.setLayout(null);
        pnlLogin.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        lblTitle = new JLabel("Library Management System - Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setBounds(80, 30, 350, 30);
        pnlLogin.add(lblTitle);
        
        // UserName
        lblLoginUsername = new JLabel("Username:");
        lblLoginUsername.setBounds(100, 100, 80, 25);
        pnlLogin.add(lblLoginUsername);
        
        tfLoginUsername = new JTextField();
        tfLoginUsername.setBounds(200, 100, 200, 25);
        pnlLogin.add(tfLoginUsername);
        
        // Password
        lblLoginPassword = new JLabel("Password:");
        lblLoginPassword.setBounds(100, 150, 80, 25);
        pnlLogin.add(lblLoginPassword);
        
        pfLoginPassword = new JPasswordField();
        pfLoginPassword.setBounds(200, 150, 200, 25);
        pnlLogin.add(pfLoginPassword);
        
        // Login button
        btnLogin = new JButton("Login");
        btnLogin.setBounds(200, 200, 100, 30);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });
        pnlLogin.add(btnLogin);
        
        // Register link
        btnToRegister = new JButton("Create New Account");
        btnToRegister.setBounds(160, 250, 180, 30);
        btnToRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(pnlCards, "register");
            }
        });
        pnlLogin.add(btnToRegister);
    }
    
    private void setupRegisterPanel() {
        pnlRegister = new JPanel();
        pnlRegister.setLayout(null);
        pnlRegister.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel lblRegTitle = new JLabel("Create New Account");
        lblRegTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblRegTitle.setBounds(150, 20, 200, 30);
        pnlRegister.add(lblRegTitle);
        
        // Surname
        lblRegSurname = new JLabel("Surname:");
        lblRegSurname.setBounds(80, 70, 100, 25);
        pnlRegister.add(lblRegSurname);
        
        tfRegSurname = new JTextField();
        tfRegSurname.setBounds(180, 70, 200, 25);
        pnlRegister.add(tfRegSurname);
        
        // Name
        lblRegName = new JLabel("Name:");
        lblRegName.setBounds(80, 105, 100, 25);
        pnlRegister.add(lblRegName);
        
        tfRegName = new JTextField();
        tfRegName.setBounds(180, 105, 200, 25);
        pnlRegister.add(tfRegName);
        
        // Identity Card
        lblRegIdentity = new JLabel("Identity Card:");
        lblRegIdentity.setBounds(80, 140, 100, 25);
        pnlRegister.add(lblRegIdentity);
        
        tfRegIdentity = new JTextField();
        tfRegIdentity.setBounds(180, 140, 200, 25);
        pnlRegister.add(tfRegIdentity);
        
        // Phone
        lblRegPhone = new JLabel("Phone Number:");
        lblRegPhone.setBounds(80, 175, 100, 25);
        pnlRegister.add(lblRegPhone);
        
        tfRegPhone = new JTextField();
        tfRegPhone.setBounds(180, 175, 200, 25);
        pnlRegister.add(tfRegPhone);
        
        /*// Position
        lblRegPosition = new JLabel("Position:");
        lblRegPosition.setBounds(80, 210, 100, 25);
        pnlRegister.add(lblRegPosition);
        
        rdoStudent = new JRadioButton("Sinh viên");
        rdoStudent.setBounds(180, 210, 100, 25);
        rdoStudent.setSelected(true);
        pnlRegister.add(rdoStudent);
        
        rdoLecturer = new JRadioButton("Giảng viên");
        rdoLecturer.setBounds(280, 210, 100, 25);
        pnlRegister.add(rdoLecturer);
        
        grPosition = new ButtonGroup();
        grPosition.add(rdoStudent);
        grPosition.add(rdoLecturer);*/
        
        // UserName
        lblRegUsername = new JLabel("Username:");
        lblRegUsername.setBounds(80, 245, 100, 25);
        pnlRegister.add(lblRegUsername);
        
        tfRegUsername = new JTextField();
        tfRegUsername.setBounds(180, 245, 200, 25);
        pnlRegister.add(tfRegUsername);
        
        // Password
        lblRegPassword = new JLabel("Password:");
        lblRegPassword.setBounds(80, 280, 100, 25);
        pnlRegister.add(lblRegPassword);
        
        pfRegPassword = new JPasswordField();
        pfRegPassword.setBounds(180, 280, 200, 25);
        pnlRegister.add(pfRegPassword);
        
        // Confirm Password
        lblConfirmPassword = new JLabel("Confirm Password:");
        lblConfirmPassword.setBounds(80, 315, 120, 25);
        pnlRegister.add(lblConfirmPassword);
        
        pfConfirmPassword = new JPasswordField();
        pfConfirmPassword.setBounds(200, 315, 180, 25);
        pnlRegister.add(pfConfirmPassword);
        
        // Register button
        btnRegister = new JButton("Register");
        btnRegister.setBounds(180, 355, 140, 30);
        btnRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        pnlRegister.add(btnRegister);
        
        // Back to login link
        btnToLogin = new JButton("Back to Login");
        btnToLogin.setBounds(180, 395, 140, 30);
        btnToLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(pnlCards, "login");
            }
        });
        pnlRegister.add(btnToLogin);
    }
    
    private void loginUser() {
        String username = tfLoginUsername.getText();
        String password = new String(pfLoginPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty", "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Connect to database and check credentials
        conn = ConnectDB.getConnection();
        String sql = """
        	    SELECT *
        	    FROM users u
        	    JOIN reader r ON u.readerid = r.readerID
        	    WHERE u.username = ? AND u.password = ?
        	""";
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); // In real application, use password hashing!
            rs = stmt.executeQuery();
            
            if (rs.next()) {
            	String job = rs.getString("job");
                int readerId = rs.getInt("readerId");
                // Login successful
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                if ("Sinh viên".equalsIgnoreCase(job)) {
                    // Open UserFrame and close LoginFrame
                    new UserFrame(readerId);
                    this.dispose();
                } else{
                    // Open MainFrame and close LoginFrame
                    new MainFrame();
                    this.dispose();
                }
                
            } else {
                // Login failed
                JOptionPane.showMessageDialog(this, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void registerUser() {
        String surname = tfRegSurname.getText();
        String name = tfRegName.getText();
        String identity = tfRegIdentity.getText();
        String phone = tfRegPhone.getText();
        String username = tfRegUsername.getText();
        String password = new String(pfRegPassword.getPassword());
        String confirmPassword = new String(pfConfirmPassword.getPassword());
        //String position = rdoStudent.isSelected() ? "Sinh viên" : "Giảng viên";
        
        // Validation
        if (name.isEmpty() || identity.isEmpty() || phone.isEmpty() || username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match", "Registration Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Register user in database
        conn = ConnectDB.getConnection();
        
        try {
            // First create reader
            String readerSql = "call sp_addReader(?,?,?,?,?)";
            PreparedStatement readerStmt = conn.prepareStatement(readerSql);
            readerStmt.setString(1, surname);
            readerStmt.setString(2, name);
            readerStmt.setString(3, identity);
            readerStmt.setString(4, phone);
            readerStmt.setString(5, "Sinh viên");
            readerStmt.execute();
            readerStmt.close();
            
            // Now create user account
            String userSql = "INSERT INTO users (username, password, readerid) SELECT ?, ?, MAX(readerid) FROM reader";
            PreparedStatement userStmt = conn.prepareStatement(userSql);
            userStmt.setString(1, username);
            userStmt.setString(2, password); // In real application, use password hashing!
            userStmt.executeUpdate();
            userStmt.close();
            
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(pnlCards, "login");
            clearRegistrationFields();
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registration error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void clearRegistrationFields() {
        tfRegSurname.setText("");
        tfRegName.setText("");
        tfRegIdentity.setText("");
        tfRegPhone.setText("");
        tfRegUsername.setText("");
        pfRegPassword.setText("");
        pfConfirmPassword.setText("");
        rdoStudent.setSelected(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame();
            }
        });
    }
}