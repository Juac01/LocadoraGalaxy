package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import model.beans.Jogo;
import model.dao.JogoDAO;

public class CadastroJogoFrame extends JFrame {
    private JTextField nomeInput;
    private JTextField precoInput;
    private JTextField classIndicativaInput;
    private JTextField generoInput;
    private JTextField qtdEstoqueInput;
    private JTextField capaInput;
    
    private JogoDAO jogoDAO = new JogoDAO();
    private Jogo jogoEdicao = null;
    private boolean modoEdicao = false;
    
    // Construtor para cadastro
    public CadastroJogoFrame() {
        this(null);
    }
    
    // Construtor para edição
    public CadastroJogoFrame(Jogo jogo) {
        this.jogoEdicao = jogo;
        this.modoEdicao = (jogo != null);
        
        setTitle(modoEdicao ? "Editar Jogo" : "Cadastrar Jogo");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        
        if (modoEdicao) {
            preencherCampos();
        }
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(new Color(128, 128, 192));
        getContentPane().add(mainPanel);
        
        // Título
        JLabel titulo = new JLabel(modoEdicao ? "Editar Jogo" : "Cadastrar Novo Jogo");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(50, 20, 800, 50);
        mainPanel.add(titulo);
        
        int y = 90;
        int labelX = 50;
        int inputX = 180;
        int inputWidth = 300;
        int spacing = 45;
        
        // Nome
        addLabel(mainPanel, "Nome do Jogo:", labelX, y);
        nomeInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Preço
        addLabel(mainPanel, "Preço (R$):", labelX, y);
        precoInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Classificação Indicativa
        addLabel(mainPanel, "Classificação:", labelX, y);
        classIndicativaInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Gênero
        addLabel(mainPanel, "Gênero:", labelX, y);
        generoInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Quantidade em Estoque
        addLabel(mainPanel, "Qtd. Estoque:", labelX, y);
        qtdEstoqueInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // URL da Capa
        addLabel(mainPanel, "URL da Capa:", labelX, y);
        capaInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
     // --- BOTÃO VISUALIZAR (Lado a lado com o input da capa) ---
        JButton btnPreview = new JButton("Preview");
        btnPreview.setBounds(inputX + inputWidth + 10, y, 90, 30);
        mainPanel.add(btnPreview);
        y += spacing;

        // --- ÁREA DA FOTO (Lado Direito) ---
        JLabel previewLabel = new JLabel("Sem Foto", SwingConstants.CENTER);
        previewLabel.setBounds(600, 90, 230, 300); // Espaço dedicado na direita
        previewLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        previewLabel.setForeground(Color.WHITE);
        mainPanel.add(previewLabel);

        // Ação do Botão Preview
        btnPreview.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String urlTexto = capaInput.getText().trim();
                    if (urlTexto.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Insira uma URL!");
                        return;
                    }
                    
                    // Mostra que está carregando
                    previewLabel.setText("Carregando...");
                    previewLabel.setIcon(null);

                    // Carrega a imagem da URL
                    URL url = new URL(urlTexto);
                    ImageIcon icon = new ImageIcon(url);
                    
                    // Redimensiona para o tamanho do previewLabel
                    Image img = icon.getImage();
                    Image newImg = img.getScaledInstance(previewLabel.getWidth(), previewLabel.getHeight(), Image.SCALE_SMOOTH);
                    
                    previewLabel.setIcon(new ImageIcon(newImg));
                    previewLabel.setText(""); 
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro: Verifique o link da imagem.");
                    previewLabel.setIcon(null);
                    previewLabel.setText("Erro na URL");
                }
            }
        });
        
   
        
        // Botões
        JButton btnSalvar = new JButton(modoEdicao ? "ATUALIZAR" : "CADASTRAR");
        btnSalvar.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        btnSalvar.setBackground(Color.WHITE);
        btnSalvar.setForeground(new Color(128, 128, 192));
        btnSalvar.setBounds(inputX, y, 150, 35);
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarJogo();
            }
        });
        mainPanel.add(btnSalvar);
        
        JButton btnCancelar = new JButton("CANCELAR");
        btnCancelar.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(128, 128, 192));
        btnCancelar.setBounds(inputX + 170, y, 150, 35);
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        mainPanel.add(btnCancelar);
        
        JButton btnLimpar = new JButton("LIMPAR");
        btnLimpar.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        btnLimpar.setBackground(Color.WHITE);
        btnLimpar.setForeground(new Color(128, 128, 192));
        btnLimpar.setBounds(inputX + 340, y, 150, 35);
        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        mainPanel.add(btnLimpar);
    }
    
    private void addLabel(JPanel panel, String text, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 180, 30);
        panel.add(label);
    }
    
    private JTextField addTextField(JPanel panel, int x, int y, int width) {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBounds(x, y, width, 30);
        panel.add(textField);
        return textField;
    }
    
    private void preencherCampos() {
        if (jogoEdicao != null) {
            nomeInput.setText(jogoEdicao.getNome());
            precoInput.setText(String.valueOf(jogoEdicao.getPreco()));
            classIndicativaInput.setText(jogoEdicao.getClassIndicativa());
            generoInput.setText(jogoEdicao.getGenero());
            qtdEstoqueInput.setText(String.valueOf(jogoEdicao.getQtdEstoque()));
            capaInput.setText(jogoEdicao.getCapa());

        }
    }
    
    private void limparCampos() {
        nomeInput.setText("");
        precoInput.setText("");
        classIndicativaInput.setText("");
        generoInput.setText("");
        qtdEstoqueInput.setText("");
        capaInput.setText("");

    }
    
    private void salvarJogo() {
        try {
            // Validação básica
            if (nomeInput.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o nome do jogo!", "Erro", JOptionPane.ERROR_MESSAGE);
                nomeInput.requestFocus();
                return;
            }
            
            if (precoInput.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o preço!", "Erro", JOptionPane.ERROR_MESSAGE);
                precoInput.requestFocus();
                return;
            }
            
            // Criar objeto Jogo
            Jogo jogo = new Jogo();
            jogo.setNome(nomeInput.getText().trim());
            jogo.setPreco(Double.parseDouble(precoInput.getText().trim()));
            jogo.setClassIndicativa(classIndicativaInput.getText().trim());
            jogo.setGenero(generoInput.getText().trim());
            jogo.setQtdEstoque(Integer.parseInt(qtdEstoqueInput.getText().trim()));
            jogo.setCapa(capaInput.getText().trim());

            
            // Salvar ou atualizar
            if (modoEdicao) {
                jogoDAO.alterarJogoObjeto(jogoEdicao.getIdMidia(), jogo);
            } else {
                jogoDAO.cadastrarJogoObjeto(jogo);
            }
            
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, verifique os valores numéricos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar jogo: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}