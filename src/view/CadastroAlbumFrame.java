package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import model.beans.Album;
import model.dao.AlbumDAO;

public class CadastroAlbumFrame extends JFrame {
    private JTextField nomeInput;
    private JTextField precoInput;
    private JTextField classIndicativaInput;
    private JTextField generoInput;
    private JTextField qtdEstoqueInput;
    private JTextField capaInput;
    private JTextField anoLancamentoInput;
    private JTextArea artistasInput;
    private JTextArea musicasInput;
    
    private AlbumDAO albumDAO = new AlbumDAO();
    private Album albumEdicao = null;
    private boolean modoEdicao = false;
    
    // Construtor para cadastro
    public CadastroAlbumFrame() {
        this(null);
    }
    
    // Construtor para edição
    public CadastroAlbumFrame(Album album) {
        this.albumEdicao = album;
        this.modoEdicao = (album != null);
        
        setTitle(modoEdicao ? "Editar Álbum" : "Cadastrar Álbum");
        setSize(900, 750);
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
        JLabel titulo = new JLabel(modoEdicao ? "Editar Álbum" : "Cadastrar Novo Álbum");
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
        addLabel(mainPanel, "Nome do Álbum:", labelX, y);
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
        
        // Ano de Lançamento
        addLabel(mainPanel, "Ano Lançamento:", labelX, y);
        anoLancamentoInput = addTextField(mainPanel, inputX, y, inputWidth);
        y += spacing;
        
        // Artistas (área de texto)
        addLabel(mainPanel, "Artistas:", labelX, y);
        JLabel helpArtistas = new JLabel("(um artista por linha)");
        helpArtistas.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        helpArtistas.setForeground(Color.LIGHT_GRAY);
        helpArtistas.setBounds(labelX, y + 20, 200, 20);
        mainPanel.add(helpArtistas);
        
        artistasInput = new JTextArea();
        artistasInput.setFont(new Font("Arial", Font.PLAIN, 14));
        artistasInput.setLineWrap(true);
        artistasInput.setWrapStyleWord(true);
        
        JScrollPane scrollArtistas = new JScrollPane(artistasInput);
        scrollArtistas.setBounds(inputX, y, inputWidth, 70);
        mainPanel.add(scrollArtistas);
        y += 90;
        
        // Músicas (área de texto)
        addLabel(mainPanel, "Músicas:", labelX, y);
        JLabel helpMusicas = new JLabel("(uma música por linha)");
        helpMusicas.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        helpMusicas.setForeground(Color.LIGHT_GRAY);
        helpMusicas.setBounds(labelX, y + 20, 200, 20);
        mainPanel.add(helpMusicas);
        
        musicasInput = new JTextArea();
        musicasInput.setFont(new Font("Arial", Font.PLAIN, 14));
        musicasInput.setLineWrap(true);
        musicasInput.setWrapStyleWord(true);
        
        JScrollPane scrollMusicas = new JScrollPane(musicasInput);
        scrollMusicas.setBounds(inputX, y, inputWidth, 70);
        mainPanel.add(scrollMusicas);
        y += 90;
        
        // Botões
        JButton btnSalvar = new JButton(modoEdicao ? "ATUALIZAR" : "CADASTRAR");
        btnSalvar.setFont(new Font("Segoe UI Black", Font.PLAIN, 14));
        btnSalvar.setBackground(Color.WHITE);
        btnSalvar.setForeground(new Color(128, 128, 192));
        btnSalvar.setBounds(inputX, y, 150, 35);
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarAlbum();
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
        if (albumEdicao != null) {
            nomeInput.setText(albumEdicao.getNome());
            precoInput.setText(String.valueOf(albumEdicao.getPreco()));
            classIndicativaInput.setText(albumEdicao.getClassIndicativa());
            generoInput.setText(albumEdicao.getGenero());
            qtdEstoqueInput.setText(String.valueOf(albumEdicao.getQtdEstoque()));
            capaInput.setText(albumEdicao.getCapa());
            anoLancamentoInput.setText(String.valueOf(albumEdicao.getAnoLancamento()));
            
            // Artistas
            if (albumEdicao.getArtistas() != null && !albumEdicao.getArtistas().isEmpty()) {
                artistasInput.setText(String.join("\n", albumEdicao.getArtistas()));
            }
            
            // Músicas
            if (albumEdicao.getMusicas() != null && !albumEdicao.getMusicas().isEmpty()) {
                musicasInput.setText(String.join("\n", albumEdicao.getMusicas()));
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
        anoLancamentoInput.setText("");
        artistasInput.setText("");
        musicasInput.setText("");
    }
    
    private void salvarAlbum() {
        try {
            // Validação básica
            if (nomeInput.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o nome do álbum!", "Erro", JOptionPane.ERROR_MESSAGE);
                nomeInput.requestFocus();
                return;
            }
            
            if (precoInput.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha o preço!", "Erro", JOptionPane.ERROR_MESSAGE);
                precoInput.requestFocus();
                return;
            }
            
            // Criar objeto Album
            Album album = new Album();
            album.setNome(nomeInput.getText().trim());
            album.setPreco(Double.parseDouble(precoInput.getText().trim()));
            album.setClassIndicativa(classIndicativaInput.getText().trim());
            album.setGenero(generoInput.getText().trim());
            album.setQtdEstoque(Integer.parseInt(qtdEstoqueInput.getText().trim()));
            album.setCapa(capaInput.getText().trim());
            album.setAnoLancamento(Integer.parseInt(anoLancamentoInput.getText().trim()));
            
            // Processar artistas
            String[] artistasArray = artistasInput.getText().trim().split("\n");
            for (String artista : artistasArray) {
                if (!artista.trim().isEmpty()) {
                    album.adicionarArtista(artista.trim());
                }
            }
            
            // Processar músicas
            String[] musicasArray = musicasInput.getText().trim().split("\n");
            for (String musica : musicasArray) {
                if (!musica.trim().isEmpty()) {
                    album.adicionarMusica(musica.trim());
                }
            }
            
            // Salvar ou atualizar
            if (modoEdicao) {
                albumDAO.alterarAlbumObjeto(albumEdicao.getIdMidia(), album);
            } else {
                albumDAO.cadastrarAlbumObjeto(album);
            }
            
            dispose();
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, verifique os valores numéricos!", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar álbum: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}