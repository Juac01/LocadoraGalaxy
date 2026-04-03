package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import model.beans.Filme;
import model.dao.FilmeDAO;

public class CadastroFilmeFrame extends JFrame {
    private JTextField nomeInput;
    private JTextField precoInput;
    private JTextField classIndicativaInput;
    private JTextField generoInput;
    private JTextField qtdEstoqueInput;
    private JTextField capaInput;
    private JTextField diretorInput;
    private JTextField duracaoInput;
    private JTextArea elencoInput;
    
    private FilmeDAO filmeDAO = new FilmeDAO();
    private Filme filmeEdicao = null; // Para edição
    private boolean modoEdicao = false;
    
    // Construtor para cadastro
    public CadastroFilmeFrame() {
        this(null);
    }
    
    // Construtor para edição
    public CadastroFilmeFrame(Filme filme) {
        this.filmeEdicao = filme;
        this.modoEdicao = (filme != null);
        
        setTitle(modoEdicao ? "Editar Filme" : "Cadastrar Filme");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        initComponents();
        
        // Se for edição, preenche os campos
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
        JLabel titulo = new JLabel(modoEdicao ? "Editar Filme" : "Cadastrar Novo Filme");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(50, 20, 800, 50);
        mainPanel.add(titulo);
        
        // --- AJUSTE DE COLUNAS PARA NÃO ATROPELAR A IMAGEM ---
        int y = 90; 
        int labelX = 50;
        int inputX = 180;     // Puxei um pouco para a esquerda
        int inputWidth = 300; // Reduzi o tamanho para sobrar espaço para a foto na direita
        int spacing = 45;     // Reduzi o espaço entre linhas para caber tudo
        
        // Nome
        addLabel(mainPanel, "Nome:", labelX, y);
        nomeInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Preço
        addLabel(mainPanel, "Preço:", labelX, y);
        precoInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Classificação
        addLabel(mainPanel, "Classificação:", labelX, y);
        classIndicativaInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;

        // Gênero
        addLabel(mainPanel, "Gênero:", labelX, y);
        generoInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;

        // Estoque
        addLabel(mainPanel, "Estoque:", labelX, y);
        qtdEstoqueInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;

        // URL da Capa
        addLabel(mainPanel, "URL Capa:", labelX, y);
        capaInput = addTextField(mainPanel, inputX, y, inputWidth);
        
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
        
        // Diretor
        addLabel(mainPanel, "Diretor:", labelX, y);
        diretorInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Duração
        addLabel(mainPanel, "Duração:", labelX, y);
        duracaoInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Elenco (área de texto)
        addLabel(mainPanel, "Elenco:", labelX, y);
        JLabel helpLabel = new JLabel("(um ator por linha)");
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        helpLabel.setForeground(Color.LIGHT_GRAY);
        helpLabel.setBounds(labelX, y + 20, 200, 20);
        mainPanel.add(helpLabel);
        
        elencoInput = new JTextArea();
        elencoInput.setFont(new Font("Arial", Font.PLAIN, 14));
        elencoInput.setLineWrap(true);
        elencoInput.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(elencoInput);
        scrollPane.setBounds(inputX, y, inputWidth, 80);
        mainPanel.add(scrollPane);
        y += 100;
        
        // Botões
        JButton btnSalvar = new JButton(modoEdicao ? "ATUALIZAR" : "CADASTRAR");
        btnSalvar.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        btnSalvar.setBackground(Color.WHITE);
        btnSalvar.setForeground(new Color(128, 128, 192));
        btnSalvar.setBounds(inputX, y, 150, 35);
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarFilme();
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
        if (filmeEdicao != null) {
            nomeInput.setText(filmeEdicao.getNome());
            precoInput.setText(String.valueOf(filmeEdicao.getPreco()));
            classIndicativaInput.setText(filmeEdicao.getClassIndicativa());
            generoInput.setText(filmeEdicao.getGenero());
            qtdEstoqueInput.setText(String.valueOf(filmeEdicao.getQtdEstoque()));
            capaInput.setText(filmeEdicao.getCapa());
            diretorInput.setText(filmeEdicao.getDiretor());
            duracaoInput.setText(filmeEdicao.getDuracao());
            
            // Elenco (join com quebras de linha)
            if (filmeEdicao.getElenco() != null && !filmeEdicao.getElenco().isEmpty()) {
                elencoInput.setText(String.join("\n", filmeEdicao.getElenco()));
            }
        }
    }
    
    private void limparCampos() {
        nomeInput.setText("");
        precoInput.setText("");
        classIndicativaInput.setText("");
        generoInput.setText("");
        qtdEstoqueInput.setText("");
        capaInput.setText("");
        diretorInput.setText("");
        duracaoInput.setText("");
        elencoInput.setText("");
    }
    
    private void salvarFilme() {
        try {
            // Validação básica
            if (nomeInput.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o nome do filme!", "Erro", JOptionPane.ERROR_MESSAGE);
                nomeInput.requestFocus();
                return;
            }
            
            if (precoInput.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o preço!", "Erro", JOptionPane.ERROR_MESSAGE);
                precoInput.requestFocus();
                return;
            }
            
            // Criar objeto Filme
            Filme filme = new Filme();
            filme.setNome(nomeInput.getText().trim());
            filme.setPreco(Double.parseDouble(precoInput.getText().trim()));
            filme.setClassIndicativa(classIndicativaInput.getText().trim());
            filme.setGenero(generoInput.getText().trim());
            filme.setQtdEstoque(Integer.parseInt(qtdEstoqueInput.getText().trim()));
            filme.setCapa(capaInput.getText().trim());
            filme.setDiretor(diretorInput.getText().trim());
            filme.setDuracao(duracaoInput.getText().trim());
            
            // Processar elenco (split por linha)
            String[] elencoArray = elencoInput.getText().trim().split("\n");
            for (String ator : elencoArray) {
                if (!ator.trim().isEmpty()) {
                    filme.adicionarAtor(ator.trim());
                }
            }
            
            // Salvar ou atualizar
            if (modoEdicao) {
                filmeDAO.alterarFilmeObjeto(filmeEdicao.getIdMidia(), filme);
            } else {
                filmeDAO.cadastrarFilmeObjeto(filme);
            }
            
            dispose(); // Fecha a janela
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, verifique os valores numéricos (preço e quantidade)!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar filme: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}